<template>
  <b-row>
    <b-col style="padding: 20px">
      <b-row class="mb-4">
        <b-col>
          <b-row>
            <b-col>
              <div>{{ author }}</div>
            </b-col>
          </b-row>
          <b-row>
            <b-col>
              <small>{{ dateCreated }}</small>
            </b-col>
          </b-row>
        </b-col>
        <b-col style="text-align: right">
          <PostDropdown v-if="showDropDown" @remove-post="$emit('remove-post')"/>
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
  props: ['dateCreated', 'author', 'showDropDown']
};
</script>

<style scoped>
</style>