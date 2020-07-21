import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import router from '@/router'
import { getToken } from '@/utils/auth'

const areaType = process.env.AREA_TYPE;

// create an axios instance
const service = axios.create({
  baseURL: process.env.BASE_API, // api的base_url
  timeout: 20000 // request timeout
})
// request interceptor
service.interceptors.request.use(
  config => {
    // Do something before request is sent
    if (store.getters.token) {
      // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
      config.headers['Authorization'] = getToken()
    }
    config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    return config
  },
  error => {
    // Do something with request error
    Promise.reject(error)
  }
)

// respone interceptor
service.interceptors.response.use(
  /**
   * 下面的注释为通过在response里，自定义code来标示请求状态
   * 当code返回如下情况则说明权限有问题，登出并返回到登录页
   * 如想通过xmlhttprequest来状态码标识 逻辑可写在下面error中
   * 以下代码均为样例，请结合自生需求加以修改，若不需要，则可删除
   */
  response => {
    const res = response.data
    if (res.code !== 200) {
      if (res.code === 401) {
        store.dispatch('FedLogOut').then(() => {
          location.reload() // 为了重新实例化vue-router对象 避免bug
        })
      }
      if (res.message) {
        Message({
          message: res.message,
          type: 'error',
          duration: 5 * 1000
        })
      }
      return Promise.reject('error')
    } else {
      let data = null
      if (res._embedded instanceof Array) {
        data = res._embedded
      } else if (res._embedded instanceof Object) {
        data = get_object_first_attribute(res._embedded)
        if (!(data instanceof Array)) {
          data = res._embedded
        }
        if (res.page && res.page.totalElements) {
          res.total = res.page.totalElements
        }
      } else {
        data = res._embedded
      }
      res.data = data
      return res
    }
  },
  error => {
    if (error.response.data.code === 401) {
      store.dispatch('FedLogOut').then(() => {
        // location.reload() // 为了重新实例化vue-router对象 避免bug
        if (areaType === 'demolition') {
          router.push('/demolition/task')
        } else if (areaType === 'disinfection') {
          router.push('/disinfection/task')
        } else if (areaType === 'materials') {
          router.push('/materials/pack')
        } else {
          router.push('/login') // 否则全部重定向到登录页
        }
      })
    }
    Message({
      message: error.response.data.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

function get_object_first_attribute(data) {
  for (var key in data) return data[key]
}

export default service
