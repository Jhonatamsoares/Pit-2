import React from 'react';
import FastfoodIcon from '@material-ui/icons/Fastfood';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Box from '@material-ui/core/Box';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Radio from '@material-ui/core/Radio';
import { Link } from "react-router-dom";
import './Login.css';

const axios = require('axios').default;

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userName: "",
      password: "",
      userType: "customer",
      loginFailed: ""
    }
    this.handleChange = this.handleChange.bind(this);
    this.loginUser = this.loginUser.bind(this);
  }

  handleChange(content) {
    this.setState(content);
  }

  loginUser(event) {
    event.preventDefault();
    axios.post("./api/" +this.state.userType + "/login", {userName: this.state.userName, password: this.state.password}).then(
      response => {
        this.props.changeUser(response.data, "login");
      }
    ).catch(err => {
      console.log(err.response.data);
      this.setState({userName: "", password: "", loginFailed: err.response.data});
    });
  }

  render() {
    return (
      <Grid container>
        <Grid item xs={7}>
          <img className="image" alt="foodImage" src='https://img.freepik.com/vetores-gratis/ordem-de-entrega-segura-de-alimentos-e-recebimento_23-2148549716.jpg?w=996&t=st=1667865867~exp=1667866467~hmac=31952efedee4298f1b2ae7f5bb41fa09b008b4046c3dc93474db96461552390d' />
        </Grid>
        <Grid item xs={5}>
          <div className="container">
            <FastfoodIcon className="icon"/>
            <Typography component="h1" variant="h5">
            Conecte-se
            </Typography>
            <Typography variant="body1" color="error">
              {this.state.loginFailed}
            </Typography>
            <form onSubmit={this.loginUser}>
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                label="Usuario"
                type="text"
                value={this.state.userName}
                autoFocus
                onChange={event => this.handleChange({userName: event.target.value})}
              />
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                label="Senha"
                type="password"
                value={this.state.password}
                onChange={event => this.handleChange({password: event.target.value})}
              />
              <FormControl component="fieldset">
                <FormLabel component="legend">Entrar como um </FormLabel>
                <RadioGroup row aria-label="UserType" name="userType" value={this.state.userType} onChange={event => this.handleChange({userType: event.target.value})}>
                  <FormControlLabel value="customer" control={<Radio />} label="Cliente" />
                  <FormControlLabel value="driver" control={<Radio />} label="Entregador" />
                  <FormControlLabel value="restaurant" control={<Radio />} label="Administrador" />
                </RadioGroup>
              </FormControl>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
              >
                Conecte-se
              </Button>
              <br/>
              <br/>
              <Link to={"/register"} className="link">
              Não tem uma conta? Inscrever-se
              </Link>
              <Box mt={5}>
                <Typography variant="body2" color="textSecondary" align="center">
                  {'Copyright © Jhonatam Soares de Oliveira '}
                  {new Date().getFullYear()}
                  {'.'}
                </Typography>
              </Box>
            </form>
          </div>
        </Grid>
      </Grid>
    );
  }
}

export default Login;