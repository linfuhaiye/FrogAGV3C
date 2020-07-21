/**
 * 判断是否通用函数
 * @param {any} val 名称值
 * @returns 是否通用函数
 */
export function validateFormatter(val) {
  const func = ['sex', 'logType']
  if (func.indexOf(val) === -1) {
    return false
  }
  return true
}

/**
 * 执行格式化函数
 * @param {any} val 值
 * @param {any} formatter 格式化函数
 * @returns 格式化后的值
 */
export function renderFormatter(val, formatter) {
  if (formatter === 'sex') {
    return formatterSex(val)
  }
  if (formatter === 'logType') {
    return formatterLogType(val)
  }
  return val
}

/**
 * 格式化性别
 * @param {any} val 值
 * @returns 性别
 */
export function formatterSex(val) {
  if (val === 1) {
    return 'sex.male'
  } else if (val === 0) {
    return 'sex.female'
  }
  return 'sex.unowk'
}

/**
 * 格式化日志操作类型
 * @param {*} val 值
 */
export function formatterLogType(val) {
  switch (val) {
    case 'add':
      return 'monitor.logType.add'
    case 'edit':
      return 'monitor.logType.edit'
    case 'del':
      return 'monitor.logType.del'
    case 'login':
      return 'monitor.logType.login'
    case 'logout':
      return 'monitor.logType.logout'
    default:
      return 'monitor.logType.other'
  }
}
