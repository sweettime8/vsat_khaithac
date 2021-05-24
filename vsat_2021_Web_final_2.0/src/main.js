// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Cookies from 'js-cookie'
import ElementUI from 'element-ui'
import App from './App'
import store from './store'
import router from './router'
import i18n from './lang' // Internationalization
import '@/icons' // icon
import '@/permission' // permission control
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import * as filters from './filters' // global filters
import VTooltip from 'v-tooltip'
Vue.use(VTooltip)
import './styles/index.scss' // global css

import { CollapseTransition } from "@ivanv/vue-collapse-transition"

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
Vue.use(CollapseTransition);

Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.config.productionTip = false

Vue.use(ElementUI, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value),
})

window.$ = window.jQuery = require('jquery');
window.moment = require('moment');

Vue.use(require('vue-moment'))

// register global utility filters.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

Vue.filter('formatDate', function(value) {
  if (value) {
    return window.moment(value).format('DD/MM/YYYY HH:mm:ss')
  }
})

Vue.directive('dialogDrag', {
  bind(el) {
  // Remarks: You can change the class name so that other elements can also achieve the drag effect
    // Mouse click and drag area
    const dialogFooterEl = el.querySelector('.el-dialog__footer');  
    const dialogHeaderEl = el.querySelector('.el-dialog__header');
      // Drag the element theme
      const dragDom = el;//.querySelector('.el-dialog__wrapper');
      console.log('drag DOM',dragDom)
      dialogHeaderEl.style.cursor = 'move';
      dialogFooterEl.style.cursor='move';
      // Get the original attribute ie dom element. CurrentStyle Firefox Google window.getComputedStyle (dom element, null);
      const sty = dragDom.currentStyle || window.getComputedStyle(dragDom, null);

      dialogHeaderEl.onmousedown = (e) => {
          // Press the mouse to calculate the distance between the current element and the visible area
          const disX = e.clientX - dialogHeaderEl.offsetLeft;
          const disY = e.clientY - dialogHeaderEl.offsetTop;

          // The obtained value has px regular matching and replacement
          let styL, styT;

          // Note that the value obtained for the first time in ie is assigned to px after the component comes with 50% movement
          if(sty.left.includes('%')) {
              // eslint-disable-next-line no-useless-escape
              styL = +document.body.clientWidth * (+sty.left.replace(/\%/g, '') / 100);
              // eslint-disable-next-line no-useless-escape
              styT = +document.body.clientHeight * (+sty.top.replace(/\%/g, '') / 100);
          }else {
              styL = +sty.left.split('px')[0];
              styT = +sty.top.split('px')[0];
          }

          document.onmousemove = function (e) {
              // Calculate the distance moved by event delegation
              const l = e.clientX - disX;
              const t = e.clientY - disY;

              // Move the current element
              dragDom.style.left = `${l + styL}px`;
              dragDom.style.top = `${t + styT}px`;

              // Pass the position at this time
              //binding.value({x:e.pageX,y:e.pageY})
          };

          document.onmouseup = function () {
              document.onmousemove = null;
              document.onmouseup = null;
          };
      }

      dialogFooterEl.onmousedown = (e) => {
        // Press the mouse to calculate the distance between the current element and the visible area
        const disX = e.clientX //- dialogFooterEl.offsetLeft;
        const disY = e.clientY //- dialogFooterEl.offsetTop;

        // The obtained value has px regular matching and replacement
        let styL, styT;

        // Note that the value obtained for the first time in ie is assigned to px after the component comes with 50% movement
        if(sty.left.includes('%')) {
            // eslint-disable-next-line no-useless-escape
            styL = +document.body.clientWidth * (+sty.left.replace(/\%/g, '') / 100);
            // eslint-disable-next-line no-useless-escape
            styT = +document.body.clientHeight * (+sty.top.replace(/\%/g, '') / 100);
        }else {
            styL = +sty.left.split('px')[0];
            styT = +sty.top.split('px')[0];
        }

        document.onmousemove = function (e) {
            // Calculate the distance moved by event delegation
            const l = e.clientX - disX;
            const t = e.clientY - disY;

            // Move the current element
            dragDom.style.left = `${l + styL}px`;
            dragDom.style.top = `${t + styT}px`;

            // Pass the position at this time
            //binding.value({x:e.pageX,y:e.pageY})
        };

        document.onmouseup = function () {
            document.onmousemove = null;
            document.onmouseup = null;
        };
    }
  }
})



/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: { App },
  template: '<App/>'
})
