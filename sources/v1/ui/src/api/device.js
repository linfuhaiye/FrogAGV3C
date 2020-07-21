import request from '@/utils/request';

// 获取设备ID
export function getDeviceId() {
  return request({
    url: '/monitor/device',
    method: 'get'
  });
}
