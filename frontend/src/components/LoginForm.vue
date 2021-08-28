<template>
  <div class="container">
    <div class="component"><h2>用户登录</h2></div>
    <div class="component">
      <InfoInput label="用户名" is-password="false"
                 name="username" :model-value="userInfo.username"
                 @update:model-value="userInfo.username = $event"/>
    </div>
    <div class="component">
      <InfoInput label="密码" is-password="true"
                 name="password" :model-value="userInfo.password"
                 @update:model-value="userInfo.password = $event"/>
    </div>
    <div class="component">
      <LoginButton @login="$emit('login',userInfo)"/>
    </div>
    <div class="component">
      <RegisterButton @register="$emit('register')"/>
    </div>
  </div>
</template>

<script>
import InfoInput from "@/components/InfoInput";
import LoginButton from "@/components/LoginButton";
import RegisterButton from "@/components/RegisterButton";

export default {
  name: "LoginForm",
  components: {RegisterButton, LoginButton, InfoInput},
  data() {
    return {
      userInfo: {
        username: null,
        password: null
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
</style>