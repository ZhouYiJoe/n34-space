<template>
  <button id="new-post-button"
          @click="$emit('add-new-post')">发布</button>
</template>

<script>
export default {
  name: "NewPostButton"
}
</script>

<style scoped>
#new-post-button {
  height: 30px;
  width: 70px;
  background-color: #ebf2ff;
  border-width: 1px;
  color: #007bff;
  border-color: rgba(0, 123, 255, 0);
  transition: background-color 0.5s, border-color 0.5s;
}

#new-post-button:hover {
  background-color: white;
  border-color: rgba(0, 123, 255, 1);
  cursor: pointer;
}
</style>