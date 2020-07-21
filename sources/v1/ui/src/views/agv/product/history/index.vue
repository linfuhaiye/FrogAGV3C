<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <div class="content flex-box fillParent">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/dashboard')"
        >配送管理</div>
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/wave')"
        >波次管理</div>
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
          >所有</div>
          <div
            class="btn btn-default btn-click flex-box flex-justify-content-center flex-align-items-center"
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

export default {
  name: 'call',
  components: { ComplexTable },
  created() {
    this.$store.dispatch('updateTitle', '叫料历史');
  },
  data() {
    return {
      callState: 0,
      fetchParams: {
        type: 1,
        teamId: 'uuidxxxxb03',
        state: this.callState
      },
      state: {},
      columns: [
        {
          text: 'agv.bom.name',
          value: 'name',
          width: '50%',
          sortable: 'false'
        },
        {
          text: 'agv.bom.code',
          value: 'code',
          width: '20%',
          sortable: 'false'
        },
        {
          text: 'agv.bom.num',
          value: 'num',
          width: '15%',
          sortable: 'false'
        },
        // {
        //   text: 'agv.bom.done',
        //   value: 'done',
        //   width: '20%',
        //   sortable: 'false'
        // },
        {
          text: 'agv.bom.status',
          value: 'status',
          width: '15%',
          sortable: 'false'
        }
      ]
    };
  },
  methods: {
    // 跳转到指定页面
    turn(url) {
      this.$router.push({ path: url });
    },
    toggleShow() {},
    callBom(bomId) {
      console.log('callBom>>>>>>>>>>>>', bomId);
    }
  }
};
</script>
