import React from 'react';
import ReactDom from 'react-dom';
import {LogInView} from 'LogInView';
import {RegisterView} from 'RegisterView';

const viewTable = {
  "LogInView":LogInView,
  "RegisterView":RegisterView
}

window.chatRoom = {

  renderComponent: (copmonent, dom, options) => {
    let view = viewTable[copmonent];
    options = options || {};
    ReactDom.render(React.createElement(view, options, null), dom);
  }
}