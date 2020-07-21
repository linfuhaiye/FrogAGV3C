<template>
  <div v-if="!item.hidden&&item.children" class="menu-wrapper">

      <router-link v-if="hasNoShowingChild(item.children) " :to="resolvePath(item.path)">
        <el-menu-item :index="resolvePath(item.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <svg-icon v-if="item&&item.icon" :icon-class="item.icon"></svg-icon>
          <span v-if="item&&item.name" slot="title">{{generateTitle(item.name)}}</span>
        </el-menu-item>
      </router-link>

      <el-submenu v-else :index="item.name||item.path">
        <template slot="title">
          <svg-icon v-if="item&&item.icon" :icon-class="item.icon"></svg-icon>
          <span v-if="item&&item.name" slot="title">{{generateTitle(item.name)}}</span>
        </template>

        <template v-for="child in item.children" v-if="!child.hidden">
          <sidebar-item :is-nest="true" class="nest-menu" v-if="child.children&&child.children.length>0" :item="child" :key="child.path" :base-path="resolvePath(child.path)"></sidebar-item>

          <router-link v-else :to="resolvePath(child.path)" :key="child.name">
            <el-menu-item :index="resolvePath(child.path)">
              <svg-icon v-if="child&&child.icon" :icon-class="child.icon"></svg-icon>
              <span v-if="child&&child.name" slot="title">{{generateTitle(child.name)}}</span>
            </el-menu-item>
          </router-link>
        </template>
      </el-submenu>

  </div>
</template>

<script>
import path from 'path'
import { generateTitle } from '@/utils/i18n'

export default {
  name: 'SidebarItem',
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      onlyOneChild: null
    }
  },
  methods: {
    hasNoShowingChild(children) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // temp set(will be used if only has one showing child )
          this.onlyOneChild = item
          return true
        }
      })
      if (showingChildren.length === 0) {
        return true
      }
      return false
    },
    resolvePath(...paths) {
      const s = path.resolve(this.basePath, ...paths)
      return s
    },
    generateTitle
  }
}
</script>

