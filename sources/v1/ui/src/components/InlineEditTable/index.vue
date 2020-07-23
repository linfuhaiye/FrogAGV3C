<template>
  <div class="app-container">
    <el-table
      :data="data"
      v-loading="loading"
      border
      fit
      highlight-current-row
      style="width: 100%"
      :height="tableHeight"
      :row-dblclick="rowDblclick"
    >
      <el-table-column
        v-for="(column, index) in columns"
        :key="column.value"
        :label="tableTitle(column.text)"
      >
        <template slot-scope="scope">
          <el-button
            v-if="column.value === 'action' "
            v-for="(action, index) in column.actions"
            :key="index"
          >{{tableTitle(action.text)}}</el-button>
          <template v-if="scope.row.edit">
            <el-input class="edit-input" size="small" v-model="scope.row.title"></el-input>
          </template>
          <span v-else>{{scope.row[column.value]}}</span>
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
import { isEmpty } from '@/utils/helper';
import request from '@/utils/request';
import { tableTitle } from '@/utils/i18n';
import MultipleSelect from '@/components/Select/multiple';

/**
 * 行内编辑表格
 */
export default {
  name: 'inlineEditTable',
  components: { MultipleSelect },
  data() {
    return {
      // 表格显示的数据 Array
      data: [],
      // 分页总数
      total: null,
      // 页码
      pageNum: 1,
      // 每页条数
      pageSize: 10,
      // 升序字段
      ascs: [],
      // 降序字段
      descs: [],
      loading: false,
      // 表格高度
      tableHeight: 200
    };
  },
  props: {
    // 请求URL链接 字符串类型 必填
    fetchUrl: {
      type: String,
      default: () => ''
    },
    // 是否显示分页 默认显示
    showPage: {
      type: Boolean,
      default: true
    },
    // 表格列表数组
    columns: {
      type: Array,
      default: () => []
    },
    // 查询条件
    fetchParams: {
      type: Object,
      default: () => {}
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
    }
  },
  created() {
    this.getList();
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
      this.loading = true;
      new Promise((resolve, reject) => {
        request({
          url: __fetchUrl,
          method: 'GET',
          params: __fetchParams
        })
          .then(response => {
            this.data = response.data;
            this.total = response.total;
            resolve();
          })
          .catch(_ => {
            this.data = [];
            this.total = 0;
            reject();
          });
      }).then(
        success => {
          this.loading = false;
          this.renderSelect(this.selectedVal);
        },
        () => {
          this.loading = false;
        }
      );
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
    // 行双击事件
    rowDblclick(row, event) {
      row.edit = true;
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
    tableTitle
  }
};
</script>
