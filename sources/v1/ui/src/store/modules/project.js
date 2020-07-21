'use strict';

/**
 * 项目store
 */
const project = {
  state: {
    projects: []
  },
  mutations: {
    SET_PROJECTS: (state, projects) => {
      state.projects = projects;
    }
  },
  actions: {
    refreshProject({ commit }, data) {
      commit('SET_PROJECTS', data);
    }
  }
};

export default project;
