import {ChatRoomPanelBase} from 'ChatRoomPanelBase';

(function() {
  const VERIFICATION = 1;
  const LOG_OUT = 2;
  const MESSAGE = 3;

  var _ = require('lodash');
  var WebSocket = require('websocket').w3cwebsocket;

  window.viewTable = {
    "ChatRoomPanel": ChatRoomPanelBase
  }
  window.chatRoom = window.chatRoom || {}; 

  chatRoom.connect = (session_id) => {
    chatRoom.session_id = session_id;
    chatRoom.ws = new WebSocket('ws://localhost:8883/'+ session_id);
  }
  
  chatRoom.send = (type, content) => {
    let message = {
      session_id: chatRoom.session_id, //TODO
      message_type: type,
      content: content,
      date: Date.now()
    }

    chatRoom.ws.send(JSON.stringify(message));
  };

  chatRoom.onMessage = (messageHandler) => {
    chatRoom.ws.onmessage = (event)=> {
      let messages = JSON.parse(event.data);
      messageHandler(messages);
    }
  }

}());