<template>
  <LoginForm @register="switchToRegister"
             @login="login"/>
</template>

<script>
import LoginForm from "@/components/LoginForm";
import axios from "axios";

export default {
  name: "Login",
  components: {LoginForm},
  methods: {
    switchToRegister() {
      this.$router.replace("register")
    },

    login(userInfo) {
      let self = this
      axios.get('/login', {
        headers: {
          username: userInfo.username,
          password: userInfo.password
        }
      }).then((response) => {
        let state = response.data.state
        if (state === "USER_NOT_EXISTS") {
          alert("用户名不存在")
        } else if (state === "WRONG_PASSWORD") {
          alert("密码错误")
        } else if (state === "LOGIN_SUCCESSFULLY") {
          localStorage.setItem("token", response.data.token)
          localStorage.setItem("username", userInfo.username)
          self.$router.replace("/home")
        }

      })
    }
  }
}
</script>

<style scoped>
.container {
  margin: 200px auto;
  overflow: hidden;
}
</style>