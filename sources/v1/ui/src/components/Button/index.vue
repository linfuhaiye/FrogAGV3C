<template>
  <el-button :style="buttonMargin + 'width:75px'" class="filter-item search-btn" type="primary" :icon="icon" @click="handleFilter" :loading="click">
    {{text}}</el-button>
</template>

<script>
import request from '@/utils/request'
import Bus from './button'
import style from '@/utils/style'

/**
 * 按钮
 */
export default {
  name: 'searchButton',
  data() {
    return {
      buttonMargin: style.button.marginLeft,
      click: false
    }
  },
  created() {
    Bus.$on('queryTable', () => {
      this.click = false
    })
  },
  props: {
    // 按钮文本
    text: '',
    // 按钮前面图标
    icon: {
      type: String,
      default: () => ''
    }
  },
  methods: {
    // 按钮点击事件
    handleFilter() {
      this.click = true
      this.$emit('click')
    }
  }
}
</script>