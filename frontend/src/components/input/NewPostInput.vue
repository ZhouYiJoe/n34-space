<template>
  <textarea placeholder="写下你的想法......" v-model="newPostBody"/>
</template>

<script>
export default {
  name: "NewPostInput",
  data() {
    return {
      newPostBody: ""
    }
  }
}
</script>

<style scoped>
textarea {
  width: 600px;
  height: 80px;
  border-top-color: #eaebec;
  border-bottom-color: #eaebec;
  border-left-width: 0;
  border-right-width: 0;
  resize: none;
  font-size: 15px;
  font-family: "Microsoft YaHei UI", serif;
  padding: 10px;
}

textarea:focus {
  outline: none;
}
</style>