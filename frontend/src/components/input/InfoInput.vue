<template>
  <div>
    <div class="label">
      <label :for="name">{{ label }}:</label>
    </div>
    <div class="input">
      <input class="input-box"
             :type="getType(isPassword)"
             :id="name"
             v-model="text">
    </div><br>
    <div id="tip">{{ tip }}</div>
  </div>
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
  methods: {
    getType(isPassword) {
      return isPassword === true ? "password" : "text"
    }
  }
}
</script>

<style scoped>
.label {
  display: inline-block;
  width: 100px;
  font-weight: 700;
}

.input {
  display: inline-block;
}

.input-box {
  border-radius: 20px;
  border-width: 0;
  outline-style: none;
  background-color: #eeeeee;
  width: 200px;
  height: 40px;
  padding-left: 20px;
}

#tip {
  color: gray;
  margin-top: 10px;
  margin-left: 100px;
  width: 200px;
  text-align: center;
  font-size: 10px;
  height: fit-content;
}
</style>