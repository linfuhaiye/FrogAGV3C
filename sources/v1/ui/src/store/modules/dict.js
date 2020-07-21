'use strict'
import request from '@/utils/request'

/**
 * 字典store
 */
const dict = {
  state: {
    dictGroups: []
  },
  mutations: {
    SET_DICTGROUP: (state, dictGroups) => {
      state.dictGroups = dictGroups
    }
  },
  actions: {
    getDictGroup({ commit, state }) {
      return new Promise((resolve, reject) => {
        request({
          url: '/system/dicts/0',
          method: 'GET'
        }).then(response => {
          commit('SET_DICTGROUP', response.data)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default dict
