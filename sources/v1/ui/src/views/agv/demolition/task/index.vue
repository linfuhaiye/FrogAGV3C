<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <div class="content flex-box fillParent" style="height:100%;">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
        >配货任务</div>
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/demolition/call')"
        >叫料</div>
        <!-- <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/demolition/call/history')"
        >叫料历史</div>-->
      </div>
      <!-- 右边内容 -->
      <div
        class="flex-box flex-direction-column"
        style="width:100%;margin-left:10px;margin-right:20px;"
      >
        <!-- 配送任务 -->
        <div class="flex-box flex-direction-column task-list-box">
          <div class="task-list-title">配送任务</div>
          <div class="data-content">
            <div v-for="(item) in tasks" :key="item.id">
              <div class="flex-box flex-direction-row data-content-produce-row">
                <div class="task-list-name fillParent">{{item.productName}}</div>
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
        <div class="task-content fillParent">
          <!-- <div class="title">库位区</div> -->
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
  import './task.scss';
  import TaskOut from './taskOut';
  import request from '@/utils/request';
  import Constants from '@/utils/constants';
  import { isEmpty } from '@/utils/helper';
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
        this.$store.dispatch('updateTitle', '拆包间配货任务');
        this.$store.dispatch('updateNeedLogin', false);
        this.timer();
      },
      // 跳转
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
            type: 5
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
            type: 3,
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
      // 发货
      deliverGoods(wave) {
        const sendItem = {
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
            console.log(_);
          });
      },
      // 退回
      turnBack(bom) {
        const sendItem = {
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
      }
    }
  };
</script>
