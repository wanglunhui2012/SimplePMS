import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'
import i18n from './lang' // internationalization
import Cookies from 'js-cookie'
import Config from './config'

Vue.config.productionTip = false;
Vue.prototype.Config = Config

import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import Element from 'element-ui'
import './styles/element-variables.scss'
import '@/styles/index.scss' // global css
import './icons' // icon
import './utils/errorLogUtil' // error log

import * as filters from './filters' // global filters
Object.keys(filters).forEach(key => { // 注册全局过滤器
    Vue.filter(key, filters[key])
})

Vue.use(Element, {
    size: Cookies.get('size') || 'medium', // set element-ui default size
    i18n: (key, value) => i18n.t(key, value)
})

new Vue({
    el: '#app',
    router,
    store,
    i18n,
    render: h => h(App)
})
