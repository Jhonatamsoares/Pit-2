import React from 'react';
import { Link } from "react-router-dom";
import {List, ListItem, ListItemText, Divider}from '@material-ui/core';


class CustomerBar extends React.Component {
  render() {
    return (
      <div>
        <br />
        <h3><b><i>Barra de ferramentas do cliente</i></b></h3>
        <br />
        <List component="nav">
          <Link to={"/customer/home"} className="link">
            <ListItem>
              <ListItemText primary={"Pagina Inicial"} />
            </ListItem>
            <Divider />
          </Link>
          <Link to={"/customer/cart"} className="link">
            <ListItem>
              <ListItemText primary={"Meu carrinho de compras"} />
            </ListItem>
            <Divider />
          </Link>
          <Link to={"/customer/orders"} className="link">
            <ListItem>
              <ListItemText primary={"Meus pedidos ativos"} />
            </ListItem>
            <Divider />
          </Link>
          <Link to={"/customer/history"} className="link">
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

export default CustomerBar;