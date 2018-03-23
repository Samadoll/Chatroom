import React from 'react';
import ReactDom from 'react-dom';
import {RegisterView} from 'RegisterView'

export class LogInView extends React.Component {

  constructor(props) {
    super(props);
    
    this.state = {
      viewMode:"login",
      account: "",
      password: ""
    }

    this.login = this.login.bind(this);
    this.switchLoginAndRegister = this.switchLoginAndRegister.bind(this);

  }

  login() {

  }

  switchLoginAndRegister() {
    this.setState({viewMode: this.state.viewMode === "login" ? "register" : "login"}); 
  }

  renderLogin() {
    return (<div>Hello world
      <p onClick={this.switchLoginAndRegister}>click me</p>
      </div>);
  }

  render() {
    const new_props = {
      switch : this.switchLoginAndRegister,
      account: this.state.account,
      password: this.state.password
    }
    return (this.state.viewMode === "login" ? this.renderLogin() : <RegisterView {...new_props} />);
	}
}