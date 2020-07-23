<template>
  <div class="dialog-height agv-dialog">
    <el-form :model="info" label-position="left" label-width="200px" ref="editForm">
      <el-row>
        <el-form-item label="原料名称" prop="materialName">
          <el-input v-model="info.materialName" readonly></el-input>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="数量" prop="count">
          <el-input v-model="info.count"></el-input>
        </el-form-item>
      </el-row>
      <!-- <el-row>
        <el-form-item label="已验收" prop="done">
          <el-input v-model="info.done"></el-input>
        </el-form-item>
      </el-row>-->
    </el-form>

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
  import request from '@/utils/request';
  import { isEmpty } from '@/utils/helper';
  import { Loading } from 'element-ui';

  export default {
    name: 'editBom',
    data() {
      return {
        info: {},
        // 加载对象
        load: null
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
        this.getWave();
      },
      // 弹出框标志变化
      toggleShow() {
        this.$emit('toggleShow');
      },
      // 修改信息
      updateData() {
        this.load = this.showErrorMessage('正在保存，请稍候......');
        request({
          url: '/agv/waves/updateWaveDetail',
          method: 'POST',
          data: this.info
        })
          .then(response => {
            // 如果遮罩层存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            if (response.errno === 0) {
              this.$emit('reloadWaves');
              this.$emit('toggleShow');
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
      getWave() {
        request({
          url: '/agv/waves/getWaveDetail/' + this.bom.id,
          method: 'GET'
        })
          .then(response => {
            if (response.errno === 0) {
              this.info = response.data;
            }
          })
          .catch(_ => {
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