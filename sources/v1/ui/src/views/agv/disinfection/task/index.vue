<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <div class="content flex-box fillParent" style="height:100%">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
        >配货任务</div>
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/disinfection/call')"
        >叫料</div>
        <!-- <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/disinfection/call/history')"
        >叫料历史</div>-->
      </div>
      <!-- 右边内容 -->
      <div
        class="flex-box"
        style="width:100%;margin-left:10px;margin-right:20px; margin-bottom:10px;"
      >
        <!-- 配送任务 -->
        <div class="task-list-box flex-box flex-direction-column">
          <div class="task-list-title">配送任务</div>
          <div style="overflow:auto; overflow-x:visible;">
            <div v-for="(item) in tasks" :key="item.id">
              <div class="task-list-name">{{item.productName+" （"+item.productLineCode+"） "}}</div>
              <div v-for="(bom) in item.callMaterialModels" :key="bom.id" style=" margin-top:5px;">
                <div
                  class="task-list-bom-name"
                  style="display:inline-block; width:65%;"
                >{{bom.materialName}}</div>
                <div class="task-list-bom-num" style="display:inline-block;">{{bom.count}}</div>
              </div>
            </div>
          </div>
        </div>
        <!-- 备货区 -->
        <div class="task-content">
          <div class="title">备货区</div>
          <div class="position-box flex-box flex-wrap">
            <div v-for="(item) in sites" :key="item.id">
              <div @click="taskOut(item)" class="pointer site-item">
                <div class="position position-pointer" v-if="item.materialBoxId">{{item.bomName}}</div>
                <div class="position" v-else></div>
                <div class="site-item-name">{{item.name}}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      v-if="state.taskOutVisible"
      :visible.sync="state.taskOutVisible"
      :title="taskOutPositionName"
      class="dialog-transfer"
      :width="'75%'"
    >
      <TaskOut :bom="taskOutBom" @toggleShow="toggleShow"></TaskOut>
    </el-dialog>
  </div>
</template>

<script>
import '../../product/home/home.scss';
import './task.scss';
import TaskOut from './taskOut';
import request from '@/utils/request';
// import Constants from '@/utils/constants';
// import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

export default {
  name: 'home',
  components: { TaskOut },
  created() {
    this.loadingInfo();
  },
  data() {
    return {
      state: {
        taskOutVisible: false
      },
      // 加载对象
      load: null,
      sites: [],
      tasks: [],
      taskOutPositionName: '',
      taskOutBom: null
    };
  },
  watch: {
    tasks: {
      deep: true,
      handler(oldVal, newVal) {
        console.log('tasks: ', oldVal, newVal);
      }
    }
  },
  methods: {
    loadingInfo() {
      this.$store.dispatch('updateTitle', '消毒间配货任务');
      this.$store.dispatch('updateNeedLogin', false);
      this.timer();
    },
    // 跳转到配送管理页面
    turn(url) {
      this.$router.push({ path: url });
    },
    toggleShow() {
      this.state.taskOutVisible = false;
    },
    timer() {
      this.getSites();
      this.getDistributionTasks();
      if (this.timer) {
        clearInterval(this.timer);
      }
      this.timer = setInterval(() => {
        this.getSites();
        this.getDistributionTasks();
      }, 5000);
    },
    taskOut(bom) {
      this.taskOutBom = bom;
      this.taskOutPositionName = bom.name;
      this.state.taskOutVisible = true;
    },
    getSites() {
      request({
        url: '/agv/sites',
        method: 'GET',
        params: {
          type: 4
        }
      })
        .then(response => {
          if (response.errno === 0) {
            this.sites = response.data;
          }
        })
        .catch(_ => {
          console.log(_);
        });
    },
    getDistributionTasks() {
      request({
        url: '/agv/callMaterials/distributionTasks',
        method: 'GET',
        params: {
          type: 1,
          state: 1
        }
      })
        .then(response => {
          if (response.errno === 0) {
            this.tasks = response.data;
          }
        })
        .catch(_ => {
          console.log(_);
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
