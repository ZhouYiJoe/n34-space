<template>
  <div class="container">
    <h1>欢迎！{{ username }}</h1>
    <button @click="logout">退出</button>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      username: null
    }
  },
  created() {
    this.username = localStorage.getItem("username")
  },
  methods: {
    logout() {
      this.$router.replace("/login")
    }
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  background-color: white;
}
</style>