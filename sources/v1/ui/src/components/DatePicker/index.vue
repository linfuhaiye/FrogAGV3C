<template>
  <el-date-picker v-model="values" type="date" :picker-options="options" placeholder="选择日期" style="width: 100%;" :value-format="valueFormat">
  </el-date-picker>
</template>

<script>
import { isEmpty } from '@/utils/helper'

/**
 * 时间控件
 */
export default {
  name: 'DatePicker',
  data() {
    return {
      options: this.getOptions(),
      values: '',
      _startDate: {
        type: Date,
        default: null
      },
      _endDate: {
        type: Date,
        default: null
      }
    }
  },
  props: {
    // 显示格式
    valueFormat: {
      type: String,
      default: () => 'yyyy-MM-dd'
    },
    // placeholder 显示字段
    placeholder: {
      type: String,
      default: () => '选择日期'
    },
    // 开始时间
    startDate: {
      type: [Date, String],
      default: () => null
    },
    // 结束时间
    endDate: {
      type: [Date, String],
      default: () => null
    },
    // 值
    value: ''
  },
  created() {
    this.initDate()
    this.values = this.value
    this.$emit('input', this.values)
  },
  watch: {
    value() {
      this.values = this.value
    },
    values() {
      this.$emit('input', this.values)
    },
    startDate() {
      this.initDate()
    },
    endDate() {
      this.initDate()
    }
  },
  methods: {
    // 判断某些日期是否可选
    getOptions() {
      const thiz = this
      return {
        disabledDate(date) {
          if (isEmpty(thiz._startDate) && isEmpty(thiz._endDate)) {
            return false
          }
          if (!isEmpty(thiz._startDate)) {
            return date && date.valueOf() < thiz._startDate.getTime() - 86400000
          }
          if (!isEmpty(thiz._endDate)) {
            return date && date.valueOf() >= thiz._endDate.getTime()
          }
          return false
        }
      }
    },
    // 初始化日期
    initDate() {
      if (this.startDate instanceof String && !isEmpty(this.startDate)) {
        this._startDate = new Date(this.startDate)
      } else {
        this._startDate = this.startDate
      }
      if (this.endDate instanceof String && !isEmpty(this.endDate)) {
        this._endDate = new Date(this.endDate)
      } else {
        this._endDate = this.endDate
      }
    }
  }
}
</script>