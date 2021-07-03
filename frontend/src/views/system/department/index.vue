<template>
    <div class="app-container">
        <!--表格上方的操作栏-->
        <div class="filter-container">
            <el-input v-model="filterForm.name" clearable :placeholder="$t('filterForm.departmentName')" style="width: 150px;" class="filter-item" />
            <el-button v-waves class="filter-item" type="success" icon="el-icon-search" @click="search">{{ $t('filterForm.search') }}</el-button>
            <el-button v-if="hasAuthority('DEPARTMENT_ADD')" class="filter-item" type="primary" icon="el-icon-plus" @click="add" style="margin-left: 0px;">{{ $t('filterForm.add') }}</el-button>
            <el-button v-if="hasAuthority('DEPARTMENT_GET')" v-waves :loading="filterExporting" class="filter-item" type="warning" icon="el-icon-download" @click="handleExport" style="margin-left: 0px;">{{ $t('filterForm.export') }}</el-button>
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
                row-key="id"
                :tree-props="tableTreeProps"
                :default-expand-all="false"
                style="width: 100%;">
            <el-table-column label="部门名" align="left" prop="name" show-overflow-tooltip min-width="80">
            </el-table-column>
            <el-table-column label="领导人" align="center" prop="leader" show-overflow-tooltip min-width="80">
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip width="160">
            </el-table-column>
            <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
                <template slot-scope="{row}">
                    <el-button type="primary" circle icon="el-icon-search" @click="get(row)" style="margin-left: 5px;"/>
                    <el-button v-if="hasAuthority('DEPARTMENT_UPDATE')" type="primary" circle icon="el-icon-edit" @click="update(row)" style="margin-left: 5px;"/>
                    <el-popover
                            v-if="hasAuthority('DEPARTMENT_DELETE')"
                            :ref="row.id"
                            placement="top"
                            width="180">
                        <p>确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！</p>
                        <div style="text-align: right; margin: 0">
                            <el-button size="mini" type="text" @click="$refs[row.id].doClose()">取消</el-button>
                            <el-button type="primary" @click="handleDelete([row.id])">确定</el-button>
                        </div>
                        <el-button slot="reference" type="danger" circle icon="el-icon-delete" style="margin-left: 5px;"/>
                    </el-popover>
                </template>
            </el-table-column>
        </el-table>
        <!--弹出对话框，:visible控制其是否显示，fullscreen全屏,append-to-body自身是否插入至body元素上，close-on-click-modal是否可以通过点击modal关闭Dialog，label-width表单域标签的宽度，label-position表单域标签的位置-->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" append-to-body width="880px" >
            <el-form ref="dialogFormRef" :model="dialogTemp" :rules="dialogRules" :inline="true"  label-position="left" label-width="140px">
                <el-form-item label="部门名" prop="name">
                    <el-input v-model="dialogTemp.name" placeholder="请输入部门名" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="上级部门" prop="parentId">
                    <el-cascader
                        v-model="dialogTemp.parentId"
                        :options="treeDepartmentList"
                        :props="cascaderProps"
                        :show-all-levels="false"
                        placeholder="请选择上级菜单"
                        clearable
                        filterable
                        style="width: 260px;">
                    </el-cascader>
                </el-form-item>
                <el-form-item label="领导人" prop="leader">
                    <el-input v-model="dialogTemp.leader" placeholder="请输入领导人" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="电话号码" prop="phoneNumber">
                    <el-input v-model="dialogTemp.phoneNumber" placeholder="请输入电话号码" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="dialogTemp.email" placeholder="请输入邮箱" style="width: 260px;"/>
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
    import waves from '@/directive/waves'
    import hasAuthority from "@/utils/permissionUtil";
    import Config from '@/config'

    // 组件

    // API
    import * as SystemDepartmentApi from '@/api/systemDepartment'


    export default {
        name: "department",
        directives: {waves},
        data(){
            return{
                // 第三方

                // 表格上方的操作栏
                filterForm: {
                    name:'' // 部门名称
                },
                filterExporting: false, // 是否正在导出

                // 表格
                tableLoading: true, // 是否正在加载
                tableList: [], // 表格列表
                tableMultipleSelectionList: [], // 表格多选列表
                tableTreeProps:{ // 树形表格属性
                    children: 'children'
                },

                // 对话框
                dialogStatus:0, // 对话框状态，0为查看，1为增加，2为修改
                dialogTitle: '', // 对话框标题,可为增加、修改2种
                dialogVisible: false, // 对话框是否可见,一开始是关闭的,点击之后才打开
                dialogTemp: { // 对话框表单中间数据
                    name: '',
                    parentId:null,
                    leader:'',
                    phoneNumber:'',
                    email:'',
                    remark: ''
                },
                dialogRules: { // 对话框验证规则
                    name:[{ required: true, message: '不能为空!', trigger: 'blur' }, {max: 255, message: '最长255位!', trigger: 'change' }],
                    parentId:[{ required: true, message: '不能为空!', trigger: 'change' }],
                    leader:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    phoneNumber:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    email: [{required:false, type: 'email', message: '格式错误!',max:255, trigger: 'blur'}],
                    remark:[{require:false,message:'最长255位!',trigger:'change',max:255}]
                },
                dialogConfirmButtonLoading:false,

                // 辅助
                cascaderProps:{ // 级联选择属性
                    multiple: false,// 是否多选
                    checkStrictly: true,// 是否严格的遵守父子节点不互相关联
                    emitPath:false,// 在选中节点改变时，是否返回由该节点所在的各级菜单的值所组成的数组，若设置 false，则只返回该节点的值
                    value:'id',// 指定选项的值为选项对象的某个属性值
                    label:'name',// 指定选项标签为选项对象的某个属性值
                    children:'children',// 指定选项的子选项为选项对象的某个属性值
                },
                treeDepartmentList:null, // 树形菜单列表
            }
        },
        created() {
            // 一开始则获取数据
            this.handleSearch()
            this.getTreeDepartmentList()
        },
        methods:{
            // 第三方
            hasAuthority,

            // 表格上方的操作栏
            search(){
                this.handleSearch()
            },
            get(row){
                this.dialogStatus=0 // 0为查看
                this.dialogVisible = true
                this.dialogTitle = '查看部门'
                this.dialogTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    name: row.name,
                    parentId:row.parentId,
                    leader:row.leader,
                    phoneNumber:row.phoneNumber,
                    email:row.email,
                    remark: row.remark
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            add(){
                this.dialogStatus=1 // 为增加
                this.dialogVisible = true
                this.dialogTitle = "增加部门"
                // 重置数据
                this.dialogTemp = {
                    name: '',
                    parentId:null,
                    leader:'',
                    phoneNumber:'',
                    email:'',
                    remark: ''
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            handleAdd(){
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogConfirmButtonLoading = true
                        SystemDepartmentApi.add(this.dialogTemp).then(res => {
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
            handleDelete(ids){
                this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(res => {
                    SystemDepartmentApi._delete(ids).then(res => {
                        this.$message({
                            message: '删除成功!',
                            type: 'success'
                        });
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
                this.dialogTitle = "修改部门"
                this.dialogTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    name: row.name,
                    parentId:row.parentId,
                    leader:row.leader,
                    phoneNumber:row.phoneNumber,
                    email:row.email,
                    remark: row.remark
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            handleUpdate(){
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogConfirmButtonLoading = true
                        SystemDepartmentApi.update(
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
            handleExport(){
                this.filterExporting = true
                import('@/utils/excelUtil').then(excel => {
                    const tHeader = ['ID', '部门名','上级部门','领导人','电话号码','备注']
                    const filterVal = ['id', 'name', 'parentId','leader','phoneNumber','email']
                    const data = this.tableList.map(v => filterVal.map(j => {
                        if (j === 'phoneNumber') {
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
                        filename: 'InvestmentFiling Department List'
                    })
                    this.filterExporting = false
                })
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
            handleSearch(){
                this.tableLoading = true
                SystemDepartmentApi.searchTree({
                    name:this.filterForm.name,
                }).then(res=>{
                    this.tableList=res
                    this.tableLoading = false
                }).catch(error=>{
                    this.tableLoading = false
                })
            },
            getTreeDepartmentList(){
                SystemDepartmentApi.searchTree({
                    name:''
                }).then(res=>{
                    this.treeDepartmentList=[{ // 附加头结点
                        id:'0', // 0不行，必须为'0'
                        name:Config.systemName,
                        children:res
                    }]
                }).catch(error=>{
                })
            }
        }
    }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
