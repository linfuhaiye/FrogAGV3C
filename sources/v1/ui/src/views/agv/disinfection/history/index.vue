<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <div class="content flex-box fillParent">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/disinfection/task')"
        >配货任务</div>
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/disinfection/call')"
        >叫料</div>
        <div
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
        >叫料历史</div>
      </div>
      <!-- 右边内容 -->
      <div
        class="flex-box flex-direction-column"
        style="width:100%;margin-left:10px;margin-right:20px;"
      >
        <!-- 按钮 -->
        <div class="flex-box content-button flex-align-items-center">
          <div
            class="btn btn-default flex-box flex-justify-content-center flex-align-items-center"
            @click="showAll()"
          >所有</div>
          <div
            class="btn btn-default btn-click flex-box flex-justify-content-center flex-align-items-center"
            @click="showUnFinish()"
          >未验收</div>
        </div>
        <!-- 数据表格 -->
        <ComplexTable
          fetchUrl="/agv/callMaterials"
          :columns="columns"
          border
          ref="dataTable"
          :fetchParams="fetchParams"
          :surplus="152"
        ></ComplexTable>
      </div>
    </div>
  </div>
</template>

<script>
import ComplexTable from '@/components/ComplexTable';
import '../../product/home/home.scss';
import '../../table.scss';
import request from '@/utils/request';
import Constants from '@/utils/constants';
import { isEmpty } from '@/utils/helper';
import { Loading } from 'element-ui';

const areaCoding = process.env.AREA_CODING;
export default {
  name: 'call',
  components: { ComplexTable },
  created() {
    this.loadingInfo();
  },
  data() {
    return {
      fetchParams: {
        areaCoding: areaCoding,
        type: 3,
        state: 0
      },
      state: {},
      // 加载对象
      load: null,
      columns: [
        {
          text: 'agv.bom.name',
          value: 'materialName',
          width: 'auto',
          sortable: 'false'
        },
        {
          text: 'agv.bom.code',
          value: 'materialCode',
          width: '20%',
          sortable: 'false'
        },
        {
          text: 'agv.bom.num',
          value: 'count',
          width: '20%',
          sortable: 'false'
        },
        {
          text: 'agv.bom.status',
          value: 'state',
          formatter: (item) => this.formaterState(item.state),
          width: '20%',
          sortable: 'false'
        },
        {
          text: 'table.actions',
          value: 'action',
          width: '20%',
          fixed: 'right',
          actions: [
            // 取消
            {
              text: 'table.cancel',
              class: 'cancel-btn',
              func: (row) => this.cancelCall(row),
              formatter: (item) => this.hasAuth(item)
            }
          ]
        }
      ]
    };
  },
  methods: {
    loadingInfo() {
      this.$store.dispatch('updateTitle', '消毒间叫料历史');
      this.$store.dispatch('updateNeedLogin', false);
    },
    showAll() {
      this.fetchParams.state = null;
      this.reloadTable();
    },
    showUnFinish() {
      this.fetchParams.state = 0;
      this.reloadTable();
    },
    // 跳转
    turn(url) {
      this.$router.push({ path: url });
    },
    toggleShow() {},
    // 取消叫料
    cancelCall(row) {
      this.load = this.showErrorMessage('正在取消，请稍候......');
      request({
        url: '/agv/callMaterials/cancel/' + row.id,
        method: 'DELETE'
      })
        .then((response) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          if (response.errno === 0) {
            this.reloadTable();
          }
        })
        .catch((_) => {
          // 如果遮罩层存在
          if (!isEmpty(this.load)) {
            this.load.close();
          }
          this.$message.error('服务器请求失败');
        });
    },
    // 是否可以点击
    hasAuth(item) {
      if (item.state === Constants.deliveryState[0].value) {
        return true;
      } else {
        return false;
      }
    },
    // 格式化状态
    formaterState(state) {
      let stateName = '';
      Constants.deliveryState.forEach((item) => {
        if (item.value === state) {
          stateName = item.label;
        }
      });
      return stateName;
    },
    // 重新加载表格数据.
    reloadTable() {
      this.$refs.dataTable.getList();
    },
    // 用遮罩层显示错误信息
    showErrorMessage(message) {
      const options = {
        lock: true,
        fullscreen: true,
        text: message,
        spinner: '',
        background: 'rgba(0, 0, 0, 0.7)'
      };
      return Loading.service(options);
    }
  }
};
</script>
