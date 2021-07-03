<template>
    <div style="padding:30px;">
        <el-alert :closable="false" title="menu 1">
            <router-view/>
        </el-alert>
    </div>
</template>
<script>
export default {
    name: 'menu1',
    created() {
        // 修复三级菜单不缓存问题
        /*const route = this.$route.matched.find(item => item.name === 'menu1')
        this.$store.dispatch('tagsView/addCachedView', route)
        console.log(111)*/
    }
}
</script>
