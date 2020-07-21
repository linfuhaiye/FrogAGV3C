import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import errorLog from './modules/errorLog'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import user from './modules/user'
import area from './modules/area'
import resource from './modules/resource'
import dict from './modules/dict'
import department from './modules/department'
import position from './modules/position'
import project from './modules/project'
import task from './modules/task'
import type from './modules/reportType'
import device from './modules/device'
import getters from './getters'
import AgvHeader from './modules/AgvHeader'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    errorLog,
    permission,
    tagsView,
    user,
    area,
    resource,
    dict,
    department,
    position,
    project,
    task,
    type,
    device,
    AgvHeader
  },
  getters
})

export default store
