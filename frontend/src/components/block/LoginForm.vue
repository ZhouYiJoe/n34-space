<template>
  <div id="login-form">
    <div class="component"><h2>用户登录</h2></div>
    <div class="component">
      <InfoInput ref="username-input"
                 label="用户名"
                 :is-password="false"
                 name="username"
                 tip="用户名为3-16位的大小写字母、数字和下划线的组合"/>
    </div>
    <div class="component">
      <InfoInput ref="password-input"
                 label="密码"
                 :is-password="true"
                 name="password"
                 tip="密码为6-16位的大小写字母和数字的组合"/>
    </div>
    <div class="component">
      <LoginButton @login="$emit('login')"/>
    </div>
    <router-link class="component" tag="div" to="/register">
      <RegisterButton/>
    </router-link>
  </div>
</template>

<script>
import InfoInput from "@/components/input/InfoInput";
import LoginButton from "@/components/button/LoginButton";
import RegisterButton from "@/components/button/RegisterButton";

export default {
  name: "LoginForm",
  components: {RegisterButton, LoginButton, InfoInput}
}
</script>

<style scoped>
#login-form {
  background-color: white;
  width: 500px;
  border-radius: 20px;
  font-family: "Microsoft YaHei UI", serif;
  padding: 20px;
  margin: 175px auto;
}

.component {
  width: fit-content;
  margin: 15px auto;
}
</style>