<template>
  <b-row>
    <b-col style="padding: 20px">
      <b-row>
        <b-col>
          <p v-if="showAuthor">{{ author }}</p>
        </b-col>
      </b-row>
      <b-row class="mb-2">
        <b-col>
          <small>{{ dateCreated }}</small>
        </b-col>
        <b-col style="text-align: right">
          <PostDropdown v-if="inOwnPage" @remove-post="$emit('remove-post')"/>
        </b-col>
      </b-row>
      <b-row>
        <b-col>
          <slot/>
        </b-col>
      </b-row>
    </b-col>
  </b-row>
</template>

<script>
import PostDropdown from "@/components/posts-page/PostDropdown";
export default {
  name: "Post",
  components: {PostDropdown},
  props: ['dateCreated', 'author', 'showAuthor', 'inOwnPage']
};
</script>

<style scoped>
</style>