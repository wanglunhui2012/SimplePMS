<template>
    <div class="app-container">
        <!--表格上方的操作栏-->
        <div class="filter-container">
            <el-input v-model="filterForm.name" clearable :placeholder="$t('filterForm.description')" style="width: 120px;" class="filter-item" />
            <el-input v-model="filterForm.username" clearable :placeholder="$t('filterForm.username')" style="width: 100px;" class="filter-item" />
            <el-select v-model="filterForm.logType" clearable :placeholder="$t('filterForm.logType')" style="width: 120px" class="filter-item">
                <el-option v-for="key in Object.keys(systemLogTypeDictionary)" :key="key" :label="systemLogTypeDictionary[key]" :value="key">
                </el-option>
            </el-select>
            <el-button v-waves class="filter-item" type="success" icon="el-icon-search" @click="search">{{ $t('filterForm.search') }}</el-button>
            <el-button v-if="hasAuthority('LOG_GET')" v-waves :loading="filterExporting" class="filter-item" type="warning" icon="el-icon-download" @click="handleExport" style="margin-left: 0px;">{{ $t('filterForm.export') }}</el-button>
        </div>
        <!--ref之后引用它，:data绑定其数据，border为边框，fit宽度自适应，highlight-current-row高亮单选行，stripe斑马线，size为大小，，height为高度设置了可固定表头，max-height为表内容最大高度设置了可固定表头-->
        <!--第一列type="selection"则多选，:label标题，fixed使其固定，sortable表示其可排序-->
        <!--@row-click为选择一行的事件，@selection-change多选框选择时的改变事件-->
        <!--template slot-scope="scope"用于自定义模板-->
        <el-table
                ref="table"
                v-loading="tableLoading"
                :data="tableList"
                height="85%"
                border
                stripe
                highlight-current-row
                style="width: 100%;">
            <el-table-column label="名称" align="center" prop="name" show-overflow-tooltip min-width="80">
            </el-table-column>
            <el-table-column label="用户名" align="center" prop="username" show-overflow-tooltip min-width="80">
            </el-table-column>
            <el-table-column label="URL" align="center" prop="url" show-overflow-tooltip min-width="100">
            </el-table-column>
            <el-table-column label="方法" align="center" prop="method" show-overflow-tooltip min-width="100">
            </el-table-column>
            <el-table-column label="参数" align="center" prop="params" show-overflow-tooltip min-width="100">
            </el-table-column>
            <el-table-column label="耗时（ms）" align="center" prop="time" show-overflow-tooltip min-width="100">
            </el-table-column>
            <el-table-column label="ip" align="center" prop="ip" show-overflow-tooltip min-width="100">
            </el-table-column>
            <el-table-column label="类型" align="center" show-overflow-tooltip min-width="100">
                <template slot-scope="scope">
                    <el-tag type="">{{ systemLogTypeDictionary[scope.row.logType]}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="异常" align="center" prop="exception" show-overflow-tooltip min-width="100">
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip width="160">
            </el-table-column>
        </el-table>
        <!--分页工具，@size-change为当前页大小改变时的事件，@current-change为当前页改变时的事件
        :current-pag为当前页，:page-size为页大小，:page-sizes页大小集合，layout可选布局入前一页后一页跳转等，:total为数据总数-->
        <el-pagination
                v-show="tableListTotal>0"
                @size-change="paginationSizeChange"
                @current-change="paginationCurrentChange"
                :current-page="tableListParam.pageNum"
                :page-sizes="[5,10,20,50]"
                :page-size="tableListParam.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="tableListTotal"
                style="padding: 6px 0px;">
        </el-pagination>
    </div>
</template>

<script>
    // 工具
    import waves from '@/directive/waves'
    import hasAuthority from '@/utils/permissionUtil';

    // 组件

    // API
    import * as SystemLogApi from '@/api/systemLog'

    export default {
        name: 'log',
        directives: {waves},
        data() {
            return {
                // 第三方

                // 表格上方的操作栏
                filterForm: { // 过滤表单
                    name:'', // 名称
                    username: '', // 用户名
                    logType: null // 类型
                },
                filterExporting: false, // 是否正在导出

                // 表格
                tableLoading: true, // 是否正在加载
                tableList: [], // 表格列表

                // 分页
                tableListTotal: 0, // 表格列表总数（为服务器总数不是list个数）
                tableListParam: { // 查询参数
                    pageNum: 1, // 当前页
                    pageSize: this.Config.pageSize // 页大小
                },

                // 对话框
                // 辅助
                systemLogTypeDictionary: {},
            }
        },
        created() {
            // 不能直接在初始化systemEnableDictionary时从Vuex拿，会造成Vuex没有数据时的报错
            this.systemLogTypeDictionary=this.$store.getters.dictionary.systemLogType
            // 一开始则获取数据
            this.handleSearch()
        },
        methods: {
            // 第三方
            hasAuthority,

            // 表格上方的操作栏
            search() {
                // search()只比handleSearch()多将pageNum设置为1的步骤，因为当点击搜索时需要将pageNum设置为1，因为搜索的条件会改变数据集this.list，如果页大小为5，上一次有8条数据有2页，
                // 但是此时页码切换到第2页，如果改变条件并点击搜索，而此时的结果只有4条数据有1页，但此时页码为2，所以不会有结果展示，所以每次点击搜索后需要将页码置为1才行，
                // 而其他的向currentChange()这种页数改变的因为搜索条件不变而不会改变数据集this.list，所以直接调用handleSearch()即可
                this.tableListParam.pageNum = 1
                this.handleSearch()
            },
            handleExport() {
                this.filterExporting = true
                import('@/utils/excelUtil').then(excel => {
                    const tHeader = ['ID', '描述', '用户名', 'url', '方法', '参数', '耗时', 'ip', '级别','异常','创建时间']
                    const filterVal = ['id', 'name', 'username', 'url', 'method', 'params', 'time', 'ip', 'logType', 'exception','createTime']
                    const data = this.tableList.map(v => filterVal.map(j => {
                        if (j === 'time') {
                            return v[j]+'ms'
                        } else {
                            return v[j]
                        }
                    }))
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: 'InvestmentFiling Log List'
                    })
                    this.filterExporting = false
                })
            },


            // 分页
            paginationSizeChange(val) { // 分页工具页大小改变事件
                this.tableListParam.pageSize = val
                this.handleSearch()
            },
            paginationCurrentChange(val) { // 分页工具当前页改变事件
                this.tableListParam.pageNum = val;
                this.handleSearch()
            },

            // 辅助
            handleSearch() {
                this.tableLoading = true
                SystemLogApi.searchPage({
                    name: this.filterForm.name,
                    username: this.filterForm.username,
                    logType: this.filterForm.logType,
                    pageNum: this.tableListParam.pageNum,
                    pageSize: this.tableListParam.pageSize
                }).then(res => {
                    this.tableList = res.records
                    this.tableListTotal = res.total
                    this.tableLoading = false
                }).catch(error => {
                    this.tableLoading = false
                })
            },
        }
    }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
