<template>
  <div class="container">
    <div class="component"><h2>用户注册</h2></div>
    <div class="component">
      <InfoInput ref="username-input"
                 label="用户名"
                 is-password="false"
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
      <InfoInput ref="email-input"
                 label="邮箱"
                 is-password="false"
                 name="email"/>
    </div>
    <div class="component">
      <InfoInput ref="nickname-input"
                 label="昵称"
                 is-password="false"
                 name="nickname"
                 tip="昵称为1-20位不含空格的任意字符组合"/>
    </div>
    <div class="component">
      <RegisterButton @register="$emit('register')"/>
    </div>
    <div class="back-to-login">
      <BackToLogin/>
    </div>
  </div>
</template>

<script>
import RegisterButton from "@/components/button/RegisterButton";
import InfoInput from "@/components/input/InfoInput";
import BackToLogin from "@/components/button/BackToLogin";

export default {
  name: "RegisterForm",
  components: {BackToLogin, RegisterButton, InfoInput}
}
</script>

<style scoped>
.container {
  background-color: white;
  width: 510px;
  border-radius: 10px;
  font-family: "Microsoft YaHei UI", serif;
}

.component {
  width: fit-content;
  margin: 15px auto;
}

.back-to-login {
  margin-left: 336px;
  margin-bottom: 10px;
}
</style>