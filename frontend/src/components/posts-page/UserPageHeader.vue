<template>
  <b-row>
    <b-col id="user-page-header-container">
      <b-row id="title">
        <b-col>
          {{ nickname }}的主页
        </b-col>
      </b-row>
    </b-col>
  </b-row>
</template>

<script>
export default {
  name: "UserPageHeader",
  props: ['nickname']
}
</script>

<style scoped>
#user-page-header-container {
  height: 200px;
  background-color: #49c6d8;
}

#title {
  position: relative;
  top: 50%;
  transform: translateY(-50%);
  font-size: 50px;
  color: white;
  text-align: center;
}
</style>