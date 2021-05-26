<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <div class="content flex-box fillParent" style="height:100%;">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
        >配货任务</div>
        <div v-if="areaCoding === '3B'"
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/demolition/call')"
        >叫料</div>
      </div>
      <!-- 右边内容 -->
      <div class="flex-box flex-direction-column right-content">
        <!-- 配送任务 -->
        <div class="flex-box flex-direction-column task-list-box" :class="areaCoding === '3B'?'':'task-list-box-all'">
          <div class="task-list-title">配送任务</div>
          <div class="data-content" :class="areaCoding === '3B'?'task-data-content':'task-data-content-all'">
            <div v-for="(item) in tasks" :key="item.id">
              <div class="flex-box flex-direction-row data-content-produce-row">
                <div
                  class="task-list-name fillParent"
                >{{item.productName + "[" + item.teamName + "]"}}</div>
                <div
                  style="width:200px;"
                  class="data-content-operation flex-box flex-align-items-center"
                >
                  <div class="bom-call" @click="deliverGoods(item)" style="width:90px;">配送</div>
                </div>
              </div>
              <div class="data-wave">
                <div
                  v-for="(bom) in item.callMaterialModels"
                  :key="bom.id"
                  class="flex-box data-content-row"
                >
                  <div class="task-list-bom-name textOverflow">{{bom.materialName}}</div>
                  <div class="task-list-bom-num">{{bom.count}}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 库位区 -->
        <div v-if="areaCoding === '3B'" class="task-content fillParent">
          <div class="position-box flex-box flex-wrap">
            <div v-for="(item) in sites" :key="item.id">
              <div @click="turnBack(item)" class="pointer site-item">
                <div class="position position-pointer">
                  <div style="height: 100%; line-heigt:0px;">退回</div>
                </div>
                <div class="site-item-name">{{item.name}}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-button id="playButton" hidden @click="playMusic"></el-button>
    <audio id="promptTone" src="../static/voices/newTask.mp3" preload="auto"></audio>
  </div>
</template>

<style scoped>
.task-list-box-all {
    width: 100%;
    height: 100%;
    background-color: #d99694;
}
.task-data-content {
  height: calc(100vh - 170px);
  background-color: #f4e9e9;
}
.task-data-content-all {
  height: calc(100vh - 90px);
}
</style>

<script>
import '../../product/home/home.scss';
import './task.scss';
import request from '@/utils/request';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

const areaCoding = process.env.AREA_CODING;
export default {
  name: 'home',
  components: {},
  created() {
    this.loadingInfo();
  },
  data() {
    return {
      // 加载对象
      load: null,
      searchParams: {
        areaCoding: areaCoding,
        type: 3,
        state: 1
      },
      sites: [],
      tasks: [],
      // 正在获取数据标志
      gettingFlag: false
    };
  },
  watch: {
    tasks: {
      deep: true,
      handler(newVal, oldVal) {
        if (!isEmpty(newVal)) {
          if (isEmpty(oldVal)) {
            document.getElementById('playButton').click();
          } else {
            for (const task of newVal) {
              if (JSON.stringify(oldVal).indexOf(JSON.stringify(task)) === -1) {
                document.getElementById('playButton').click();
                return;
              }
            }
          }
        }
      }
    }
  },
  destroyed() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    loadingInfo() {
      this.$store.dispatch('updateTitle', '拆包间配货任务');
      this.$store.dispatch('updateNeedLogin', false);
      this.timer();
    },
    // 跳转
    turn(url) {
      this.$router.push({ path: url });
    },
    timer() {
      this.getSites();
      this.getDistributionTasks();
      if (this.timer) {
        clearInterval(this.timer);
      }
      this.timer = setInterval(() => {
        if (this.gettingFlag) {
          return;
        }
        this.gettingFlag = true;
        this.getSites();
        this.getDistributionTasks();
      }, 5000);
    },
    getSites() {
      request({
        url: '/agv/sites',
        method: 'GET',
        params: {
          type: 5
        }
      })
        .then(response => {
          this.gettingFlag = false;
          if (response.errno === 0) {
            this.sites = response.data;
          }
        })
        .catch(() => {
          this.gettingFlag = false;
        });
    },
    getDistributionTasks() {
      request({
        url: '/agv/callMaterials/distributionTasks',
        method: 'GET',
        params: this.searchParams
      })
        .then(response => {
          this.gettingFlag = false;
          this.tasks = response.data;
        })
        .catch(() => {
          this.gettingFlag = false;
        });
    },
    // 发货
    deliverGoods(wave) {
      const sendItem = {
        areaCoding: areaCoding,
        waveCode: wave.waveCode,
        type: 7
      };
      this.load = this.showErrorMessage('发货中，请稍后');
      request({
        url: '/agv/delivery/addDeliveryTask',
        method: 'POST',
        data: sendItem
      })
        .then(response => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.getSites();
            this.getDistributionTasks();
          }
        })
        .catch(_ => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          this.$message.error('服务器请求失败');
        });
    },
    // 退回
    turnBack(bom) {
      const sendItem = {
        areaCoding: areaCoding,
        startSiteId: bom.id,
        materialBoxId: 0,
        type: 4
      };
      this.load = this.showErrorMessage('正在退回,请等待...');
      request({
        url: '/agv/delivery/addDeliveryTask',
        method: 'POST',
        data: sendItem
      })
        .then(response => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.getSites();
            this.getDistributionTasks();
            if (response.data === 'success') {
              this.$message.success('退货成功');
            } else {
              this.$message.error('退货失败：' + response.data);
            }
          }
        })
        .catch(_ => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          this.$message.error('服务器请求失败');
        });
    },
    // 用遮罩层显示错误信息
    showErrorMessage(message) {
      const options = {
        lock: true,
        fullscreen: true,
        text: message,
        spinner: '',
        background: 'rgba(0, 0, 0, 0.7)'
      };
      return Loading.service(options);
    },
    // 播放提示音
    playMusic() {
      const buttonAudio = document.getElementById('promptTone');
      buttonAudio.play();
    }
  }
};
</script>
