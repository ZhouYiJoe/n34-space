<template>
  <li class="nav-item dropdown">
    <div class="nav-link dropdown-toggle" data-bs-toggle="dropdown"
         role="button" aria-expanded="false">
      {{ username }}
    </div>
    <ul class="dropdown-menu">
      <li>
        <router-link class="dropdown-item" tag="div"
                     :to="'/posts/' + username">
          我的主页
        </router-link>
      </li>
      <li>
        <router-link class="dropdown-item" tag="div" to="/login">
          退出
        </router-link>
      </li>
    </ul>
  </li>
</template>

<script>
export default {
  name: "Me",
  props: ["username"]
}
</script>

<style scoped>
.dropdown-toggle {
  border-style: none;
}

.dropdown-item:hover {
  cursor: pointer;
}
</style>