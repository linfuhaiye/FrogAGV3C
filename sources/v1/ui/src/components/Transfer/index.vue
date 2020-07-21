<template>
  <div>
    <el-transfer filterable :filter-method="filterMethod" filter-placeholder="请输入关键字" v-model="targets" :data="data" :titles="titles">
    </el-transfer>
    <div slot="footer" class="dialog-footer" align="center">
      <el-button @click="toggleTransfer">{{$t('table.cancel')}}</el-button>
      <el-button type="primary" @click="updateData">{{$t('table.confirm')}}</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

/**
 * 穿梭框封装
 */
export default {
  data() {
    return {
      data: [],
      targets: [],
      filterMethod(query, item) {
        return item.label.indexOf(query) > -1
      }
    }
  },
  // 页面DOM创建前获取数据
  created() {
    this.getList()
  },
  watch: {
    fetchUrl() {
      this.getList()
    },
    paramId() {
      this.getList()
    }
  },
  props: {
    // 请求链接
    fetchUrl: {
      type: String,
      required: true
    },
    // 修改链接
    updateUrl: {
      type: String,
      required: true
    },
    // 上方标题
    titles: {
      type: Array,
      default: () => {
        return ['source', 'target']
      }
    },
    // 关键索引
    paramId: ''
  },
  methods: {
    // 获取数据
    getList() {
      request({
        url: this.fetchUrl,
        method: 'get',
        params: { paramId: this.paramId }
      }).then(response => {
        this.renderData(response.data)
      })
    },
    // 保存数据
    updateData() {
      request({
        url: this.updateUrl,
        method: 'put',
        params: { id: this.paramId, allocationIds: this.targets.join() }
      }).then(response => {
        this.$message(this.$t('message.transferSuccess'))
        this.toggleTransfer()
      })
    },
    // 操作弹出框
    toggleTransfer() {
      this.$emit('toggleTransfer')
    },
    // 组装数据
    renderData(data) {
      const datas = []
      const values = []
      Array.from(data).forEach((record) => {
        if (record.selected) {
          values.push(record.value)
        }
        datas.push({
          label: record.name,
          key: record.value
        })
      })
      this.data = datas
      this.targets = values
    }
  }
}
</script>