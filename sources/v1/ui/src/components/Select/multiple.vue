<template>
  <div>
    <div style="position:relative">
      <el-select
        v-model="values"
        placeholder="请选择"
        ref="refreshSelect"
        v-bind="$attrs"
        style="width:100%"
        :multiple="multiple === 'true'"
        :disabled="disabled"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
      <div
        style="position:absolute;top:0px;left:0px;line-height:36px;width:100%;height:100%;z-index:9999"
        @click="showDialog"
        :class="disabled ? 'hidden' : 'bb' "
      ></div>
    </div>
    <el-dialog
      :visible.sync="selectFormVisible"
      :title="title"
      append-to-body
      class="multiple-dialog"
    >
      <div class="app-container">
        <!-- 查询条件 -->
        <div class="filter-container">
          <el-input class="filter-item search-input" v-model="fetchParams.name"></el-input>
          <SearchButton @click="handleFilter" :text="$t('table.search')"></SearchButton>
        </div>
        <!-- table表单数据 -->
        <ComplexTable
          :fetchUrl="url"
          :columns="columns"
          border
          ref="multipleTable"
          :fetchParams="fetchParams"
          multipleTable="true"
          @handleSelectionChange="handleSelectionChange"
          :selectedVal="value"
          :defaultHeight="defaultHeight"
          :showPage="false"
          :visible="selectFormVisible"
        ></ComplexTable>

        <div slot="footer" class="dialog-footer" align="center" style="margin-top:10px">
          <el-button @click="toggleShow">{{$t('table.cancel')}}</el-button>
          <el-button type="primary" @click="saveData">{{$t('table.confirm')}}</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import ComplexTable from '@/components/ComplexTable'
import SearchButton from '@/components/Button'
import request from '@/utils/request'
import { isEmpty } from '@/utils/helper'

/**
 * 模糊选择
 */
export default {
    name: 'multipleSelect',
    components: { ComplexTable, SearchButton },
    data() {
      return {
        defaultHeight: 'calc(100vh - 410px)',
        selectFormVisible: false,
        options: [],
        // 查询条件
        fetchParams: {
          name: ''
        },
        values: [],
        temp: []
      }
  },
    props: {
      title: {
        type: String,
        default: '选择人员'
      },
      url: {
        type: [String, Function],
        default: () => {
          ''
        }
      },
      multiple: false,
      disabled: false,
      reloadAfterSave: false,
      value: [Number, Array, String],
      // 表格列表数组
      columns: {
        type: Array,
        default: () => [
          {
            text: '名称',
            value: 'name',
            width: 'auto'
          }
        ]
      }
    },
    created() {
      this.temp = []
      this.handleIconClick()
      this.values = this.value
      this.$emit('input', this.values)
  },
    watch: {
      value() {
        if (isEmpty(this.value)) {
          this.values = ''
        } else {
          this.values = this.value
        }
      },
      values() {
        this.$emit('input', this.values)
      },
      selectFormVisible() {
        if (this.selectFormVisible) {
          this.temp = []
          this.handleIconClick()

          if (this.reloadAfterSave) {
            this.handleFilter()
          }
        }
      }
    },
    methods: {
      handleFilter() {
        if (!isEmpty(this.$refs.multipleTable)) {
          this.$refs.multipleTable.getList()
        }
      },
      showDialog() {
        this.selectFormVisible = true
      },
      toggleShow() {
        this.selectFormVisible = false
      },
      saveData() {
        this.values = this.temp
        if (isEmpty(this.temp)) {
          this.values = ''
        } else if (this.temp.length == 0) {
          this.values = ''
        }
        this.$emit('input', this.values)
        this.toggleShow()
      },
      // 点击复选框事件
      handleSelectionChange(val, data) {
        if (!isEmpty(val)) {
          // 单选
          if (!this.multiple) {
            if (!isEmpty(val)) {
              if (val.length > 1) {
                const u_val = []
                for (let i = 0; i < val.length - 1; i++) {
                  u_val.push(val[i].id)
                }
                data.forEach(row => {
                  if (u_val.indexOf(row.id) > -1) {
                    this.$refs.multipleTable.$refs.queryTable.toggleRowSelection(
                      row,
                      false
                    )
                  }
                })
              } else if (val.length === 1) {
                this.temp = val[0].id
              } else {
                this.temp = ''
              }
            }
          } else {
            // 多选
            this.temp = []
            for (let i = 0; i < val.length; i++) {
              this.temp.push(val[i].id)
            }
          }
        }
      },
      // 渲染下拉框数据
      renderSelected(data) {
        if (isEmpty(data) || data.length === 0) {
          return []
        }
        let tmp = []
        const render = data => {
          Array.from(data).forEach(record => {
            const obj = {
              value: record.id,
              label: record.name
            }
            tmp = tmp.concat(obj)
            if (record.children && record.children.length > 0) {
              render(record.children)
            }
          })
        }
        render(data)
        return tmp
      },
      // 刷新
      handleIconClick() {
        if (!isEmpty(this.url)) {
          request({
            url: this.url,
            method: 'GET'
          }).then(response => {
            this.options = this.renderSelected(response.data)
            this.$emit('mulCallBack', this.options)
          })
        }
      }
    }
  }
</script>