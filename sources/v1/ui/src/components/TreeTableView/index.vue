<template>
  <div class="app-container">
    <!-- 新增 -->
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" @click="addCallback" type="primary" icon="el-icon-edit">{{$t('table.add')}}</el-button>
    </div>
    <!-- 列表 -->
    <TreeGrid :fetchUrl="url" :columns="columnsTable" border ref="areaTable"></TreeGrid>
    <!-- 编辑信息界面 -->
    <el-dialog :visible.sync="state.editFormVisible" :title="$t(textMap[dialogStatus])">
      <Edit :temp="temp" @toggleShow="toggleShow" :dialogStatus="dialogStatus" :url="url" :columns="columnsEdit"></Edit>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'
import TreeGrid from '@/components/TreeGrid/index'
import Edit from './edit'

/**
 * 通用树形管理页面
 */
export default {
  name: 'TreeTableView',
  components: { TreeGrid, Edit },
  data() {
    return {
      state: {
        // 弹出框标志
        editFormVisible: false
      },
      // 编辑区域界面model信息
      temp: {},
      // 查询条件
      fetchParams: {
        name: ''
      },
      // dialog显示标题
      textMap: {
        update: 'system.dict.update',
        create: 'system.dict.create'
      },
      dialogStatus: 'update',
      columnsTable: [],
      columnsEdit: []
    }
  },
  created() {
    this.columnsEdit = this.columns
    Array.from(this.columns).forEach((record) => {
      if (record.isShow !== 'false') {
        this.columnsTable.push(record)
      }
    })

    this.columnsTable.push({
      text: 'table.actions',
      value: 'action',
      width: '50',
      actions: [{
        text: 'table.edit',
        func: (row) => this.updateCallback(row)
      }, {
        text: 'table.delete',
        func: (row) => this.deleteCallback(row)
      }, {
        text: 'table.add',
        func: () => this.addCallback()
      }]
    })
  },
  props: {
    // 请求URL链接 字符串类型 必填
    url: {
      type: String,
      required: true
    },
    // 弹出框显示内容
    columns: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    // 行操作中点击编辑回调函数 弹出编辑页面
    updateCallback(row) {
      this.dialogStatus = 'update'
      this.temp = row
      this.state.editFormVisible = true
    },
    // 行操作中点击删除回调函数
    deleteCallback(row) {
      this.$confirm(this.$t('message.deleteMsg'), this.$t('message.info'), {
        confirmButtonText: this.$t('message.confirm'),
        cancelButtonText: this.$t('message.cancel'),
        type: 'warning'
      }).then(() => {
        request({
          url: this.url + '/' + row.id,
          method: 'delete'
        }).then(response => {
          this.$message(this.$t('message.deleteSuccess'))
          this.toggleShow()
        }).catch(_ => {
          this.$message(this.$t('message.deleteError'))
          this.toggleShow()
        })
      })
    },
    // 修改弹出框标志的值
    toggleShow() {
      this.state.editFormVisible = false
      this.$refs.areaTable.getList()
    },
    // 查询
    handleFilter() {
      this.fetchParams.pageNum = 1
      this.$refs.areaTable.getList()
    },
    // 弹出新增界面
    addCallback(row) {
      this.dialogStatus = 'create'
      this.temp = {
        parentId: row.id
      }
      this.state.editFormVisible = true
    }
  }
}
</script>