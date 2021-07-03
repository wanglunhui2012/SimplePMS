<template>
    <div class="app-container">
        <!--表格上方的操作栏-->
        <div class="filter-container">
            <el-input v-model="filterForm.name" clearable :placeholder="$t('filterForm.menuName')" style="width: 150px;" class="filter-item" />
            <el-button v-waves class="filter-item" type="success" icon="el-icon-search" @click="search">{{ $t('filterForm.search') }}</el-button>
            <el-button v-if="hasAuthority('MENU_ADD')" class="filter-item" type="primary" icon="el-icon-plus" @click="add" style="margin-left: 0px;">{{ $t('filterForm.add') }}</el-button>
            <el-button v-if="hasAuthority('MENU_GET')" v-waves :loading="filterExporting" class="filter-item" type="warning" icon="el-icon-download" @click="handleExport" style="margin-left: 0px;">{{ $t('filterForm.export') }}</el-button>
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
            <el-table-column label="菜单名" align="left" prop="name" show-overflow-tooltip min-width="100">
            </el-table-column>
            <el-table-column label="排序" align="center" show-overflow-tooltip min-width="60">
                <template slot-scope="scope">
                    <el-tag>{{ scope.row.sort }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="路径" align="center" prop="path" show-overflow-tooltip min-width="80">
            </el-table-column>
            <el-table-column label="组件" align="center" prop="component" show-overflow-tooltip min-width="80">
            </el-table-column>
            <el-table-column label="图标" align="center" show-overflow-tooltip min-width="60">
                <template slot-scope="scope">
                    <svg-icon :icon-class="scope.row.icon" />
                </template>
            </el-table-column>
            <el-table-column label="重定向" align="center" prop="redirect" show-overflow-tooltip min-width="100">
            </el-table-column>
            <el-table-column label="隐藏" align="center" show-overflow-tooltip min-width="60">
                <template slot-scope="scope">
                    <span>{{ systemYesNoDictionary[scope.row.hidden]}}</span>
                </template>
            </el-table-column>
            <el-table-column label="总是显示" align="center" show-overflow-tooltip min-width="80">
                <template slot-scope="scope">
                    <span>{{ systemYesNoDictionary[scope.row.alwaysShow]}}</span>
                </template>
            </el-table-column>
            <el-table-column label="不缓存" align="center"show-overflow-tooltip min-width="70">
                <template slot-scope="scope">
                    <span>{{ systemYesNoDictionary[scope.row.noCache]}}</span>
                </template>
            </el-table-column>
            <el-table-column label="面包屑显示" align="center" show-overflow-tooltip min-width="100">
                <template slot-scope="scope">
                    <span>{{ systemYesNoDictionary[scope.row.breadcrumb]}}</span>
                </template>
            </el-table-column>
            <el-table-column label="固定tags" align="center" show-overflow-tooltip min-width="80">
                <template slot-scope="scope">
                    <span>{{ systemYesNoDictionary[scope.row.affix]}}</span>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" show-overflow-tooltip width="160">
            </el-table-column>
            <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
                <template slot-scope="{row}">
                    <el-button type="primary" circle icon="el-icon-search" @click="get(row)" style="margin-left: 5px;"/>
                    <el-button v-if="hasAuthority('MENU_DELETE')" type="primary" circle icon="el-icon-edit" @click="update(row)" style="margin-left: 5px;"/>
                    <el-popover
                            v-if="hasAuthority('MENU_DELETE')"
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
                <el-form-item label="菜单名" prop="name">
                    <el-input v-model="dialogTemp.name" placeholder="请输入菜单名" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="上级菜单" prop="parentId">
                    <el-cascader
                        v-model="dialogTemp.parentId"
                        :options="treeMenuList"
                        :props="cascaderProps"
                        :show-all-levels="false"
                        placeholder="请选择上级菜单"
                        clearable
                        filterable
                        style="width: 260px;">
                    </el-cascader>
                </el-form-item>
                <el-form-item label="排序" prop="sort">
                    <el-input-number v-model="dialogTemp.sort" :min="0" :max="999" controls-position="right"  placeholder="请输入排序" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="路径" prop="path">
                    <el-input v-model="dialogTemp.path"  placeholder="请输入路径" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="组件" prop="component">
                    <el-input v-model="dialogTemp.component"  placeholder="请输入组件" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="图标" prop="icon">
                    <!--<el-popover
                            placement="bottom-start"
                            width="460"
                            trigger="click"
                            @show="$refs['iconSelect'].reset()">
                        <icon-select ref="iconSelect" @selected="dialogIconSelect" />
                        <el-input slot="reference" v-model="dialogTemp.icon" style="width: 200px;" placeholder="请选择图标" readonly>
                            <svg-icon v-if="dialogTemp.icon" slot="prefix" :icon-class="dialogTemp.icon" class="el-input__icon" style="height: 32px;width: 16px;" />
                            <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                        </el-input>
                    </el-popover>-->
                    <el-popover
                        ref="systemMenuDialogIconElPopoverRef"
                        placement="bottom"
                        width="460"
                        trigger="click">
                        <icons style="height: 250px;overflow-y: scroll;" @select="dialogIconSelect"/>
                        <el-input slot="reference" v-model="dialogTemp.icon" style="width: 260px;" placeholder="请选择图标" readonly>
                            <svg-icon v-if="dialogTemp.icon" slot="prefix" :icon-class="dialogTemp.icon" class="el-input__icon" style="width: 16px;" />
                            <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                        </el-input>
                    </el-popover>
                </el-form-item>
                <el-form-item label="重定向" prop="redirect">
                    <el-input v-model="dialogTemp.redirect"  placeholder="请输入重定向" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="隐藏">
                    <el-radio-group v-model="dialogTemp.hidden" style="width: 260px;">
                        <!--不能用label必须用:label才能绑定Boolean值，label绑定的是String值，radio的显示值是label而option的是value二者相反-->
                        <el-radio-button v-for="key in Object.keys(systemYesNoDictionary)" :key="key" :label="key" >{{systemYesNoDictionary[key]}}</el-radio-button>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="总是显示">
                    <el-radio-group v-model="dialogTemp.alwaysShow" style="width: 260px;">
                        <!--不能用label必须用:label才能绑定Boolean值，label绑定的是String值，radio的显示值是label而option的是value二者相反-->
                        <el-radio-button v-for="key in Object.keys(systemYesNoDictionary)" :key="key" :label="key" >{{systemYesNoDictionary[key]}}</el-radio-button>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="不缓存">
                    <el-radio-group v-model="dialogTemp.noCache" style="width: 260px;">
                        <!--不能用label必须用:label才能绑定Boolean值，label绑定的是String值，radio的显示值是label而option的是value二者相反-->
                        <el-radio-button v-for="key in Object.keys(systemYesNoDictionary)" :key="key" :label="key" >{{systemYesNoDictionary[key]}}</el-radio-button>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="面包屑显示">
                    <el-radio-group v-model="dialogTemp.breadcrumb" style="width: 260px;">
                        <!--不能用label必须用:label才能绑定Boolean值，label绑定的是String值，radio的显示值是label而option的是value二者相反-->
                        <el-radio-button v-for="key in Object.keys(systemYesNoDictionary)" :key="key" :label="key" >{{systemYesNoDictionary[key]}}</el-radio-button>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="固定tags">
                    <el-radio-group v-model="dialogTemp.affix" style="width: 260px;">
                        <!--不能用label必须用:label才能绑定Boolean值，label绑定的是String值，radio的显示值是label而option的是value二者相反-->
                        <el-radio-button v-for="key in Object.keys(systemYesNoDictionary)" :key="key" :label="key" >{{systemYesNoDictionary[key]}}</el-radio-button>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="dialogTemp.remark" type="textarea" max="255" :autosize="{ minRows: 2, maxRows: 4}"  placeholder="请输入备注" style="width: 675px;"/>
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
    import Config from '@/config'
    import {getInternational} from '@/utils/i18nUtil'
    import waves from '@/directive/waves'
    import hasAuthority from "@/utils/permissionUtil";

    // 组件
    import IconSelect from '@/components/IconSelect'
    import Icons from '@/views/icons'

    // API
    import * as SystemMenuApi from '@/api/systemMenu'
    import * as SystemDepartmentApi from "@/api/systemDepartment";


    export default {
        name: "menu",
        components: {Icons,IconSelect},
        directives: {waves},
        data(){
            const validateSort = (rule, value, callback) => {
                if(value<0||value>999){
                    callback(new Error("范围:0-999!"))
                } else {
                    callback()
                }
            }
            return{
                // 第三方

                // 表格上方的操作栏
                filterForm: {
                    name:'' // 菜单名
                },
                filterExporting: false, // 是否正在导出

                // 表格
                tableLoading: true, // 是否正在加载
                tableList: [], // 表格列表
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
                    sort:'',
                    path:'',
                    component:'',
                    icon:'',
                    redirect:'',
                    hidden:false,
                    alwaysShow:true,
                    noCache:false,
                    breadcrumb:true,
                    affix:false,
                    remark: ''
                },
                dialogRules: { // 对话框验证规则
                    name:[{ required: true, message: '不能为空!', trigger: 'blur' }, {max: 255, message: '最长255位!', trigger: 'change' }],
                    parentId:[{ required: true, message: '不能为空!', trigger: 'change' }],
                    sort:[{ required: true, trigger: 'blur', validator: validateSort }],
                    path:[{ required: true, message: '不能为空!', trigger: 'blur' }, {max: 255, message: '最长255位!', trigger: 'change' }],
                    component:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    icon:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    redirect:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    remark:[{require:false,message:'最长255位!',trigger:'change',max:255}]
                },
                dialogConfirmButtonLoading:false,

                // 辅助
                systemYesNoDictionary: {}, // 系统是否字典
                cascaderProps:{ // 级联选择属性
                    multiple: false,// 是否多选
                    checkStrictly: true,// 是否严格的遵守父子节点不互相关联
                    emitPath:false,// 在选中节点改变时，是否返回由该节点所在的各级菜单的值所组成的数组，若设置 false，则只返回该节点的值
                    value:'id',// 指定选项的值为选项对象的某个属性值
                    label:'name',// 指定选项标签为选项对象的某个属性值
                    children:'children',// 指定选项的子选项为选项对象的某个属性值
                },
                treeMenuList:null, // 树形菜单列表
            }
        },
        created() {
            // 不能直接在初始化systemEnableDictionary时从Vuex拿，会造成Vuex没有数据时的报错
            this.systemYesNoDictionary=this.$store.getters.dictionary.systemYesNo
            // 一开始则获取数据
            this.handleSearch()
            this.getTreeMenuList()

        },
        methods:{
            // 第三方
            hasAuthority,
            getInternational,

            // 表格上方的操作栏
            search(){
                // search()只比handleSearch()多将pageNum设置为1的步骤，因为当点击搜索时需要将pageNum设置为1，因为搜索的条件会改变数据集this.list，如果页大小为5，上一次有8条数据有2页，
                // 但是此时页码切换到第2页，如果改变条件并点击搜索，而此时的结果只有4条数据有1页，但此时页码为2，所以不会有结果展示，所以每次点击搜索后需要将页码置为1才行，
                // 而其他的向currentChange()这种页数改变的因为搜索条件不变而不会改变数据集this.list，所以直接调用handleSearch()即可
                this.handleSearch()
            },
            get(row){
                this.dialogStatus=0 // 0为查看
                this.dialogVisible = true
                this.dialogTitle = '查看菜单'
                this.dialogTemp = {
                    id: row.id,
                    name: row.name,
                    parentId:row.parentId,
                    sort:row.sort,
                    path:row.path,
                    component:row.component,
                    icon:row.icon,
                    redirect:row.redirect,
                    hidden:row.hidden,
                    alwaysShow:row.alwaysShow,
                    noCache:row.noCache,
                    breadcrumb:row.breadcrumb,
                    affix:row.affix,
                    remark: row.remark
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate()
                })
            },
            add(){
                this.dialogStatus=1 // 为增加
                this.dialogVisible = true
                this.dialogTitle = "增加菜单"
                // 重置数据
                this.dialogTemp = {
                    name: '',
                    parentId:null,
                    sort:'',
                    path:'',
                    component:'',
                    icon:'',
                    redirect:'',
                    hidden:'0',
                    alwaysShow:'1',
                    noCache:'0',
                    breadcrumb:'1',
                    affix:'0',
                    remark: ''
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate()
                })
            },
            handleAdd(){
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogConfirmButtonLoading = true
                        SystemMenuApi.add(this.dialogTemp).then(res => {
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
                    SystemMenuApi._delete(ids).then(res => {
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
                this.dialogTitle = "修改菜单"
                this.dialogTemp = { // 复制表格行数据到dialogTemp
                    id: row.id,
                    name: row.name,
                    parentId:row.parentId,
                    sort:row.sort,
                    path:row.path,
                    component:row.component,
                    icon:row.icon,
                    redirect:row.redirect,
                    hidden:row.hidden,
                    alwaysShow:row.alwaysShow,
                    noCache:row.noCache,
                    breadcrumb:row.breadcrumb,
                    affix:row.affix,
                    remark: row.remark
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate()
                })
            },
            handleUpdate(){
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogConfirmButtonLoading = true
                        SystemMenuApi.update(this.dialogTemp).then(res => {
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
                    const tHeader = ['ID', '菜单名','上级菜单','排序','路径', '组件', '图标','重定向','隐藏',  '总是显示', '不缓存', '面包屑显示','固定tags']
                    const filterVal = ['id', 'name', 'parentId','sort','path', 'component', 'icon','redirect','hidden',  'alwaysShow', 'noCache', 'breadcrumb','affix']
                    const data = this.tableList.map(v => filterVal.map(j => {
                        if (j === 'icon') {
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
                        filename: 'InvestmentFiling Menu List'
                    })
                    this.filterExporting = false
                })
            },

            // 对话框
            dialogIconSelect(name){
                this.dialogTemp.icon=name
                this.$refs.systemMenuDialogIconElPopoverRef.doClose() // 关闭选择
            },
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
                SystemMenuApi.searchTree({
                    name:this.filterForm.name,
                }).then(res=>{
                    this.tableList=res
                    this.tableLoading = false
                }).catch(error=>{
                    this.tableLoading = false
                })
            },
            getTreeMenuList(){
                SystemMenuApi.searchTree({
                    name:''
                }).then(res=>{
                    this.treeMenuList=[{ // 附加头结点
                        id:'0', // 0不行，必须为'0'
                        name:Config.systemName,
                        children:res
                    }]
                }).catch(error=>{
                })
            },
        }
    }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
        text-align: left;
    }
</style>
