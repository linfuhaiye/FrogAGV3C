<template>
  <div class="flex-box flex-direction-column setting" style="height:100%">
    <div class="content flex-box fillParent">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/dashboard')"
        >配送管理</div>
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/wave')"
        >波次管理</div>
        <div
          v-if="auth==='admin'"
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
        >生产设置</div>
        <div
          v-if="auth==='admin'"
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/task')"
        >任务管理</div>
      </div>
      <!-- 右边内容 -->
      <div
        class="flex-box flex-direction-column right-content"
      >
        <!-- 按钮 -->
        <div class="flex-box content-button setting-button-div flex-align-items-center">
          <div
            class="btn btn-default btn-click flex-box flex-justify-content-center flex-align-items-center"
            @click="unSetting()"
          >未配置产品</div>
          <div
            class="btn btn-default flex-box flex-justify-content-center flex-align-items-center"
            @click="seted()"
          >已配置产品</div>
          <div
            class="btn btn-default btn-add flex-box flex-justify-content-center flex-align-items-center"
            @click="addPlan()"
          >新增生产计划</div>
          <div
            class="btn btn-default btn-update flex-box flex-justify-content-center flex-align-items-center"
            @click="updatePlans()"
          >更新计划</div>
        </div>
        <!-- 表头内容 -->
        <div
          class="flex-box data-header-content setting-table-div flex-align-items-center"
          style="width:100%;"
        >
          <div class="data-header-name">产品名称</div>
          <div class="data-header-sort">版本号</div>
          <div class="data-header-num">数量</div>
          <div class="data-header-operation"></div>
        </div>
        <!-- 表格内容 -->
        <div class="data-wave">
          <div class="flex-box data-content flex-direction-column" style="width:100%;">
            <div
              v-for="(bom) in boms"
              :key="bom.id"
              class="flex-box data-content-row setting-table-div"
            >
              <div
                class="bom-name flex-box flex-align-items-center flex-justify-content-center"
              >{{bom.materialName}}</div>
              <div
                class="bom-sort flex-box flex-align-items-center flex-justify-content-center"
              >{{bom.version}}</div>
              <div
                class="bom-num flex-box flex-align-items-center flex-justify-content-center"
              >{{bom.fullCount}}</div>
              <div class="data-content-operation flex-box flex-align-items-center">
                <div class="bom-save" @click="setting(bom)">配置</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog
      v-if="state.settingBomVisible"
      :visible.sync="state.settingBomVisible"
      title="配置BOM"
      class="dialog-transfer"
      :width="'80%'"
    >
      <BackBom :bomItem="settingBom" @toggleShow="toggleShow" @reloadData="getBoms"></BackBom>
    </el-dialog>
    <el-dialog
      v-if="state.addPlanVisible"
      :visible.sync="state.addPlanVisible"
      title="新增计划"
      class="dialog-transfer"
      :width="'80%'"
    >
      <SaveBom @toggleShow="toggleShow"></SaveBom>
    </el-dialog>
  </div>
</template>

<style scoped>
.content-button .btn {
    width: 16vmin;
}
</style>
<script>
import draggable from 'vuedraggable';
import '../home/home.scss';
import BackBom from './backBom';
import SaveBom from './saveBom';
import request from '@/utils/request';
// import Constants from '@/utils/constants';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

const areaTypeString = process.env.AREA_TYPE;
export default {
  name: 'home',
  components: { draggable, BackBom, SaveBom },
  created() {
    this.loadingInfo();
  },
  data() {
    return {
      state: {
        settingBomVisible: false,
        addPlanVisible: false
      },
      // 加载对象
      load: null,
      waveState: 0,
      settingBom: null,
      updateState: 0,
      boms: [],
      areaType: 1, // 区域类型,默认灌装区 1:灌装区;2:包装区
      auth: 'user',
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
      this.auth = this.$store.state.AgvHeader.auth;
      this.formateAreaType();
      this.timer();
    },
    formateAreaType() {
      if (areaTypeString === 'filling') {
        this.areaType = 1;
        this.$store.dispatch('updateTitle', '灌装区生产设置');
      } else if (areaTypeString === 'packing') {
        this.areaType = 2;
        this.$store.dispatch('updateTitle', '包装区生产设置');
      }
    },
    timer() {
      this.getBoms();
      if (this.timer) {
        clearInterval(this.timer);
      }
      this.timer = setInterval(() => {
        if (this.gettingFlag) {
          return;
        }
        this.gettingFlag = true;
        this.getBoms();
      }, 5000);
    },
    unSetting() {
      this.updateState = 0;
      this.getBoms();
    },
    seted() {
      this.updateState = 1;
      this.getBoms();
    },
    updatePlans() {
      this.load = this.showErrorMessage('计划更新中,请稍后...');
      request({
        url: '/agv/getData/updatePlans',
        method: 'GET'
      })
        .then((response) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.getBoms();
          }
        })
        .catch((_) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          this.$message.error('服务器请求失败,请重试...');
        });
    },
    getBoms() {
      request({
        url: '/agv/bom/getBoms',
        method: 'GET',
        params: {
          updateState: this.updateState
        }
      })
        .then((response) => {
          this.gettingFlag = false;
          if (response.errno === 0) {
            this.boms = response.data;
          }
        })
        .catch(() => {
          this.gettingFlag = false;
        });
    },
    // 跳转到指定页面
    turn(url) {
      this.$router.push({ path: url });
    },
    // 配置BOM信息
    setting(bom) {
      this.settingBom = bom;
      this.state.settingBomVisible = true;
    },
    addPlan() {
      this.state.addPlanVisible = true;
    },
    toggleShow() {
      this.state.settingBomVisible = false;
      this.state.addPlanVisible = false;
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
