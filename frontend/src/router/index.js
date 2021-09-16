import Vue from 'vue'
import VueRouter from "vue-router";
import Login from "@/views/Login";
import Register from "@/views/Register";
import User from "@/views/User";
import Home from "@/views/Home";
import axios from "axios";
import Posts from "@/views/Posts";
import FourOFour from "@/views/FourOFour";

Vue.use(VueRouter)

const router = new VueRouter({
    routes: [
        {
            path: "/login",
            component: Login
        },
        {
            path: "/register",
            component: Register
        },
        {
            path: "/user",
            component: User
        },
        {
            path: "/home",
            component: Home
        },
        {
            path: "/posts/:username",
            component: Posts
        },
        {
            path: "/404",
            component: FourOFour
        }
    ],
    mode: "history"
})

router.beforeEach((to, from, next) => {
    if (to.path.startsWith("/login") ||
        to.path.startsWith("/register")) {
        localStorage.removeItem("token")
        localStorage.removeItem("username")
        next()
    } else {
        let token = localStorage.getItem("token")
        axios.get("/check-token", {
            headers: {
                token: token
            }
        }).then((response) => {
            if (!response.data) {
                alert("登录信息已失效，请重新登录")
                next({path: "/login"})
            }
            next()
        })
    }
})

export default router