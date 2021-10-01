<template>
  <b-container fluid id="register-container">
    <b-row id="vertical-center">
      <b-col sm="6" offset-sm="3" cols="12" offset="0" id="register-form-col">
        <RegisterForm ref="register-form" @register="register"/>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import RegisterForm from "@/components/authentication/RegisterForm";
import axios from "axios";

export default {
  name: "Register",
  components: {RegisterForm},
  methods: {
    register() {
      let self = this
      const username = this.$refs["register-form"]
          .$refs["username-input"].text;
      const password = this.$refs["register-form"]
          .$refs["password-input"].text;
      const email = this.$refs["register-form"]
          .$refs["email-input"].text;
      const nickname = this.$refs["register-form"]
          .$refs["nickname-input"].text;

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

      const emailRegex = new RegExp(
          "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$");
      if (emailRegex.test(email) === false) {
        alert("邮箱格式非法");
        return;
      }

      const nicknameRegex = new RegExp("^\\S{1,20}$");
      if (nicknameRegex.test(nickname) === false) {
        alert("昵称格式非法");
        return;
      }

      axios.post("/register", {
        username: username,
        password: password,
        email: email,
        nickname: nickname
      }).then((response) => {
        if (response.data.status === "REPEATED_USERNAME") {
          alert("用户名已存在")
        } else if (response.data.status === "REPEATED_EMAIL") {
          alert("邮箱已被注册")
        } else if (response.data.status === "SUCCESS") {
          alert("注册成功，请进行登录")
          self.$router.push("/login")
        }
      })
    }
  }
}
</script>

<style scoped>
#register-container {
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