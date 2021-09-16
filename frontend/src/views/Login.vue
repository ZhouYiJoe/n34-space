<template>
  <div id="login">
    <LoginForm ref="login-form"
               @register="switchToRegister"
               @login="login"/>
  </div>
</template>

<script>
import LoginForm from "@/components/block/LoginForm";
import axios from "axios";

export default {
  name: "Login",
  components: {LoginForm},
  methods: {
    switchToRegister() {
      this.$router.replace("register")
    },

    login() {
      let self = this;
      const username = this.$refs["login-form"]
          .$refs["username-input"].text;
      const password = this.$refs["login-form"]
          .$refs["password-input"].text;

      const usernameRegex = new RegExp("^[0-9a-zA-Z_]{3,16}$");
      if (usernameRegex.test(username) === false) {
        alert("用户名格式非法");
        return;
      }

      const passwordRegex = new RegExp("^[0-9a-zA-Z]{6,16}$");
      if (passwordRegex.test(password) === false) {
        alert("密码格式非法");
        return;
      }

      axios.get('/login', {
        headers: {
          username: username,
          password: password
        }
      }).then((response) => {
        let state = response.data.state
        if (state === "USER_NOT_EXISTS") {
          alert("用户名不存在")
        } else if (state === "WRONG_PASSWORD") {
          alert("密码错误")
        } else if (state === "LOGIN_SUCCESSFULLY") {
          localStorage.setItem("token", response.data.token);
          localStorage.setItem("username", username);
          self.$router.replace("/posts/" + username);
        }
      })
    }
  }
}
</script>

<style scoped>
#login {
  width: 100%;
  height: 100%;
  position: absolute;
  overflow: hidden;
  background-image: url(../../public/img/login-bg.png);
}
</style>