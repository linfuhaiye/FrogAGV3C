<template>
  <div class="flex-box flex-direction-column" style="height:100%">
    <div class="content flex-box fillParent">
      <!-- 左边菜单 -->
      <div class="left-menu">
        <div
          class="menu-item current-menu flex-box flex-justify-content-center flex-align-items-center"
        >配送管理</div>
        <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/wave')"
        >波次管理</div>
        <div
          v-if="auth==='admin'"
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/setting')"
        >生产设置</div>
        <!-- <div
          class="menu-item flex-box flex-justify-content-center flex-align-items-center"
          @click="turn('/agv/call/history')"
        >叫料历史</div>-->
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
          >显示所有</div>
          <div
            class="btn btn-default btn-click flex-box flex-justify-content-center flex-align-items-center"
            @click="showUnFinish()"
          >未完成</div>
          <!-- <div
            class="btn btn-default btn-add flex-box flex-justify-content-center flex-align-items-center"
            @click="addWave()"
          >新增</div>-->
        </div>
        <!-- 表头内容 -->
        <div class="flex-box data-header-content flex-align-items-center" style="width:100%;">
          <div class="data-header-name">名称</div>
          <div class="data-header-num">数量</div>
          <!-- <div class="data-header-done">已验收</div> -->
          <div class="data-header-operation"></div>
        </div>
        <!-- 表格内容 -->
        <div class="flex-box data-content flex-direction-column" style="width:100%; color:yellow;">
          <div v-for="(item) in waves" :key="item.id">
            <div class="data-content-produce-row flex-box flex-align-items-center">
              <div
                class="data-name"
              >{{item.materialName+" （"+item.productLineCode+"） "+item.executionTime}}</div>
              <div class="bom-num"></div>
              <!-- <div class="bom-done"></div> -->
              <div class="data-content-produce-operation flex-box flex-align-items-center">
                <div class="wave-add" @click="addWave(item)">新增</div>
                <div class="wave-delete" @click="deleteProduce(item)">删除</div>
                <div class="wave-add" @click="changeLine(item)">换线</div>
              </div>
            </div>
            <div class="data-wave" v-for="(wave,index) in item.waveModels" :key="wave.id">
              <div class="data-content-wave-row flex-box flex-align-items-center">
                <div class="wave-name">{{'波次' + (index + 1)+" ["+formateState(wave.state)+"]"}}</div>
                <div class="bom-num"></div>
                <!-- <div class="bom-done"></div> -->
                <div class="data-content-wave-operation flex-box flex-align-items-center">
                  <div class="wave-delete" @click="deleteWave(wave)">删除</div>
                </div>
              </div>
              <div
                v-for="(bom) in wave.waveDetailModels"
                :key="bom.id"
                class="flex-box data-content-row"
              >
                <div
                  class="bom-name flex-box flex-align-items-center flex-justify-content-center"
                >{{bom.materialName}}</div>
                <div
                  class="bom-num flex-box flex-align-items-center flex-justify-content-center"
                >{{bom.count}}</div>
                <!-- <div
                  class="bom-done flex-box flex-align-items-center flex-justify-content-center"
                >{{bom.done}}</div>-->
                <div class="data-content-operation flex-box flex-align-items-center">
                  <div class="bom-delete" @click="deleteBom(bom.id)">删除</div>
                  <div class="bom-update" @click="updateBom(bom)">修改</div>
                </div>
              </div>
              <div class="flex-box data-content-row">
                <div class="bom-add-icon" @click="addBom(wave)"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog
      v-if="state.editBomVisible"
      :visible.sync="state.editBomVisible"
      title="修改原料"
      class="dialog-transfer"
    >
      <EditBom :bom="editBom" @toggleShow="toggleShow" @reloadWaves="getWaves"></EditBom>
    </el-dialog>
    <el-dialog
      v-if="state.addBomVisible"
      :visible.sync="state.addBomVisible"
      title="添加原料"
      class="dialog-transfer"
      :width="'75%'"
    >
      <AddBom :bom="addBomWave" @toggleShow="toggleShow" @reloadWaves="getWaves"></AddBom>
    </el-dialog>
    <el-dialog
      v-if="state.changeLineVisible"
      :visible.sync="state.changeLineVisible"
      :title="changeLinePositionName"
      class="dialog-transfer"
      :width="'75%'"
    >
      <TaskOut :product="changeLineProduct" @toggleShow="toggleShow" @reloadWaves="getWaves"></TaskOut>
    </el-dialog>
  </div>
</template>

