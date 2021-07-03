<template>
    <div class="app-container">
        <!--表格上方的操作栏-->
        <div class="filter-container">
            <el-input v-model="filterForm.name" clearable :placeholder="$t('filterForm.jobName')" style="width: 120px;" class="filter-item" />
            <el-button v-waves class="filter-item" type="success" icon="el-icon-search" @click="search">{{ $t('filterForm.search') }}</el-button>
            <el-button v-if="hasAuthority('JOB_ADD')" class="filter-item" type="primary" icon="el-icon-plus" @click="add" style="margin-left: 0px;">{{ $t('filterForm.add') }}</el-button>
            <el-button v-if="hasAuthority('JOB_DELETE')" class="filter-item" type="danger" icon="el-icon-delete" @click="_delete" style="margin-left: 0px;">{{ $t('filterForm.delete') }}</el-button>
            <el-button v-if="hasAuthority('JOB_GET')" v-waves :loading="filterExporting" class="filter-item" type="warning" icon="el-icon-download" @click="handleExport" style="margin-left: 0px;">{{ $t('filterForm.export') }}</el-button>
        </div>
        <!--ref之后引用它，:data绑定其数据，border为边框，fit宽度自适应，highlight-current-row高亮单选行，stripe斑马线，size为大小，，height为高度设置了可固定表头，max-height为表内容最大高度设置了可固定表头-->
        <!--第一列type="selection"则多选，:label标题，fixed使其固定，sortable表示其可排序-->
        <!--@row-click为选择一行的事件，@selection-change多选框选择时的改变事件-->
        <!--template slot-scope="scope"用于自定义模板-->
        <el-table
                ref="tableRef"
                v-loading="tableLoading"
                :data="tableList"
                height="85%"
                border
                stripe
                highlight-current-row
                @row-click="tableRowClick"
                @selection-change="tableSelectionChange"
                style="width: 100%;">
            <el-table-column type="selection" width="40"/>
            <el-table-column type="index" label="#" align="center" width="50">
            </el-table-column>
            <el-table-column label="职位代码" align="center" prop="code" show-overflow-tooltip min-width="120">
            </el-table-column>
            <el-table-column label="职位名" align="center" prop="name" show-overflow-tooltip min-width="120">
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip width="160">
            </el-table-column>
            <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
                <template slot-scope="{row}">
                    <el-button type="primary" circle icon="el-icon-search" @click="get(row)" style="margin-left: 5px;"/>
                    <el-button v-if="hasAuthority('JOB_UPDATE')" type="primary" circle icon="el-icon-edit" @click="update(row)" style="margin-left: 5px;"/>
                    <el-popover
                            v-if="hasAuthority('JOB_DELETE')"
                            :ref="row.id"
                            placement="top"
                            width="180">
                        <p>确定删除本条数据吗?</p>
                        <div style="text-align: right; margin: 0">
                            <el-button size="mini" type="text" @click="$refs[row.id].doClose()">取消</el-button>
                            <el-button type="primary" @click="handleDelete([row.id])">确定</el-button>
                        </div>
                        <el-button slot="reference" type="danger" circle icon="el-icon-delete" style="margin-left: 5px;"/>
                    </el-popover>
                </template>
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
        <!--弹出对话框，:visible控制其是否显示，fullscreen全屏,append-to-body自身是否插入至body元素上，close-on-click-modal是否可以通过点击modal关闭Dialog，label-width表单域标签的宽度，label-position表单域标签的位置-->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" append-to-body width="880px" >
            <el-form ref="dialogFormRef" :model="dialogTemp" :rules="dialogRules" :inline="true"  label-position="left" label-width="140px">
                <el-form-item label="职位代码" prop="code">
                    <el-input v-model="dialogTemp.code" placeholder="请输入职位代码" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="职位名" prop="name">
                    <el-input v-model="dialogTemp.name" placeholder="请输入职位名" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="dialogTemp.remark" type="textarea" max="255" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入备注" style="width: 675px;"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button v-if="dialogStatus!==0" type="primary" :loading="dialogConfirmButtonLoading" @click="handleDialogConfirm">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    // 工具
    import {getInternational} from '@/utils/i18nUtil'
    import hasAuthority from '@/utils/permissionUtil';

    // 组件
    import waves from '@/directive/waves'

    // API
    import * as SystemJobApi from '@/api/systemJob'

    export default {
        name: 'job',
        directives: {waves},
        filters: {},
        data() {
            return {
                // 第三方

                // 表格上方的操作栏
                filterForm: { // 过滤表单
                    name: '', // 职位名
                },
                filterExporting: false, // 是否正在导出

                // 表格
                tableLoading: true, // 是否正在加载
                tableList: [], // 表格列表
                tableMultipleSelectionList: [], // 表格多选列表

                // 分页
                tableListTotal: 0, // 表格列表总数（为服务器总数不是list个数）
                tableListParam: { // 查询参数
                    pageNum: 1, // 当前页
                    pageSize: this.Config.pageSize // 页大小
                },

                // 对话框
                dialogStatus:0, // 对话框状态，0为查看，1为增加，2为修改
                dialogTitle: '', // 对话框标题,可为增加、修改2种
                dialogVisible: false, // 对话框是否可见,一开始是关闭的,点击之后才打开
                dialogTemp: { // 对话框表单中间数据
                    code:'',
                    name: '',
                    remark: '',
                },
                dialogRules: { // 对话框验证规则
                    code:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    name:[{ required: true, message: '不能为空!', trigger: 'blur' }, {max: 255, message: '职位最长255位!', trigger: 'change' }],
                    remark:[{require:false,message:'最长255位!',trigger:'change',max:255}]
                },
                dialogConfirmButtonLoading:false,

                // 辅助
            }
        },
        created() {
            // 一开始则获取数据
            this.handleSearch()
        },
        methods: {
            // 第三方
            hasAuthority,
            getInternational,

            // 表格上方的操作栏
            search() {
                // search()只比handleSearch()多将pageNum设置为1的步骤，因为当点击搜索时需要将pageNum设置为1，因为搜索的条件会改变数据集this.list，如果页大小为5，上一次有8条数据有2页，
                // 但是此时页码切换到第2页，如果改变条件并点击搜索，而此时的结果只有4条数据有1页，但此时页码为2，所以不会有结果展示，所以每次点击搜索后需要将页码置为1才行，
                // 而其他的向currentChange()这种页数改变的因为搜索条件不变而不会改变数据集this.list，所以直接调用handleSearch()即可
                this.tableListParam.pageNum = 1
                this.handleSearch()
            },
            get(row){
                this.dialogStatus=0 // 0为查看
                this.dialogVisible = true
                this.dialogTitle = '查看职位'
                this.dialogTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    code:row.code,
                    name: row.name,
                    remark: row.remark,
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            add() {
                this.dialogStatus=1 // 为增加
                this.dialogVisible = true
                this.dialogTitle = "增加职位"
                // 重置数据
                this.dialogTemp = {
                    code:'',
                    name: '',
                    remark: '',
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            handleAdd() {
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogConfirmButtonLoading = true
                        SystemJobApi.add(this.dialogTemp).then(res => {
                            this.$message({
                                message: '增加成功!',
                                type: 'success'
                            });
                            this.dialogConfirmButtonLoading = false
                            this.dialogVisible = false
                            this.handleSearch()
                        }).catch(error => {
                            this.dialogConfirmButtonLoading = false
                        })
                    }
                })
            },
            _delete() {
                if (this.tableMultipleSelectionList.length == 0) {
                    this.$message.error('请选择要删除的行!');
                    return
                }
                let ids = this.tableMultipleSelectionList.map(function (item, index, array) {
                    return item.id
                })
                this.handleDelete(ids)
            },
            handleDelete(ids) {
                this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(res => {
                    SystemJobApi._delete(ids).then(res => {
                        this.$message({
                            message: '删除成功!',
                            type: 'success'
                        });
                        this.tableListParam.pageNum = 1; // 删除成功之后,有时数据可能会都没有,所以此时的当前页pageNum可能没有,所以需要将其设置为1
                        this.handleSearch()
                    }).catch(error => {
                    })
                }).catch(error => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除!'
                    });
                });
            },
            update(row){
                this.dialogStatus=2 // 2为修改
                this.dialogVisible = true
                this.dialogTitle = "修改职位"
                this.dialogTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    code:row.code,
                    name: row.name,
                    remark: row.remark,
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            handleUpdate() {
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogConfirmButtonLoading = true
                        SystemJobApi.update(
                            this.dialogTemp
                        ).then(res => {
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            });
                            this.dialogConfirmButtonLoading = false
                            this.dialogVisible = false
                            this.handleSearch()
                        }).catch(error => {
                            this.dialogConfirmButtonLoading = false
                        })
                    }
                })
            },
            handleExport() {
                this.filterExporting = true
                import('@/utils/excelUtil').then(excel => {
                    const tHeader = ['ID', '职位名', '创建时间']
                    const filterVal = ['id', 'name', 'createTime']
                    const data = this.tableList.map(v => filterVal.map(j => {
                        if (j === 'createTime') {
                            if (!v[j]) {
                                return '无'
                            } else {
                                return v[j]
                            }
                        } else {
                            return v[j]
                        }
                    }))
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: 'InvestmentFiling Job List'
                    })
                    this.filterExporting = false
                })
            },

            // 表格
            tableRowClick(row) { // 表格行点击事件

            },
            tableSelectionChange(rows) { // 表格前面方框选择改变时的事件，rows为选中行的数组
                this.tableMultipleSelectionList = rows
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

            // 对话框
            handleDialogConfirm() {
                if(this.dialogStatus===1){
                    this.handleAdd()
                }else if(this.dialogStatus===2){
                    this.handleUpdate()
                }
            },

            // 辅助
            handleSearch() {
                this.tableLoading = true
                SystemJobApi.searchPage({
                    name: this.filterForm.name,
                    pageNum: this.tableListParam.pageNum,
                    pageSize: this.tableListParam.pageSize,
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
