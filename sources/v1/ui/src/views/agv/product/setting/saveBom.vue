<template>
  <div class="dialog-height agv-dialog">
    <div class="flex-box flex-direction-column">
      <!-- 搜索条件 -->
      <div class="flex-box flex-direction-row flex-align-items-center" style="padding-right:20px;">
        <div class="flex-box flex-align-items-center fillParent">
          <div class="item-title">生产单号:</div>
          <div class="item-content fillParent">
            <el-input @keyup.enter.native="getMoInfo" style="width:100%; height:40px" v-model="moNumber" placeholder="请输入MO号" />
          </div>
        </div>
        <div class="flex-box flex-align-items-center" style="min-width:210px;">
          <el-button class="condition-btn search-button" type="primary" @click="getMoInfo">搜索</el-button>
          <el-button class="condition-btn reset-button" type="primary" @click="reset">重置</el-button>
        </div>
      </div>
      <!-- 生产单信息 -->
      <el-card class="box-card" style="margin-top:15px;">
        <div slot="header" class="clearfix">
          <span class="card-title">生产单基础信息</span>
        </div>
        <div class="text item">
          <div class="flex-box flex-direction-row fillParent flex-wrap" style="width:100%;">
            <div class="flex-box production-information">
              <div class="item-title">生产单号:</div>
              <div class="item-content">{{billInfo.bill_code}}</div>
            </div>
            <div class="flex-box production-information">
              <div class="item-title">班组:</div>
              <div class="item-content">{{billInfo.workgroupname}}</div>
            </div>
            <div class="flex-box production-information">
              <div class="item-title">产品名称:</div>
              <div class="item-content">{{billInfo.invname}}</div>
            </div>
            <div class="flex-box production-information">
              <div class="item-title">生产线:</div>
              <div class="item-content">{{billInfo.productlinename}}</div>
            </div>
            <div class="flex-box production-information">
              <div class="item-title">计划开工日期:</div>
              <div class="item-content">{{billInfo.jhkgrq}}</div>
            </div>
            <div class="flex-box production-information">
              <div class="item-title">计划数量:</div>
              <div class="item-content">{{isEmpty(billInfo.jhsl)?'':`${billInfo.jhsl}(${billInfo.invunit})`}}</div>
            </div>
          </div>
        </div>
      </el-card>
      <!-- BOM信息 -->
      <el-card class="box-card" style="margin-top:15px;">
        <div slot="header" class="clearfix">
          <span class="card-title">BOM信息</span>
        </div>
        <div class="text item" style="margin:-20px;">
          <div class="flex-box flex-direction-row flex-align-items-center">
            <div class="flex-box flex-align-items-center fillParent" style="margin-right:20px;">
              <div class="item-title">产品名称:</div>
              <div class="item-content">{{bomInfo.materialName}}</div>
            </div>
            <div class="flex-box flex-align-items-center" style="width:350px;">
              <div class="item-title">满车数量:</div>
              <div class="item-content">
                <el-input
                  style="width:200px; height:40px"
                  v-model="bomInfo.fullCount"
                  placeholder="满车箱子数量"
                />箱
              </div>
            </div>
          </div>
          <div class="flex-box flex-direction-column" style="margin-top:10px;">
            <!-- 表格头部 -->
            <div
              class="flex-box data-header-content flex-align-items-center"
              style="width:100%; height:45px;"
            >
              <div class="bom-detail-title-name">原料名称</div>
              <div class="bom-detail-title-num">数量</div>
              <div class="bom-detail-title-type">类型</div>
              <div class="bom-detail-title-full">满车数量</div>
            </div>
            <!-- 表体 -->
            <div class="data-wave">
              <div class="flex-box data-content flex-direction-column" style="width:100%;">
                <div
                  style="width:100%;"
                  class="flex-box data-content-row"
                  v-for="(bomDetail) in bomInfo.bomDetails"
                  :key="bomDetail.id"
                >
                  <div class="bom-detail-data-name">{{bomDetail.materialName}}</div>
                  <div class="bom-detail-data-num">{{bomDetail.count}}</div>
                  <div class="bom-detail-data-type">
                    <input type="radio" value="1" v-model="bomDetail.type" />
                    <span>内包材</span>
                    <input type="radio" value="2" v-model="bomDetail.type" />
                    <span>外包材</span>
                    <input type="radio" value="3" v-model="bomDetail.type" />
                    <span>其 它</span>
                  </div>
                  <div class="bom-detail-data-full">
                    <el-input type="number" v-model="bomDetail.fullCount" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
      <!-- 领料单详情 -->
      <el-card class="box-card" style="margin-top:15px;">
        <div slot="header" class="clearfix">
          <span class="card-title">领料单信息</span>
        </div>
        <div class="text item" style="margin:-20px;">
          <el-table
            ref="multipleTable"
            :data="billFullInfos"
            style="width: 100%"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column label="原料名称" width="auto">
              <template slot-scope="scope">{{ scope.row.invname }}</template>
            </el-table-column>
            <el-table-column label="数量" width="100">
              <template slot-scope="scope">{{ scope.row.nnum }}</template>
            </el-table-column>
            <el-table-column label="单位" width="100">
              <template slot-scope="scope">{{ scope.row.invunit }}</template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
      <!-- 选择产线 -->
      <div class="flex-box flex-direction-row out-item" style="margin-top:10px;">
        <div class="title">选择产线：</div>
        <div>
          <el-select v-model="billInfo.line" placeholder="请选择产线">
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
      >生成计划</el-button>
    </div>
  </div>
