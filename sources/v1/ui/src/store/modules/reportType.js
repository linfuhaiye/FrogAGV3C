'use strict';

/**
 * 项目store
 */
const type = {
  state: {
    types: []
  },
  mutations: {
    SET_TYPES: (state, types) => {
      state.types = types;
    }
  },
  actions: {
    refreshType({ commit }, data) {
      commit('SET_TYPES', data);
    }
  }
};

export default type;
