<template>
  <div
    class="flex-box flex-justify-content-center flex-align-items-center flex-direction-column"
    style="height:100%"
  >
    <div style="color:white; font-size:50px; font-weight:700;">请选择班组</div>
    <div
      class="flex-box flex-direction-row flex-wrap fillParent flex-justify-content-center"
      style="margin-top:20px;"
    >
      <div
        v-for="(team) in teams"
        :key="team.pk_defdoc"
        @click="chooseTeam(team)"
        style="width:120px; color:white; background-color:green; height:50px; line-height:50px; border-radius: 10px; font-size: 20px; font-weight:600; margin-left:20px;"
      >{{team.docname}}</div>
    </div>
  </div>
</template>

<script>
  import './login.scss';
  import { Loading } from 'element-ui';
  import request from '@/utils/request';
  import { isEmpty } from '@/utils/helper';

  /**
   * 登录页
   */
  export default {
    name: 'login',
    data() {
      return {
        teams: [],
        load: null
      };
    },
    created() {
      this.loadingInfo();
    },
    methods: {
      loadingInfo() {
        this.getTeams();
      },
      chooseTeam(team) {
        this.$store.dispatch('updateTeamId', team.pk_defdoc);
        this.$store.dispatch('updateUserName', team.docname);
        this.$router.push({ path: 'dashboard' });
      },
      getTeams() {
        request({
          url: '/agv/getData/getTeams',
          method: 'GET'
        })
          .then(response => {
            // 如果加载存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            if (response.errno === 0) {
              this.teams = response.data;
            }
          })
          .catch(_ => {
            // 如果加载存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            this.$message.error('服务器请求失败');
          });
      },
      // 遮罩层显示提示信息
      showErrorMessage(message) {
        const options = {
          lock: true,
          fullscreen: true,
          text: message,
          spinner: '',
          background: 'rgba(0, 0, 0, 0.7)'
        };
        return Loading.service(options);
      }
    }
  };
</script>
