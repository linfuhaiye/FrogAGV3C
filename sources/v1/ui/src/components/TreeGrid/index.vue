<template>
  <el-table :data="formatData" :row-style="showRow" v-bind="$attrs" style="width: 100%;"   v-loading="loading" element-loading-text="" element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
    <el-table-column v-if="columns.length===0" width="150">
      <template slot-scope="scope">
        <span v-for="space in scope.row._level" class="ms-tree-space" :key="space"></span>
        <span class="tree-ctrl" v-if="iconShow(0,scope.row)" @click="toggleExpanded(scope.$index)">
          <i v-if="!scope.row._expanded" class="el-icon-plus"></i>
          <i v-else class="el-icon-minus"></i>
        </span>
        {{scope.$index}}
      </template>
    </el-table-column>
    <el-table-column v-else v-for="(column, index) in columns" :key="column.value" :label="tableTitle(column.text)" :width="columnWidth(column.width)" min-width="100">
      <template slot-scope="scope">
        <span v-if="index === 0" v-for="space in scope.row._level" class="ms-tree-space" :key="space"></span>
        <span class="tree-ctrl" v-if="iconShow(index,scope.row)" @click="toggleExpanded(scope.$index)">
          <i v-if="!scope.row._expanded" class="el-icon-plus"></i>
          <i v-else class="el-icon-minus"></i>
        </span>
        <el-button v-if="column.value === 'action' && actionText(scope.row,column.formatter)"  size="mini" :key="index" v-for="(action, index) in column.actions" @click="callback(action.func,scope.row)" :class="action.class">{{tableTitle(action.text)}}</el-button>
        <span v-if="column.value !== 'action' && column.formatter">{{formaterVal(scope.row,column.formatter)}}</span>
        <span v-else>{{scope.row[column.value]}}</span>
      </template>
    </el-table-column>
    <slot></slot>
  </el-table>
</template>
<script>
import request from '@/utils/request'
import { tableTitle } from '@/utils/i18n'
import { validateFormatter, renderFormatter } from '@/utils/formatter'
import Bus from '@/components/Button/button'
import { isEmpty } from '@/utils/helper'
import treeToArray from './eval'

/**
 * 树形表格
 */
export default {
  name: 'TreeGrid',
  data() {
    return {
      data: [],
      loading: false
    }
  },
  props: {
    // 请求链接
    fetchUrl: {
      type: String,
      required: true
    },
    // 展示列
    columns: {
      type: Array,
      default: () => []
    },
    evalFunc: Function,
    evalArgs: Array,
    expandAll: {
      type: Boolean,
      default: true
    }
  },
  created() {
    this.getList()
  },
  computed: {
    // 格式化数据源
    formatData: function() {
      let tmp
      if (!Array.isArray(this.data)) {
        tmp = [this.data]
      } else {
        tmp = this.data
      }
      const func = this.evalFunc || treeToArray
      const args = this.evalArgs ? Array.concat([tmp, this.expandAll], this.evalArgs) : [tmp, this.expandAll]
      return func.apply(null, args)
    }
  },
  methods: {
    getList() {
      this.loading = true
      new Promise((resolve, reject) => {
        request({
          url: this.fetchUrl,
          method: 'get',
          params: this.fetchParams
        }).then(response => {
          this.data = JSON.parse(response.data)
          resolve()
        }).catch(_ => {
          this.data = null
          reject()
        })
      }).then((success) => {
        this.loading = false
        Bus.$emit('queryTable')
      }, (err) => {
        this.loading = false
        Bus.$emit('queryTable')
      })
    },
    showRow: function(row) {
      const show = (row.row.parent ? (row.row.parent._expanded && row.row.parent._show) : true)
      row.row._show = show
      return show ? 'animation:treeTableShow 1s;-webkit-animation:treeTableShow 1s;' : 'display:none;'
    },
    // 切换下级是否展开
    toggleExpanded: function(trIndex) {
      const record = this.formatData[trIndex]
      record._expanded = !record._expanded
    },
    // 图标显示
    iconShow(index, record) {
      return (index === 0 && record.children && record.children.length > 0)
    },
    // 回调父页面函数
    callback(func, row) {
      func(row)
    },
    // 格式化column值
    formaterVal(row, formatter) {
      return formatter(row)
    },
    actionText(row, formatterFunc) {
      if (typeof formatterFunc === 'function') {
        return formatterFunc(row)
      }
      return true
    },
    // 处理百分比宽度、固定宽度
    columnWidth(val) {
      if (isEmpty(val)) {
        return 100
      }
      const _width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth || 1000
      let tableWidth = _width
      if (this.$store.getters.sidebar.opened) {
        // body - max-sidebar-padding
        tableWidth = _width - 180 - 40
      } else {
        // body - min-sidebar-padding
        tableWidth = _width - 36 - 40
      }
      if (val.indexOf('%') >= 0) {
        val = val.replace('%', '')
        const ss = tableWidth * Number(val) / 100
        return ss
      }
      return val
    },
    tableTitle
  }
}
</script>

<style rel="stylesheet/css">
@keyframes treeTableShow {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@-webkit-keyframes treeTableShow {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>

<style lang="scss" rel="stylesheet/scss" scoped>
$color-blue: #2196F3;
$space-width: 18px;
.ms-tree-space {
  position: relative;
  top: 1px;
  display: inline-block;
  font-style: normal;
  font-weight: 400;
  line-height: 1;
  width: $space-width;
  height: 14px;
  &::before {
    content: ""
  }
}

.processContainer {
  width: 100%;
  height: 100%;
}

table td {
  line-height: 26px;
}

.tree-ctrl {
  position: relative;
  cursor: pointer;
  color: $color-blue;
  margin-left: -$space-width;
}
</style>