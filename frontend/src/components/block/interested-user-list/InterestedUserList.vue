<template>
  <div id="interested-user-list">
    <div id="interested-user-list-header">
      <InterestedUserListHeader/>
    </div>
    <div class="interested-user-list-item"
         v-for="user in interestedUsers"
         :key="user.username">
      <InterestedUserListItem :username="user.username"
                              :nickname="user.nickname"/>
    </div>
  </div>
</template>

<script>
import InterestedUserListHeader from "@/components/block/interested-user-list/InterestedUserListHeader";
import InterestedUserListItem from "@/components/block/interested-user-list/InterestedUserListItem";
export default {
  name: "InterestedUserList",
  components: {InterestedUserListItem, InterestedUserListHeader},
  data() {
    return {
      interestedUsers: null
    }
  }
}
</script>

<style scoped>
#interested-user-list {
  width: fit-content;
  height: fit-content;
}
</style>