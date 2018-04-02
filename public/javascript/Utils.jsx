import React from 'react';
import ReactDom from 'react-dom';

const utils = (function () {
  var _ = require('lodash');

  var registerClassHandlers = (selector, event, handler) => {
    let class_arr = selector.split(".");
    let result_dom = [document];
    _.forEach(class_arr, (elem)=> {
      while(result_dom.length != 0) {
        let dom_piece = result_dom.pop();
        _.union(result_dom, dom_piece.getElementsByClassName(elem));
      }
    });
    _.each(result_dom, (e)=> {
      e.addEventListener(event, handler);
    });
  };

  return {
    registerEventHandlers: (events, component) => {
      let registerTable = [];
      if (events) {
        _.forEach(events, (value, key)=> {
          let key_arr = key.split(" ");
          let event = key_arr[1];
          key = key_arr[0];
          if (key[0] === '#') {
            registerTable.push({"selector": key, "type": "id", "event": event, "handler": value});
          } else if (key[0] === '.') {
            registerTable.push({"selector": key, "type": "class", "event": event, "handler": value});
          } else {
            let match_data = key.match(/^(\w+)(#.+|\..+)/);
            if (match_data[2][0] === "#")
              registerTable.push({"selector": match_data[2], "type":"id", "event": event, "handler": value});
            else
              registerTable.push({"selector": match_data[2], "type": "class", "event": event, "handler": value});
          }
        });
        let mount = undefined;
        let registerEvent = ()=> {
          _.forEach(registerTable, (event_object) => {
            let selector = event_object["selector"];

            if (event_object["type"] === "id") {
              let element = document.getElementById(selector.split("#")[1]);
              if (element === null) return;
              element.addEventListener(event_object["event"], event_object["handler"]);
            } else {
              registerClassHandlers(selector, event_object["event"], event_object["handler"]);
            }
          });
        };
        if (component.componentDidMount) {
          mount = component.componentDidMount.bind(component);
        }
        component.componentDidMount = ()=> {
          if (mount) {
            mount();
          }
          registerEvent();
          
        } 
      }
    }
  }
}());

module.exports.tools = utils;