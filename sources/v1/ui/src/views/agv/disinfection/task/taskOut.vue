<template>
  <div class="dialog-height agv-dialog">
    <div class="flex-box flex-direction-column">
      <!-- 库位信息 -->
      <div class="flex-box flex-direction-row out-item">
        <div class="flex-box flex-direction-row">
          <div class="title">库位状态：</div>
          <div class="content">{{formmatSiteState(info.siteDetailState)}}</div>
        </div>
      </div>
      <!-- 料车信息 -->
      <div class="flex-box flex-direction-row out-item">
        <!-- 当前料车 -->
        <div class="flex-box flex-direction-row fillParent">
          <div class="title">当前料车：</div>
          <div class="content">{{isEmpty(info.materialBoxId)?'无':info.materialBoxModel.name}}</div>
        </div>
        <!-- 料车状态 -->
        <div class="flex-box flex-direction-row item-right">
          <div class="title">料车状态：</div>
          <div
            class="content"
          >{{isEmpty(info.materialBoxId)?'':formmatMaterialBoxState(info.materialBoxModel.state)}}</div>
        </div>
      </div>
      <!-- 任务信息 -->
      <div v-if="!isEmpty(info.deliveryTaskId)" class="flex-box flex-direction-row out-item">
        <!-- 当前任务 -->
        <div class="flex-box flex-direction-row fillParent">
          <div class="title">配送任务：</div>
          <div class="content">{{info.deliveryTaskModel.taskNo}}</div>
        </div>
        <!-- 任务状态 -->
        <div class="flex-box flex-direction-row item-right">
          <div class="title">任务状态：</div>
          <div class="content">{{formmatDeliveryTaskState(info.deliveryTaskModel.state)}}</div>
        </div>
      </div>
      <!-- 原料清单 -->
      <div class="out-item">
        <div class="title">原料清单：</div>
        <div>
          <el-table ref="multipleTable" :data="datas" style="width: 100%">
            <el-table-column label="名称" width="auto">
              <template slot-scope="scope" class="textOverflow">{{ scope.row.materialName }}</template>
            </el-table-column>
            <el-table-column label="规格" width="150">
              <template slot-scope="scope">{{ scope.row.materialSpecs }}</template>
            </el-table-column>
            <el-table-column label="单位" width="100">
              <template slot-scope="scope">{{ scope.row.materialUnit }}</template>
            </el-table-column>
            <el-table-column label="数量" width="100">
              <template slot-scope="scope">{{ scope.row.count }}</template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <!-- 选择产线 -->
      <div class="flex-box flex-direction-row out-item">
        <div class="title">选择产线：</div>
        <div>
          <el-select v-model="info.line" placeholder="请选择产线">
            <el-option :label="item.name" :value="item.id" v-for="(item) in lines" :key="item.id"></el-option>
          </el-select>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer" align="center">
      <el-button @click="toggleShow" class="dialog-cancel-btn">{{$t('table.cancel')}}</el-button>
      <el-button type="primary" @click="updateData" class="dialog-update-btn">{{$t('table.send')}}</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request';
import Constants from '@/utils/constants';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

const areaCoding = process.env.AREA_CODING;
export default {
  name: 'editBom',
  data() {
    return {
      info: {
        name: '原料A',
        num: 50,
        unit: '个'
      },
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
    bom: [Object]
  },
  methods: {
    loadingInfo() {
      this.getSiteInfo();
      this.getProductLines();
    },
    isEmpty,
    // 弹出框标志变化
    toggleShow() {
      this.$emit('toggleShow');
    },
    // 修改信息
    updateData() {
      console.log('updateData>>>>>>>>>>>');
      this.$emit('toggleShow');
    },
    getSiteInfo() {
      this.load = this.showErrorMessage('正在获取站点信息，请稍候......');
      request({
        url: '/agv/sites/' + this.bom.id,
        method: 'GET'
      })
        .then((response) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.info = response.data;
            if (!isEmpty(response.data)) {
              this.formmatMaterials(response.data);
            }
          }
        })
        .catch((_) => {
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
        .then((response) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.lines = response.data;
          }
        })
        .catch((_) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          this.$message.error('服务器请求失败');
        });
    },
    // 格式化站点状态
    formmatSiteState(siteState) {
      let stateName = '';
      Constants.siteState.forEach((item) => {
        if (item.value === siteState) {
          stateName = item.label;
        }
      });
      return stateName;
    },
    formmatDeliveryTaskState(deliveryTaskState) {
      let stateName = '';
      Constants.deliveryTaskState.forEach((item) => {
        if (item.value === deliveryTaskState) {
          stateName = item.label;
        }
      });
      return stateName;
    },
    formmatMaterialBoxState(materialBoxState) {
      let stateName = '';
      Constants.materialBoxState.forEach((item) => {
        if (item.value === materialBoxState) {
          stateName = item.label;
        }
      });
      return stateName;
    },
    // 格式化原料列表
    formmatMaterials(siteInfo) {
      if (!isEmpty(siteInfo.materialBoxModel)) {
        this.datas = siteInfo.materialBoxModel.materialBoxMaterialModels;
      } else {
        this.datas = [];
      }
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