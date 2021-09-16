<template>
  <button @click="$emit('login')">登录</button>
</template>

<script>
export default {
  name: "LoginButton"
}
</script>

<style scoped>
button {
  border-width: 0;
  width: 220px;
  height: 40px;
  border-radius: 20px;
  background-color: #57b847;
  color: white;
}

button:hover {
  background-color: #49a03e;
  cursor: pointer;
}

button:active {
  background-color: #3e8834;
}
</style>