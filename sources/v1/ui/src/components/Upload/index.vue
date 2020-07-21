<template>
  <div>
    <el-upload class="avatar-uploader" :action="action" :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload" :on-error="handleAvatarError" :headers="headers" >
      <img v-if="imageUrl" :src="imageUrl" class="avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
    <div style="text-align: center;">{{label}}</div>
  </div>
</template>

<script>
import request from '@/utils/request'
import { isEmpty } from '@/utils/helper'
import { getToken } from '@/utils/auth'

/**
 * 上传图片组件
 */
export default {
  name: 'uploadAvatar',
  data() {
    return {
      // 图片上传地址
      action: process.env.BASE_API + '/file/upload',
      // 图片回显地址
      imageUrl: '',
      // 服务器图片数据库ID
      uuid: '',
      headers: { 'Authorization': getToken() }
    }
  },
  created() {
    this.renderAvatar()
  },
  props: {
    // 文件ID
    value: '',
    label: {
      type: String,
      default: '上传图片'
    }
  },
  watch: {
    value() {
      // 渲染头像
      this.renderAvatar()
    }
  },
  methods: {
    // 上传成功
    handleAvatarSuccess(res, file) {
      if (res && res.code === 200 && res._embedded) {
        // this.imageUrl = process.env.SERVER_URL + '/attachments/' + res._embedded.name
        this.uuid = res._embedded.id
        this.$emit('input', this.uuid)
      } else {
        this.imageUrl = ''
        this.uuid = ''
        this.$emit('input', this.uuid)
        this.$message('上传失败')
      }
    },
    // 上传之前
    beforeAvatarUpload(file) {
      return true
    },
    // 上传失败
    handleAvatarError(err) {
      this.$message(err)
    },
    // 渲染头像
    renderAvatar() {
      console.log('process.env.BASE_API: ', process.env.BASE_API, process.env.SERVER_URL)
      if (isEmpty(this.value)) {
        this.imageUrl = ''
        return
      }
      request({
        url: '/file/' + this.value,
        method: 'get'
      }).then(response => {
        this.imageUrl = response.data
      })
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
