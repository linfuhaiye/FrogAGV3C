<template>
  <div class="dialog-height agv-dialog">
    <div class="flex-box flex-direction-column">
      <div class="flex-box flex-direction-row out-item">
        <!-- 当前班组 -->
        <div class="flex-box flex-direction-row fillParent">
          <div class="base-title">当前班组：</div>
          <div class="base-content">{{info.teamName}}</div>
        </div>
        <!-- 当前产线 -->
        <div class="flex-box flex-direction-row item-right">
          <div class="base-title">当前产线：</div>
          <div class="base-content">{{info.productLineCode}}</div>
        </div>
      </div>
      <div class="flex-box flex-direction-row out-item">
        <!-- 生产单号 -->
        <div class="flex-box flex-direction-row fillParent">
          <div class="base-title">生产单号：</div>
          <div class="base-content">{{info.productionOrderNo}}</div>
        </div>
        <!-- 执行日期 -->
        <div class="flex-box flex-direction-row item-right">
          <div class="base-title">执行日期：</div>
          <div class="base-content">{{info.executionTime}}</div>
        </div>
      </div>
      <!-- 选择产线 -->
      <div class="flex-box flex-direction-row out-item">
        <div class="base-title">选择产线：</div>
        <div>
          <el-select v-model="info.line" placeholder="请选择产线">
            <el-option :label="item.name" :value="item.code" v-for="(item) in lines" :key="item.id"></el-option>
          </el-select>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer" align="center">
      <el-button @click="toggleShow" class="dialog-cancel-btn">{{$t('table.cancel')}}</el-button>
      <el-button
        type="primary"
        @click="updateData"
        class="dialog-update-btn"
      >{{$t('table.changeLine')}}</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

const areaCoding = process.env.AREA_CODING;
export default {
  name: 'editBom',
  data() {
    return {
      info: {},
      // 加载对象
      load: null,
      selectSearchParams: {
        code: 'PRODUCT_FILLING',
        areaCoding: areaCoding
      },
      datas: [],
      lines: []
    };
  },
  created() {
    this.loadingInfo();
  },
  props: {
    product: [Object]
  },
  methods: {
    loadingInfo() {
      this.getProductLines();
      this.info = JSON.parse(JSON.stringify(this.product));
    },
    isEmpty,
    // 弹出框标志变化
    toggleShow() {
      this.$emit('toggleShow');
    },
    // 修改信息
    updateData() {
      if (isEmpty(this.info.line)) {
        this.$message.error('请选择产线');
      }
      this.load = this.showErrorMessage('正在更换产线，请稍候......');
      request({
        url: '/agv/waves/changeLine',
        method: 'PUT',
        params: {
          productOrderNo: this.info.productionOrderNo,
          lineCode: this.info.line,
          areaCoding: areaCoding
        }
      })
        .then(response => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.$message.success(response.data);
            this.$emit('toggleShow');
            this.$emit('reloadWaves');
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
    getProductLines() {
      this.load = this.showErrorMessage('正在获取生产线列表，请稍候......');
      request({
        url: '/agv/agvAreas/selectProductLines',
        method: 'GET',
        params: this.selectSearchParams
      })
        .then(response => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.lines = response.data;
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
<style>
.base-title {
  font-size: 30px;
}
.base-content {
  font-size: 20px;
}
.out-item {
  min-height: 30px;
  margin-top: 10px;
  height: 40px;
  line-height: 40px;
}
</style>