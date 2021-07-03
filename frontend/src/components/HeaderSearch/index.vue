<template>
    <div :class="{'show':show}" class="header-search">
        <svg-icon class-name="search-icon" icon-class="search" @click.stop="click"/>
        <el-select
            ref="headerSearchSelect"
            v-model="search"
            :remote-method="querySearch"
            filterable
            default-first-option
            remote
            :placeholder="searchPlaceholder"
            class="header-search-select"
            @change="change"
        >
            <el-option v-for="item in options" :key="item.path" :value="item" :label="item.title.join(' > ')"/>
        </el-select>
    </div>
</template>

<script>
import Fuse from 'fuse.js' // 轻量级的搜索模块
import path from 'path'
import i18n from '@/lang'
import {getInternational} from "@/utils/i18nUtil"

export default {
    name: 'HeaderSearch',
    data() {
        return {
            search: '', // 输入的搜索的内容
            options: [], // 搜索候选项
            searchPool: [], // 搜索池，存放所有的可搜索内容
            show: false, // 是否显示搜索框
            fuse: undefined // 持有Fuse轻量级的搜索模块
        }
    },
    computed: {
        searchPlaceholder(){// 搜索提示那内容
            return getInternational('navigationBar.search');
        },
        routes() { // 搜索提示内容
            return this.$store.getters.routes
        },
        lang() { // 当前国际化语言
            return this.$store.getters.language
        },
        supportPinyinSearch() { // 是否支持pinyin搜索
            return this.$store.state.settings.supportPinyinSearch
        }
    },
    watch: {
        lang() { // 监听当前国际化语言变化，重置搜索池
            this.searchPool = this.generateRoutes(this.routes)
        },
        routes() { // 监听当前用户的所有路由变化，重置搜索池
            this.searchPool = this.generateRoutes(this.routes)
        },
        searchPool(list) { // 监听搜索池变化，初始化Fuse
            // 支持pinyin搜索
            if (this.lang === 'zh' && this.supportPinyinSearch) {
                this.addPinyinField(list)
            }
            this.initFuse(list)
        },
        show(value) { // 监听是否显示搜索框
            if (value) { // 显示的时候添加关闭事件
                document.body.addEventListener('click', this.close)
            } else { // 关闭的时候移除关闭事件
                document.body.removeEventListener('click', this.close)
            }
        }
    },
    mounted() {
        this.searchPool = this.generateRoutes(this.routes)
    },
    methods: {
        getInternational,
        async addPinyinField(list) {
            const {default: pinyin} = await import('pinyin')
            if (Array.isArray(list)) {
                list.forEach(element => {
                    const title = element.title
                    if (Array.isArray(title)) {
                        title.forEach(v => {
                            v = pinyin(v, {
                                style: pinyin.STYLE_NORMAL
                            }).join('')
                            element.pinyinTitle = v
                        })
                    }
                })
                return list
            }
        },
        click() { // 搜索按钮点击，逆转show值，显示搜索框
            this.show = !this.show
            if (this.show) {
                this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.focus()
            }
        },
        close() { // 关闭搜索框
            this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.blur()
            this.options = [] // 置空搜索候选项
            this.show = false // 关闭搜索框
        },
        change(val) { // 搜索框内容选中时的事件
            this.$router.push(val.path) // 跳转到相应的路由
            this.search = '' // 置空搜索内容
            this.options = [] // 置空搜索候选项
            this.$nextTick(() => {
                this.show = false // 关闭搜索框
            })
        },
        initFuse(list) { // 初始化Fuse
            this.fuse = new Fuse(list, {
                shouldSort: true,// 启用排序
                threshold: 0.4, // 阈值，匹配算法在什么时候放弃了。阈值0.0需要完美匹配（字母和位置），1.0的阈值将匹配任何东西。
                location: 0, // 确定文本中预期找到的模式的大致位置。
                distance: 100, // 确定匹配必须与模糊位置（由位置指定）的接近程度。距离模糊位置的距离字符的精确字母匹配将得分为完全不匹配。距离0要求匹配位于指定的确切位置，距离1000将要求完全匹配在使用阈值0.8的待查找位置的800个字符内。
                maxPatternLength: 32, // 模式的最大长度。模式越长（即搜索查询），搜索操作就越密集。只要模式超过maxPatternLength，就会抛出错误。
                minMatchCharLength: 1, // 最少匹配的字符长度
                keys: [{ // 要搜索的属性列表。这支持嵌套属性，加权搜索，在字符串和对象数组中搜索
                    name: 'title', // 搜索内容标题
                    weight: 0.7
                }, {
                    name: 'pinyinTitle', // 搜索内容pinyin
                    weight: 0.3
                }, {
                    name: 'path', // 搜索内容路径
                    weight: 0.3
                }]
            })
        },
        generateRoutes(routes, basePath = '/', prefixTitle = []) { // 获取搜索池，过滤当前用户的所有路由只需要能显示的路由并将其国际化
            let res = []
            for (const router of routes) {// 遍历当前用户的所有路由
                if (router.hidden) { // 隐藏的路由不需要
                    continue
                }
                const data = {
                    path: path.resolve(basePath, router.path), // 搜索内容路径
                    title: [...prefixTitle] // 搜索内容标题
                }
                if (router.meta && router.meta.title) { // 如果有标题，则将其国际化
                    const i18ntitle = i18n.t(`route.${router.meta.title}`)
                    data.title = [...data.title, i18ntitle] // 搜索内容标题在这里变为多个值，一个是原先的值，另一个是国际化的值
                    if (router.redirect !== 'noRedirect') { // 重定向路由不需要
                        // 只放入路由标题
                        // 例外: 需要排除没有redirect的父级路由
                        res.push(data)
                    }
                }
                // 递归子路由
                if (router.children) {
                    const tempRoutes = this.generateRoutes(router.children, data.path, data.title)
                    if (tempRoutes.length >= 1) {
                        res = [...res, ...tempRoutes]
                    }
                }
            }
            return res
        },
        querySearch(query) { // 搜索
            if (query !== '') {
                this.options = this.fuse.search(query) // 搜索候选项从fuse中获取
            } else {
                this.options = []// 设置搜索候选项为空
            }
        }
    }
}
</script>

<style lang="scss" scoped>
.header-search {
    font-size: 0 !important;

    .search-icon {
        cursor: pointer;
        font-size: 18px;
        vertical-align: middle;
    }

    .header-search-select {
        font-size: 18px;
        transition: width 0.2s;
        width: 0;
        overflow: hidden;
        background: transparent;
        border-radius: 0;
        display: inline-block;
        vertical-align: middle;

        ::v-deep .el-input__inner {
            border-radius: 0;
            border: 0;
            padding-left: 0;
            padding-right: 0;
            box-shadow: none !important;
            border-bottom: 1px solid #d9d9d9;
            vertical-align: middle;
        }
    }

    &.show {
        .header-search-select {
            width: 210px;
            margin-left: 10px;
        }
    }
}
</style>
