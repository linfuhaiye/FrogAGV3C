<template>
  <div style="position:relative">
    <el-select
      v-model="values"
      placeholder="请选择"
      ref="refreshSelect"
      v-bind="$attrs"
      style="width:100%"
      :disabled="disabled"
    >
      <el-option
        v-for="item in options"
        :key="item.value + item.label"
        :label="item.label"
        :value="item.value"
      ></el-option>
    </el-select>
    <span
      style="position:absolute;top:0px;right:30px;line-height:36px;"
      :class="disabled ? 'hidden' : 'bb' "
    >
      <i
        class="el-icon-refresh pointer"
        @click="handleIconClick"
        style="background-color: #ffffff;"
      ></i>
    </span>
  </div>
</template>

<script>
  import request from '@/utils/request'
import { isEmpty } from '@/utils/helper'

/**
 * 选择组件
 */
export default {
    name: 'FrSelect',
    data() {
      return {
        // 列表
        options: [],
        values: '',
        datas: null
      }
  },
    props: {
      // 请求URL
      url: '',
      parentId: '',
      dataFunc: {
        type: Function,
        default: () => {
          return []
        }
      },
      data: {
        type: Array,
        default: () => []
      },
      // 是否以code作为值
      valueIsCode: {
        type: [Boolean],
        default: false
      },
      value: '',
      // 是否是查询条件
      isQueryCriteria: false,
      // 额外的
      additional: {
        type: Array,
        default: () => []
      },
      // 是否默认选中第一条
      defaultFirst: false,
      // 默认值
      defaultValue: '',
      disabled: false,
      skey: 'default'
    },
    created() {
      if (isEmpty(this.url)) {
        let tmp = this.dataFunc()
        // 如果是查询条件，加全部
        if (this.isQueryCriteria) {
          const all = {
            value: '',
            label: '全部'
          }
          tmp.push(all)
        }

        Array.from(this.data).forEach(record => {
          tmp = tmp.concat(record)
        })
        this.options = tmp
      } else {
        this.handleIconClick()
      }
      // 没有项目时，显示空，不显示0
      if (this.value === 0) {
        this.values = ''
      } else {
        this.values = this.value
      }
      this.$emit('input', this.values)
  },
    watch: {
      value() {
        this.values = this.value
        this.renderSelected(this.datas)
        this.$emit('input', this.values)
      },
      values() {
        this.$emit('input', this.values)
      }
    },
    methods: {
      // 渲染下拉数据
      renderSelected(data) {
        if (isEmpty(data)) {
          return
        }
        let tmp = []
        // 如果是查询条件，加全部
        if (this.isQueryCriteria) {
          const all = {
            value: '',
            label: '全部'
          }
          tmp.push(all)
        }
        Array.from(this.additional).forEach(add => {
          tmp.push(add)
        })
        const render = data => {
          Array.from(data).forEach(record => {
            let obj = {}
            if (this.valueIsCode) {
              obj = {
                value: Number(record.code),
                label: record.name
              }
            } else {
              obj = {
                value: record.id,
                label: record.name
              }
            }
            tmp = tmp.concat(obj)
            if (record.children && record.children.length > 0) {
              render(record.children)
            }
          })
        }
        render(data)
        if (isEmpty(this.value)) {
          if (!isEmpty(this.defaultValue) && tmp.length > 0) {
            // 设置为默认值
            this.values = this.defaultValue
          } else if (this.defaultFirst && tmp.length > 0) {
            // 默认选中第一条
            this.values = tmp[0].value
          }
        }
        return tmp
      },
      // 刷新按钮
      handleIconClick() {
        if (!isEmpty(this.url)) {
          request({
            url: this.url,
            method: 'GET'
          }).then(response => {
            if (response && response.data && response.data.length > 0) {
              this.options = this.renderSelected(response.data)
              this.datas = response.data
            } else {
              this.options = this.additional
              this.datas = null
            }
            this.$emit('selectCallBack', this.options)
          })
        }
      }
    }
  }
</script>