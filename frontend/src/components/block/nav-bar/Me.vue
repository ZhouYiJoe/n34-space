<template>
  <li class="nav-item dropdown">
    <div class="nav-link dropdown-toggle"
         data-bs-toggle="dropdown"
         role="button"
         aria-expanded="false">{{ username }}
    </div>
    <ul class="dropdown-menu">
      <li>
        <div class="dropdown-item"
             @click="switchToOwnPage">我的主页
        </div>
      </li>
      <li>
        <div class="dropdown-item"
             @click="backToLogin">退出
        </div>
      </li>
    </ul>
  </li>
</template>

<script>
export default {
  name: "Me",
  props: ["username"],
  methods: {
    switchToOwnPage() {
      this.$router.replace("/posts/"
          + localStorage.getItem("username"));
    },

    backToLogin() {
      this.$router.replace("/login");
    }
  }
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