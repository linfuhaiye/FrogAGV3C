<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <div class="content flex-box fillParent">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/disinfection/task')"
        >配货任务</div>
        <div
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
        >叫料</div>
        <!-- <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/disinfection/call/history')"
        >
          叫料历史
          <span class="pick">{{this.num}}</span>
        </div>-->
      </div>
      <!-- 右边内容 -->
      <div
        class="flex-box flex-direction-column"
        style="width:100%;margin-left:10px;margin-right:20px;"
      >
        <div class="flex-box flex-direction-row">
          <div>
            <span
              style="color:black;width: 115px;margin-top: 16px;margin-left: 32px;font-size: 23px;"
            >生产线：</span>
            <SelectIndex class="el-select" v-model="params.productLine" :url="''" :parentId="''"></SelectIndex>
          </div>
          <div>
            <p
              style="color:black;width: 115px;font-size: 23px;margin-bottom: -55px;margin-left: 14px;margin-top: 17px;"
            >生产日期：</p>
            <el-date-picker
              class="el-input"
              v-model="params.executionTime"
              type="date"
              placeholder="选择日期"
              style="width: 100%;"
              :value-format="'yyyy-MM-dd'"
            ></el-date-picker>
          </div>
        </div>
        <!-- 表头内容 -->
        <div class="flex-box data-header-content flex-align-items-center" style="width:100%;">
          <div class="data-header-name">名称</div>
          <div class="data-header-num">需求数量</div>
          <div class="data-header-done">状态</div>
          <div class="data-header-operation"></div>
        </div>
        <!-- 表格内容 -->
        <div class="flex-box data-content flex-direction-column" style="width:100%; height:11.5%;">
          <div v-for="(item) in callPlans" :key="item.id">
            <div
              class="data-content-produce-row flex-box flex-align-items-center"
              :id="'a'+item.id"
            >
              <div
                class="data-name"
              >{{item.materialName+" （"+item.productLineCode+"）"+item.executionTime+" 【总共"+item.waveModels.length+"条】"}}</div>
              <div class="bom-num"></div>
              <div class="bom-done"></div>
              <div class="data-content-produce-operation flex-box flex-align-items-center"></div>
            </div>
            <div
              :id="'waveModel'+item.id"
              class="data-wave"
              v-for="(wave,index) in item.waveModels"
              :key="wave.id"
            >
              <div class="data-content-wave-row flex-box flex-align-items-center" v-if="index < 5">
                <div class="wave-name">{{'波次' + (index + 1)}}</div>
                <div class="bom-num"></div>
                <div class="bom-done"></div>
                <div class="data-content-operation flex-box flex-align-items-center">
                  <div
                    class="bom-call"
                    @click="callWave(wave)"
                    v-if="!wave.isCalled"
                    style="width:90px;"
                  >叫料</div>
                </div>
                <div class="data-content-operation flex-box flex-align-items-center">
                  <div
                    class="bom-cancel"
                    @click="cancelWave(wave)"
                    v-if="wave.isCalled"
                    style="width:90px;"
                  >取消</div>
                </div>
              </div>
              <div
                v-if="index < 5"
                v-for="(bom) in wave.waveDetailModels"
                :key="bom.id"
                class="flex-box data-content-row"
              >
                <div class="bom-name textOverflow">{{bom.materialName}}</div>
                <div
                  class="bom-num flex-box flex-align-items-center flex-justify-content-center"
                >{{bom.count}}</div>
                <div class="bom-done flex-box flex-align-items-center flex-justify-content-center">
                  <span v-if="!isEmpty(bom.callId)">已叫料</span>
                  <span v-else>未叫料</span>
                </div>
                <div class="data-content-operation flex-box flex-align-items-center">
                  <!-- <div class="bom-delete" @click="callBom(bom)" v-if="isEmpty(bom.callId)">叫料</div> -->
                </div>
              </div>
            </div>
          </div>
        </div>
        <div id="poi">asdfawe</div>
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
.el-input {
  transition: all 0.3s;
  height: 10px;
  width: 80%;
  width: 100%;
  margin-top: 24px;
  margin-left: 142px;
}
</style>

<script>
import '../../product/home/home.scss';
import './call.scss';
import request from '@/utils/request';
// import Constants from '@/utils/constants';
import SelectIndex from '@/components/Select/index';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

export default {
  name: 'call',
  components: { SelectIndex },
  created() {
    this.loadingInfo();
  },
  mounted() {
    console.log('*****', this.gas);
  },
  data() {
    return {
      num: '99+',
      state: {},
      // 加载对象
      load: null,
      callPlans: [],
      params: {
        waveType: 1,
        callType: 3
      },
      gas: this.$store.state.msk
    };
  },
  methods: {
    loadingInfo() {
      this.$store.dispatch('updateTitle', '消毒间叫料');
      this.$store.dispatch('updateNeedLogin', false);
      this.timer();
    },
    isEmpty,
    // 跳转
    turn(url) {
      this.$router.push({ path: url });
    },
    toggleShow() {},
    timer() {
      this.getCallPlans();
      if (this.timer) {
        clearInterval(this.timer);
      }
      this.timer = setInterval(() => {
        this.getCallPlans();
        var uio = document.getElementById('a1182');
        console.log('444444', uio, document);
        uio.scrollTop = uio.scrollHeight;
      }, 5000);
    },
    // 叫波次
    callWave(wave) {
      if (!isEmpty(wave.waveDetailModels) && wave.waveDetailModels.length > 0) {
        wave.waveDetailModels.forEach(item => {
          item.areaType = 3;
        });
      }
      this.load = this.showErrorMessage('叫料中，请稍候......');
      request({
        url: '/agv/callMaterials/addWaveDetailCallMaterials',
        method: 'POST',
        data: wave.waveDetailModels
      })
        .then(response => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.getCallPlans();
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
    // 叫详情
    callBom(bom) {
      const callBoms = [];
      if (!isEmpty(bom)) {
        bom.areaType = 3;
        callBoms.push(bom);
      }
      this.load = this.showErrorMessage('叫料中，请稍后');
      request({
        url: '/agv/callMaterials/addWaveDetailCallMaterials',
        method: 'POST',
        data: callBoms
      })
        .then(response => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.getCallPlans();
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
    // 波次取消叫料
    cancelWave(wave) {
      this.load = this.showErrorMessage('正在取消，请稍后');
      request({
        url: '/agv/callMaterials/cancelWave',
        method: 'POST',
        params: {
          waveCode: wave.code
        }
      })
        .then(response => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.getCallPlans();
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
    // 获取叫料计划
    getCallPlans() {
      request({
        url: '/agv/waves/callPlans',
        method: 'GET',
        params: this.params
      })
        .then(response => {
          console.log(response);
          if (response.errno === 0) {
            this.callPlans = response.data;
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
