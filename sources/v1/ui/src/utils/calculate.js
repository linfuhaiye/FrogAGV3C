import { isEmpty } from '@/utils/helper'

/**
 * 根据 结束日期 - 开始日期 加1天 得到 天数
 *
 * @export
 * @param {any} stringStartDate 开始日期:YYYY-MM-DD
 * @param {any} stringEndDate 结束日期:YYYY-MM-DD
 * @returns 天数
 */
export function getDay(stringStartDate, stringEndDate) {
  if ((isEmpty(stringStartDate)) || (isEmpty(stringEndDate))) {
    return 0
  }
  var arrayDate, startDate, endDate
  startDate = new Date()
  endDate = new Date()
  // 将字符串格式日期 年-月-日 转为日期格式
  arrayDate = stringStartDate.split('-')
  startDate.setFullYear(arrayDate[0], arrayDate[1], arrayDate[2])
  arrayDate = stringEndDate.split('-')
  endDate.setFullYear(arrayDate[0], arrayDate[1], arrayDate[2])
  // 预计结束日期 - 预计开始日期 得到的毫秒数转换为天数,得到的天数加1代表，开始到结束共多少天
  return parseInt((endDate - startDate) / 1000 / 60 / 60 / 24) + 1
}

/**
 * 获取yyyy-MM-DD格式的当前日期
 *
 * @export
 * @returns 当前日期的年-月-日
 */
export function getCurrentFormatDate() {
  var date = new Date()
  var seperator1 = '-'
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var strDate = date.getDate()
  if (month >= 1 && month <= 9) month = '0' + month
  if (strDate >= 0 && strDate <= 9) strDate = '0' + strDate
  var currentdate = year + seperator1 + month + seperator1 + strDate
  return currentdate
}

/**
 * 计算进度
 *
 * @export
 * @param {any} row 行数据
 * @returns 进度百分比
 */
export function calculateSchedule(row) {
  if (
    calculateConsumptionTime(row) < 0 ||
    calculateExpectedTime(row) <= 0 ||
    calculateConsumptionTime(row) === '-'
  ) {
    // 消耗时间为负数、预计天数为0天
    return '0%'
  } else {
    const schedule = calculateConsumptionTime(row) / calculateExpectedTime(row)
    return schedule > 1 ? '100%' : (schedule * 100).toFixed(2) + '%'
  }
}

/**
 * 计算预计天数：(开始)预计结束-实际开始;(未开始)预计结束-预计开始
 *
 * @export
 * @param {any} row 行数据
 * @returns 预计天数
 */
export function calculateExpectedTime(row) {
  // 是否开始
  if (!isEmpty(row.realStartTime)) {
    // 项目已经开始: 预计结束 - 实际开始
    const day = getDay(
      row.realStartTime,
      row.endTime
    )
    return day > 0 ? day : 0
  } else {
    // 项目未开始: 预计结束 - 预计开始
    const day = getDay(
      row.startTime,
      row.endTime
    )
    return day > 0 ? day : 0
  }
}

/**
 * 计算已用天数：当前天数 - 实际开始;
 *    未开始以及实际结束，已用天数为空  '-'
 *
 * @export
 * @param {any} row 行数据
 * @returns 已用天数
 */
export function calculateConsumptionTime(row) {
  if (row.realStartTime != null) {
    // 已开始 [ 已结束: 已用天数为 '-';  未结束：当前时间 - 实际开始 ]
    return row.realEndTime != null
      ? '-'
      : getDay(row.realStartTime, getCurrentFormatDate()) - 1
  } else {
    // 未开始:'-'
    return '-'
  }
}

/**
 * 计算剩余时间
 *
 * @export
 * @param {any} row 行数据
 * @returns 剩余工时
 */
export function calculateRemainingTime(row) {
  if (row.realStartTime != null) {
    // 已开始 [ 已结束: 剩余天数为 '-';  未结束：预期结束 - 当前时间 逾期则剩余天数为0 ]
    const remainingTime = getDay(
      getCurrentFormatDate(),
      row.endTime
    )
    return row.realEndTime != null ? '-' : remainingTime < 0 ? 0 : remainingTime
  } else {
    // 未开始:'-'
    return '-'
  }
}

/**
 * 计算逾期
 *
 * @export
 * @param {any} row
 * @returns
 */
export function calculateOverdue(row) {
  if (row.realEndTime != null) {
    // 已结束: 实际结束时间>预期结束时间？
    if (row.realEndTime > row.endTime) {
      return '是'
    } else {
      return '否'
    }
  } else {
    // 未结束: 当前时间>预期结束时间？
    if (getCurrentFormatDate() > row.endTime) {
      return '是'
    } else {
      return '否'
    }
  }
}
