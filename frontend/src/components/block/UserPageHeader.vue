<template>
  <div id="user-page-header">
    <div id="title">{{ username }}的主页</div>
  </div>
</template>

<script>
export default {
  name: "UserPageHeader",
  props: ['username']
}
</script>

<style scoped>
#user-page-header {
  height: 200px;
  background-color: #49c6d8;
}

#title {
  text-align: center;
  color: white;
  font-size: 50px;
  line-height: 200px;
}
</style>