<template>
  <div style="position:relative">
    <el-cascader :options="options" style="width:100%" change-on-select v-model="values"></el-cascader>
    <span style="position:absolute;top:0px;right:30px;line-height:36px;">
      <i class="el-icon-refresh pointer" @click="handleIconClick"></i>
    </span>
  </div>
</template>

<script>
  import request from '@/utils/request'
import { isEmpty } from '@/utils/helper'

/**
 * 级联下拉界面
 */
export default {
    name: 'CascaderSelect',
    data() {
      return {
        // 列表
        options: [],
        values: []
      }
  },
    props: {
      // 请求URL
      url: '',
      datas: {
        type: Array,
        default: () => []
      },
      value: ''
    },
    created() {
      this.values = this.datas
      if (isEmpty(this.url)) {
        this.options = this.datas
      } else {
        this.handleIconClick()
      }
    },
    watch: {
      // 监听编辑页面datas数据
      datas() {
        this.values = this.datas
      },
      value() {
        if (isEmpty(this.value)) {
          this.values = []
        } else {
          this.values = this.value
        }
      },
      values() {
        this.$emit('input', this.values)
      }
    },
    methods: {
      handleIconClick() {
        if (!isEmpty(this.url)) {
          request({
            url: this.url,
            method: 'GET'
          }).then(response => {
            this.options = this.renderCas(JSON.parse(response.data))
          })
        }
      },
      // 返回级联数据
      renderCas(data) {
        let tmp = []
        if (isEmpty(data)) {
          return tmp
        }
        Array.from(data).forEach(record => {
          const obj = {
            value: record.id,
            label: record.name
          }

          if (record.children && record.children.length > 0) {
            const children = this.renderCas(record.children)
            obj.children = children
          }
          tmp = tmp.concat(obj)
        })
        return tmp
      }
    }
  }
</script>