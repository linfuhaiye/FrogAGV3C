import Cookies from 'js-cookie';
import { isEmpty } from './helper';
import Constants from './constants';

const TokenKey = 'Admin-Token';

export function getToken() {
  return Cookies.get(TokenKey);
}

export function setToken(token) {
  return Cookies.set(TokenKey, token);
}

export function removeToken() {
  return Cookies.remove(TokenKey);
}

export function setUserInfo() {
  localStorage.clear();
}

/**
 *  设置设备ID
 */
export function setDeviceId(deviceId) {
  localStorage.setItem('deviceId', deviceId);
}

export function getDeviceID() {
  return localStorage.getItem('deviceId');
}

/**
 * 设置用户头像
 */
export function setUserIcon(icon) {
  localStorage.setItem('userIcon', icon);
}

export function getUserIcon() {
  return isEmpty(localStorage.getItem('userIcon'))
    ? process.env.SERVER_URL + Constants.userIcon
    : localStorage.getItem('userIcon');
}

/**
 *  设置用户权限
 *
 */
export function setUserAuth(auth) {
  if (isEmpty(auth)) {
    return;
  }
  var paths = [];
  Array.from(auth).forEach(item => {
    if (item.type === 1 && !isEmpty(item.path)) {
      paths.push(item.path);
    }
  });
  localStorage.setItem('auth', JSON.stringify(paths));
}

/**
 * 判断用户是否有权限
 * @param {string} auth 权限
 * @returns true/false
 */
export function hasAuth(auth) {
  var allAuth = localStorage.getItem('auth');
  if (isEmpty(allAuth) || isEmpty(auth)) {
    return false;
  } else {
    allAuth = JSON.parse(allAuth);
    auth = auth.split(',');
    for (var i in auth) {
      for (var cAuth in allAuth) {
        if (allAuth[cAuth] === auth[i]) {
          return true;
        }
      }
    }

    return false;
  }
}

/**
 * 设置用户个人配置
 */
export function setUserConfigure(userConfigure) {
  if (isEmpty(userConfigure)) {
    return;
  }
  localStorage.setItem('userConfigure', JSON.stringify(userConfigure));
}

export function getUserConfigure() {
  return isEmpty(localStorage.getItem('userConfigure'))
    ? {}
    : JSON.parse(localStorage.getItem('userConfigure'));
}

/**
 * 设置用户个人配置
 */
export function setUserName(userName) {
  if (isEmpty(userName)) {
    return;
  }
  localStorage.setItem('userName', userName);
}

export function getUserName() {
  return isEmpty(localStorage.getItem('userName'))
    ? ''
    : localStorage.getItem('userName');
}