</template>

<style scoped>
.condition-btn {
  padding: 10px 20px;
  width: 100px;
}
.production-information {
  margin-right:20px;
  width:47%;
  min-height:45px;
}
.card-title {
  font-size:28px;
  font-weight:600;
}
.title {
  font-size: 25px;
  text-align: center;
  height: 40px;
  line-height: 40px;
  font-weight: 500;
  min-width: 125px;
}
.agv-dialog .dialog-update-btn {
  width: 120px;
}
.agv-dialog .dialog-cancel-btn {
  width: 120px;
}
</style>

<script>
import './setting.scss';
import request from '@/utils/request';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

const areaCoding = process.env.AREA_CODING;
export default {
  name: 'saveBom',
  data() {
    return {
      moNumber: '',
      billInfo: {},
      bomDetails: [],
      bomInfo: {},
      multipleSelection: [],
      selectSearchParams: {
        code: 'PRODUCT_FILLING',
        areaCoding: areaCoding
      },
      // 加载对象
      load: null,
      lines: [],
      billFullInfos: []
    };
  },
  created() {
    this.loadingInfo();
  },
  methods: {
    isEmpty,
    loadingInfo() {
      this.getProductLines();
    },
    // 获取MO信息
    getMoInfo() {
      this.load = this.showErrorMessage('正在获取MO信息,请稍后');
      request({
        url: '/agv/getData/getProductInfoByMo',
        method: 'GET',
        params: {
          billCode: this.moNumber
        }
      })
        .then((response) => {
          console.log(response);
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            if (response._embedded.errno === 0) {
              this.billInfo = response._embedded.billInfo;
              this.bomInfo = response._embedded.bomModel;
              this.billFullInfos = response._embedded.billFullInfoItems;
            } else {
              this.$message.error(response._embedded.errorMessage);
            }
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
    reset() {
      console.log('重置');
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(this.multipleSelection);
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
          this.lines = response.data;
        })
        .catch((_) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          this.$message.error('服务器请求失败');
        });
    },
    // 弹出框标志变化
    toggleShow() {
      this.$emit('toggleShow');
    },
    // 修改信息
    updateData() {
      if (isEmpty(this.billInfo.line)) {
        this.$message.error('请选择产线');
        return;
      }
      if (isEmpty(this.multipleSelection) || this.multipleSelection.length === 0) {
        this.$message.error('请勾选领料单');
        return;
      } else {
        let enableSend = true;
        this.multipleSelection.forEach(selectItem => {
          const item = this.bomInfo.bomDetails.filter(ele => ele.materialCode === selectItem.invcode);
          if (isEmpty(item) || item[0].type === 3) {
            enableSend = false;
          }
          console.log('===:', item);
        });
        if (!enableSend) {
          this.$message.error('选中的领料单必须为非其它类型');
          return;
        }
      }
      if (isEmpty(this.bomInfo.bomDetails)) {
        this.$message.error('请先获取BOM信息');
        return;
      } else {
        let enableSend = false;
        this.bomInfo.bomDetails.forEach(bomDetail => {
          if (bomDetail.type !== 3) {
            enableSend = true;
          }
        });
        if (!enableSend) {
          this.$message.error('必须有一个子BOM为非其它类型');
          return;
        }
      }
      request({
        url: '/agv/getData/generate/plan',
        method: 'POST',
        data: {
          billFullInfoItems: this.multipleSelection,
          billInfo: this.billInfo,
          bomModel: this.bomInfo,
          line: this.billInfo.line
        }
      })
        .then((response) => {
          console.log(response);
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.$message.info(response._embedded);
          }
        })
        .catch((_) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          this.$message.error('服务器请求失败,请重试...');
        });
      console.log('multipleSelection:', this.multipleSelection);
      console.log('billInfo.line:', this.billInfo);
      console.log('bomInfo.bomDetails:', this.bomInfo);
      // this.$emit('toggleShow');
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