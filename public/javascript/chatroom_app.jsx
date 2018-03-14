import React from 'react';
import ReactDom from 'react-dom';
import {LogInView} from 'LogInView';

const viewTable = {
    	"LoginView": <LogInView/>
	};

window.chatRoom = {

    renderComponent: (copmonent, dom) => {
        let react_component = viewTable[copmonent];
        ReactDom.render(react_component, dom);
    }
}