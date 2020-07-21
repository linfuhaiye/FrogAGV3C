export default {
  hoist: {
    table: {
      code: '提升机编号',
      currBatchNo: '当前批次号',
      total: '生产总量',
      nextBatchNo: '下一批次号',
      hoistState: '提升机状态',
      branchState: '分线状态'
    },
    button: {
      clear: '清线',
      update: '修改',
      start: '启动',
      save: '保存',
      refresh: '刷新'
    }
  },
  branch: {
    table: {
      code: '分线编号',
      hoistCode: '提升机编号',
      batchNo: '批次号',
      branchState: '分线状态',
      nextBatchNo: '下一批次号'
    },
    button: {
      clear: '完成清线',
      update: '修改',
      save: '保存',
      refresh: '刷新'
    }
  },
  batch: {
    table: {
      batchNo: '产品序号',
      batchNum: '产品条码',
      sizeNo: '尺寸序号',
      total: '码垛到位感应器个数'
    },
    button: {
      refresh: '刷新'
    }
  },
  size: {
    table: {
      sizeNo: '尺寸序号',
      length: '长',
      width: '宽',
      height: '高'
    },
    form: {
      no: '尺寸序号：',
      length: '长：',
      width: '宽：',
      height: '高：'
    },
    placeholder: {
      no: '请输入尺寸序号',
      length: '请输入长度',
      width: '请输入宽度',
      height: '请输入高度'
    },
    title: {
      update: '编辑尺寸',
      create: '新增尺寸'
    }
  },
  history: {
    today: {
      table: {
        batchNo: '产品批次号',
        num: '生产数量',
        count: '码垛数量',
        ngCount: 'NG数量',
        productionDate: '生产日期',
        state: '执行状态'
      },
      placeholder: {
        batchNo: '请输入产品批次号'
      }
    },
    batch: {
      table: {
        batchNo: '产品批次号',
        num: '生产数量',
        count: '码垛数量',
        ngCount: 'NG数量',
        productionDate: '生产日期'
      },
      form: {
        startDate: '开始时间：',
        endDate: '结束时间：'
      },
      placeholder: {
        batchNo: '请输入产品批次号',
        startDate: '开始时间',
        endDate: '结束时间'
      }
    },
    branch: {
      table: {
        num: '生产数量',
        count: '码垛数量',
        ngCount: 'NG数量',
        productionDate: '生产日期',
        branchCode: '分线编号'
      },
      form: {
        startDate: '开始时间：',
        endDate: '结束时间：'
      },
      placeholder: {
        branchCode: '请输入分线编号',
        startDate: '开始时间',
        endDate: '结束时间'
      }
    },
    button: {
      clear: '清空条件'
    }
  }
};
