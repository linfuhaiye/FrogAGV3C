'use strict'
import request from '@/utils/request'

/**
 * 项目store
 */
const task = {
  state: {
    tasks: []
  },
  mutations: {
    SET_TASKS: (state, tasks) => {
      state.tasks = tasks
    }
  },
  actions: {
    refreshTask({ commit }, data) {
      commit('SET_TASKS', data)
    }
  }
}

export default task
