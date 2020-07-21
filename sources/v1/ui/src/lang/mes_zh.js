export default {
  factory: {
    placeholder: {
      name: '请输入工厂名称'
    },
    search: {
      name: '工厂名称：'
    },
    table: {
      name: '工厂名称',
      code: '编码',
      managerName: '负责人',
      area: '地点',
      description: '描述'
    },
    form: {
      name: '工厂名称：',
      code: '工厂编码：',
      area: '地点：',
      managerName: '负责人：',
      description: '描述：'
    },
    title: {
      update: '编辑工厂信息',
      create: '添加工厂信息'
    }
  },
  productionLine: {
    placeholder: {
      name: '请输入生产线名称'
    },
    search: {
      name: '生产线名称：'
    },
    table: {
      name: '生产线名称',
      code: '编码',
      typeName: '类型',
      managerName: '负责人',
      workshop: '车间',
      description: '描述'
    },
    form: {
      name: '生产线名称：',
      code: '生产线编码：',
      managerName: '负责人：',
      description: '描述：',
      workshop: '车间：',
      type: '类型：'
    },
    title: {
      update: '编辑生产线信息',
      create: '添加生产线信息',
      workshop: '选择车间',
      type: '选择类型'
    }
  },
  workshop: {
    placeholder: {
      name: '请输入车间名称'
    },
    search: {
      name: '车间名称：'
    },
    table: {
      name: '车间名称',
      code: '编码',
      managerName: '负责人',
      factory: '工厂',
      description: '描述'
    },
    form: {
      name: '车间名称：',
      code: '车间编码：',
      area: '区域：',
      managerName: '负责人：',
      factory: '工厂：',
      description: '描述：'
    },
    title: {
      update: '编辑车间信息',
      create: '添加车间信息',
      factory: '选择工厂'
    }
  },
  workingStation: {
    placeholder: {
      name: '请输入工作站名称'
    },
    search: {
      name: '工作站名称：'
    },
    table: {
      name: '工作站名称',
      code: '编码',
      workingStationCategoryName: '类型',
      managerName: '负责人',
      productionLine: '生产线',
      description: '描述'
    },
    form: {
      name: '工作站名称：',
      code: '工作站编码：',
      managerName: '负责人：',
      description: '描述：',
      productionLine: '生产线：',
      workingStationCategory: '类型：'
    },
    title: {
      update: '编辑工作站信息',
      create: '添加工作站信息',
      productionLine: '选择生产线',
      workingStationCategory: '选择工作站类型'
    }
  },
  workingStationCategory: {
    placeholder: {
      name: '请输入工作站类型名称'
    },
    search: {
      name: '工作站类型名称：'
    },
    table: {
      name: '工作站类型名称',
      description: '描述'
    },
    form: {
      name: '工作站类型名称：',
      description: '描述：'
    },
    title: {
      update: '编辑工作站类型信息',
      create: '添加工作站类型信息'
    }
  },
  workshift: {
    placeholder: {
      name: '请输入班次名称'
    },
    search: {
      name: '班次名称：'
    },
    table: {
      name: '班次名称',
      code: '班次编码',
      description: '描述',
      worktime: '工作时间(小时)',
      starttime: '生效时间',
      endtime: '失效时间'
    },
    form: {
      name: '班次名称：',
      code: '班次编码：',
      worktime: '工作时间(小时)：',
      starttime: '生效时间：',
      endtime: '失效时间：'
    },
    title: {
      update: '编辑班次信息',
      create: '添加班次信息',
      choose: '选择班次'
    }
  },
  workingSystem: {
    placeholder: {
      name: '请输入班制名称'
    },
    search: {
      name: '班制名称：'
    },
    table: {
      name: '班次名称',
      code: '班次编码',
      worktime: '工作时间(小时)',
      starttime: '开始时间',
      endtime: '结束时间',
      description: '描述',
      workInterval: '工作区间',
      num: '序号'
    },
    form: {
      name: '班制名称：',
      code: '班制编码：',
      worktime: '工作时间(小时)：',
      starttime: '生效时间：',
      endtime: '失效时间：',
      description: '描述：'
    },
    title: {
      update: '编辑班制信息',
      create: '添加班制信息'
    }
  },
  workingCalendar: {
    placeholder: {
      name: '请输入工作日历名称'
    },
    search: {
      name: '工作日历名称：'
    },
    table: {
      name: '工作日历名称',
      code: '工作日历编码',
      description: '描述',
      worktime: '工作时间',
      starttime: '生效时间',
      endtime: '失效时间'
    },
    form: {
      name: '工作日历名称：',
      code: '工作日历编码：',
      worktime: '工作时间：',
      starttime: '生效时间：',
      endtime: '失效时间：'
    },
    title: {
      update: '编辑工作日历信息',
      create: '添加工作日历信息'
    }
  },
  timeperiod: {
    table: {
      num: '序号',
      description: '描述',
      worktime: '工作时间(小时)',
      starttime: '开始时间',
      endtime: '结束时间'
    }
  },
  equipmentCategory: {
    placeholder: {
      name: '请输入设备类型名称'
    },
    search: {
      name: '设备类型名称：'
    },
    table: {
      name: '设备类型名称',
      description: '描述'
    },
    form: {
      name: '设备类型名称：',
      description: '描述：'
    },
    title: {
      update: '编辑设备类型信息',
      create: '添加设备类型信息'
    }
  },
  equipment: {
    placeholder: {
      name: '请输入设备名称'
    },
    search: {
      name: '设备名称：'
    },
    table: {
      name: '设备名称',
      code: '编码',
      equipmentCategoryName: '类型',
      ownerName: '负责人',
      manufacturerName: '设备厂商',
      model: '设备型号',
      workshopName: '所属车间',
      productionLineName: '所属产线',
      stateName: '设备状态',
      description: '描述'
    },
    form: {
      name: '设备名称：',
      code: '编码：',
      equipmentCategoryName: '类型：',
      ownerName: '负责人：',
      manufacturerName: '设备厂商：',
      model: '设备型号：',
      workshopName: '所属车间：',
      productionLineName: '所属产线：',
      stateName: '设备状态：',
      description: '描述：'
    },
    title: {
      update: '编辑设备信息',
      create: '添加设备信息',
      manufacturer: '选择供应商',
      owner: '选择负责人',
      equipmentCategory: '选择设备类型',
      workshop: '选择车间',
      productionLine: '选择生产线'
    }
  },
  billOfMaterial: {
    placeholder: {
      name: '请输入物料名称',
      code: '请输入物料编码',
      drawingNumber: '请输入图号',
      count: '请输入数量',
      weight: '请输入重量',
      specification: '请输入规格'
    },
    search: {
      name: '物料名称：'
    },
    table: {
      name: '物料名称',
      code: '编码',
      category: '类型',
      specification: '规格',
      drawingNumber: '图号',
      unit: '单位',
      source: '来源',
      weight: '重量',
      description: '描述',
      audit: '审核'
    },
    form: {
      name: '物料名称：',
      code: '编码：',
      category: '类型：',
      drawingNumber: '图号：',
      source: '来源：',
      count: '数量：',
      unit: '单位：',
      weight: '重量：',
      manufacturer: '供应商：',
      specification: '规格：',
      state: '状态：',
      description: '描述：'
    },
    title: {
      update: '编辑物料信息',
      create: '添加物料信息',
      info: '审核物料信息',
      manufacturer: '选择供应商'
    },
    button: {
      all: '全部',
      audited: '已审核',
      unaudited: '待审核',
      pass: '审核通过'
    }
  }
};
