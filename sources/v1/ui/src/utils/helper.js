/**
 * 对象是否为空
 *
 * @param {any} object 对象
 * @returns 是否为空
 */
export function isEmpty(object) {
  return (
    typeof object === 'undefined' ||
    object === null ||
    object === '' ||
    object === 'undefined' ||
    object === 'null'
  )
}

/**
 * 对象转url参数
 *
 * @param {*} param 将要转为URL参数字符串的对象
 * @param {*} key URL参数字符串的前缀
 * @param {*} encode 是否进行URL编码,默认为true
 */
export function urlEncode(param, key, encode) {
  if (param == null) return ''
  var paramStr = ''
  var t = typeof param
  if (t === 'string' || t === 'number' || t === 'boolean') {
    paramStr +=
      '&' +
      key +
      '=' +
      (encode == null || encode ? encodeURIComponent(param) : param)
  } else {
    for (var i in param) {
      var k =
        key == null
          ? i
          : key + (param instanceof Array ? '[' + i + ']' : '.' + i)
      paramStr += urlEncode(param[i], k, encode)
    }
  }

  return paramStr
}

/**
 * 深拷贝
 *
 * @export
 * @param {any} arr 需要拷贝的数组
 * @returns 拷贝对象
 */
export function coppyArray(arr) {
  return arr.map(e => {
    if (typeof e === 'object') {
      return Object.assign({}, e)
    } else {
      return e
    }
  })
}

export function exportFile(url) {
  if (navigator.userAgent.indexOf('Firefox') > 0) {
    window.open(url)
  } else {
    var a = document.createElement('a')
    a.href = url
    a.click()
  }
}

/**
 * 传入 YYYY-MM-DD 型日期,获取日期(时间控件上,控制开始以及结束日期)
 *
 * @export
 * @param {any} date YYYY-MM-DD 日期字符串
 * @returns 日期
 */
export function getTime(date) {
  return isEmpty(date) ? null : new Date(date)
}
