<template>
  <b-row>
    <b-col id="register-form-container">
      <b-row class="mt-3">
        <b-col>
          <h2 style="text-align: center; font-weight: 700">
            用户注册
          </h2>
        </b-col>
      </b-row>
      <b-row class="mt-3">
        <b-col sm="10" offset-sm="1" cols="12" offset="0">
          <InfoInput ref="username-input" label="用户名" :is-password="false"
                     name="username" tip="用户名为3-16位的可包含大小写字母、数字和下划线的字符组合(不区分大小写)"/>
        </b-col>
      </b-row>
      <b-row class="mt-3">
        <b-col sm="10" offset-sm="1" cols="12" offset="0">
          <InfoInput ref="password-input" label="密码" :is-password="true"
                     name="password" tip="密码为6-16位的可包含大小写字母和数字的字符组合"/>
        </b-col>
      </b-row>
      <b-row class="mt-3">
        <b-col sm="10" offset-sm="1" cols="12" offset="0">
          <InfoInput ref="email-input" label="邮箱" :is-password="false"
                     name="email" tip="注意邮箱地址的格式"/>
        </b-col>
      </b-row>
      <b-row class="mt-3">
        <b-col sm="10" offset-sm="1" cols="12" offset="0">
          <InfoInput ref="nickname-input" label="昵称" :is-password="false"
                     name="nickname" tip="昵称为1-20位不含空格的任意字符组合"/>
        </b-col>
      </b-row>
      <b-row class="mt-3">
        <b-col offset-sm="4" sm="4" cols="6" offset="3">
          <div class="d-grid">
            <b-button variant="primary" @click="$emit('register')">注册</b-button>
          </div>
        </b-col>
      </b-row>
      <b-row class="mt-3">
        <b-col sm="3" offset-sm="9" cols="6" offset="6">
          <router-link tag="div" to="/login" id="back-to-login-container">已有账号，直接登录>></router-link>
        </b-col>
      </b-row>
    </b-col>
  </b-row>
</template>

<script>
import InfoInput from "@/components/authentication/InfoInput";

export default {
  name: "RegisterForm",
  components: {InfoInput}
}
</script>

<style scoped>
#register-form-container {
  background-color: white;
  border-radius: 20px;
  overflow: hidden;
}

#back-to-login-container {
  font-size: 14px;
  color: deepskyblue;
}

#back-to-login-container:hover {
  color: blue;
  cursor: pointer;
}
</style>