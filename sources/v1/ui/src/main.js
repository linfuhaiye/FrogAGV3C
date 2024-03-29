import 'babel-polyfill';
import Vue from 'vue';

import 'normalize.css/normalize.css'; // A modern alternative to CSS resets

import Element from 'element-ui';
import lodash from 'lodash';
import 'element-ui/lib/theme-chalk/index.css';

import '@/styles/index.scss'; // global css

import App from './App';
import router from './router';
import store from './store';

import i18n from './lang'; // Internationalization
import './errorLog'; // error log

// import './permission' // permission control
// import './mock' // simulation data

// import * as filters from './filters'; // global filters

Vue.use(Element, {
  size: 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
});

// register global utility filters.
// Object.keys(filters).forEach(key => {
//   Vue.filter(key, filters[key])
// })
Object.defineProperty(Vue.prototype, '$_', { value: lodash });
Vue.config.productionTip = false;

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
});
