import {
  loginByUsername,
  logout,
  getUserInfo,
  registerUser,
  updatePassword,
  updateUserConfigure
} from '@/api/login'
import {
  getToken,
  setToken,
  removeToken,
  setUserAuth,
  setUserIcon,
  setUserConfigure,
  setUserName
} from '@/utils/auth'

// 设置公共参数
export function setCommonParm(response) {
  setUserIcon(response.data.iconUrl)
  setUserAuth(response.data.resources)
  setToken(response.newToken)
  setUserConfigure(response.data.userConfigure)
  setUserName(response.data.name)
}

const user = {
  state: {
    user: '',
    status: '',
    code: '',
    token: getToken(),
    name: '',
    avatar: '',
    introduction: '',
    roles: [],
    setting: {
      articlePlatform: []
    }
  },

  mutations: {
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        loginByUsername(username, userInfo.password)
          .then(response => {
            commit('SET_TOKEN', response.newToken)
            // commit(
            //   'SET_AVATAR',
            //   response.data.iconUrl
            //     ? response.data.iconUrl
            //     : 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'
            // )
            setCommonParm(response)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo()
          .then(response => {
            if (!response.data) {
              // 由于mockjs 不支持自定义状态码只能这样hack
              reject('error')
            }
            const data = response.data
            data.roles = '["admin"]'
            if (data.roles && data.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              commit('SET_ROLES', data.roles)
            } else {
              reject('getInfo: roles must be a non-null array !')
            }
            commit('SET_NAME', data.name)
            setUserIcon(data.avatar)
            commit('SET_INTRODUCTION', data.introduction)
            resolve(response)
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token)
          .then(() => {
            commit('SET_TOKEN', '')
            commit('SET_ROLES', [])
            removeToken()
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    // 动态修改权限
    ChangeRoles({ commit }, role) {
      return new Promise(resolve => {
        commit('SET_TOKEN', role)
        setToken(role)
        getUserInfo(role).then(response => {
          const data = response.data
          commit('SET_ROLES', data.roles)
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_INTRODUCTION', data.introduction)
          resolve()
        })
      })
    },

    // 用户注册
    Register({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        registerUser(userInfo)
          .then(response => {
            commit('SET_TOKEN', response.newToken)
            setCommonParm(response)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 修改用户密码
    UpdatePassword({ commit }, passwordInfo) {
      return new Promise((resolve, reject) => {
        updatePassword(passwordInfo)
          .then(response => {
            commit('SET_TOKEN', response.newToken)
            setCommonParm(response)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 更新用户配置
    UpdateUserConfigure({ commit }, userConfigureInfo) {
      return new Promise((resolve, reject) => {
        updateUserConfigure(userConfigureInfo)
          .then(response => {
            commit('SET_TOKEN', response.newToken)
            setCommonParm(response)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    }
  }
}

export default user
