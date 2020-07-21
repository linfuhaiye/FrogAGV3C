'use strict'
import request from '@/utils/request'

/**
 * 部门store
 */
const department = {
  state: {
    departments: []
  },
  mutations: {
    SET_DEPARTMENT: (state, departments) => {
      state.departments = departments
    }
  },
  actions: {
    getDepartments({ commit, state }) {
      return new Promise((resolve, reject) => {
        request({
          url: '/system/organizations/list',
          method: 'GET'
        })
          .then(response => {
            commit('SET_DEPARTMENT', response.data)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    }
  }
}

export default department
