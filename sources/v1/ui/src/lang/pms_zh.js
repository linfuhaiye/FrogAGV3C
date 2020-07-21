export default {
  search_btn: {
    all: '所有',
    notBegin: '未开始',
    beginning: '进行中',
    hangUp: '已挂起',
    notReview: '未评审',
    end: '已结束'
  },
  project: {
    table: {
      name: '项目名称',
      code: '项目代号',
      ownerName: '负责人',
      type: '类型',
      startTime: '开始日期',
      endTime: '截止日期',
      state: '状态',
      schedule: '进度',
      expectedTime: '预计天数',
      consumptionTime: '已用天数',
      remainingTime: '剩余天数',
      overdue: '逾期'
    },
    form: {
      name: '项目名称：',
      code: '项目代号：',
      ownerName: '项目经理：',
      expectedTime: '起止日期：',
      realTime: '实际起止日期：',
      type: '项目类型：',
      assessor: '评审人员：',
      acceptanceStaff: '验收人员：',
      description: '项目描述：',
      attachments: '项目附件：',
      sort: '排序：',
      schedule: '项目进度：',
      assigee: '当前负责人：',
      state: '项目状态：',
      expectedDays: '预计天数：',
      usedDays: '已用天数：',
      remainingDays: '剩余天数：',
      requirementCount: '需求数量：',
      taskCount: '任务数量：',
      bugCount: 'bug数量：'
    },
    title: {
      create: '新增项目',
      update: '修改项目'
    }
  },
  task: {
    search: {
      name: '任务名称：',
      ownerName: '负责人：',
      time: '时间段：',
      state: '状态：'
    },
    table: {
      priority: '优先级',
      name: '任务名称',
      ownerName: '负责人',
      realStartTime: '开始日期',
      endTime: '截止日期',
      state: '状态',
      schedule: '进度',
      expectedTime: '预计天数',
      consumptionTime: '已用天数',
      remainingTime: '剩余天数',
      overdue: '逾期'
    },
    form: {
      name: '任务名称：',
      ownerName: '负责人：',
      type: '任务类型：',
      acceptersName: '验收人:',
      startToEnd: '起止日期：',
      dependentModule: '从属模块：',
      realStartToEnd: '实际起止日期：',
      description: '任务描述：',
      requirement: '关联需求：',
      priority: '优先级：',
      assigee: '当前负责人：',
      attachments: '附件：',
      stateName: '任务状态：'
    },
    button: {
      clear: '清空条件',
      search: '搜索',
      export: '导出',
      add: '添加',
      see: '查看'
    },
    title: {
      update: '编辑任务',
      create: '添加任务',
      info: '任务详情',
      mulOwner: '选择负责人',
      mulAccepters: '选择验收人',
      mulRequirement: '选择关联需求'
    }
  },
  handle_btn: {
    start: '启动',
    assigned: '指派',
    complete: '完成',
    rejected: '驳回',
    hang: '挂起',
    invalid: '作废',
    restore: '恢复',
    shutdowm: '关闭',
    delay: '延期'
  },
  requirement: {
    search: {
      name: '需求名称：',
      user: '创建人：',
      time: '时间段：',
      state: '状态：'
    },
    table: {
      priority: '优先级',
      name: '需求名称',
      createUser: '创建人',
      createTime: '创建时间',
      state: '状态'
    },
    form: {
      name: '需求名称：',
      source: '需求来源：',
      time: '起止时间：',
      module: '从属模块：',
      description: '需求描述：',
      standard: '验收标准：',
      accepters: '验收人员：',
      priority: '优先级：',
      attachments: '需求附件：',
      stateName: '需求状态：'
    },
    title: {
      create: '新增需求',
      update: '修改需求',
      info: '查看需求'
    }
  },
  module: {
    form: {
      name: '名称：',
      description: '描述：'
    }
  },
  team: {
    table: {
      name: '姓名',
      type: '类型',
      title: '岗位',
      time: '加入时间'
    },
    form: {
      name: '人员：',
      type: '类型：',
      title: '岗位：'
    },
    search: {
      name: '姓名：',
      type: '类型：',
      title: '岗位：'
    },
    title: {
      create: '新增成员',
      update: '修改成员'
    }
  }
}
