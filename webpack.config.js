var path = require('path');
var webpack = require('webpack');

const config = {
  context: path.resolve(__dirname, "./"),
  entry: {
    chatroom_app: path.join(__dirname,'./public/javascript/chatroom_app.js'),
  },
  output: {
    path: path.resolve(__dirname, 'public/assets'),
    filename: '[name].min.js'
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: [/node_modules/]
      },
      {
        test: /\.jsx$/,
        loader: 'babel-loader',
        exclude: [/node_modules/],
        query: {
          presets: ['es2015', 'react', 'es2017']
        }
      },
      {
        test: /\.css$/,
        loader: ['style-loader', 'css-loader']
      }
    ]
  },
  resolve: {
    alias: {
      'react':  path.join(__dirname,'./node_modules/react/umd/react.development.js'),
      'LogInView': path.join(__dirname, './public/javascript/login_view.jsx'),
      'RegisterView': path.join(__dirname, './public/javascript/register_view.jsx'),
      'utils': path.join(__dirname, './public/javascript/utils.jsx')
    },
    extensions: ['.js', '.jsx', '.es8', '.css']
  },

  plugins: [
    new webpack.DefinePlugin({
    "process.env": { 
      NODE_ENV: JSON.stringify("development") 
    }
})
  ]


}

config.devtool = "cheap-eval-source-map";

module.exports = config