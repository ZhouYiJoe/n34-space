import Vue from 'vue'
import VueRouter from "vue-router";
import Login from "@/views/Login";
import Register from "@/views/Register";
import axios from "axios";
import Posts from "@/views/Posts";
import FourOFour from "@/views/FourOFour";
import Home from "@/views/Home";

Vue.use(VueRouter)

const router = new VueRouter({
    routes: [
        {
            path: "/",
            component: Home
        },
        {
            path: "/login",
            component: Login
        },
        {
            path: "/register",
            component: Register
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
            if (response.data.status === "FAILED") {
                next({path: "/login"})
            } else if (response.data.status === "SUCCESS") {
                next()
            }
        })
    }
})

export default router