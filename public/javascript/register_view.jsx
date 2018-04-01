import React from 'react';
import ReactDom from 'react-dom';
import * as Utils from 'utils';
import axios from 'axios';


export class RegisterView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      account: this.props.account || "",
      password: this.props.password || "",
      accountEmpty: "none",
      passwordEmpty: "none"
    };

    this.handleChange = this.handleChange.bind(this);
    this.events = {
      "input#register-account change": this.handleChange,
      "input#register-password change": this.handleChange,
      "button#register-button click": this.register.bind(this),
      "#switch-login click": this.switchLogin.bind(this)
    };
    Utils.tools.registerEventHandlers(this.events, this);
  }

  componentDidMount() {
    document.getElementById("register-account").value = this.state.account;
    document.getElementById("register-password").value = this.state.password;
  }

  handleChange(e) {
    this.setState({[e.target.name] : e.target.value});
    this.setState({[e.target.name + "Empty"] : "none"});
  }

  switchLogin() {
    window.chatRoom.renderComponent(
      "LogInView", 
      document.getElementById("view-container"), 
      {"account": this.state.account, "password": this.state.password}
    );
  }

  register(e) {
    e.preventDefault();
    if (this.state.account === "" || this.state.password === "") {  
        this.setState({"accountEmpty": this.state.account === "" ? "block" : "none"});
        this.setState({"passwordEmpty": this.state.password === "" ? "block" : "none"});
    } else {
      var loginParams = {
        account: this.state.account,
        password: this.state.password
      };

      axios({
        method: "get",
        url: "/account/login",
        data: loginParams,
        responseType: "json"
      })
      .then((response) => {
        if (response["status"] === "success") {
          this.setState({"status": "success"});
          // TODO
        } else {
          this.setState({"status": "failed"});
        }
      })
      .catch((error) => {

      }); 
    }
  }

  render() {
    return (
      <div id="container">

        <div id="left-block">
          <div class="center-title">
            <h1 id="chatroom-title">CHATROOM</h1>
            <p id="intro">The ultimate communication tool you've ever seen!</p>
          </div>
        </div>

        <div id="register-form">
          <div id="options">
            <span class="header-link">Mobile download</span>
            <span>|</span>
            <span class="header-link">About</span>
          </div>
          <form class="center-form">
            <div>
              <h3 style={{"font-weight": "500"}}>Join today for more surprise !</h3>
            </div>
            <div class="form-group">
              <input name="account" class="form-control" id="register-account" placeholder="Email/Username" />
              <p class="input-warning" style={{"display": this.state.accountEmpty}}>Username cannot be blank!</p>
            </div>
            <div class="form-group">
              <input type="password" name="password" class="form-control" id="register-password" placeholder="Password" />
              <p class="input-warning" style={{"display": this.state.accountEmpty}}>Username cannot be blank!</p>
            </div>
            <button type="submit" class="btn bg-info text-white" id="register-button">Register</button>
            <span class="remind">Already a user? <a href="#" id="switch-login" > Log in</a></span>
          </form>
        </div>
      </div>
    );
  }
}