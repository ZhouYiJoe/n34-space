<template>
  <button @click="$emit('register')">注册</button>
</template>

<script>
export default {
  name: "RegisterButton"
}
</script>

<style scoped>
button {
  border-width: 1px;
  border-style: solid;
  border-color: #757575;
  width: 220px;
  height: 40px;
  border-radius: 20px;
  background-color: white;
  color: black;
}

button:hover {
  background-color: #e3e3e3;
}

button:active {
  background-color: #bdbdbd;
}
</style>