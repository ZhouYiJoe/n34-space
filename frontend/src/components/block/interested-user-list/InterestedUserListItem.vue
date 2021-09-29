<template>
    <router-link tag="div" :to="'/posts/' + username"
                 id="interested-user-list-item">
      <div id="nickname">{{ nickname }}</div>
      <div id="username">{{ username }}</div>
    </router-link>
</template>

<script>
export default {
  name: "InterestedUserListItem",
  props: ["username", "nickname"]
}
</script>

<style scoped>
#interested-user-list-item {
  width: 290px;
  height: 60px;
  background-color: white;
  line-height: 20px;
  text-indent: 2em;
  overflow: hidden;
  transition: background-color 0.5s;
}

#interested-user-list-item:hover {
  background-color: #dcdcdc;
  cursor: pointer;
}

#nickname {
  height: 20px;
  margin: 5px 0;
  font-weight: 600;
  overflow: hidden;
}

#username {
  height: 20px;
  margin: 5px 0;
  font-size: 10px;
  color: #b7b9cc;
}
</style>