'use strict'
import request from '@/utils/request'

/**
 * 岗位store
 */
const position = {
  state: {
    positions: []
  },
  mutations: {
    SET_POSITION: (state, positions) => {
      state.positions = positions
    }
  },
  actions: {
    getPositions({ commit, state }) {
      return new Promise((resolve, reject) => {
        request({
          url: '/system/positions/list',
          method: 'GET'
        })
          .then(response => {
            commit('SET_POSITION', response.data)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    }
  }
}

export default position
