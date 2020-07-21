<template>
  <TreeTable :data="data" :columns="columns" border>
  </TreeTable>
</template>

<script>
import request from '@/utils/request'
import TreeTable from './index'
export default {
  name: 'complexTable',
  components: { TreeTable },
  data() {
    return {
      data: []
    }
  },
  props: {
    // 请求URL链接 字符串类型 必填
    fetchUrl: {
      type: String,
      required: true
    },
    // 表格列表数组
    columns: {
      type: Array,
      default: () => []
    }
  },
  // 页面DOM创建前获取数据
  created() {
    this.getList()
  },
  methods: {
    // 获取数据
    getList() {
      request({
        url: this.fetchUrl,
        method: 'get'
      }).then(response => {
        this.data = JSON.parse(response.data)
      })
    }
  }
}
</script>