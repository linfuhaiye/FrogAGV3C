<template>
  <el-upload class="upload-demo" :action="action" multiple :limit="uploadCount" :on-success="handleSuccessed" :file-list="fileList" :on-remove="handleRemove" :before-remove="beforeRemove" :headers="headers" :on-progress="handleProgress" :on-preview="preview" :on-exceed="handleExceed">
    <el-button class="upload-btn" size="small" type="primary">点击上传</el-button>
  </el-upload>
</template>

<script>
import request from '@/utils/request'
import { getToken } from '@/utils/auth'
import { isEmpty, exportFile } from '@/utils/helper'

/**
 * 上传文件组件
 */
export default {
  name: 'uploadFile',
  data() {
    return {
      // 上传地址
      action: process.env.BASE_API + '/file/upload',
      files: [],
      headers: { Authorization: getToken() },
      // 上传的文件列表
      fileList: [],
      uploadCount: 5
    }
  },
  props: {
    limit: 5,
    value: {
      type: Array,
      default: () => []
    }
  },
  created() {
    if (isEmpty(this.value)) {
      this.fileList = []
    } else {
      this.fileList = this.value
    }
    if (isEmpty(this.limit)) {
      this.uploadCount = 5
    } else {
      this.uploadCount = this.limit
    }
  },
  watch: {
    value() {
      if (isEmpty(this.value)) {
        this.fileList = []
      } else {
        this.fileList = this.value
        this.fileList.forEach(file => {
          if (isEmpty(file.name)) {
            file.name = file.showName
          }
        })
      }
    },
    fileList() {
      if (isEmpty(this.fileList)) {
        this.fileList = []
      }
      this.$emit('input', this.fileList)
    },
    limit() {
      if (isEmpty(this.limit)) {
        this.uploadCount = 5
      } else {
        this.uploadCount = this.limit
      }
    }
  },
  methods: {
    // 上传成功
    handleSuccessed(response, file, fileList) {
      file.showName = response._embedded.name
      file.id = response._embedded.id
      this.fileList = fileList
      // this.saveFileList.push(response._embedded);
    },
    // 移除附件
    handleRemove(file, fileList) {
      this.fileList = fileList
      // this.handlerValue(fileList);
    },
    // 删除前确认
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    // 点击已上传的文件
    preview(file) {
      if (file && !isEmpty(file.name)) {
        const url = process.env.SERVER_URL + '/attachments/' + file.showName
        exportFile(url)
      }
    },
    // 上传时
    handleProgress(event, file, fileList) {
      console.log('Progress-file:', file)
      console.log('Progress-fileList:', fileList)
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 ${this.uploadCount} 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      )
    }
  }
}
</script>