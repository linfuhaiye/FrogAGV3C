<template>
  <el-scrollbar wrapClass="scrollbar-wrapper">
    <el-menu
      mode="vertical"
      :show-timeout="200"
      :default-active="$route.path"
      :collapse="isCollapse"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
    >
      <SidebarItem
        v-for="route in permission_routers"
        :key="route.name"
        :item="route"
        :base-path="route.path"
      ></SidebarItem>
    </el-menu>
  </el-scrollbar>
</template>

<script>
import { mapGetters } from 'vuex';
import SidebarItem from './SidebarItem';

export default {
  components: { SidebarItem },
  computed: {
    ...mapGetters(['permission_routers', 'sidebar']),
    isCollapse() {
      return !this.sidebar.opened;
    }
  },
  created() {
    this.$store.dispatch('loadMenus', this.loginForm);
  }
};
</script>
