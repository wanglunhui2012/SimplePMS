<template>
    <el-dropdown trigger="click" @command="handleSetSize">
        <div>
            <svg-icon class-name="size-icon" icon-class="size"/>
        </div>
        <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-for="item of sizeOptions" :key="item.value" :disabled="size===item.value"
                              :command="item.value">
                {{
                    item.label
                }}
            </el-dropdown-item>
        </el-dropdown-menu>
    </el-dropdown>
</template>

<script>
import {getInternational} from "@/utils/i18nUtil"
export default {
    data() {
        return {
        }
    },
    computed: {
        size() { // 当前大小
            return this.$store.getters.size
        },
        sizeOptions() {// 大小选项，不能放在data中必须放在computed中才可以在切换大小时自动切换
            return [
                // Element UI大小可选项只有3个：medium、small、mini
                {label: getInternational('navigationBar.sizeOptions.default'), value: 'default'},
                {label: getInternational('navigationBar.sizeOptions.medium'), value: 'medium'},
                {label: getInternational('navigationBar.sizeOptions.small'), value: 'small'},
                {label: getInternational('navigationBar.sizeOptions.mini'), value: 'mini'}
            ]
        }
    },
    methods: {
        handleSetSize(size) { // 大小选项，不能放在data中必须放在computed中才可以在切换大小时自动切换
            this.$ELEMENT.size = size
            this.$store.dispatch('app/setSize', size)
            this.refreshView()
            this.$message({
                message: 'Switch Size Success',
                type: 'success'
            })
        },
        refreshView() {
            // 让缓存页面重新渲染
            this.$store.dispatch('tagsView/delAllCachedViews', this.$route)

            const {fullPath} = this.$route

            this.$nextTick(() => {
                this.$router.replace({
                    path: '/redirect' + fullPath
                })
            })
        }
    }

}
</script>
