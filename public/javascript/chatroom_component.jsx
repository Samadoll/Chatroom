import React from 'react';
import ReactDom from 'react-dom';
var _ = require('lodash');

export class ChatRoomComponent extends React.Component {

  constructor(props) {
    super(props);
  }

  registerEventHandlers() {
    if (this.events) {
      _.foreach(this.events, (key, value)=> {
        console.log(key);
      });
    }
  }

  componentDidMount() {
    this.registerEventHandlers();
  }
}