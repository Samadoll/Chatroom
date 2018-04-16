import React from 'react';
import ReactDom from 'react-dom';
import * as Utils from 'utils';
import axios from 'axios';

export class LogInView extends React.Component {

  constructor(props) {
    super(props);
    
    this.state = {
      account: this.props.account || "",
      password: this.props.password || "",
      accountEmpty: "none",
      passwordEmpty: "none",
      status: "unauthorized"
    }
    this.handleChange = this.handleChange.bind(this);
    this.switchRegister = this.switchRegister.bind(this);
    this.events = {
      "input#login-account change": this.handleChange,
      "input#login-password change": this.handleChange,
      "button#login-button click": this.login.bind(this),
      "a#switch-register click": this.switchRegister
    };
    Utils.tools.registerEventHandlers(this.events, this);
  }

  componentDidMount() {
    document.getElementById("login-account").value = this.state.account;
    document.getElementById("login-password").value = this.state.password;
  }

  login(ev) {
  	ev.preventDefault();
    if (this.state.account === "" || this.state.password === "") {	
        this.setState({"accountEmpty": this.state.account === "" ? "block" : "none"});
        this.setState({"passwordEmpty": this.state.password === "" ? "block" : "none"});
  	} else {
      var loginParams = {
        account: this.state.account,
        password: this.state.password
      };

      axios.post("/account/login", loginParams).then(
        (response) => {
        if (response.data["status"] === "success") {
          this.setState({"status": "success"});
          window.location = "/chatroom/app";
        } else {
          this.setState({"status": "failed"});
        }
      })
      .catch((error) => {
        this.setState({"status": "error:" + error.response.status});
      });  
  	}
  }

  handleChange(e) {
    this.setState({[e.target.name] : e.target.value});
    this.setState({[e.target.name + "Empty"] : "none"});
  }

  switchRegister() {
    window.chatRoom.renderComponent(
      "RegisterView", 
      document.getElementById("view-container"), 
      {"account": this.state.account, "password": this.state.password}
    );
  }

  render() {
    let loginFail = this.state.status==="failed" ? "block" : "none";
    let warningMessage = "";
    if (this.state.status === "failed"){
      warningMessage = "Either the Username or Password is incorrect!";
    } else if (this.state.status.match(/error/)) {
      warningMessage = "An Error occur, please try again! Error Code: " + this.state.status.split(":")[1];
    }

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
                  <input name="account" class="form-control" id="login-account" defaultvalue="" placeholder="Email/Username" />
                  <p class="input-warning" style={{"display": this.state.accountEmpty}}>Username cannot be blank!</p>
                </div>
                <div class="form-group">
                  <input type="password" name="password" class="form-control" id="login-password" defaultvalue="" placeholder="Password" />
                  <p class="input-warning" style={{"display": this.state.passwordEmpty}}>Password cannot be blank!</p>
                </div>
                <button type="submit" class="btn bg-info text-white" id="login-button">Login</button>
                <span class="remind">Not a user yet? <a href="#" id="switch-register"> Register</a></span>
                <div class="login-fail" style={{"display": loginFail }}>
                  <p class= "input-warning">
                    {warningMessage}
                  </p>
                </div>
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
}