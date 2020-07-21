<template>
  <div
    id="app"
    :style="{backgroundImage:`url(${backgroundImage})`,backgroundRepeat: 'no-repeat', backgroundSize: 'cover'}"
  >
    <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>

<script>
  export default {
    name: 'App',
    provide() {
      return {
        reload: this.reload
      }
  },
    data() {
      return {
        backgroundImage: require('./images/background.jpg'),
        isRouterAlive: true
      }
  },
    methods: {
      reload() {
        this.isRouterAlive = false
        this.$nextTick(() => (this.isRouterAlive = true))
      }
    }
  }
</script>
