<template>
  <div>
    <el-table
      ref="queryTable"
      border
      :data="data"
      v-bind="$attrs"
      style="width: 100%;"
      :height="tableHeight"
      @sort-change="sortChange"
      v-loading="loading"
      element-loading-text
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
      @selection-change="handleSelectionChange"
      :row-class-name="rowClassName"
      size="mini"
    >
      <el-table-column type="selection" width="55" v-if="multipleTable"></el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        v-for="(column, index) in columns"
        :key="column.value"
        :label="tableTitle(column.text)"
        :width="columnWidth(column.width)"
        :min-width="column.minWidth === null?100:column.minWidth"
        align="center"
        :sortable="column.value !== 'action' && column.sortable !== 'false'"
        :fixed="column.fixed"
        :prop="column.value"
      >
        <template slot-scope="scope">
          <el-button
            v-if="column.value === 'action' && hasAutho(action.auth) && actionText(scope.row,column.formatter) "
            size="mini"
            :key="index"
            v-for="(action, index) in column.actions"
            @click="callback(action.func,scope.row,action.formatter)"
            :class="buttonAuth(scope.row,action.formatter)?action.class:action.class + ' disable-class'"
          >{{getButtonText(scope.row,action.text,action)}}</el-button>
          <template v-if="column.value !== 'action' && scope.row.edit && column.edit">
            <FrSelect
              v-if="column.editType === 'select'"
              v-model="scope.row[column.value]"
              :dataFunc="column.selectData"
            />
            <!-- <el-input v-else class="edit-input" size="small" v-model="scope.row[column.value]"></el-input> -->
            <el-input v-else class="edit-input" size="small" v-model="scope.row[column.value]"></el-input>
          </template>
          <template v-else>
            <template v-if="column.editType === 'select'">
              <!-- <FrSelect
                v-model="scope.row[column.value]"
                :dataFunc="column.selectData"
                :disabled="true"
              />-->
              <span>{{scope.row[column.value]}}</span>
            </template>
            <template v-else>
              <span
                v-if="column.value !== 'action' && column.formatter"
              >{{formaterVal(scope.row[column.value],column.formatter,scope.row)}}</span>
              <span v-else>{{formatTableCellValue(scope.row, column.value)}}</span>
            </template>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container" v-if="showPage === true">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="fetchParams.pageNum || pageNum"
        :page-sizes="[10,20,30, 50]"
        :page-size="fetchParams.pageSize || pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request';
import { tableTitle } from '@/utils/i18n';
import { validateFormatter, renderFormatter } from '@/utils/formatter';
import Bus from '@/components/Button/button';
import { isEmpty } from '@/utils/helper';
import { hasAuth } from '@/utils/auth';
import FrSelect from '@/components/Select/index';

/**
 * 普通表格控件
 */
