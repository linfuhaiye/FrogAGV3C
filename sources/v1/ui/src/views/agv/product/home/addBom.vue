<template>
  <div class="dialog-height agv-dialog">
    <el-table
      ref="multipleTable"
      :data="bomDetails"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="原料名称" width="auto">
        <template slot-scope="scope">{{ scope.row.materialName }}</template>
      </el-table-column>
      <el-table-column label="数量" width="200">
        <template slot-scope="scope">
          <el-input v-model="scope.row.num"></el-input>
        </template>
      </el-table-column>
      <!-- <el-table-column label="已验收" width="200">
        <template slot-scope="scope">
          <el-input v-model="scope.row.done"></el-input>
        </template>
      </el-table-column>-->
    </el-table>

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
        datas: [
          { id: 1, name: '原料A', num: 0, done: 0 },
          { id: 2, name: '原料B', num: 0, done: 0 },
          { id: 3, name: '原料C', num: 0, done: 0 },
          { id: 4, name: '原料D', num: 0, done: 0 },
          { id: 5, name: '原料E', num: 0, done: 0 }
        ],
        bomDetails: [],
        multipleSelection: [],
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
        this.getBomDetails();
      },
      // 弹出框标志变化
      toggleShow() {
        this.$emit('toggleShow');
      },
      // 修改信息
      updateData() {
        const addItems = [];
        if (
          !isEmpty(this.multipleSelection) &&
          this.multipleSelection.length > 0
        ) {
          this.multipleSelection.forEach(item => {
            const addItem = {
              code: item.materialCode,
              waveCode: this.bom.code,
              materialId: item.materialId,
              count: item.num,
              enabled: 1
            };
            addItems.push(addItem);
          });
        } else {
          return;
        }
        this.load = this.showErrorMessage('正在保存，请稍候......');
        request({
          url: '/agv/waves/addWaveDetails',
          method: 'POST',
          data: addItems
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
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      getBomDetails() {
        request({
          url: '/agv/waves/getBomDetails',
          method: 'GET',
          params: {
            materialCode: this.bom.materialCode
          }
        })
          .then(response => {
            if (response.errno === 0) {
              this.bomDetails = response.data;
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