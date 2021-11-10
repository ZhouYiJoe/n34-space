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

axios.interceptors.request.use(config => {
    config.headers = {
        ...config.headers,
        token: localStorage.getItem("token")
    }
    return config
}, error => {
    return Promise.reject(error)
})

axios.interceptors.response.use(response => {
    if (response.data.status === "INVALID_TOKEN") {
        router.push("/login")
        return Promise.reject("登录信息已过期，请重新登陆")
    } else {
        return response
    }
}, error => {
    return Promise.reject(error)
})

Vue.config.productionTip = false
axios.defaults.baseURL = process.env.VUE_APP_AXIOS_BASE_URL

new Vue({
    render: h => h(App),
    router
}).$mount('#app')
