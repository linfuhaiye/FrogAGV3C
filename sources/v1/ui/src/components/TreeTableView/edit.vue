<template>
  <div>
    <el-form :model="temp" label-position="left" label-width="100px" ref="editForm">
      <el-row v-for="(column, index) in columns" :key="index">
        <el-form-item :label="$t(column.text)">
          <EditSelect v-if="column.selected" :url="column.selected" :parentId="temp[column.value]">
          </EditSelect>
          <el-input v-else v-model="temp[column.value]"></el-input>
        </el-form-item>
      </el-row>
      <el-input type="hidden" v-model="temp.id" />
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="toggleShow">{{$t('table.cancel')}}</el-button>
      <el-button v-if="dialogStatus==='create'" type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
      <el-button v-else type="primary" @click="updateData">{{$t('table.confirm')}}</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import EditSelect from '@/components/Select/index'

/**
 * 编辑界面
 */
export default {
  name: 'editView',
  components: { EditSelect },
  data() {
    return {
      // 父级区域列表
      options: []
    }
  },
  props: {
    temp: {
      type: Object,
      default: () => { }
    },
    // 请求URL链接 字符串类型 必填
    url: {
      type: String,
      required: true
    },
    // 弹出框状态
    dialogStatus: {
      type: String,
      default: () => ''
    },
    // 弹出框显示内容
    columns: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    // 弹出框标志变化
    toggleShow() {
      this.$emit('toggleShow')
    },
    // 修改信息
    updateData() {
      this.$refs['editForm'].validate((valid) => {
        delete this.temp.children
        delete this.temp.parent
        if (valid) {
          request({
            url: this.url + '/' + this.temp.id,
            method: 'put',
            params: this.temp
          }).then(response => {
            this.$message(this.$t('message.updateSuccess'))
            this.toggleShow()
          })
        } else {
          return false
        }
      })
    },
    // 新增信息
    createData() {
      this.$refs['editForm'].validate((valid) => {
        if (valid) {
          request({
            url: this.url,
            method: 'post',
            params: this.temp
          }).then(response => {
            this.$message(this.$t('message.insertSuccess'))
            this.toggleShow()
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>