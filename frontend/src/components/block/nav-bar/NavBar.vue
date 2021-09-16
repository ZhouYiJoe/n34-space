<template>
  <ul class="nav nav-tabs justify-content-end" id="nav-bar">
    <Me :username="username"/>
  </ul>
</template>

<script>
import Me from "@/components/block/nav-bar/Me";
export default {
  name: "NavBar",
  components: {Me},
  props: ["username"]
}
</script>

<style scoped>
#nav-bar {
  background-color: #1d2f38;
}
</style>