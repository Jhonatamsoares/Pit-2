import React from 'react';
import { Link } from "react-router-dom";
import {List, ListItem, ListItemText, Divider}
from '@material-ui/core';

class RestaurantBar extends React.Component {
  render() {
    return (
      <div>
        <br />
        <h3><b><i>Barra de Ferramenta do Administrador</i></b></h3>
        <br />
        <List component="nav">
          <Link to={"/restaurant/home"} className="link">
            <ListItem>
              <ListItemText primary={"Pagina Inicial"} />
            </ListItem>
            <Divider />
          </Link>
          <Link to={"/restaurant/information"} className="link">
            <ListItem>
              <ListItemText primary={"Informações do Estabelecimento"} />
            </ListItem>
            <Divider />
          </Link>
          <Link to={"/restaurant/menu"} className="link">
            <ListItem>
              <ListItemText primary={"Cardápio"} />
            </ListItem>
            <Divider />
          </Link>
          <Link to={"/restaurant/order"} className="link">
            <ListItem>
              <ListItemText primary={"Meus pedidos ativos"} />
            </ListItem>
            <Divider />
          </Link>
          <Link to={"/restaurant/history"} className="link">
            <ListItem>
              <ListItemText primary={"Meu histórico de pedidos"} />
            </ListItem>
            <Divider />
          </Link>
        </List>
      </div>
    );
  }
}

export default RestaurantBar;