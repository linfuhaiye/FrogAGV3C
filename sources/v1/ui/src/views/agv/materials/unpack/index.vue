<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <!-- 头部 -->
    <div class="content flex-box fillParent" style="height:100%">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
        >拆包配货</div>
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/materials/pack')"
        >包装配货</div>
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/materials/stock')"
        >备货任务</div>
      </div>
      <!-- 右边内容 -->
      <div
        class="flex-box"
        style="width:100%;margin-left:10px;margin-right:20px;margin-bottom:10px;"
      >
        <!-- 配送任务 -->
        <div class="task-list-box flex-box flex-direction-column">
          <div class="task-list-title">配送任务</div>
          <div style="overflow:auto; overflow-x:visible;">
            <div v-for="(item) in tasks" :key="item.id">
              <div class="task-list-name">{{item.productName}}</div>
              <div
                v-for="(bom) in item.callMaterialModels"
                :key="bom.id"
                class="flex-box"
                style=" margin-top:5px;"
              >
                <div class="task-list-bom-name">{{bom.materialName}}</div>
                <div class="task-list-bom-num">{{bom.count}}</div>
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
                <div class="position position-pointer" v-if="item.materialBoxId">
                  <div
                    style="height: 100%; line-heigt:0px;"
                  >{{formatShowName(item.materialBoxModel)}}</div>
                </div>
                <div class="position" v-else>（空）</div>
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
    >
      <TaskOut :bom="taskOutBom" @toggleShow="toggleShow" @reloadParent="loadingInfo"></TaskOut>
    </el-dialog>
  </div>
</template>

<script>
import '../../product/home/home.scss';
import '../pack/task.scss';
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
  methods: {
    loadingInfo() {
      this.$store.dispatch('updateTitle', '包材仓-拆包间任务');
      this.getSites();
      this.getDistributionTasks();
    },
    // 跳转到配送管理页面
    turn(url) {
      this.$router.push({ path: url });
    },
    toggleShow() {
      this.state.taskOutVisible = false;
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
          type: 6
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
          type: 4,
          state: 0
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
    formatShowName(item) {
      const showName = '';
      // if (
      //   !isEmpty(item.materialBoxMaterialModels) &&
      //   item.materialBoxMaterialModels.length > 0
      // ) {
      //   item.materialBoxMaterialModels.forEach(obj => {
      //     showName += obj.materialName + ' ' + obj.count + ' \n';
      //   });
      // }
      return showName;
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
