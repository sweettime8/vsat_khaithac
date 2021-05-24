'use strict'
const path = require('path')

module.exports = {
  runtimeCompiler: true,
  lintOnSave: false,
  chainWebpack: config => {
    config.module
      .rule('svg')
      .test(/\.svg$/)
      .include.add(path.resolve(__dirname, './src/icons'))
      .end()
      .use('file-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]',
      })
      .end()
  }
}