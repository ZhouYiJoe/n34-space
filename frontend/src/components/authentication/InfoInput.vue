<template>
  <b-row>
    <b-col>
      <b-row>
        <b-col cols="3">
          <label :for="name" class="col-form-label">{{ label }}:</label>
        </b-col>
        <b-col cols="9">
          <b-form-input :id="name" :type="type" v-model="text"></b-form-input>
        </b-col>
      </b-row>
      <b-row>
        <b-col offset="3" cols="9" style="text-align: center">
          <b-form-text>{{ tip }}</b-form-text>
        </b-col>
      </b-row>
    </b-col>
  </b-row>
</template>

<script>
export default {
  name: "InfoInput",
  props: ["label", "isPassword", 'name', 'tip'],
  data() {
    return {
      text: ""
    }
  },
  computed: {
    type() {
      return this.isPassword === true ? "password" : "text"
    }
  }
}
</script>

<style scoped>
</style>