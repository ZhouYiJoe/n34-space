<template>
  <b-dropdown variant="link" right>
    <b-dropdown-item-button @click="$emit('remove-post')">
      删除
    </b-dropdown-item-button>
  </b-dropdown>
</template>

<script>
export default {
  name: "PostDropdown"
}
</script>

<style scoped>

</style>