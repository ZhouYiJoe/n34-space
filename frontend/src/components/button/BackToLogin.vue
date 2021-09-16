<template>
  <div class="container" @click="$emit('back-to-login')">已有账号，直接登录>></div>
</template>

<script>
export default {
  name: "BackToLogin"
}
</script>

<style scoped>
.container {
  display: inline-block;
  font-size: 14px;
  color: deepskyblue;
}

.container:hover {
  color: blue;
  cursor: pointer;
}
</style>