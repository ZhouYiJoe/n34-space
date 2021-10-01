<template>
  <b-container fluid id="login-container">
    <b-row id="vertical-center">
      <b-col sm="4" offset-sm="4" offset="0" cols="12" id="login-form-col">
        <LoginForm ref="login-form" @login="login"/>
      </b-col>
    </b-row>
  </b-container>
</template>
<script>
import LoginForm from "@/components/authentication/LoginForm";
import axios from "axios";

export default {
  name: "Login",
  components: {LoginForm},
  methods: {
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
        if (response.data.status === "USER_NOT_FOUND") {
          alert("用户名不存在")
        } else if (response.data.status === "WRONG_PASSWORD") {
          alert("密码错误")
        } else if (response.data.status === "SUCCESS") {
          localStorage.setItem("token", response.data.payload);
          localStorage.setItem("username", username);
          self.$router.push("/posts/" + username);
        }
      })
    }
  }
}
</script>

<style scoped>
#login-container {
  height: 100%;
  position: absolute;
  background-image: url(../../public/img/login-bg.png);
}

#vertical-center {
  position: relative;
  top: 50%;
  transform: translateY(-50%);
}
</style>