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
    this.handleChange = this.handleChange.bind(this);
  }

  login() {

  }

  handleChange(e) {
    this.setState({[e.target.name] : e.target.value});
  }

  switchLoginAndRegister() {
    this.setState({viewMode: this.state.viewMode === "login" ? "register" : "login"}); 
  }

  renderLogin() {
    return (
      <div id="login-background">
        <div id="container">
          <div id="login-block">
            <div class="login-section center-form">
              <form class="login-form">
                <div>
                  <h3 style={{"font-weight": "500"}}>See what's happening in the ChatRoom!</h3>
                </div>
                <div class="form-group">
                  <input name="account" class="form-control" id="login_account" value={this.state.account} onChange={this.handleChange} placeholder="Email/Username" />
                </div>
                <div class="form-group">
                  <input type="password" name="password" class="form-control" id="password" value={this.state.password} onChange={this.handleChange} placeholder="Password" />
                </div>
                <button type="submit" class="btn bg-info text-white" id="register-button" onClick={this.login}>Login</button>
                <span class="remind">Not a user yet? <a href="#" onClick={this.switchLoginAndRegister} > Register</a></span>
              </form>
            </div>
          </div>

          <div id="right-block">
            <div class="center-title">
              <h1 id="chatroom-title">CHATROOM</h1>
              <p id="intro">The ultimate communication tool you've ever seen!</p>
            </div>
          </div>
        </div>
      </div>);
  }

  render() {
    const newProps = {
      switch : this.switchLoginAndRegister,
      account: this.state.account,
      password: this.state.password
    }
    return (this.state.viewMode === "login" ? this.renderLogin() : <RegisterView {...newProps} />);
	}
}