export default {
  name: 'complexTable',
  components: { FrSelect },
  data() {
    return {
      // 表格显示的数据 Array
      data: null,
      // 分页总数
      total: null,
      // 页码
      pageNum: 1,
      // 每页条数
      pageSize: 10,
      // 表格高度
      tableHeight: 200,
      // 升序字段
      ascs: [],
      // 降序字段
      descs: [],
      loading: false
    };
  },
  props: {
    // 请求URL链接 字符串类型 必填
    fetchUrl: {
      type: String,
      default: () => ''
    },
    // 表格列表数组
    columns: {
      type: Array,
      default: () => []
    },
    // 是否显示分页 默认显示
    showPage: {
      type: Boolean,
      default: true
    },
    // 是否可选择
    multipleTable: false,
    // 查询条件
    fetchParams: {
      type: Object,
      default: () => {}
    },
    selectedVal: [Number, Array, String],
    defaultHeight: null,
    defaultData: {
      type: Array,
      default: () => []
    },
    visible: {
      type: Boolean
    },
    defaultAscs: {
      type: Array,
      default: () => []
    },
    defaultDescs: {
      type: Array,
      default: () => []
    },
    // 显示加载
    showLoading: {
      type: Boolean,
      default: true
    },
    // 除表格高度外，剩余高度
    surplus: {
      type: Number,
      default: 100
    },
    // 界面可视高度
    clientHeightValue: {
      type: Number,
      default: () => document.body.clientHeight
    },
    // 界面可视宽度
    clientWidthValue: {
      type: Number,
      default: () => document.body.clientWidth
    }
  },
  // 页面DOM创建前获取数据
  created() {
    this.tableHeight =
      this.defaultHeight || this.clientHeightValue - this.surplus;
    this.getList();
  },
  watch: {
    visible() {
      this.renderSelect(this.selectedVal);
    },
    clientHeightValue() {
      this.tableHeight =
        this.defaultHeight || this.clientHeightValue - this.surplus;
    }
  },
  methods: {
    // 获取数据
    getList(_fetchParams, _fetchUrl) {
      const __fetchUrl = _fetchUrl || this.fetchUrl;
      if (isEmpty(__fetchUrl)) {
        this.data = [];
        this.total = 0;
        return;
      }
      const __fetchParams = _fetchParams || this.fetchParams;
      __fetchParams.pageSize = this.fetchParams.pageSize || this.pageSize;
      __fetchParams.pageNum = this.fetchParams.pageNum || this.pageNum;
      if (
        (isEmpty(this.ascs) || this.ascs.length === 0) &&
        (isEmpty(this.descs) || this.descs.length === 0)
      ) {
        __fetchParams.ascs = this.defaultAscs.join();
        __fetchParams.descs = this.defaultDescs.join();
      } else {
        __fetchParams.ascs = this.ascs.join();
        __fetchParams.descs = this.descs.join();
      }
      if (this.showLoading) {
        this.loading = true;
      }
      new Promise((resolve, reject) => {
        request({
          url: __fetchUrl,
          method: 'GET',
          params: __fetchParams
        })
          .then(response => {
            console.log(response);
            // const items = response.data
            // this.data = items.map(v => {
            //     this.$set(v, 'edit', false)
            //     return v
            // })
            this.data = this.renderData(response.data);
            this.total = response.total;
            resolve();
          })
          .catch(_ => {
            this.data = [];
            this.total = 0;
            reject();
          });
      })
        .then(success => {
          this.loading = false;
          this.renderSelect(this.selectedVal);
          Bus.$emit('queryTable');
        })
        .catch(_ => {
          this.loading = false;
          Bus.$emit('queryTable');
        });
    },
    renderData(items) {
      var rData = [];
      Array.from(items).forEach((item, index) => {
        this.$set(item, 'edit', false);
        if (this.data && this.data.length > 0) {
          const i = this.data[index];
          if (i && item.id === i.id && i.edit) {
            this.$set(item, 'edit', true);
            this.columns.forEach(column => {
              if (column.edit) {
                item[column.value] = i[column.value];
              }
            });
          }
          //   this.data.forEach(i => {
          //     if (item.id === i.id && i.edit) {
          //       item.edit = true;
          //       this.columns.forEach(column => {
          //         if (column.edit) {
          //           item[column.value] = i[column.value];
          //         }
          //       });
          //     }
          //   });
        }
        rData.push(item);
      });
      return rData;
    },
    // 回显
    renderSelect(val) {
      const thiz = this;
      if (!isEmpty(this.$refs.queryTable)) {
        this.$refs.queryTable.clearSelection();
      }
      if (
        this.multipleTable &&
        !isEmpty(val) &&
        !isEmpty(this.data) &&
        this.data instanceof Array
      ) {
        this.data.forEach(column => {
          if (val instanceof Array) {
            if (val.length === 0) {
              thiz.$refs.queryTable.clearSelection();
            } else if (val.indexOf(column.id) > -1) {
              thiz.$refs.queryTable.toggleRowSelection(column, true);
            }
          } else {
            if (val === column.id) {
              thiz.$refs.queryTable.toggleRowSelection(column, true);
            }
          }
        });
      }
      if (this.multipleTable && isEmpty(val)) {
        thiz.$refs.queryTable.clearSelection();
      }
    },
    // 回调父页面函数
    callback(func, row, formatter) {
      if (this.buttonAuth(row, formatter)) {
        func(row);
      }
    },
    // 页码变化调用
    handleSizeChange(val) {
      this.fetchParams.pageSize = val;
      this.getList();
    },
    // 页数变化调用
    handleCurrentChange(val) {
      this.fetchParams.pageNum = val;
      this.getList();
    },
    // 格式化column值
    formaterVal(val, formatter, row) {
      if (validateFormatter(formatter)) {
        return this.$t(renderFormatter(val, formatter));
      }
      if (typeof formatter === 'function') {
        return formatter(row);
      }
      return val;
    },
    // 按钮权限
    buttonAuth(row, formatterFunc) {
      if (typeof formatterFunc === 'function') {
        var s = formatterFunc(row);
        return s;
      }
      return true;
    },
    actionText(row, formatterFunc) {
      if (typeof formatterFunc === 'function') {
        var s = formatterFunc(row);
        return s;
      }
      return true;
    },
    // 排序
    sortChange(column) {
      this.ascs = [];
      this.descs = [];
      if (column.order === 'ascending') {
        this.ascs.push(column.prop);
      } else {
        this.descs.push(column.prop);
      }
      this.getList();
    },
    // 处理百分比宽度、固定宽度
    columnWidth(val) {
      if (isEmpty(val)) {
        return 100;
      }
      const _width =
        window.innerWidth ||
        document.documentElement.clientWidth ||
        document.body.clientWidth ||
        1000;
      let tableWidth = _width;
      if (this.$store.getters.sidebar.opened) {
        // body - max-sidebar-padding
        tableWidth = _width - 180 - 40;
      } else {
        // body - min-sidebar-padding
        tableWidth = _width - 36 - 40;
      }
      if (val.indexOf('%') >= 0) {
        val = val.replace('%', '');
        const ss = (tableWidth * Number(val)) / 100;
        return ss;
      }
      return val;
    },
    handleSelectionChange(val) {
      this.$emit('handleSelectionChange', val, this.data);
    },
    // 是否有权限操作
    hasAutho(val) {
      if (isEmpty(val)) {
        return true;
      }
      return this.hasAuth(val);
    },
    rowClassName(row, index) {
      if (
        !isEmpty(row) &&
        !isEmpty(row.row) &&
        row.row.hiddenRow === 'hidden'
      ) {
        return 'visibility-hidden';
      }
      return '';
    },
    getButtonText(row, text, action) {
      if (row && row.edit && !isEmpty(action.editText)) {
        text = action.editText;
      }
      return this.tableTitle(text);
    },
    // 表格标题国际化函数
    tableTitle,
    hasAuth,
    // 格式化表格单元值
    formatTableCellValue(row, columnValue) {
      const layer = columnValue.split('.');
      if (layer.length > 0) {
        var cellValue = row[layer[0]];
        if (layer.length > 1) {
          for (let i = 1; i < layer.length; i++) {
            cellValue = cellValue[layer[i]];
          }
        }
        return cellValue;
      }
    }
  }
};
</script>