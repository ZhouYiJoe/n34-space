<template>
  <b-row style="border: deepskyblue solid 1px; padding: 5px; border-radius: 5px">
    <b-col>
      <b-row class="mb-1">
        <b-form-textarea v-model="newPostBody" ref="newPostInput"
                         placeholder="写下你的想法......" rows="5"/>
      </b-row>
      <b-row>
        <b-col offset-sm="10" sm="2" offset="9" cols="3" class="p-0">
          <b-button variant="outline-primary" @click="$emit('add-new-post')" block>
            提交
          </b-button>
        </b-col>
      </b-row>
    </b-col>
  </b-row>
</template>

<script>
export default {
  name: "AddPost",
  data() {
    return {
      newPostBody: ""
    }
  }
}
</script>

<style scoped>
</style>