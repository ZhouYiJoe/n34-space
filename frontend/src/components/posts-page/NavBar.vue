<template>
  <b-navbar type="dark" variant="info">
    <b-navbar-nav class="ml-auto">
      <b-nav-item-dropdown :text="username" right>
        <router-link :to="'/posts/' + username" tag="b-dropdown-item">我的主页</router-link>
        <router-link to="/login" tag="b-dropdown-item">退出</router-link>
      </b-nav-item-dropdown>
    </b-navbar-nav>
  </b-navbar>
</template>

<script>

export default {
  name: "NavBar",
  props: ["username"]
}
</script>

<style scoped>
</style>