<script>
  import './home.scss';
  import TaskOut from './changeLine';
  import EditBom from './editBom';
  import AddBom from './addBom';
  import request from '@/utils/request';
  import Constants from '@/utils/constants';
  import { isEmpty } from '@/utils/helper';
  import { Loading } from 'element-ui';

  const areaTypeString = process.env.AREA_TYPE;
  // 配送管理
  export default {
    name: 'home',
    components: { EditBom, AddBom, TaskOut },
    created() {
      this.loadingInfo();
    },
    data() {
      return {
        state: {
          editBomVisible: false,
          addBomVisible: false,
          changeLineVisible: false
        },
        // 加载对象
        load: null,
        editBom: null,
        addBomWave: null,
        waves: [],
        waveState: 0,
        teamId: '',
        areaType: 1, // 区域类型,默认灌装区 1:灌装区;2:包装区
        auth: 'user',
        changeLineProduct: null,
        changeLinePositionName: ''
      };
    },
    methods: {
      loadingInfo() {
        this.teamId = this.$store.state.AgvHeader.teamId;
        this.auth = this.$store.state.AgvHeader.auth;
        this.formateAreaType();
        this.timer();
      },
      formateAreaType() {
        if (areaTypeString === 'filling') {
          this.areaType = 1;
          this.$store.dispatch('updateTitle', '灌装区配送管理');
        } else if (areaTypeString === 'packing') {
          this.areaType = 2;
          this.$store.dispatch('updateTitle', '包装区配送管理');
        }
      },
      timer() {
        this.getWaves();
        if (this.timer) {
          clearInterval(this.timer);
        }
        this.timer = setInterval(() => {
          this.getWaves();
        }, 5000);
      },
      showAll() {
        this.waveState = null;
        this.getWaves();
      },
      showUnFinish() {
        this.waveState = 0;
        this.getWaves();
      },
      // 跳转到指定页面
      turn(url) {
        this.$router.push({ path: url });
      },
      // 删除原料 -删除完后需要刷新
      deleteBom(bomId) {
        this.load = this.showErrorMessage('删除中,请稍后...');
        request({
          url: '/agv/waves/deleteDetail/' + bomId,
          method: 'DELETE'
        })
          .then(response => {
            // 如果遮罩层存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            if (response.errno === 0) {
              this.getWaves();
            }
          })
          .catch(_ => {
            // 如果遮罩层存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            this.$message.error('服务器请求失败');
          });
      },
      // 修改原料信息
      updateBom(bom) {
        this.editBom = bom;
        this.state.editBomVisible = true;
      },
      addBom(wave) {
        this.addBomWave = wave;
        this.state.addBomVisible = true;
      },
      // 删除波次
      deleteWave(wave) {
        this.load = this.showErrorMessage('删除中,请稍后...');
        request({
          url: '/agv/waves/deleteWave',
          method: 'DELETE',
          params: {
            waveCode: wave.code
          }
        })
          .then(response => {
            // 如果遮罩层存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            if (response.errno === 0) {
              this.getWaves();
            }
          })
          .catch(_ => {
            // 如果遮罩层存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            this.$message.error('服务器请求失败');
          });
      },
      // 删除产品
      deleteProduce(produce) {
        this.load = this.showErrorMessage('删除中,请稍后...');
        request({
          url: '/agv/waves/deleteWaves',
          method: 'DELETE',
          data: produce
        })
          .then(response => {
            // 如果遮罩层存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            if (response.errno === 0) {
              this.getWaves();
            }
          })
          .catch(_ => {
            // 如果遮罩层存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            this.$message.error('服务器请求失败');
          });
      },
      // 根据产品 增加一个波次
      addWave(produce) {
        // 如果遮罩层存在
        if (!isEmpty(this.load)) {
          this.load.close();
        }
        this.load = this.showErrorMessage('新增中,请稍后...');
        request({
          url: '/agv/waves',
          method: 'POST',
          data: produce
        })
          .then(response => {
            // 如果遮罩层存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            if (response.errno === 0) {
              this.getWaves();
            }
          })
          .catch(_ => {
            // 如果遮罩层存在
            if (!isEmpty(this.load)) {
              this.load.close();
            }
            this.$message.error('服务器请求失败');
          });
      },
      changeLine(product) {
        console.log(product);
        this.changeLineProduct = product;
        this.changeLinePositionName = product.materialName;
        this.state.changeLineVisible = true;
      },
      toggleShow() {
        this.state.editBomVisible = false;
        this.state.addBomVisible = false;
        this.state.changeLineVisible = false;
      },
      getWaves() {
        request({
          url: '/agv/waves',
          method: 'GET',
          params: {
            type: this.areaType,
            teamId: this.teamId,
            state: this.waveState
          }
        })
          .then(response => {
            if (response.errno === 0) {
              this.waves = response.data;
            }
          })
          .catch(_ => {
            console.log(_);
          });
      },
      // 格式化状态
      formateState(waveState) {
        let stateName = '';
        Constants.waveState.forEach(item => {
          if (item.value === waveState) {
            stateName = item.label;
          }
        });
        return stateName;
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
