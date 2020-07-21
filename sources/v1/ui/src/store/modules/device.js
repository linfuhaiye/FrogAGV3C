import { getDeviceId } from '@/api/device'
import { setDeviceId, getToken } from '@/utils/auth'

const device = {
  state: {
    deviceId: ''
  },

  mutations: {
    SET_DEVICE_ID: (state, deviceId) => {
      state.deviceId = deviceId
    }
  },

  actions: {
    // 获取设备ID
    GetDeviceId({ commit, state }) {
      return new Promise((resolve, reject) => {
        getDeviceId()
          .then(response => {
            if (!response.data) {
              // 由于mockjs 不支持自定义状态码只能这样hack
              reject('error')
            }
            const data = response.data
            commit('SET_DEVICE_ID', data)
            setDeviceId(data)
            resolve(response)
          })
          .catch(error => {
            reject(error)
          })
      })
    },
    setDeviceId({ commit, deviceId }) {
      commit('SET_DEVICE_ID', deviceId)
    }
  }
}

export default device
