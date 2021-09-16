<template>
  <div id="post">
    <div id="post-drop-down">
      <PostDropDown @remove-post="$emit('remove-post')"
                    v-if="inOwnPage"/>
    </div>
    <div id="author" v-if="showAuthor === true">{{ author }}</div>
    <div id="date-created">{{ dateCreated }}</div>
    <div id="body">
      <slot/>
    </div>
  </div>
</template>

<script>
import PostDropDown from "@/components/dropdown/PostDropDown";
export default {
  name: "Post",
  components: {PostDropDown},
  props: ['dateCreated', 'author', 'showAuthor', 'inOwnPage']
};
</script>

<style scoped>
#post {
  width: 600px;
  min-height: 200px;
  background-color: white;
  margin-top: 10px;
  overflow: hidden;
  white-space: normal;
  word-break: break-all;
}

#author {
  width: fit-content;
  height: fit-content;
  margin: 5px auto;
}

#date-created {
  font-size: 13px;
  color: #bcc4c3;
  margin: 5px auto;
  width: fit-content;
  height: 24px;
  line-height: 24px;
}

#body {
  text-indent: 2em;
  width: 550px;
  margin: 0 auto;
}

#post-drop-down {
  float: right;
  height: 24px;
}
</style>