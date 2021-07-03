<template>
    <div class="app-container">
        <!--表格上方的操作栏-->
        <div class="filter-container">
            <el-input v-model="filterForm.name" clearable :placeholder="$t('filterForm.username')" style="width: 100px;" class="filter-item" />
            <el-select v-model="filterForm.enable" clearable :placeholder="$t('filterForm.status')" style="width: 90px;" class="filter-item">
                <el-option v-for="key in Object.keys(systemEnableDictionary)" :key="key" :label="systemEnableDictionary[key]" :value="key">
                </el-option>
            </el-select>
            <el-cascader
                ref="filterFormCascaderRef"
                v-model="filterForm.departmentId"
                :options="treeDepartmentList"
                :props="filterFormDepartmentCascaderProps"
                :show-all-levels="false"
                :placeholder="$t('filterForm.department')"
                clearable
                filterable
                style="width: 150px;"
                class="filter-item">
            </el-cascader>
            <el-button v-waves class="filter-item" type="success" icon="el-icon-search" @click="search" >
                {{ $t('filterForm.search') }}
            </el-button>
            <el-button v-if="hasAuthority('USER_ADD')" class="filter-item" type="primary" icon="el-icon-plus" @click="add" >
                {{ $t('filterForm.add') }}
            </el-button>
            <el-button v-if="hasAuthority('USER_DELETE')" class="filter-item" type="danger" icon="el-icon-delete" @click="_delete" >
                {{ $t('filterForm.delete') }}
            </el-button>
            <el-button v-permission="['USER_GET']" v-waves :loading="filterExporting" class="filter-item" type="warning" icon="el-icon-download" @click="handleExport" >
                {{ $t('filterForm.export') }}
            </el-button>
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
            <el-table-column label="用户名" align="center" prop="name" show-overflow-tooltip min-width="120">
            </el-table-column>
            <el-table-column label="状态" align="center" width="80" class-name="status-col" >
                <template slot-scope="scope">
                    <el-tag :type="scope.row.enable ? '' : 'info'">{{ systemEnableDictionary[scope.row.enable]}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="邮箱" align="center" prop="email" show-overflow-tooltip min-width="160">
            </el-table-column>
            <el-table-column label="电话号码" align="center" prop="phoneNumber" show-overflow-tooltip width="120">
            </el-table-column>
            <el-table-column label="性别" align="center" width="80" class-name="status-col" >
                <template slot-scope="scope">
                    <el-tag :type="scope.row.sex ? '' : 'info'">{{ systemUserSexDictionary[scope.row.sex]}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip width="160">
            </el-table-column>
            <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
                <template slot-scope="{row}">
                    <el-button type="primary" circle icon="el-icon-search" @click="get(row)" style="margin-left: 0px;"/>
                    <el-button v-permission="['USER_UPDATE']" type="primary" circle icon="el-icon-edit" @click="update(row)" style="margin-left: 5px;"/>
                    <el-popover
                            v-permission="['USER_DELETE']"
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
                style="padding: 6px 0;">
        </el-pagination>
        <!--弹出对话框，:visible控制其是否显示，fullscreen全屏,append-to-body自身是否插入至body元素上，close-on-click-modal是否可以通过点击modal关闭Dialog，label-width表单域标签的宽度，label-position表单域标签的位置-->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" append-to-body width="880px" >
            <!--ref用于之后引用此表单用于验证，inline一行可放多个表单，rules为验证规则，之后需要在el-form-item上用prop属性即可验证-->
            <el-form ref="dialogFormRef" :model="dialogTemp" :rules="dialogRules" :inline="true"  label-position="left" label-width="140px">
                <el-form-item label="用户名" prop="name">
                    <el-input v-model="dialogTemp.name" placeholder="请输入用户名" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="昵称" prop="nickName">
                    <el-input v-model="dialogTemp.nickName" placeholder="请输入昵称" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="状态">
                    <el-radio-group v-model="dialogTemp.enable" style="width: 260px;">
                        <el-radio v-for="key in Object.keys(systemEnableDictionary)" :key="key" :label="key" >{{systemEnableDictionary[key]}}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="dialogTemp.email" placeholder="请输入邮箱" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="电话号码" prop="phoneNumber">
                    <el-input v-model="dialogTemp.phoneNumber" placeholder="请输入电话号码" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="dialogTemp.sex" placeholder="请选择性别" style="width: 260px;">
                        <el-radio v-for="key in Object.keys(systemUserSexDictionary)" :key="key" :label="key" >{{systemUserSexDictionary[key]}}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="头像">
                    <!--图片上传,action为图片地址(必须有)，multiple多选，list-type显示类型，auto-upload是否自动上传，before-remove移除的处理,name上传时的文件名字段，accept限定文件格式，http-request为自定义上传-->
                    <el-upload
                        ref="dialogAvatarUploadRef"
                        action=""
                        accept="image/*"
                        :show-file-list="false"
                        :auto-upload="false"
                        :on-change="dialogAvatarUploadOnChange"
                        class="avatar-uploader"
                        style="width: 260px;">
                        <img v-if="dialogAvatar.url" :src="dialogAvatar.url" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="部门">
                    <el-cascader
                        ref="dialogDepartmentCascaderRef"
                        v-model="dialogTemp.departmentId"
                        :options="treeDepartmentList"
                        :props="filterFormDepartmentCascaderProps"
                        :show-all-levels="false"
                        placeholder="部门"
                        clearable
                        filterable
                        @change="dialogTreeDepartmentChange"
                        style="width: 260px;">
                    </el-cascader>
                </el-form-item>
                <el-form-item label="角色">
                    <el-select v-model="dialogTemp.roleIdList" multiple clearable collapse-tags loading-text="正在加载..." placeholder="请选择角色" style="width: 260px;">
                        <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="职位">
                    <el-select v-model="dialogTemp.jobIdList" multiple clearable collapse-tags loading-text="正在加载..." placeholder="请选择职位" style="width: 260px;">
                        <el-option v-for="item in jobList" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
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
    import permission from '@/directive/permission'
    import hasAuthority from '@/utils/permissionUtil';

    // 组件
    import waves from '@/directive/waves'

    // API
    import * as SystemUserApi from '@/api/systemUser'
    import * as SystemRoleApi from '@/api/systemRole'
    import * as SystemDepartmentApi from '@/api/systemDepartment'
    import * as SystemJobApi from '@/api/systemJob'

    export default {
        name: 'user',
        directives: {waves,permission},
        filters: {},
        data() {
            return {
                // 第三方

                // 表格上方的操作栏
                filterForm: { // 过滤表单
                    name: '', // 用户名
                    enable: null, // 是否启用
                    departmentId:null, // 部门
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
                    name: '',
                    nickName:'',
                    enable: null,
                    email: '',
                    phoneNumber: '',
                    sex:'',
                    remark: '',
                    departmentId:null,
                    roleIdList: [],
                    jobIdList:[],
                },
                dialogRules: { // 对话框验证规则
                    name:[{ required: true, message: '不能为空!', trigger: 'blur' }, {max: 255, message: '最长255位!', trigger: 'change' }],
                    nickName:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    enable:[{ required: true, message: '不能为空!', trigger: 'blur' }],
                    email: [{required:false, type: 'email', message: '格式错误!', trigger: 'blur' }],
                    phoneNumber:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    sex:[{ required: true, message: '不能为空!', trigger: 'blur' }],
                    remark:[{require:false,message:'最长255位!',trigger:'change',max:255}]
                },
                dialogAvatar:{ // 对话框头像
                    raw:null,
                    url:null
                },
                dialogConfirmButtonLoading:false,

                // 辅助
                systemEnableDictionary: {}, // 系统启用字典
                systemUserSexDictionary:{}, // 系统用户性别字典
                treeDepartmentList:null, // 树形部门列表，必须为null不能为[]，因为延迟加载时首先必须为null
                filterFormDepartmentCascaderProps:{
                    multiple: false,// 是否多选
                    checkStrictly: true,// 是否严格的遵守父子节点不互相关联
                    emitPath:false,// 在选中节点改变时，是否返回由该节点所在的各级菜单的值所组成的数组，若设置 false，则只返回该节点的值
                    value:'id',// 指定选项的值为选项对象的某个属性值
                    label:'name',// 指定选项标签为选项对象的某个属性值
                    children:'children',// 指定选项的子选项为选项对象的某个属性值
                },
                roleList: null, // 角色列表
                jobList: null, // 职位列表
            }
        },
        created() {
            // 不能直接在初始化systemEnableDictionary时从Vuex拿，会造成Vuex没有数据时的报错
            this.systemEnableDictionary=this.$store.getters.dictionary.systemEnable
            this.systemUserSexDictionary=this.$store.getters.dictionary.systemUserSex
            // 一开始则获取数据
            this.handleSearch()
            this.getTreeDepartmentList()
            this.getRoleList()
            this.getJobList()
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
                this.dialogTitle = '查看用户'
                this.dialogTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    name: row.name,
                    nickName:row.nickName,
                    enable: row.enable,
                    email: row.email,
                    phoneNumber: row.phoneNumber,
                    sex:row.sex,
                    remark: row.remark,
                    departmentId: row.departmentId,
                    roleIdList:row.roleIdList,
                    jobIdList:row.jobIdList
                }

                // 修改对话框文件
                this.dialogAvatar={
                    raw:null,
                    url:row.avatarUrl
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            add() {
                this.dialogStatus=1 // 为增加
                this.dialogVisible = true
                this.dialogTitle = "增加用户"
                // 重置数据
                this.dialogTemp = {
                    name: '',
                    nickName:'',
                    enable: '1',
                    email: '',
                    phoneNumber: '',
                    sex:'0',
                    remark: '',
                    departmentId:null,
                    roleIdList: [],
                    jobIdList:[],
                }
                this.dialogAvatar={
                    raw:null,
                    url:null
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            handleAdd() {
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        let form = new FormData()  // 创建form对象

                        form.append('json',JSON.stringify(this.dialogTemp))// json数据
                        form.append('avatar', this.dialogAvatar.raw)  // 通过append向form对象添加原始文件数据

                        this.dialogConfirmButtonLoading = true
                        SystemUserApi.add(form).then(res => {
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
                if (this.tableMultipleSelectionList.length === 0) {
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
                    SystemUserApi._delete(ids).then(res => {
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
                this.dialogTitle = "修改用户"
                this.dialogTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    name: row.name,
                    nickName:row.nickName,
                    enable: row.enable,
                    email: row.email,
                    phoneNumber: row.phoneNumber,
                    sex:row.sex,
                    remark: row.remark,
                    departmentId: row.departmentId,
                    roleIdList:row.roleIdList,
                    jobIdList:row.jobIdList
                }

                // 修改对话框文件
                this.dialogAvatar={
                    raw:null,
                    url:row.avatarUrl
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            handleUpdate() {
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogConfirmButtonLoading = true

                        let form = new FormData()  // 创建form对象

                        form.append('json',JSON.stringify(this.dialogTemp))// json数据
                        if(this.dialogAvatar.raw!=null){
                            form.append('newAvatar', this.dialogAvatar.raw)  // 通过append向form对象添加原始文件数据
                        }

                        SystemUserApi.update(
                            form
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
                    const tHeader = ['ID', '用户名', '状态', '邮箱', '电话号码', '创建时间']
                    const filterVal = ['id', 'name', 'enable', 'email', 'phoneNumber', 'createTime']
                    const data = this.tableList.map(v => filterVal.map(j => {
                        if (j === 'enable') {// 格式化enable
                            if (v[j]) {
                                return '激活'
                            } else {
                                return '锁定'
                            }
                        } else {
                            return v[j]
                        }
                    }))
                    excel.export_json_to_excel({
                        header: tHeader,
                        data,
                        filename: 'InvestmentFiling User List'
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
            dialogAvatarUploadOnChange(file){
                this.dialogAvatar.raw=file.raw
                this.dialogAvatar.url = URL.createObjectURL(file.raw);
            },
            dialogTreeDepartmentChange(node){
                if(node){//必须做节点判断，因为清除选择时也会回调此方法
                    let checkedNode=this.$refs.dialogDepartmentCascaderRef.getCheckedNodes()[0]
                    if(checkedNode.children.length!==0){
                        this.$message.error('只能选择子部门!');
                        this.$nextTick(() => {
                            this.$refs.dialogDepartmentCascaderRef.handleClear()
                        })
                    }
                }
            },
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
                SystemUserApi.searchPage({
                    name: this.filterForm.name,
                    enable: this.filterForm.enable,
                    departmentIdList: this.filterForm.departmentId?[this.filterForm.departmentId]:[],
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
            getTreeDepartmentList(){
                SystemDepartmentApi.searchTree({
                    name:''
                }).then(res=>{
                    this.treeDepartmentList=res
                }).catch(error=>{
                })
            },
            getRoleList() {
                SystemRoleApi.searchPage({
                    pageNum:0,
                    pageSize:-1
                }).then(res => {
                    this.roleList = res.records
                }).catch(error => {
                })
            },
            getJobList() {
                SystemJobApi.searchPage({
                    pageNum:0,
                    pageSize:-1
                }).then(res => {
                    this.jobList = res.records
                }).catch(error => {
                })
            },
        }
    }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
    .avatar-uploader ::v-deep .el-upload { // 必须加上/deep/或者>>>（哪个可以用用哪个），否则在scope下不能应用，这里隐藏<el-upload>的文件输入框只需要显示的文件列表
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader ::v-deep .el-upload:hover { // /deep/不能使用了，只能使用::v-deep
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
</style>
