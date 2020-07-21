<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>

    <breadcrumb class="breadcrumb-container"></breadcrumb>

    <div class="right-menu">
      <error-log class="errLog-container right-menu-item"></error-log>

      <!--<el-tooltip effect="dark" :content="$t('navbar.screenfull')" placement="bottom">
        <screenfull class="screenfull right-menu-item"></screenfull>
      </el-tooltip>

      <lang-select class="international right-menu-item"></lang-select>

      <el-tooltip effect="dark" :content="$t('navbar.theme')" placement="bottom">
        <theme-picker class="theme-switch right-menu-item"></theme-picker>
      </el-tooltip>-->

      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper" style="display:flex">
          <span style="margin-right:5px;">{{this.getUserName()}}</span>
          <img class="user-avatar" :src="userIcon" />
          <i class="el-icon-caret-bottom"></i>
        </div>
        <el-dropdown-menu slot="dropdown">
          <!-- <router-link to="/"> -->
          <router-link to="/pms/index">
            <el-dropdown-item>{{$t('navbar.dashboard')}}</el-dropdown-item>
          </router-link>
          <!--<a target='_blank' href="https://github.com/PanJiaChen/vue-element-admin/">
            <el-dropdown-item>
              {{$t('navbar.github')}}
            </el-dropdown-item>
          </a>-->
          <el-dropdown-item divided>
            <span @click="changePassword" style="display:block;">{{$t('navbar.changePassword')}}</span>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span @click="setUserConfigure" style="display:block;">{{$t('navbar.userConfigure')}}</span>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span @click="logout" style="display:block;">{{$t('navbar.logOut')}}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex';
import Breadcrumb from '@/components/Breadcrumb';
import Hamburger from '@/components/Hamburger';
import ErrorLog from '@/components/ErrorLog';
import Screenfull from '@/components/Screenfull';
import LangSelect from '@/components/LangSelect';
import ThemePicker from '@/components/ThemePicker';
import { getUserIcon, getUserConfigure, getUserName } from '@/utils/auth';

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    LangSelect,
    ThemePicker
  },
  computed: {
    ...mapGetters(['sidebar', 'name', 'avatar'])
  },
  data() {
    return {
      userIcon: '',
      state: {
        editFormVisible: false,
        configureVisible: false
      },
      // 修改密码数据
      temp: {},
      // 个人配置数据
      configeData: {}
    };
  },
  created() {
    this.userIcon = this.getUserIcon();
    this.configeData = this.getUserConfigure();
  },
  methods: {
    // 用户头像
    getUserIcon,
    // 用户个人配置
    getUserConfigure,
    getUserName,
    // 修改弹出框标志的值
    togglePasswordShow() {
      this.state.editFormVisible = false;
    },
    toggleConfigureShow() {
      this.state.configureVisible = false;
    },
    toggleSideBar() {
      this.$store.dispatch('toggleSideBar');
    },
    // 修改密码
    changePassword() {
      this.state.editFormVisible = true;
    },
    // 个人配置
    setUserConfigure() {
      this.state.configureVisible = true;
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload(); // In order to re-instantiate the vue-router object to avoid bugs
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .breadcrumb-container {
    float: left;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .right-menu {
    float: right;
    height: 100%;
    &:focus {
      outline: none;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
    }
    .screenfull {
      height: 20px;
    }
    .international {
      vertical-align: top;
    }
    .theme-switch {
      vertical-align: 15px;
    }
    .avatar-container {
      height: 50px;
      margin-right: 30px;
      .avatar-wrapper {
        cursor: pointer;
        margin-top: 5px;
        position: relative;
        .user-avatar {
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
