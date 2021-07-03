<template>
    <div class="app-container">
        <el-row :gutter="20" style="height: 100%;">
            <el-col :xl="12" :lg="12" :md="12" :sm="12" :xs="12" class="card-container" style="height: 100%;">
                <!--表格上方的操作栏-->
                <div class="filter-container">
                    <el-input v-model="dictionaryFilterForm.name" clearable :placeholder="$t('filterForm.dictionaryName')" style="width: 110px;" class="filter-item" />
                    <el-button v-waves class="filter-item" type="success" icon="el-icon-search" @click="searchDictionary">{{ $t('filterForm.search') }}</el-button>
                    <el-button v-if="hasAuthority('DICTIONARY_ADD')" class="filter-item" type="primary" icon="el-icon-plus" @click="addDictionary" style="margin-left: 0px;">{{ $t('filterForm.add') }}</el-button>
                    <el-button v-if="hasAuthority('DICTIONARY_DELETE')" class="filter-item" type="danger" icon="el-icon-delete" @click="deleteDictionary" style="margin-left: 0px;">{{ $t('filterForm.delete') }}</el-button>
                </div>
                <!--ref之后引用它，:data绑定其数据，border为边框，fit宽度自适应，highlight-current-row高亮单选行，stripe斑马线，size为大小，，height为高度设置了可固定表头，max-height为表内容最大高度设置了可固定表头-->
                <!--第一列type="selection"则多选，:label标题，fixed使其固定，sortable表示其可排序-->
                <!--@row-click为选择一行的事件，@selection-change多选框选择时的改变事件-->
                <!--template slot-scope="scope"用于自定义模板-->
                <el-table
                    ref="dictionaryTableRef"
                    v-loading="dictionaryTableLoading"
                    :data="dictionaryTableList"
                    height="85%"
                    border
                    stripe
                    highlight-current-row
                    @row-click="dictionaryTableRowClick"
                    @selection-change="dictionaryTableSelectionChange"
                    @current-change="dictionaryTableCurrentChange"
                    style="width: 100%;">
                    <el-table-column type="selection" width="40"/>
                    <el-table-column type="index" label="序号" align="center" width="50">
                    </el-table-column>
                    <el-table-column label="名称" align="center" prop="name" show-overflow-tooltip min-width="100">
                    </el-table-column>
                    <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip width="160">
                    </el-table-column>
                    <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
                        <template slot-scope="{row}">
                            <el-button type="primary" circle icon="el-icon-search" @click="getDictionary(row)" style="margin-left: 5px;"/>
                            <el-button v-if="hasAuthority('DICTIONARY_UPDATE')" type="primary" circle icon="el-icon-edit" @click="updateDictionary(row)" style="margin-left: 5px;"/>
                            <el-popover
                                v-if="hasAuthority('DICTIONARY_DELETE')"
                                :ref="'dictionary_'+row.id"
                                placement="top"
                                width="180">
                                <p>确定删除本条数据吗?</p>
                                <div style="text-align: right; margin: 0">
                                    <el-button size="mini" type="text" @click="$refs['dictionary_'+row.id].doClose()">取消</el-button>
                                    <el-button type="primary" @click="handleDeleteDictionary([row.id])">确定</el-button>
                                </div>
                                <el-button slot="reference" type="danger" circle icon="el-icon-delete" style="margin-left: 5px;"/>
                            </el-popover>
                        </template>
                    </el-table-column>
                </el-table>
                <!--分页工具，@size-change为当前页大小改变时的事件，@current-change为当前页改变时的事件
                :current-pag为当前页，:page-size为页大小，:page-sizes页大小集合，layout可选布局入前一页后一页跳转等，:total为数据总数-->
                <el-pagination
                    v-show="dictionaryTableListTotal>0"
                    @size-change="dictionaryPaginationSizeChange"
                    @current-change="dictionaryPaginationCurrentChange"
                    :current-page="dictionaryTableListParam.pageNum"
                    :page-sizes="[5,10,20,50]"
                    :page-size="dictionaryTableListParam.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="dictionaryTableListTotal"
                    style="padding: 6px 0px;">
                </el-pagination>
            </el-col>
            <el-col :xl="12" :lg="12" :md="12" :sm="12" :xs="12" style="height: 100%;">
                <!--表格上方的操作栏-->
                <div class="filter-container">
                    <el-input v-model="dictionaryDetailFilterForm.key" type="number" clearable :placeholder="$t('filterForm.key')" style="width: 100px;" class="filter-item" />
                    <el-input v-model="dictionaryDetailFilterForm.value" clearable :placeholder="$t('filterForm.value')" style="width: 100px;" class="filter-item" />
                    <el-button v-waves class="filter-item" type="success" icon="el-icon-search" @click="searchDictionaryDetail">{{ $t('filterForm.search') }}</el-button>
                    <el-button v-if="hasAuthority('DICTIONARY_DETAIL_ADD')" class="filter-item" type="primary" icon="el-icon-plus" @click="addDictionaryDetail" style="margin-left: 0px;">{{ $t('filterForm.add') }}</el-button>
                    <el-button v-if="hasAuthority('DICTIONARY_DETAIL_DELETE')" class="filter-item" type="danger" icon="el-icon-delete" @click="deleteDictionaryDetail" style="margin-left: 0px;">{{ $t('filterForm.delete') }}</el-button>
                </div>
                <!--ref之后引用它，:data绑定其数据，border为边框，fit宽度自适应，highlight-current-row高亮单选行，stripe斑马线，size为大小，，height为高度设置了可固定表头，max-height为表内容最大高度设置了可固定表头-->
                <!--第一列type="selection"则多选，:label标题，fixed使其固定，sortable表示其可排序-->
                <!--@row-click为选择一行的事件，@selection-change多选框选择时的改变事件-->
                <!--template slot-scope="scope"用于自定义模板-->
                <el-table
                    ref="dictionaryDetailTable"
                    v-loading="dictionaryDetailTableLoading"
                    :data="dictionaryDetailTableList"
                    height="85%"
                    border
                    stripe
                    highlight-current-row
                    @row-click="dictionaryDetailTableRowClick"
                    @selection-change="dictionaryDetailTableSelectionChange"
                    style="width: 100%;">
                    <el-table-column type="selection" width="40"/>
                    <el-table-column type="index" label="序号" align="center" width="50">
                    </el-table-column>
                    <el-table-column label="键" align="center" prop="key" show-overflow-tooltip min-width="100">
                    </el-table-column>
                    <el-table-column label="值" align="center" prop="value" show-overflow-tooltip min-width="100">
                    </el-table-column>
                    <el-table-column label="排序" align="center" show-overflow-tooltip min-width="60">
                        <template slot-scope="scope">
                            <el-tag>{{ scope.row.sort }}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
                        <template slot-scope="{row}">
                            <el-button type="primary" circle icon="el-icon-search" @click="getDictionaryDetail(row)" style="margin-left: 5px;"/>
                            <el-button v-if="hasAuthority('DICTIONARY_DETAIL_UPDATE')" type="primary" circle icon="el-icon-edit" @click="updateDictionaryDetail(row)" style="margin-left: 5px;"/>
                            <el-popover
                                v-if="hasAuthority('DICTIONARY_DETAIL_DELETE')"
                                :ref="'dictionaryDetail_'+row.id"
                                placement="top"
                                width="180">
                                <p>确定删除本条数据吗?</p>
                                <div style="text-align: right; margin: 0">
                                    <el-button size="mini" type="text" @click="$refs['dictionaryDetail_'+row.id].doClose()">取消</el-button>
                                    <el-button type="primary" @click="handleDeleteDictionaryDetail([row.id])">确定</el-button>
                                </div>
                                <el-button slot="reference" type="danger" circle icon="el-icon-delete"  style="margin-left: 5px;"/>
                            </el-popover>
                        </template>
                    </el-table-column>
                </el-table>
                <!--分页工具，@size-change为当前页大小改变时的事件，@current-change为当前页改变时的事件
                :current-pag为当前页，:page-size为页大小，:page-sizes页大小集合，layout可选布局入前一页后一页跳转等，:total为数据总数-->
                <el-pagination
                    v-show="dictionaryDetailTableListTotal>0"
                    @size-change="dictionaryDetailPaginationSizeChange"
                    @current-change="dictionaryDetailPaginationCurrentChange"
                    :current-page="dictionaryDetailTableListParam.pageNum"
                    :page-sizes="[5,10,20,50]"
                    :page-size="dictionaryDetailTableListParam.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="dictionaryDetailTableListTotal"
                    style="padding: 6px 0px;">
                </el-pagination>
            </el-col>
        </el-row>
        <!--弹出对话框，:visible控制其是否显示，fullscreen全屏,append-to-body自身是否插入至body元素上，close-on-click-modal是否可以通过点击modal关闭Dialog，label-width表单域标签的宽度，label-position表单域标签的位置-->
        <el-dialog :title="dialogDictionaryTitle" :visible.sync="dialogDictionaryVisible" append-to-body width="880px" >
            <!--ref用于之后引用此表单用于验证，inline一行可放多个表单，rules为验证规则，之后需要在el-form-item上用prop属性即可验证-->
            <el-form ref="dialogDictionaryFormRef" :model="dialogDictionaryTemp" :rules="dialogDictionaryRules"  label-position="left" label-width="140px">
                <el-form-item label="字典名称" prop="name">
                    <el-input v-model="dialogDictionaryTemp.name" placeholder="请输入字典名称" style="width: 675px;"/>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="dialogDictionaryTemp.remark" type="textarea" max="255" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入备注" style="width: 675px;"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogDictionaryVisible = false">取消</el-button>
                <el-button v-if="dialogDictionaryStatus!==0" type="primary" :loading="dialogDictionaryConfirmButtonLoading" @click="handleDialogDictionaryConfirm">确定</el-button>
            </div>
        </el-dialog>
        <!--弹出对话框，:visible控制其是否显示，fullscreen全屏,append-to-body自身是否插入至body元素上，close-on-click-modal是否可以通过点击modal关闭Dialog，label-width表单域标签的宽度，label-position表单域标签的位置-->
        <el-dialog :title="dialogDictionaryDetailTitle" :visible.sync="dialogDictionaryDetailVisible" append-to-body width="880px" >
            <!--ref用于之后引用此表单用于验证，inline一行可放多个表单，rules为验证规则，之后需要在el-form-item上用prop属性即可验证-->
            <el-form ref="dialogDictionaryDetailFormRef" :model="dialogDictionaryDetailTemp" :rules="dialogDictionaryDetailRules" :inline="true"  label-position="left" label-width="140px">
                <el-form-item label="所属字典" prop="dictionaryId">
                    <el-select v-model="dialogDictionaryDetailTemp.dictionaryId" clearable placeholder="请选择所属字典" style="width: 200px;">
                        <el-option v-for="item in dictionaryList" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="键" prop="key">
                    <el-input v-model="dialogDictionaryDetailTemp.key" placeholder="请输入键" style="width: 200px;"/>
                </el-form-item>
                <el-form-item label="值" prop="value">
                    <el-input v-model="dialogDictionaryDetailTemp.value" placeholder="请输入值" style="width: 200px;"/>
                </el-form-item>
                <el-form-item label="排序" prop="sort">
                    <el-input-number v-model="dialogDictionaryDetailTemp.sort" :min="1" :max="999" style="width: 200px;"/>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="dialogDictionaryDetailTemp.remark" type="textarea" max="255" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入备注" style="width: 675px;"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogDictionaryDetailVisible = false">取消</el-button>
                <el-button v-if="dialogDictionaryDetailStatus!==0" type="primary" :loading="dialogDictionaryDetailConfirmButtonLoading" @click="handleDialogDictionaryDetailConfirm">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    // 工具
    import hasAuthority from "@/utils/permissionUtil";

    // 组件
    import waves from '@/directive/waves'

    // API
    import * as SystemDictionaryApi from '@/api/systemDictionary'
    import * as SystemDictionaryDetailApi from '@/api/systemDictionaryDetail'

    export default {
        name: "dictionary",
        directives: {waves},
        data(){
            return{
                // 第三方

                // 表格上方的操作栏
                dictionaryFilterForm: {
                    name:'' // 字典名
                },
                dictionaryDetailFilterForm:{
                    dictionaryId:'', // 字典id
                    key:'', // 键
                    value:'', //值
                },

                // 表格
                dictionaryTableLoading: true, // 是否正在加载
                dictionaryDetailTableLoading: true, // 是否正在加载
                dictionaryTableList: [], // 表格列表
                dictionaryDetailTableList: [], // 表格列表
                dictionaryTableMultipleSelection: [], // 表格多选列表
                dictionaryDetailTableMultipleSelection: [], // 表格多选列表

                // 分页
                dictionaryTableListTotal: 0, // 表格列表总数（为服务器总数不是list个数）
                dictionaryDetailTableListTotal: 0, // 表格列表总数（为服务器总数不是list个数）
                dictionaryTableListParam: { // 查询参数
                    pageNum: 1, // 当前页
                    pageSize: this.Config.pageSize // 页大小
                },
                dictionaryDetailTableListParam: { // 查询参数
                    pageNum: 1, // 当前页
                    pageSize: this.Config.pageSize // 页大小
                },

                // 对话框
                dialogDictionaryStatus:0, // 对话框状态，0为查看，1为增加，2为修改
                dialogDictionaryDetailStatus:0, // 对话框状态，0为查看，1为增加，2为修改
                dialogDictionaryTitle: '', // 对话框标题,可为增加、修改2种
                dialogDictionaryDetailTitle: '', // 对话框标题,可为增加、修改2种
                dialogDictionaryVisible: false, // 对话框是否可见,一开始是关闭的,点击之后才打开
                dialogDictionaryDetailVisible: false, // 对话框是否可见,一开始是关闭的,点击之后才打开
                dialogDictionaryTemp: { // 对话框表单中间数据
                    name: '',
                    remark: ''
                },
                dialogDictionaryDetailTemp: { // 对话框表单中间数据
                    dictionaryId: '',
                    key: 1,
                    value:'',
                    sort:'',
                    remark:''
                },
                dialogDictionaryRules: { // 对话框验证规则
                    name:[{ required: true, message: '不能为空!', trigger: 'blur' }, {max: 255, message: '最长255位!', trigger: 'change' }],
                    remark:[{require:false,message:'最长255位!',trigger:'change',max:255}]
                },
                dialogDictionaryDetailRules: { // 对话框验证规则
                    dictionaryId:[{ required: true, message: '不能为空!', trigger: 'blur' }],
                    key:[{ required: true, message: '不能为空!', trigger: 'blur' }],
                    value:[{required:true,message:'不能为空!',trigger:'blur'}],
                    sort:[{required:true,message:'不能为空!',trigger:'blur'}],
                    remark:[{require:false,message:'最长255位!',trigger:'change',max:255}]
                },
                dialogDictionaryConfirmButtonLoading:false,
                dialogDictionaryDetailConfirmButtonLoading:false,

                // 辅助
                dictionaryList:[], // 字典列表
            }
        },
        created() {
            // 一开始则获取数据
            this.handleSearchDictionary()
            this.handleSearchDictionaryDetail()
            this.getDictionaryList()
        },
        methods:{
            // 第三方
            hasAuthority,

            // 表格上方的操作栏
            searchDictionary(){
                // search()只比handleSearch()多将pageNum设置为1的步骤，因为当点击搜索时需要将pageNum设置为1，因为搜索的条件会改变数据集this.list，如果页大小为5，上一次有8条数据有2页，
                // 但是此时页码切换到第2页，如果改变条件并点击搜索，而此时的结果只有4条数据有1页，但此时页码为2，所以不会有结果展示，所以每次点击搜索后需要将页码置为1才行，
                // 而其他的向currentChange()这种页数改变的因为搜索条件不变而不会改变数据集this.list，所以直接调用handleSearch()即可
                this.dictionaryTableListParam.pageNum=1
                this.handleSearchDictionary()
            },
            searchDictionaryDetail(){
                // search()只比handleSearch()多将pageNum设置为1的步骤，因为当点击搜索时需要将pageNum设置为1，因为搜索的条件会改变数据集this.list，如果页大小为5，上一次有8条数据有2页，
                // 但是此时页码切换到第2页，如果改变条件并点击搜索，而此时的结果只有4条数据有1页，但此时页码为2，所以不会有结果展示，所以每次点击搜索后需要将页码置为1才行，
                // 而其他的向currentChange()这种页数改变的因为搜索条件不变而不会改变数据集this.list，所以直接调用handleSearch()即可
                this.dictionaryDetailTableListParam.pageNum=1
                this.handleSearchDictionaryDetail()
            },
            getDictionary(row){
                this.dialogDictionaryStatus=0 // 0为查看
                this.dialogDictionaryVisible = true
                this.dialogDictionaryTitle = '查看字典'
                this.dialogDictionaryTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    name: row.name,
                    remark: row.remark,
                }

                this.$nextTick(() => {
                    this.$refs['dialogDictionaryFormRef'].clearValidate() // 清除验证
                })
            },
            getDictionaryDetail(row){
                this.dialogDictionaryDetailStatus=0 // 0为查看
                this.dialogDictionaryDetailVisible = true
                this.dialogDictionaryDetailTitle = '查看字典详情'
                this.dialogDictionaryDetailTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    dictionaryId: row.dictionaryId,
                    key: row.key,
                    value: row.value,
                    sort: row.sort,
                    remark:row.remark
                }

                this.$nextTick(() => {
                    this.$refs['dialogDictionaryDetailFormRef'].clearValidate() // 清除验证
                })
            },
            addDictionary(){
                this.dialogDictionaryStatus=1 // 为增加
                this.dialogDictionaryVisible = true
                this.dialogDictionaryTitle = "增加字典"
                // 重置数据
                this.dialogDictionaryTemp = {
                    name: '',
                    remark: ''
                }

                this.$nextTick(() => {
                    this.$refs['dialogDictionaryFormRef'].clearValidate() // 清除验证
                })
            },
            addDictionaryDetail(){
                this.dialogDictionaryDetailStatus=1 // 为增加
                this.dialogDictionaryDetailVisible = true
                this.dialogDictionaryDetailTitle = "增加字典详情"
                // 重置数据
                this.dialogDictionaryDetailTemp = {
                    dictionaryId:'',
                    key:'',
                    value:'',
                    sort:0,
                    remark:''
                }

                this.$nextTick(() => {
                    this.$refs['dialogDictionaryDetailFormRef'].clearValidate() // 清除验证
                })
            },
            handleAddDictionary(){
                this.$refs['dialogDictionaryFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogDictionaryConfirmButtonLoading = true
                        SystemDictionaryApi.add(this.dialogDictionaryTemp).then(res => {
                            this.$message({
                                message: '增加成功!',
                                type: 'success'
                            });
                            this.dialogDictionaryConfirmButtonLoading = false
                            this.dialogDictionaryVisible = false
                            this.handleSearchDictionary()
                            // 重新搜索所有字典，否则字典没有这次新增加的
                            this.getDictionaryList()
                        }).catch(error => {
                            this.dialogDictionaryConfirmButtonLoading = false
                        })
                    }
                })
            },
            handleAddDictionaryDetail(){
                this.$refs['dialogDictionaryDetailFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogDictionaryDetailConfirmButtonLoading = true
                        SystemDictionaryDetailApi.add(this.dialogDictionaryDetailTemp).then(res => {
                            this.$message({
                                message: '增加成功!',
                                type: 'success'
                            });
                            this.dialogDictionaryDetailConfirmButtonLoading = false
                            this.dialogDictionaryDetailVisible = false
                            this.handleSearchDictionaryDetail()
                        }).catch(error => {
                            this.dialogDictionaryDetailConfirmButtonLoading = false
                        })
                    }
                })
            },
            deleteDictionary(){
                if (this.dictionaryTableMultipleSelection.length === 0) {
                    this.$message.error('请选择要删除的行!');
                    return
                }
                let ids = this.dictionaryTableMultipleSelection.map(function (item, index, array) {
                    return item.id
                })
                this.handleDeleteDictionary(ids)
            },
            deleteDictionaryDetail(){
                if (this.dictionaryDetailTableMultipleSelection.length === 0) {
                    this.$message.error('请选择要删除的行!');
                    return
                }
                let ids = this.dictionaryDetailTableMultipleSelection.map(function (item, index, array) {
                    return item.id
                })
                this.handleDeleteDictionaryDetail(ids)
            },
            handleDeleteDictionary(ids){
                this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(res => {
                    SystemDictionaryApi._delete(ids).then(res => {
                        this.$message({
                            message: '删除成功!',
                            type: 'success'
                        });
                        this.dictionaryTableListParam.pageNum = 1; // 删除成功之后,有时数据可能会都没有,所以此时的当前页pageNum可能没有,所以需要将其设置为1
                        this.handleSearchDictionary()
                        // 重新搜索所有字典，否则字典还有这次删除的
                        this.getDictionaryList()
                    }).catch(error => {
                    })
                }).catch(error => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除!'
                    });
                });
            },
            handleDeleteDictionaryDetail(ids){
                this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(res => {
                    SystemDictionaryDetailApi._delete(ids).then(res => {
                        this.$message({
                            message: '删除成功!',
                            type: 'success'
                        });
                        this.dictionaryDetailTableListParam.pageNum = 1; // 删除成功之后,有时数据可能会都没有,所以此时的当前页pageNum可能没有,所以需要将其设置为1
                        this.handleSearchDictionaryDetail()
                    }).catch(error => {
                    })
                }).catch(error => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除!'
                    });
                });
            },
            updateDictionary(row){
                this.dialogDictionaryStatus=2 // 2为修改
                this.dialogDictionaryVisible = true
                this.dialogDictionaryTitle = "修改字典"
                this.dialogDictionaryTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    name: row.name,
                    remark: row.remark,
                }

                this.$nextTick(() => {
                    this.$refs['dialogDictionaryFormRef'].clearValidate() // 清除验证
                })
            },
            updateDictionaryDetail(row){
                this.dialogDictionaryDetailStatus=2 // 2为修改
                this.dialogDictionaryDetailVisible = true
                this.dialogDictionaryDetailTitle = "修改字典详情"
                this.dialogDictionaryDetailTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    dictionaryId: row.dictionaryId,
                    key: row.key,
                    value: row.value,
                    sort: row.sort,
                    remark:row.remark
                }

                this.$nextTick(() => {
                    this.$refs['dialogDictionaryDetailFormRef'].clearValidate() // 清除验证
                })
            },
            handleUpdateDictionary(){
                this.$refs['dialogDictionaryFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogDictionaryConfirmButtonLoading = true
                        SystemDictionaryApi.update(
                            this.dialogDictionaryTemp
                        ).then(res => {
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            });
                            this.dialogDictionaryConfirmButtonLoading = false
                            this.dialogDictionaryVisible = false
                            this.handleSearchDictionary()
                            // 重新搜索所有字典，否则字典没有这次新修改的
                            this.getDictionaryList()
                        }).catch(error => {
                            this.dialogDictionaryConfirmButtonLoading = false
                        })
                    }
                })
            },
            handleUpdateDictionaryDetail(){
                this.$refs['dialogDictionaryDetailFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogDictionaryDetailConfirmButtonLoading = true
                        SystemDictionaryDetailApi.update(
                            this.dialogDictionaryDetailTemp
                        ).then(res => {
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            });
                            this.dialogDictionaryDetailConfirmButtonLoading = false
                            this.dialogDictionaryDetailVisible = false
                            this.handleSearchDictionaryDetail()
                        }).catch(error => {
                            this.dialogDictionaryDetailConfirmButtonLoading = false
                        })
                    }
                })
            },


            // 表格
            dictionaryTableRowClick(row) {

            },
            dictionaryDetailTableRowClick(row) {

            },
            dictionaryTableSelectionChange(rows) { // 表格前面方框选择改变时的事件，rows为选中行的数组
                this.dictionaryTableMultipleSelection = rows
            },
            dictionaryDetailTableSelectionChange(rows) { // 表格前面方框选择改变时的事件，rows为选中行的数组
                this.dictionaryDetailTableMultipleSelection = rows
            },
            dictionaryTableCurrentChange(currentRow, oldCurrentRow){
                if(currentRow){
                    this.dictionaryDetailFilterForm.dictionaryId=currentRow.id
                }else{
                    this.dictionaryDetailFilterForm.dictionaryId=null
                }
                // 重新搜索字典详情
                this.searchDictionaryDetail()
            },

            // 分页
            dictionaryPaginationSizeChange(val) { // 分页工具页大小改变事件
                this.dictionaryTableListParam.pageSize=val
                this.handleSearchDictionary()
            },
            dictionaryDetailPaginationSizeChange(val) { // 分页工具页大小改变事件
                this.dictionaryDetailTableListParam.pageSize=val
                this.handleSearchDictionaryDetail()
            },
            dictionaryPaginationCurrentChange(val) { // 分页工具当前页改变事件
                this.dictionaryTableListParam.pageNum=val;
                this.handleSearchDictionary()
            },
            dictionaryDetailPaginationCurrentChange(val) { // 分页工具当前页改变事件
                this.dictionaryDetailTableListParam.pageNum=val;
                this.handleSearchDictionaryDetail()
            },

            // 对话框
            handleDialogDictionaryConfirm() {
                if(this.dialogDictionaryStatus===1){
                    this.handleAddDictionary()
                }else if(this.dialogDictionaryStatus===2){
                    this.handleUpdateDictionary()
                }
            },
            handleDialogDictionaryDetailConfirm() {
                if(this.dialogDictionaryDetailStatus===1){
                    this.handleAddDictionaryDetail()
                }else if(this.dialogDictionaryDetailStatus===2){
                    this.handleUpdateDictionaryDetail()
                }
            },

            // 辅助
            handleSearchDictionary(){
                this.dictionaryTableLoading = true
                SystemDictionaryApi.searchPage({
                    name:this.dictionaryFilterForm.name,
                    pageNum:this.dictionaryTableListParam.pageNum,
                    pageSize:this.dictionaryTableListParam.pageSize,
                }).then(res=>{
                    this.dictionaryTableList=res.records
                    this.dictionaryTableListTotal=res.total
                    this.dictionaryTableLoading = false
                }).catch(error=>{
                    this.dictionaryTableLoading = false
                })
            },
            handleSearchDictionaryDetail(){
                this.dictionaryDetailTableLoading = true
                SystemDictionaryDetailApi.searchPage({
                    dictionaryId:this.dictionaryDetailFilterForm.dictionaryId,
                    key:this.dictionaryDetailFilterForm.key,
                    value:this.dictionaryDetailFilterForm.value,
                    pageNum:this.dictionaryDetailTableListParam.pageNum,
                    pageSize:this.dictionaryDetailTableListParam.pageSize,
                }).then(res=>{
                    this.dictionaryDetailTableList=res.records
                    this.dictionaryDetailTableListTotal=res.total
                    this.dictionaryDetailTableLoading = false
                }).catch(error=>{
                })
            },
            getDictionaryList(){
                SystemDictionaryApi.searchPage({
                    name:'',
                    pageNum:0,
                    pageSize:-1
                }).then(res => {
                    this.dictionaryList = res.records
                }).catch(error => {
                })
            },
        }
    }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
    .card-container ::v-deep .el-card__header{
        padding: 5px 0px 0px 5px;
    }

</style>
