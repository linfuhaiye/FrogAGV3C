export default {
  user: {
    table: {
      name: '用户名称',
      username: '登录名称',
      mobile: '电话号码',
      code: '用户编码',
      sex: '性别',
      update: '用户编辑',
      create: '用户新增'
    },
    form: {
      name: '用户名称：',
      username: '登录名称：',
      mobile: '电话号码：',
      code: '用户编码：',
      sex: '性别：',
      birthday: '生日：',
      area: '区域：',
      email: '邮箱：',
      remark: '备注：',
      department: '部门：',
      title: '岗位：',
      password: '密码：',
      oldPassword: '旧密码：',
      newPassword: '新密码：',
      reNewPassword: '确认新密码：',
      reportType: '日报类型：'
    }
  },
  resource: {
    table: {
      name: '资源名称',
      path: '资源编码',
      remark: '备注',
      priority: '排序号',
      update: '资源编辑',
      create: '资源新增'
    },
    form: {
      name: '资源名称：',
      path: '资源编码：',
      remark: '备注：',
      priority: '排序号：',
      parentId: '父级资源：',
      type: '资源类型：'
    }
  },
  permission: {
    table: {
      name: '权限名称',
      remark: '备注',
      update: '权限编辑',
      create: '权限新增'
    },
    form: {
      name: '权限名称：',
      remark: '备注：'
    }
  },
  role: {
    table: {
      name: '角色名称',
      remark: '备注',
      code: '角色编码'
    },
    form: {
      name: '角色名称：',
      remark: '备注：',
      update: '角色编辑',
      create: '角色新增',
      code: '角色编码：',
      system: '系统内置：'
    }
  },
  area: {
    table: {
      name: '区域名称',
      code: '区域代码',
      priority: '排序号',
      remark: '备注信息',
      update: '区域编辑',
      create: '区域新增'
    },
    form: {
      name: '区域名称：',
      code: '区域代码：',
      parentId: '上级区域：',
      priority: '排序号：',
      type: '区域类型：',
      remark: '备注信息：'
    }
  },
  dict: {
    table: {
      name: '字典名称',
      code: '字典编码',
      dictGroupId: '字典组别',
      system: '系统内置',
      remark: '备注信息',
      sort: '排序',
      update: '字典编辑',
      create: '字典新增',
      info: '字典列表'
    },
    form: {
      name: '字典名称：',
      code: '字典编码：',
      dictGroupId: '字典组别：',
      system: '系统内置：',
      remark: '备注信息：',
      sort: '排序：'
    }
  },
  dictgroup: {
    btn: {
      addGroup: '添加字典组',
      addDict: '添加字典'
    },
    table: {
      name: '字典组别',
      code: '组别代码',
      system: '系统内置',
      remark: '备注信息',
      sort: '排序',
      update: '字典组编辑',
      create: '字典组新增'
    },
    form: {
      name: '字典组别：',
      code: '组别代码：',
      system: '系统内置：',
      remark: '备注信息：',
      sort: '排序：'
    }
  },
  organization: {
    table: {
      name: '部门名称',
      code: '部门编码',
      sort: '排序号',
      duty: '部门职责',
      createSub: '添加子部门',
      update: '修改部门',
      create: '添加部门'
    },
    form: {
      parentId: '上级部门：',
      name: '部门名称：',
      code: '部门编码：',
      sort: '排序号：',
      duty: '部门职责：'
    }
  },
  position: {
    table: {
      name: '岗位名称',
      type: '岗位类型',
      departmentId: '所属部门',
      duty: '岗位职责',
      update: '修改岗位',
      create: '添加岗位',
      createSub: '添加子岗位'
    },
    form: {
      parentId: '上级岗位：',
      name: '岗位名称：',
      type: '岗位类型：',
      departmentId: '所属部门；',
      duty: '岗位职责：'
    }
  },
  weeklyConfig: {
    form: {
      mailReceiver: '接收邮件：',
      mailSender: '邮件发送者：',
      otherReportType: '其他日报类型：'
    },
    title: {
      sender: '选择发送者',
      otherReportType: '其他日报类型'
    },
    remark: {
      mailReceiver: '请输入接收周报的邮箱',
      sender: '请选择需要发送周报的成员',
      otherReportType: '可不选项目、任务的特殊日报类型'
    }
  }
}
