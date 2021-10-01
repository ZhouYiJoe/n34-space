import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from "axios"
import {BootstrapVue, IconsPlugin, DropdownPlugin} from "bootstrap-vue"

import "bootstrap/dist/css/bootstrap.css"
import "bootstrap-vue/dist/bootstrap-vue.css"

Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.use(DropdownPlugin)

Vue.config.productionTip = false
axios.defaults.baseURL = "http://localhost:8080"

new Vue({
  render: h => h(App),
  router,
  axios
}).$mount('#app')
