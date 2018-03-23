import React from 'react';
import ReactDom from 'react-dom';

export class RegisterView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      account: this.props.account,
      password: this.props.password
    };
    this.register = this.register.bind(this);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(e) {
    this.setState({[e.target.name] : e.target.value});
  }

  register(e) {
    e.preventDefault();
    alert(this.state.account + " : " + this.state.password);
    console.log("trigger");
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
              <input name="account" class="form-control" id="login_account" value={this.state.account} onChange={this.handleChange} placeholder="Email/Username" />
            </div>
            <div class="form-group">
              <input type="password" name="password" class="form-control" id="password" value={this.state.password} onChange={this.handleChange} placeholder="Password" />
            </div>
            <button type="submit" class="btn bg-info text-white" id="register-button" onClick={this.register}>Register</button>
            <span class="remind">Already a user? <a href="#" onClick={this.props.switch} > Log in</a></span>
          </form>
        </div>
      </div>
    );
  }
}