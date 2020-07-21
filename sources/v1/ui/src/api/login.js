import request from '@/utils/request';
import md5 from 'js-md5';

// 登入
export function loginByUsername(username, password) {
  password = md5(password);
  const data = {
    username,
    password
  };
  return request({
    url: '/system/login',
    method: 'post',
    data
  });
}

// 登出
export function logout() {
  return request({
    url: '/system/logout',
    method: 'post'
  });
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/system/users/currentUser',
    method: 'get'
  });
}

// 注册用户
export function registerUser(user) {
  const data = {
    username: user.username,
    password: md5(user.password),
    email: user.email,
    name: user.name,
    code: user.code,
    department: user.department
  };
  return request({
    url: '/system/register',
    method: 'post',
    data
  });
}

// 修改用户密码
export function updatePassword(passwordInfo) {
  console.log(passwordInfo);
  const data = {
    oldPassword: md5(passwordInfo.oldPassword),
    newPassword: md5(passwordInfo.newPassword),
    reNewPassword: md5(passwordInfo.reNewPassword)
  };
  return request({
    url: '/system/users/userPassword',
    method: 'put',
    params: data
  });
}

// 更新用户配置
export function updateUserConfigure(userConfigureInfo) {
  return request({
    url: '/system/users/configure',
    method: 'post',
    data: userConfigureInfo
  });
}
