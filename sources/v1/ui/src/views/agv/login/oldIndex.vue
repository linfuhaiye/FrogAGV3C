<template>
  <div class="flex-box flex-justify-content-center flex-align-items-center" style="height:100%">
    <div class="login-form">
      <div class="flex-box form-row">
        <div class="form-label">员工编号：</div>
        <div class="form-value">{{this.info.userCode}}</div>
      </div>
      <div class="flex-box form-row">
        <div class="form-label">班组号：</div>
        <div class="form-value">{{this.info.teanCode}}</div>
      </div>
      <div class="flex-box form-row">
        <div class="form-label">姓名：</div>
        <div class="form-value">{{this.info.userName}}</div>
      </div>
      <div
        class="flex-box card-row flex-justify-content-center flex-align-items-center"
        @click="turnHome"
      >
        <div class="card-label">请刷工号</div>
        <div class="card-icon"></div>
      </div>
    </div>
  </div>
</template>

<script>
  import './login.scss';
  import { Loading } from 'element-ui';

  /**
   * 登录页
   */
  export default {
    name: 'login',
    data() {
      return {
        info: {
          userCode: 'A123456',
          teanCode: 'B03组',
          userName: '王某某'
        },
        teams: [],
        load: null
      };
    },
    methods: {
      turnHome() {
        this.$router.push({ path: 'dashboard' });
      },
      getTeams() {
        request({
          url: '/external/teams',
          method: 'GET'
        })
          .then(response => {
            console.log(response);
            if (response.errno === 0) {
              // 如果加载存在
              if (!isEmpty(this.load)) {
                this.load.close();
              }
            }
          })
          .catch(_ => {
            this.load = this.showErrorMessage('服务器请求失败');
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
