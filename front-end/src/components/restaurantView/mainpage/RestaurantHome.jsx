import React from 'react';
import { Typography, Divider } from '@material-ui/core';

const axios = require('axios').default;

class RestaurantHome extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      restaurant : undefined
    }
    this.getRestaurant = this.getRestaurant.bind(this);
  }

  componentDidMount() {
    this.getRestaurant();
  }

  getRestaurant() {
    let restaurantId = this.props.currentUser.id;
    axios.get("/api/restaurant/" + restaurantId).then(
      response => {
        this.setState({restaurant : response.data});
      }
    ).catch(err => console.log(err));
  }

  render() {
    return this.state.restaurant ? (
      <div>
        <Typography paragraph variant="h5">Bem-vindo ao <i><b>Sistema de Entrega</b></i> !</Typography>
        <Typography paragraph>Se você é um novo usuário, forneça as informações e o cardápio do seu estabelecimento usando os links na barra lateral</Typography>
        <Typography paragraph>Depois de terminá-los, seu estabelecimento ficará visível para os clientes</Typography>
        <Typography paragraph>Para ser notado, você sempre pode atualizá-los</Typography>
        <Typography paragraph>Apreciar!!!</Typography>
        <Divider />
        <br />
        <div>Estado das informações do estabelecimento : 
        {this.state.restaurant.information !== null ? <Typography color="primary">verificado</Typography> : <Typography color="error">Vazia</Typography>}
        </div>
        <br />
        <div>Estado do cardápio : 
        {this.state.restaurant.menu && this.state.restaurant.menu.length !== 0 ? <Typography color="primary">verificado</Typography> : <Typography color="error">Vazio</Typography>}
        </div>
      </div>
    ) : <div />;
  }
}

export default RestaurantHome;