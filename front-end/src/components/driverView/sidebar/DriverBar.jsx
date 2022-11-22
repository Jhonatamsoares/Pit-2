import React from 'react';
import { Link } from "react-router-dom";
import { List, ListItem, ListItemText, Divider}from '@material-ui/core';


class DriverBar extends React.Component {
  render() {
    return (
      <div>
        <br />
        <h3><b><i>Barra de ferramentas do entregador</i></b></h3>
        <br />
        <List component="nav">
          <Link to={"/driver/home"} className="link">
            <ListItem>
              <ListItemText primary={"Todos os pedidos pendentes"} />
            </ListItem>
            <Divider />
          </Link>
          <Link to={"/driver/order"} className="link">
            <ListItem>
              <ListItemText primary={"Meu pedido ativo"} />
            </ListItem>
            <Divider />
          </Link>
          <Link to={"/driver/history"} className="link">
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

export default DriverBar;