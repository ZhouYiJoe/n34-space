<template>
  <b-row>
    <b-col id="login-form-container">
      <b-row class="mt-3">
        <b-col>
          <h2 style="text-align: center; font-weight: 700">
            用户登录
          </h2>
        </b-col>
      </b-row>
      <b-row class="mt-3">
        <b-col sm="8" offset-sm="2" cols="12" offset="0">
          <InfoInput ref="username-input" label="用户名" :is-password="false" name="username"/>
        </b-col>
      </b-row>
      <b-row class="mt-3">
        <b-col sm="8" offset-sm="2" cols="12" offset="0">
          <InfoInput ref="password-input" label="密码" :is-password="true" name="password"/>
        </b-col>
      </b-row>
      <b-row class="mt-3">
        <b-col offset-sm="4" sm="4" cols="6" offset="3">
          <div class="d-grid">
            <b-button variant="success" @click="$emit('login')">登录</b-button>
          </div>
        </b-col>
      </b-row>
      <router-link class="row mt-3 mb-3" tag="div" to="/register">
        <b-col offset-sm="4" sm="4" cols="6" offset="3">
          <div class="d-grid">
            <b-button variant="primary">注册</b-button>
          </div>
        </b-col>
      </router-link>
    </b-col>
  </b-row>
</template>

<script>
import InfoInput from "@/components/authentication/InfoInput";

export default {
  name: "LoginForm",
  components: {InfoInput}
}
</script>

<style scoped>
#login-form-container {
  background-color: white;
  border-radius: 20px;
  overflow: hidden;
}
</style>