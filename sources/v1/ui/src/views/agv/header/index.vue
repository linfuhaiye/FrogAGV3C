<template>
  <div class="flex-box header-box flex-justify-content-space-between flex-align-items-center">
    <div class="header-time">{{this.currentTime}}</div>
    <div class="header-title">{{this.title}}</div>
    <div class="header-user flex-box flex-align-items-center">
      <div class="header-user-name" v-if="this.needLogin">{{this.userName}}</div>
      <el-dropdown v-if="this.needLogin">
        <div class="header-icon"></div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item divided>
            <span @click="logout" style="display:block;">{{$t('navbar.logOut')}}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>
<script>
import './header.scss';
// import mqtt from 'mqtt';
import { isEmpty } from '@/utils/helper';

// const options = {
//   connectTimeout: 40000,
//   clientId: 'mqtitId-Home',
//   username: process.env.MQTT_USERNAME,
//   password: process.env.MQTT_PASSWORD,
//   clean: true
// };
// 创建MQTT连接
// var client = mqtt.connect(process.env.MQTT_SERVICE, options);

export default {
  name: 'AGVheaders',
  data() {
    return {
      currentTime: '',
      userName: '请选择班组',
      teamId: ''
    };
  },
  created() {
    this.getTime();
    // this.mqttMSG();
    this.userName = isEmpty(this.$store.state.AgvHeader.userName)
      ? ''
      : this.$store.state.AgvHeader.userName;
    this.teamId = this.$store.state.AgvHeader.teamId;
  },
  computed: {
    title() {
      return this.$store.state.AgvHeader.title;
    },
    needLogin() {
      return this.$store.state.AgvHeader.needLogin;
    }
  },
  mounted() {
    const _this = this;
    if (!this.timer) {
      this.timer = setInterval(() => {
        _this.getTime();
      }, 500);
    }
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
    }
  },
  props: {},
  methods: {
    // mqttMSG() {
    //   console.log('mqttMSG>>>>>>>>>>>>>>>');
    //   // mqtt连接
    //   client.on('connect', e => {
    //     console.log('连接成功:');
    //     client.subscribe('/World1234', { qos: 1 }, error => {
    //       if (!error) {
    //         console.log('订阅成功');
    //       } else {
    //         console.log('订阅失败');
    //       }
    //     });
    //   });
    //   // 接收消息处理
    //   client.on('message', (topic, message) => {
    //     console.log('收到来自', topic, '的消息', message.toString());
    //     this.msg = message.toString();
    //   });
    //   // 断开发起重连
    //   client.on('reconnect', error => {
    //     console.log('正在重连:', error);
    //   });
    //   // 链接异常处理
    //   client.on('error', error => {
    //     console.log('连接失败:', error);
    //   });
    // },
    getTime: function() {
      var _this = this;
      const yy = new Date().getFullYear();
      const mm = new Date().getMonth() + 1;
      const dd = new Date().getDate();
      const hh = new Date().getHours();
      const mf =
        new Date().getMinutes() < 10
          ? '0' + new Date().getMinutes()
          : new Date().getMinutes();
      const ss =
        new Date().getSeconds() < 10
          ? '0' + new Date().getSeconds()
          : new Date().getSeconds();
      _this.currentTime =
        yy + '-' + mm + '-' + dd + ' ' + hh + ':' + mf + ':' + ss;
    },
    logout() {
      this.$store.dispatch('updateTeamId', '');
      this.$store.dispatch('updateUserName', '请选择班组');
      this.$store.dispatch('LogOut').then(() => {
        location.reload(); // In order to re-instantiate the vue-router object to avoid bugs
      });
    }
  }
};
</script>
