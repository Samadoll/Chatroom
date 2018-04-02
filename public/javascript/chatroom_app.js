import React from 'react';
import ReactDom from 'react-dom';

window.chatRoom = {

  renderComponent: (copmonent, dom, options) => {
    let view = viewTable[copmonent];
    options = options || {};
    ReactDom.render(React.createElement(view, options, null), dom);
  }
}