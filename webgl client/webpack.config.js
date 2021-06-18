const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
   entry: './src/index.js',
   output: {
      filename: 'bundle.js',
      // sourceMapFilename: "bundle.js.map"
      sourceMapFilename: '[name].[contenthash].js'
   },
   devtool: "source-map",
   mode: "development", // none, development, production

   devServer: {
      port: 8080,
      headers: {
         'Cache-Control': 'no-store',
      },
   },
   plugins: [
      new HtmlWebpackPlugin({
         hash: true,
         filename: './index.html', //relative to root of the application
         title: "Rummikub",
         template: './src/index.html',
         h1: "THREEJS webpack project",
      })
   ],
   module: {
      rules: [
         {
            test: /\.s[ac]ss$/i,
            use: ['style-loader', 'css-loader', "sass-loader"]
         },
         {
            test: /\.(jpe?g|png|gif|svg)$/i,
            use: ['file-loader']
         },
         {
            test: /.(md2)$/i,
            type: 'asset/resource',
         },
         {
            test: /.(stl)$/i,
            type: 'asset/resource',
         },
         {
            test: /.(fbx)$/i,
            type: 'asset/resource',
         },
      ],
   },
};