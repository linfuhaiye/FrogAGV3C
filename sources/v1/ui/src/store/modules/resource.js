'use strict';
import request from '@/utils/request';

/**
 * 树形菜单类型装完list
 * @param {any} data 原始数据
 * @returns resource级联数据
 */
function renderResources(data) {
  let tmp = [{ value: '0', label: '顶级菜单' }];

  const render = data => {
    Array.from(data).forEach(record => {
      const obj = {
        value: record.id,
        label: record.name
      };
      tmp = tmp.concat(obj);
      if (record.children && record.children.length > 0) {
        render(record.children);
      }
    });
  };
  render(data);

  return tmp;
}

/**
 * 资源store
 */
const area = {
  state: {
    resources: []
  },
  mutations: {
    SET_RESOURCES: (state, resources) => {
      state.resources = resources;
    }
  },
  actions: {
    getResources({ commit, state }) {
      return new Promise((resolve, reject) => {
        request({
          url: '/system/resources',
          method: 'GET'
        })
          .then(response => {
            const list = renderResources(JSON.parse(response.data));
            commit('SET_RESOURCES', list);
            resolve();
          })
          .catch(error => {
            reject(error);
          });
      });
    }
  }
};

export default area;
