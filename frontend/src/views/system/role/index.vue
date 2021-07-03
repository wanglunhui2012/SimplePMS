<template>
    <div class="app-container">
        <el-row :gutter="20" style="height: 100%;">
            <el-col :xl="16" :lg="16" :md="16" :sm="16" :xs="16" class="card-container" style="height: 100%;">
                <!--表格上方的操作栏-->
                <div class="filter-container">
                    <el-input v-model="filterForm.name" clearable :placeholder="$t('filterForm.roleName')" style="width: 100px;" class="filter-item" />
                    <el-button v-waves class="filter-item" type="success" icon="el-icon-search" @click="search">{{ $t('filterForm.search') }}</el-button>
                    <el-button v-if="hasAuthority('ROLE_ADD')" class="filter-item" type="primary" icon="el-icon-plus" @click="add" style="margin-left: 0px;">{{ $t('filterForm.search') }}</el-button>
                    <el-button v-if="hasAuthority('ROLE_DELETE')" class="filter-item" type="danger" icon="el-icon-delete" @click="_delete" style="margin-left: 0px;">{{ $t('filterForm.delete') }}</el-button>
                    <el-button v-if="hasAuthority('ROLE_GET')" v-waves :loading="filterExporting" class="filter-item" type="warning" icon="el-icon-download" @click="handleExport" style="margin-left: 0px;">{{ $t('filterForm.export') }}</el-button>
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
                    @current-change="tableCurrentChange"
                    style="width: 100%;">
                    <el-table-column type="selection" width="40"/>
                    <el-table-column type="index" label="#" align="center" width="50">
                    </el-table-column>
                    <el-table-column label="角色名" align="center" prop="name" show-overflow-tooltip min-width="120">
                    </el-table-column>
                    <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip width="160">
                    </el-table-column>
                    <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
                        <template slot-scope="{row}">
                            <el-button type="primary" circle icon="el-icon-search" @click="get(row)" style="margin-left: 0px;"/>
                            <el-button v-if="hasAuthority('ROLE_UPDATE')" type="primary" circle icon="el-icon-edit" @click="update(row)" style="margin-left: 5px;"/>
                            <el-popover
                                v-if="hasAuthority('ROLE_DELETE')"
                                :ref="row.id"
                                placement="top"
                                width="180">
                                <p>确定删除本条数据吗?</p>
                                <div style="text-align: right; margin: 0">
                                    <el-button size="mini" type="text" @click="$refs[row.id].doClose()">取消</el-button>
                                    <el-button type="primary" :loading="tableRowDeleteLoading" @click="handleDelete([row.id])">确定</el-button>
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
            </el-col>
            <el-col :xl="8" :lg="8" :md="8" :sm="8" :xs="8" style="height: 100%;">
                <el-tabs type="border-card" style="height: 100%;overflow-y: auto;">
                    <el-tab-pane :label="$t('filterForm.menu')">
                        <el-tree
                                ref="tabTreeMenuRef"
                                :data="treeMenuList"
                                node-key="id"
                                accordion
                                show-checkbox
                                :props="treeProps"
                                :check-strictly="true"
                                class="tabTreeMenu">
                        </el-tree>
                    </el-tab-pane>
                    <el-tab-pane :label="$t('filterForm.permission')">
                        <el-tree
                                ref="tabTreePermissionRef"
                                :data="permissionList"
                                node-key="id"
                                accordion
                                show-checkbox
                                :props="treeProps">
                        </el-tree>
                    </el-tab-pane>
                    <el-tab-pane :label="$t('filterForm.dataScope')">
                        <el-form >
                            <el-form-item label="数据权限">
                                <el-select v-model="tableCurrentRowDataScope" clearable placeholder="数据权限" disabled>
                                    <el-option v-for="key in Object.keys(systemDataScopeDictionary)" :key="key" :label="systemDataScopeDictionary[key]" :value="key">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                        <el-tree
                                v-if="tableCurrentRowDataScope==='1'"
                                ref="tabTreeDepartmentRef"
                                :data="treeDepartmentList"
                                node-key="id"
                                accordion
                                show-checkbox
                                :props="treeProps"
                                :check-strictly="true">
                        </el-tree>
                    </el-tab-pane>
                </el-tabs>
            </el-col>
        </el-row>
        <!--弹出对话框，:visible控制其是否显示，fullscreen全屏,append-to-body自身是否插入至body元素上，close-on-click-modal是否可以通过点击modal关闭Dialog，label-width表单域标签的宽度，label-position表单域标签的位置-->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" append-to-body width="880px" >
            <el-form ref="dialogFormRef" :model="dialogTemp" :rules="dialogRules" :inline="true"  label-position="left" label-width="140px">
                <el-form-item label="角色名" prop="name">
                    <el-input v-model="dialogTemp.name" placeholder="请输入角色名" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="菜单">
                    <el-cascader
                        ref="dialogCascaderRef"
                        v-model="dialogTemp.menuIdList"
                        :options="treeMenuList"
                        :props="dialogMenuCascaderProps"
                        :show-all-levels="false"
                        placeholder="请选择菜单"
                        clearable
                        filterable
                        style="width: 260px;">
                    </el-cascader>
                </el-form-item>
                <el-form-item label="权限">
                    <el-select v-model="dialogTemp.permissionIdList" multiple clearable collapse-tags loading-text="正在加载..."  placeholder="请选择权限" style="width: 260px;">
                        <el-option v-for="item in permissionList" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="数据权限" prop="dataScope">
                    <el-select v-model="dialogTemp.dataScope" clearable  placeholder="请选择数据权限" style="width: 260px" @change="dialogDataScopeSelectChange">
                        <el-option v-for="key in Object.keys(systemDataScopeDictionary)" :key="key" :label="systemDataScopeDictionary[key]" :value="key" />
                    </el-select>
                </el-form-item>
                <el-form-item label="数据权限范围" v-if="dialogTemp.dataScope==='1'" prop="departmentIdList">
                    <el-tree
                        v-model="dialogTemp.departmentIdList"
                        :data="treeDepartmentList"
                        node-key="id"
                        accordion
                        show-checkbox
                        :props="treeProps"
                        :check-strictly="true"
                        @check="dialogDepartmentIdListTreeCheck"
                        style="width: 260px;">
                    </el-tree>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="dialogTemp.remark" type="textarea" max="255" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入备注" style="width: 675px;"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button v-if="dialogStatus!==0" type="primary" @click="handleDialogConfirm" :loading="dialogConfirmButtonLoading">确定</el-button>
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
    import * as SystemRoleApi from '@/api/systemRole'
    import * as SystemMenuApi from '@/api/systemMenu'
    import * as SystemPermissionApi from '@/api/systemPermission'
    import * as SystemDeaprtmentApi from '@/api/systemDepartment'

    export default {
        name: "role",
        directives: {waves},
        data(){
            return{
                // 第三方

                // 表格上方的操作栏
                filterForm: { // 过滤表单
                    name:'' // 角色名
                },
                filterExporting: false, // 是否正在导出

                // 表格
                tableLoading: true, // 是否正在加载
                tableList: [], // 表格列表
                tableRowDeleteLoading:false, // 是否正在删除行
                tableMultipleSelection: [], // 表格多选列表
                tableCurrentRowDataScope:null, // 表格当前选中行dataScope

                // 分页
                tableListTotal: 0, // 表格列表总数（为服务器总数不是list个数）
                tableListParam: { // 查询参数
                    pageNum: 1, // 当前页
                    pageSize: this.Config.pageSize // 页大小
                },

                // tab

                // 对话框
                dialogStatus:0, // 对话框状态，0为查看，1为增加，2为修改
                dialogTitle: '', // 对话框标题，可为增加、修改2种
                dialogVisible: false, // 对话框是否可见,一开始是关闭的，点击之后才打开
                dialogTemp: { // 对话框表单中间数据
                    name: '',
                    remark: '',
                    dataScope:'',
                    menuIdList: [],
                    permissionIdList:[],
                    departmentIdList:[],
                },
                dialogRules: { // 对话框验证规则
                    name:[{ required: true, message: '不能为空!', trigger: 'blur' }, {max: 255, message: '最长255位!', trigger: 'change' }],
                    dataScope:[{ required: true, message: '不能为空!', trigger: 'blur' }],
                    remark:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    departmentIdList:[{ required: true, message: '不能为空!', trigger: 'blur' }],
                },
                dialogConfirmButtonLoading:false,

                // 辅助
                systemDataScopeDictionary:{}, // 系统数据权限字典
                treeProps:{ // 树形属性
                    label:'name',// 指定节点标签为节点对象的某个属性值
                    children:'children',// 指定子树为节点对象的某个属性值
                },
                dialogMenuCascaderProps:{ // 树形属性
                    multiple: true,// 是否多选
                    checkStrictly: true,// 是否严格的遵守父子节点不互相关联
                    emitPath:false,// 在选中节点改变时，是否返回由该节点所在的各级菜单的值所组成的数组，若设置 false，则只返回该节点的值
                    value:'id',// 指定选项的值为选项对象的某个属性值
                    label:'name',// 指定选项标签为选项对象的某个属性值
                    children:'children',// 指定选项的子选项为选项对象的某个属性值
                },
                treeMenuList:null, // 树形菜单列表
                permissionList:[], // 权限列表
                treeDepartmentList:null, // 树形部门列表，必须为null不能为[]，因为延迟加载时首先必须为null
            }
        },
        created() {
            // 不能直接在初始化systemEnableDictionary时从Vuex拿，会造成Vuex没有数据时的报错
            this.systemDataScopeDictionary=this.$store.getters.dictionary.systemDataScope
            // 一开始则获取数据
            this.handleSearch()
            this.getTreeMenuList()
            this.getPermissionList()
            this.getTreeDepartmentList()
        },
        methods:{
            // 第三方
            hasAuthority,

            // 表格上方的操作栏
            get(row){
                this.dialogStatus=0 // 0为查看
                this.dialogVisible = true
                this.dialogTitle = '查看角色'
                this.dialogTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    name: row.name,
                    remark: row.remark,
                    dataScope:row.dataScope,
                    menuIdList: row.menuIdList,
                    permissionIdList: row.permissionIdList,
                    departmentIdList:row.departmentIdList
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate()
                })
            },
            search(){
                this.tableListParam.pageNum=1
                this.handleSearch()
            },
            add(){
                this.dialogStatus=1 // 为增加
                this.dialogVisible = true
                this.dialogTitle = "增加角色"
                // 重置数据
                this.dialogTemp = {
                    name: '',
                    remark: '',
                    dataScope:'',
                    menuIdList: [],
                    permissionIdList:[],
                    departmentIdList:[],
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate()
                })
            },
            handleAdd(){
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogConfirmButtonLoading = true
                        SystemRoleApi.add(this.dialogTemp).then(res => {
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
            _delete(){
                if (this.tableMultipleSelection.length === 0) {
                    this.$message.error('请选择要删除的行!');
                    return
                }
                let ids = this.tableMultipleSelection.map(function (item, index, array) {
                    return item.id
                })
                this.handleDelete(ids)
            },
            handleDelete(ids){
                this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(res => {
                    this.tableRowDeleteLoading=true
                    SystemRoleApi._delete(ids).then(res => {
                        this.$message({
                            message: '删除成功!',
                            type: 'success'
                        });
                        this.tableRowDeleteLoading=false
                        this.tableListParam.pageNum = 1; // 删除成功之后,有时数据可能会都没有,所以此时的当前页pageNum可能没有,所以需要将其设置为1
                        this.handleSearch()
                    }).catch(error => {
                        this.tableRowDeleteLoading=false
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
                this.dialogTitle = "修改角色"
                this.dialogTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    name: row.name,
                    remark: row.remark,
                    dataScope:row.dataScope,
                    menuIdList: row.menuIdList,
                    permissionIdList: row.permissionIdList,
                    departmentIdList:row.departmentIdList
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate()
                })
            },
            handleUpdate(){
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogConfirmButtonLoading = true
                        SystemRoleApi.update({
                            id: this.dialogTemp.id,
                            name: this.dialogTemp.name,
                            remark: this.dialogTemp.remark,
                            dataScope: this.dialogTemp.dataScope,
                            menuIdList: this.dialogTemp.menuIdList,
                            permissionIdList: this.dialogTemp.permissionIdList,
                            departmentIdList:this.dialogTemp.departmentIdList
                        }).then(res => {
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
            handleExport(){
                this.filterExporting = true
                import('@/utils/excelUtil').then(excel => {
                    const tHeader = ['ID', '角色名','备注']
                    const filterVal = ['id', 'name', 'remark']
                    const data = this.tableList.map(v => filterVal.map(j => {
                        if (j === 'remark') {
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
                        filename: 'InvestmentFiling Role List'
                    })
                    this.filterExporting = false
                })
            },

            // 表格
            tableRowClick(row) { // 表格行点击事件

            },
            tableSelectionChange(rows) { // 表格前面方框选择改变时的事件，rows为选中行的数组
                this.tableMultipleSelection = rows
            },
            tableCurrentChange(currentRow, oldCurrentRow){
                if(currentRow){
                    this.$refs.tabTreeMenuRef.setCheckedKeys(currentRow.menuIdList)
                    this.$refs.tabTreePermissionRef.setCheckedKeys(currentRow.permissionIdList)
                    this.tableCurrentRowDataScope=currentRow.dataScope
                    if(this.tableCurrentRowDataScope==='1'){
                        this.$nextTick(() => {
                            this.$refs.tabTreeDepartmentRef.setCheckedKeys(currentRow.departmentIdList)
                        })
                    }
                }else{
                    this.$refs.tabTreeMenuRef.setCheckedKeys([])
                    this.$refs.tabTreePermissionRef.setCheckedKeys([])
                    this.tableCurrentRowDataScope=null
                }
            },

            // 分页
            paginationSizeChange(val) { // 分页工具页大小改变事件
                this.tableListParam.pageSize=val
                this.handleSearch()
            },
            paginationCurrentChange(val) { // 分页工具当前页改变事件
                this.tableListParam.pageNum=val;
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
            dialogDataScopeSelectChange(value){
                if(value!=='1'){ // 如果之前数据权限选择了自定义且勾选了数据权限部门，后面数据权限改为其他的，则数据权限部门需要重置
                    this.dialogTemp.departmentIdList=[]
                }
            },
            dialogDepartmentIdListTreeCheck(node,selectedState){
                // el-tree没有v-model，只能使用这样的方式设置被选中节点
                this.dialogTemp.departmentIdList=selectedState.checkedKeys
            },

            // 辅助
            handleSearch(){
                this.tableLoading = true
                SystemRoleApi.searchPage({
                    name:this.filterForm.name,
                    pageNum:this.tableListParam.pageNum,
                    pageSize:this.tableListParam.pageSize,
                }).then(res=>{
                    this.tableList=res.records
                    this.tableListTotal=res.total
                    this.tableLoading = false
                }).catch(error=>{
                    this.tableLoading = false
                })
            },
            getTreeMenuList(){
                SystemMenuApi.searchTree({
                    name:''
                }).then(res=>{
                    this.treeMenuList=res
                }).catch(error=>{
                })
            },
            getPermissionList() {
                SystemPermissionApi.searchPage({
                    pageNum:0,
                    pageSize:-1
                }).then(res => {
                    this.permissionList = res.records
                }).catch(error => {
                })
            },
            getTreeDepartmentList(){
                SystemDeaprtmentApi.searchTree({
                    name:''
                }).then(res=>{
                    this.treeDepartmentList=res
                }).catch(error=>{
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
