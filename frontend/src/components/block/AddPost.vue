<template>
  <div id="add-post">
    <div id="new-post-input">
      <NewPostInput ref="newPostInput"/>
    </div>
    <div id="new-post-button">
      <NewPostButton @add-new-post="$emit('add-new-post')"/>
    </div>
  </div>
</template>

<script>
import NewPostInput from "@/components/input/NewPostInput";
import NewPostButton from "@/components/button/NewPostButton";

export default {
  name: "AddPost",
  components: {NewPostButton, NewPostInput}
}
</script>

<style scoped>
#add-post {
  background-color: white;
  height: 122px;
  width: fit-content;
}

#new-post-button {
  margin-top: 0;
  margin-left: 262px;
}
</style>