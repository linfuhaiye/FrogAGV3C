<template>
  <div class="dialog-height agv-dialog">
    <el-form :model="info" label-position="left" label-width="200px" ref="editForm">
      <el-row>
        <el-form-item label="原料名称" prop="name">
          <el-input v-model="info.name" readonly></el-input>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="数量" prop="num">
          <el-input v-model="info.num" readonly></el-input>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="已验收" prop="done">
          <el-input v-model="info.done"></el-input>
        </el-form-item>
      </el-row>
      <el-form-item label="退货理由">
        <el-input type="textarea" v-model="info.backRemark"></el-input>
      </el-form-item>
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
export default {
  name: 'backBom',
  data() {
    return {
      info: {
        name: '原料A',
        num: 50,
        done: 45,
        backRemark: ''
      }
    };
  },
  created() {
    this.loadingInfo();
  },
  props: {
    id: [String, Number]
  },
  methods: {
    loadingInfo() {},
    // 弹出框标志变化
    toggleShow() {
      this.$emit('toggleShow');
    },
    // 修改信息
    updateData() {
      console.log('updateData>>>>>>>>>>>', this.info);
      this.$emit('toggleShow');
    }
  }
};
</script>