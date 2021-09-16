<template>
  <div class="dropdown">
    <button class="btn btn-secondary dropdown-toggle"
            type="button"
            id="dropdownMenuButton1"
            data-bs-toggle="dropdown"
            aria-expanded="false">
    </button>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
      <li><div class="dropdown-item" @click="$emit('remove-post')">删除</div></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "PostDropDown"
}
</script>

<style scoped>
.btn {
  background-color: white;
  border: none;
}

.btn-secondary {
  color: black;
  transition: color 0.5s;
}

.btn:focus {
  box-shadow: none;
}

.btn-secondary:hover {
  color: gray;
}

.dropdown-item {
  font-size: 15px;
}

.dropdown-item:hover {
  cursor: pointer;
}
</style>