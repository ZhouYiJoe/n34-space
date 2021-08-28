<template>
  <div class="container">
    <div class="component"><h2>用户注册</h2></div>
    <div class="component">
      <InfoInput label="用户名" is-password="false" name="username"
                 :model-value="userInfo.username"
                 @update:model-value="userInfo.username = $event"/>
    </div>
    <div class="component">
      <InfoInput label="密码" is-password="true" name="password"
                 :model-value="userInfo.password"
                 @update:model-value="userInfo.password = $event"/>
    </div>
    <div class="component">
      <InfoInput label="邮箱" is-password="false" name="email"
                 :model-value="userInfo.email"
                 @update:model-value="userInfo.email = $event"/>
    </div>
    <div class="component">
      <InfoInput label="昵称" is-password="false" name="nickname"
                 :model-value="userInfo.nickname"
                 @update:model-value="userInfo.nickname = $event"/>
    </div>
    <div class="component">
      <RegisterButton @register="$emit('register', userInfo)"/>
    </div>
    <div class="back-to-login">
      <BackToLogin @back-to-login="$emit('back-to-login')"/>
    </div>
  </div>
</template>

<script>
import RegisterButton from "@/components/RegisterButton";
import InfoInput from "@/components/InfoInput";
import BackToLogin from "@/components/BackToLogin";

export default {
  name: "RegisterForm",
  components: {BackToLogin, RegisterButton, InfoInput},
  data() {
    return {
      userInfo: {
        username: null,
        password: null,
        email: null,
        nickname: null
      }
    }
  }
}
</script>

<style scoped>
.container {
  background-color: white;
  width: 500px;
  border-radius: 10px;
  font-family: "Microsoft YaHei UI", serif;
}

.component {
  width: fit-content;
  margin: 30px auto;
}

.back-to-login {
  margin-left: 336px;
  margin-bottom: 10px;
}
</style>