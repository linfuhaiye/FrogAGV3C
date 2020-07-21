import Vue from 'vue';
import Router from 'vue-router';
Vue.use(Router);
import Layout from '@/views/layout/Layout';
export const constantRouterMap = [
  {
    path: '/login',
    component: () => import('@/views/agv/login'),
    hidden: true
  },
  {
    path: '/selectTeam',
    component: () => import('@/views/agv/login/selectTeam'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/agv/product/home'),
        name: 'dashboard',
        meta: { title: 'dashboard', icon: 'dashboard', noCache: true }
      }
    ]
  },
  {
    path: '/agv',
    component: Layout,
    redirect: 'noredirect',
    children: [
      {
        path: 'setting',
        component: () => import('@/views/agv/product/setting/index'),
        name: 'agvSetting',
        meta: { title: 'agvSetting', icon: 'agvSetting', noCache: true }
      },
      {
        path: 'wave',
        component: () => import('@/views/agv/product/wave/index'),
        name: 'agvWave',
        meta: { title: 'agvWave', icon: 'agvWave', noCache: true }
      },
      {
        path: 'call/history',
        component: () => import('@/views/agv/product/history/index'),
        name: 'agvHistory',
        meta: { title: 'agvHistory', icon: 'agvHistory', noCache: true }
      }
    ]
  },
  // 消毒间
  {
    path: '/disinfection',
    component: Layout,
    redirect: 'noredirect',
    children: [
      {
        path: 'task',
        component: () => import('@/views/agv/disinfection/task/index'),
        name: 'disinfectionTask',
        meta: {
          title: 'disinfectionTask',
          icon: 'disinfectionTask',
          noCache: true
        }
      },
      {
        path: 'call',
        component: () => import('@/views/agv/disinfection/call/index'),
        name: 'disinfectionCall',
        meta: {
          title: 'disinfectionCall',
          icon: 'disinfectionCall',
          noCache: true
        }
      },
      {
        path: 'call/history',
        component: () => import('@/views/agv/disinfection/history/index'),
        name: 'disinfectionCallHistory',
        meta: {
          title: 'disinfectionCallHistory',
          icon: 'disinfectionCallHistory',
          noCache: true
        }
      }
    ]
  },
  // 拆包间
  {
    path: '/demolition',
    component: Layout,
    redirect: 'noredirect',
    children: [
      {
        path: 'task',
        component: () => import('@/views/agv/demolition/task/index'),
        name: 'demolitionTask',
        meta: {
          title: 'demolitionTask',
          icon: 'demolitionTask',
          noCache: true
        }
      },
      {
        path: 'call',
        component: () => import('@/views/agv/demolition/call/index'),
        name: 'demolitionCall',
        meta: {
          title: 'demolitionCall',
          icon: 'demolitionCall',
          noCache: true
        }
      },
      {
        path: 'call/history',
        component: () => import('@/views/agv/demolition/history/index'),
        name: 'demolitionCallHistory',
        meta: {
          title: 'demolitionCallHistory',
          icon: 'demolitionCallHistory',
          noCache: true
        }
      }
    ]
  },
  // 包材仓
  {
    path: '/materials',
    component: Layout,
    redirect: 'noredirect',
    children: [
      {
        path: 'unpack',
        component: () => import('@/views/agv/materials/unpack/index'),
        name: 'materialsUnpack',
        meta: {
          title: 'materialsUnpack',
          icon: 'materialsUnpack',
          noCache: true
        }
      },
      {
        path: 'pack',
        component: () => import('@/views/agv/materials/pack/index'),
        name: 'materialsPack',
        meta: {
          title: 'materialsPack',
          icon: 'materialsPack',
          noCache: true
        }
      },
      {
        path: 'stock',
        component: () => import('@/views/agv/materials/stock/index'),
        name: 'materialsStock',
        meta: {
          title: 'materialsStock',
          icon: 'materialsStock',
          noCache: true
        }
      }
    ]
  }
];

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
});

export const asyncRouterMap = [];
