<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <div class="content flex-box fillParent">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/materials/pack')"
        >包装配货</div>
        <div
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
        >备货任务</div>
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
          >灌装区</div>
          <div
            class="btn btn-default btn-click flex-box flex-justify-content-center flex-align-items-center"
          >包装区</div>
        </div>
        <!-- 表头内容 -->
        <div class="flex-box data-header-content flex-align-items-center" style="width:100%;">
          <div class="data-header-name">名称</div>
          <div class="data-header-num">数量</div>
          <div class="data-header-done">状态</div>
        </div>
        <!-- 表格内容 -->
        <div class="flex-box data-content flex-direction-column" style="width:100%;">
          <div v-for="(item) in cData" :key="item.id">
            <div class="data-content-produce-row flex-box flex-align-items-center">
              <div class="data-name">{{item.name}}</div>
              <div class="bom-num"></div>
              <div class="bom-done"></div>
            </div>
            <div class="data-wave" v-for="(wave,index) in item.waves" :key="wave.id">
              <div class="data-content-wave-row flex-box flex-align-items-center">
                <div class="wave-name">{{'波次' + (index + 1)}}</div>
                <div class="bom-num"></div>
                <div class="bom-done"></div>
              </div>
              <div v-for="(bom) in wave.boms" :key="bom.id" class="flex-box data-content-row">
                <div
                  class="bom-name flex-box flex-align-items-center flex-justify-content-center"
                >{{bom.name}}</div>
                <div
                  class="bom-num flex-box flex-align-items-center flex-justify-content-center"
                >{{bom.num}}</div>
                <div
                  class="bom-done flex-box flex-align-items-center flex-justify-content-center"
                >{{_getStatus(bom.done)}}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import '../../product/home/home.scss';
// 包材商备货任务
export default {
  name: 'home',
  components: {},
  created() {
    this.$store.dispatch('updateTitle', '包材仓备货任务');
    this.$store.dispatch('updateNeedLogin', false);
  },
  data() {
    return {
      state: {
        editBomVisible: false,
        addBomVisible: false
      },
      editBomId: null,
      addBomWaveId: null,
      cData: [
        {
          id: 1,
          name: '产品A（L15）',
          waves: [
            {
              id: 1,
              boms: [
                { id: 1, name: '原料A', num: 50, done: 1 },
                { id: 2, name: '原料B', num: 50, done: 1 },
                { id: 3, name: '原料C', num: 50, done: 1 }
              ]
            },
            {
              id: 2,
              boms: [
                { id: 1, name: '原料A', num: 50, done: 0 },
                { id: 2, name: '原料B', num: 50, done: 0 }
              ]
            }
          ]
        },
        {
          id: 2,
          name: '产品B（L15）',
          waves: [
            {
              id: 2,
              boms: [
                { id: 1, name: '原料A', num: 50, done: 1 },
                { id: 2, name: '原料B', num: 50, done: 0 }
              ]
            }
          ]
        }
      ]
    };
  },
  methods: {
    _getStatus(status) {
      if (status === 1) {
        return '已备货';
      } else if (status === 0) {
        return '未备货';
      }
      return status;
    },
    // 跳转到波次管理页面
    turn(url) {
      this.$router.push({ path: url });
    },
    // 删除原料 -删除完后需要刷新cData
    deleteBom(bomId) {
      console.log('deleteBom>>>>>>' + bomId);
    },
    // 修改原料信息
    updateBom(bomId) {
      this.editBomId = bomId;
      this.state.editBomVisible = true;
    },
    addBom(waveId) {
      console.log('addBom----waveId', waveId);
      this.addBomWaveId = waveId;
      this.state.addBomVisible = true;
    },
    // 删除波次
    deleteWave(waveId) {
      console.log('deleteWave>>>>>>' + waveId);
    },
    // 删除产品
    deleteProduce(produceId) {
      console.log('deleteProduce>>>>>>' + produceId);
    },
    // 根据产品ID 增加一个波次
    addWave(produceId) {
      console.log('addWave>>>>>>' + produceId);
    },
    toggleShow() {
      this.state.editBomVisible = false;
      this.state.addBomVisible = false;
    }
  }
};
</script>
<style>
.data-content {
  height: calc(100vh - 180px);
}
</style>
