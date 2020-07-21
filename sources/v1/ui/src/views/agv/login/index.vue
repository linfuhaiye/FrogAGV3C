<template>
  <div class="flex-box flex-justify-content-center flex-align-items-center" style="height:100%">
    <div
      class="flex-box flex-direction-column flex-justify-content-center flex-align-items-center"
      style="width:30%;height:80%;background-color:red;color:white;font-size:48px;font-wight:700;margin-right:10%;"
      @click="adminLogin"
    >
      <div>点击进入</div>
      <div>管理页面</div>
    </div>
    <div
      class="flex-box flex-direction-column flex-justify-content-center flex-align-items-center"
      style="width:30%;height:80%;background-color:green;color:white;font-size:48px;font-wight:700;"
      @click="turn('/selectTeam')"
    >
      <div>点击进入</div>
      <div>选择班组</div>
    </div>
  </div>
</template>

<script>
  import './login.scss';

  /**
   * 登录页
   */
  export default {
    name: 'login',
    data() {
      return {};
    },
    methods: {
      adminLogin() {
        this.$store.dispatch('updateAuth', 'admin');
        this.$store.dispatch('updateTeamId', '');
        this.$store.dispatch('updateUserName', '管理员');
        this.$router.push({ path: 'dashboard' });
      },
      // 跳转到波次管理页面
      turn(url) {
        this.$store.dispatch('updateAuth', 'user');
        this.$router.push({ path: url });
      }
    }
  };
</script>
