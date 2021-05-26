<template>
  <div class="dialog-height agv-dialog">
    <el-table
      ref="multipleTable"
      :data="datas"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="原料名称" width="auto">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column label="数量" width="200">
        <template slot-scope="scope">
          <el-input v-model="scope.row.num"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="已验收" width="200">
        <template slot-scope="scope">
          <el-input v-model="scope.row.done"></el-input>
        </template>
      </el-table-column>
    </el-table>

    <div slot="footer" class="dialog-footer" align="center">
      <el-button @click="toggleShow" class="dialog-cancel-btn">{{
        $t("table.cancel")
      }}</el-button>
      <el-button type="primary" @click="updateData" class="dialog-update-btn">{{
        $t("table.confirm")
      }}</el-button>
    </div>
  </div>
</template>

<script>
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
      multipleSelection: []
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
      console.log('updateData>>>>>>>>>>>', this.multipleSelection);
      // 保存选择内容，成功后关闭弹出框
      this.$emit('toggleShow');
    },
    handleSelectionChange(val) {
      console.log('handleSelectionChange>>>>>', val);
      this.multipleSelection = val;
    }
  }
};
</script>
