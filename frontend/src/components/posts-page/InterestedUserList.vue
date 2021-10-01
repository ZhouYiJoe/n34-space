<template>
  <b-list-group>
    <b-list-group-item variant="primary">你可能感兴趣的用户</b-list-group-item>
    <router-link :to="'/posts/' + user.username" tag="b-list-group-item"
                 v-for="user in interestedUsers" :key="user.username"
                 class="interested-user-list-item">
      <h5>{{ user.nickname }}</h5>
      <small>{{ user.username }}</small>
    </router-link>
  </b-list-group>
</template>

<script>
export default {
  name: "InterestedUserList",
  data() {
    return {
      interestedUsers: null
    }
  }
}
</script>

<style scoped>
.interested-user-list-item {
  transition: background-color 0.5s;
}

.interested-user-list-item:hover {
  background-color: lightgray;
  cursor: pointer;
}
</style>