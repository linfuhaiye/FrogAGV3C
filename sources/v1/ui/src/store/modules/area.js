'use strict'
import request from '@/utils/request'

/**
 * 充装页面需要的areas级联数据
 * @param {any} data 原始数据
 * @returns areas级联数据
 */
function renderAreas(data) {
  let tmp = []

  Array.from(data).forEach((record) => {
    const obj = {
      value: record.id,
      label: record.name
    }

    if (record.children && record.children.length > 0) {
      const children = renderAreas(record.children)
      obj.children = children
    }
    tmp = tmp.concat(obj)
  })
  return tmp
}

/**
 * 页面下拉框数据
 * @param {any} data 原始数据
 * @returns 下拉框数据
 */
function renderAreaList(data) {
  let tmp = [{ value: '0', label: '一级区域' }]
  const render = (data) => {
    Array.from(data).forEach((record) => {
      const obj = {
        value: record.id,
        label: record.name
      }
      tmp = tmp.concat(obj)
      if (record.children && record.children.length > 0) {
        render(record.children)
      }
    })
  }
  render(data)
  return tmp
}

/**
 * 区域store
 */
const area = {
  state: {
    areas: [],
    areaList: []
  },
  mutations: {
    SET_AREAS: (state, areas) => {
      state.areas = areas
    },
    SET_AREALIST: (state, areaList) => {
      state.areaList = areaList
    }
  },
  actions: {
    getAreas({ commit, state }) {
      return new Promise((resolve, reject) => {
        request({
          url: '/system/areas',
          method: 'GET'
        }).then(response => {
          const areaList = JSON.parse(response.data)
          commit('SET_AREAS', renderAreas(areaList))
          commit('SET_AREALIST', renderAreaList(areaList))
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default area
