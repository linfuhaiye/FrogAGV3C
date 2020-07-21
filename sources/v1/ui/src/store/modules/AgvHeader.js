const AgvHeader = {
  state: {
    title: '青蛙AGV系统',
    needLogin: true
  },
  mutations: {
    UPDATE_TITLE: (state, title) => {
      state.title = title;
    },
    UPDATE_NEED_LOGIN: (state, needLogin) => {
      state.needLogin = needLogin;
    },
    UPDATE_USER_NAME: (state, userName) => {
      state.userName = userName;
    },
    UPDATE_TEAM_ID: (state, teamId) => {
      state.teamId = teamId;
    },
    UPDATE_AUTH: (state, auth) => {
      state.auth = auth;
    }
  },
  actions: {
    updateTitle({ commit, state }, title) {
      commit('UPDATE_TITLE', title);
    },
    updateNeedLogin({ commit, state }, needLogin) {
      commit('UPDATE_NEED_LOGIN', needLogin);
    },
    updateUserName({ commit, state }, userName) {
      commit('UPDATE_USER_NAME', userName);
    },
    updateTeamId({ commit, state }, teamId) {
      commit('UPDATE_TEAM_ID', teamId);
    },
    updateAuth({ commit, state }, auth) {
      commit('UPDATE_AUTH', auth);
    }
  }
};

export default AgvHeader;
