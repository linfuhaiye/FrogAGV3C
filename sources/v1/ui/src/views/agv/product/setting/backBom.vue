<template>
  <div class="dialog-height agv-dialog">
    <div class="flex-box flex-direction-column">
      <!-- BOM信息 -->
      <div class="flex-box flex-direction-row flex-align-items-center">
        <div class="flex-box flex-align-items-center fillParent" style="margin-right:20px;">
          <div class="item-title">产品名称:</div>
          <div class="item-content">{{info.materialName}}</div>
        </div>
        <div class="flex-box flex-align-items-center" style="width:350px;">
          <div class="item-title">满车数量:</div>
          <div class="item-content">
            <el-input
              style="width:200px; height:40px"
              v-model="info.fullCount"
              placeholder="请输入满车箱子数量"
            />箱
          </div>
        </div>
      </div>
      <!-- 详情 -->
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
              v-for="(bomDetail) in bomDetails"
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

    <div slot="footer" class="dialog-footer" align="center">
      <el-button @click="toggleShow" class="dialog-cancel-btn">{{$t('table.cancel')}}</el-button>
      <el-button
        type="primary"
        @click="updateData"
        class="dialog-update-btn"
      >{{$t('table.confirm')}}</el-button>
    </div>
  </div>
</template>

<script>
import './setting.scss';
import request from '@/utils/request';
// import Constants from '@/utils/constants';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

export default {
  name: 'backBom',
  data() {
    return {
      info: {},
      bomDetails: [],
      // 加载对象
      load: null
    };
  },
  created() {
    this.loadingInfo();
  },
  props: {
    bomItem: Object
  },
  methods: {
    loadingInfo() {
      this.info = this.bomItem;
      this.getBomDetail();
    },
    getBomDetail() {
      this.load = this.showErrorMessage('正在获取BOM清单,请稍后');
      request({
        url: '/agv/bom/getBomDetails',
        method: 'GET',
        params: {
          bomId: this.bomItem.id
        }
      })
        .then((response) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.bomDetails = response.data;
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
    // 弹出框标志变化
    toggleShow() {
      this.$emit('toggleShow');
    },
    // 修改信息
    updateData() {
      const sendItem = this.info;
      sendItem.bomDetails = this.bomDetails;
      this.load = this.showErrorMessage('正在更新BOM,请稍后');
      request({
        url: '/agv/bom/updateBom',
        method: 'POST',
        data: sendItem
      })
        .then((response) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            if (response.data) {
              this.$emit('reloadData');
              this.$emit('toggleShow');
            } else {
              this.$message.error('更新失败,请重试...');
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