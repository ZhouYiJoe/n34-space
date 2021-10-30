<template>
  <b-nav>
    <router-link to="/" tag="b-nav-item">主页</router-link>
    <router-link :to="'/posts/' + username" tag="b-nav-item">我的博文</router-link>
  </b-nav>
</template>

<script>
export default {
  name: "UserPageNavBar",

  props: ["username"]
}
</script>

<style scoped>

</style>