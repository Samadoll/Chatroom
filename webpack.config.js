 var path = require('path');
 var webpack = require('webpack');
     
 module.exports = {
     entry: './public/javascript/chatroom.js',
     output: {
         path: path.resolve(__dirname, 'build'),
         filename: 'chatroom.bundle.js'
     },
     module: {
         rules: [
            {
                 test: /\.js$/,
                 loader: 'babel-loader',
                 query: {
                     presets: ['es2015', 'react', 'es2017']
                 }
             },
             {
                 test: /\.jsx$/,
                 loader: 'babel-loader',
                 query: {
                     presets: ['es2015', 'react', 'es2017']
                 }
             }
         ]
     },
     resolve: {
        extensions: ['.js', '.jsx', '.es8', '.css']
     }
 }