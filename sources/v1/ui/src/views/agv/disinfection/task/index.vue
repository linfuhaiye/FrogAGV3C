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
      </div>
      <!-- 右边内容 -->
      <div class="flex-box flex-direction-column right-content">
        <!-- 配送任务 -->
        <div class="task-list-box flex-box flex-direction-column">
          <div class="task-list-title">配送任务</div>
          <!-- 查询框 -->
          <div class="flex-box setting-button-div flex-align-items-center">
            <div style="width:41vmin;" class="flex-box flex-direction-row flex-justify-content-center flex-align-items-center">
              <p class="search-title">生产日期：</p>
              <el-date-picker
              class="el-input"
              v-model="searchParams.executionTime"
              type="date"
              placeholder="选择日期"
              style="width: 100%;"
              :value-format="'yyyy-MM-dd'"
              ></el-date-picker>
            </div>
            <div style="width:41vmin;" class="flex-box flex-direction-row flex-justify-content-center flex-align-items-center">
              <span class="search-title">生产线：</span>
              <SelectIndex
                style="width: 100%;"
                v-model="searchParams.productLine"
                :url="'/agv/agvAreas/selectProductLines'"
                :isQueryCriteria="true"
                :defaultFirst="true"
                :valueIsCode="true"
                :valueIsNumber="false"
                :searchParams="selectSearchParams"
              ></SelectIndex>
            </div>
          </div>
          <!-- 列表 -->
          <div class="task-data-content" style="overflow:auto; overflow-x:visible; min-height:11.5%;">
            <div v-for="(item) in tasks" :key="item.id">
              <div class="flex-box flex-direction-row flex-align-items-center">
                <div class="task-list-name">{{item.productName+" （"+item.productLineCode+"） "}}</div>
                <!-- buttom click 方法  el-button -->
                <el-button @click="run(item)" round>前往叫料</el-button>
              </div>
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
      </div>
    </div>
    <el-button id="playButton" hidden @click="playMusic"></el-button>
    <audio id="promptTone" src="../static/voices/newTask.mp3" preload="auto"></audio>
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

<style scoped>
.flex-direction-row {
  -webkit-box-orient: horizontal;
  background: #f4e9e9;
  height: 60px;
}
.el-select {
  display: inline-block;
  position: relative;
  /* margin-top: 12px; */
  margin-left: -10px;
  width: 109px;
}
.el-input {
  transition: all 0.3s;
}
.flex-direction-row {
  background-color: unset;
}
</style>

<script>
import '../../product/home/home.scss';
import './task.scss';
import TaskOut from './taskOut';
import request from '@/utils/request';
import SelectIndex from '@/components/Select/index';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

const areaCoding = process.env.AREA_CODING;
export default {
  name: 'home',
  components: { TaskOut, SelectIndex },
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
      selectSearchParams: {
        code: 'PRODUCT_FILLING',
        areaCoding: areaCoding
      },
      searchParams: {
        areaCoding: areaCoding,
        type: 1,
        state: 1
      },
      tasks: [],
      taskOutPositionName: '',
      taskOutBom: null,
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
      this.$store.dispatch('updateTitle', '消毒间配货任务');
      this.$store.dispatch('updateNeedLogin', false);
      this.timer();
    },
    // 跳转到配送管理页面
    turn(url) {
      this.$router.push({ path: url });
    },
    run(ho) {
      this.$store.dispatch('riseValue', ho);
      this.$router.push({ path: '/disinfection/call' });
    },
    toggleShow() {
      this.state.taskOutVisible = false;
    },
    timer() {
      this.getDistributionTasks();
      if (this.timer) {
        clearInterval(this.timer);
      }
      this.timer = setInterval(() => {
        if (this.gettingFlag) {
          return;
        }
        this.gettingFlag = true;
        this.getDistributionTasks();
      }, 5000);
    },
    taskOut(bom) {
      this.taskOutBom = bom;
      this.taskOutPositionName = bom.name;
      this.state.taskOutVisible = true;
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
