<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <div class="content flex-box fillParent">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
        >配送管理</div>
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/wave')"
        >波次管理</div>
        <div
          v-if="auth==='admin'"
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/setting')"
        >生产设置</div>
        <div
          v-if="auth==='admin'"
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/task')"
        >任务管理</div>
        <!-- <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/call/history')"
        >叫料历史</div>-->
      </div>
      <!-- 右边内容 -->
      <div class="flex-box flex-direction-column right-content">
        <!-- 表头内容 -->
        <div class="flex-box data-header-content flex-align-items-center" style="width:100%;">
          <div class="data-header-source">起始点</div>
          <div class="data-header-destination">目标点</div>
          <div class="data-header-status">任务ID</div>
          <div class="data-header-status">任务状态</div>
          <div class="data-header-operation"></div>
        </div>
        <!-- 表格内容 -->
        <div class="flex-box data-content flex-direction-column" style="width:100%; color:black; height:calc(100vh - 110px);">
          <div v-for="(item) in tasks" :key="item.id">
            <div class="flex-box data-content-row">
              <div
                class="content-source flex-box flex-align-items-center flex-justify-content-center"
              >{{item.sourceName}}</div>
              <div
                class="content-destination flex-box flex-align-items-center flex-justify-content-center"
              >{{item.destinationName}}</div>
              <div
                class="content-status flex-box flex-align-items-center flex-justify-content-center"
              >{{item.wcsTaskId}}</div>
              <div
                class="content-status flex-box flex-align-items-center flex-justify-content-center"
              >{{formmateTaskState(item.status)}}</div>
              <div class="data-content-operation flex-box flex-align-items-center">
                <div class="bom-delete" @click="cancelTask(item.wcsTaskId)">取消</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.flex-direction-row {
  -webkit-box-orient: horizontal;
  background: #f4e9e9;
  height: 60px;
}
.el-select {
  display: inline-block;
  position: relative;
  margin-top: 12px;
}
</style>

<script>
import '../home/home.scss';
import request from '@/utils/request';
import Constants from '@/utils/constants';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

const areaTypeString = process.env.AREA_TYPE;
const areaCoding = process.env.AREA_CODING;
// 配送管理
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
      tasks: [],
      waveState: 0,
      teamId: '',
      areaType: 1, // 区域类型,默认灌装区 1:灌装区;2:包装区
      auth: 'user',
      searchParams: {},
      selectSearchParams: {
        code: 'PRODUCT_FILLING',
        areaCoding: areaCoding
      },
      // 正在获取数据标志
      gettingFlag: false
    };
  },
  destroyed() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    loadingInfo() {
      this.teamId = this.$store.state.AgvHeader.teamId;
      this.auth = this.$store.state.AgvHeader.auth;
      this.formateAreaType();
      this.timer();
    },
    formateAreaType() {
      if (areaTypeString === 'filling') {
        this.areaType = 1;
        this.$store.dispatch('updateTitle', '灌装区任务管理');
      } else if (areaTypeString === 'packing') {
        this.areaType = 2;
        this.$store.dispatch('updateTitle', '包装区任务管理');
      }
    },
    formmateTaskState(status) {
      let stateName = '';
      Constants.agvTaskState.forEach(item => {
        if (item.value === status) {
          stateName = item.label;
        }
      });
      return stateName;
    },
    timer() {
      this.getTasks();
      if (this.timer) {
        clearInterval(this.timer);
      }
      this.timer = setInterval(() => {
        if (this.gettingFlag) {
          return;
        }
        this.gettingFlag = true;
        this.getTasks();
      }, 5000);
    },
    // 跳转到指定页面
    turn(url) {
      this.$router.push({ path: url });
    },
    // 删除任务
    cancelTask(wcsTaskId) {
      this.load = this.showErrorMessage('正在取消,请稍后...');
      request({
        url: '/agv/cancel/task',
        method: 'GET',
        params: {
          wcsTaskId: wcsTaskId
        }
      })
        .then(response => {
          console.log(response);
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.getTasks();
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
    getTasks() {
      request({
        url: '/agv/getAllTasks',
        method: 'GET'
      })
        .then(response => {
          console.log('getTasks:', response);
          this.gettingFlag = false;
          if (response.errno === 0) {
            this.tasks = response.data;
          }
        })
        .catch(() => {
          this.gettingFlag = false;
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
    }
  }
};
</script>
