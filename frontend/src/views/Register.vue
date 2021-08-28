<template>
  <RegisterForm @register="register"
                @back-to-login="backToLogin"/>
</template>

<script>
import RegisterForm from "@/components/RegisterForm";
import axios from "axios";

export default {
  name: "Register",
  components: {RegisterForm},
  methods: {
    backToLogin() {
      this.$router.replace("/login")
    },

    register(userInfo) {
      let self = this
      axios.post("/register", {
        username: userInfo.username,
        password: userInfo.password,
        email: userInfo.email,
        nickname: userInfo.nickname
      }).then((response) => {
        let state = response.data.state
        if (state === "USERNAME_ALREADY_EXISTS") {
          alert("用户名已存在")
        } else if (state === "EMAIL_ALREADY_EXISTS") {
          alert("邮箱已被注册")
        } else if (state === "REGISTER_SUCCESSFULLY") {
          alert("注册成功，请进行登录")
          self.$router.replace("/login")
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  margin: 150px auto;
  overflow: hidden;
}
</style>