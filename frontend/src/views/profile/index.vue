<template>
    <div class="app-container">
        <el-row :gutter="20" style="height: 100%;">
            <el-col :xs="24" :sm="24" :md="9" :lg="6" :xl="5" style="height: 100%;">
                <el-card class="box-card" style="height: 100%;" :body-style="{ height: '100%' }">
                    <div slot="header" class="clearfix">
                        <span>{{ $t('common.personalInfo') }}</span>
                    </div>
                    <div>
                        <div style="text-align: center">
                            <el-upload
                                ref="profileAvatarUploadRef"
                                action=""
                                accept="image/*"
                                :show-file-list="false"
                                :auto-upload="false"
                                :on-change="avatarUploadOnChange">
                                <img :src="avatarUrl" title="点击上传头像" style="width: 120px;height:120px;border-radius: 50%;">
                            </el-upload>
                        </div>
                        <ul class="user-info">
                            <li><svg-icon icon-class="user" /> {{ $t('common.username') }} <div class="user-right">{{ user.name }}</div></li>
                            <li><svg-icon icon-class="phone-number" /> {{ $t('common.phoneNumber') }} <div class="user-right">{{ user.phoneNumber }}</div></li>
                            <li><svg-icon icon-class="department" /> {{ $t('common.belongDepartment') }} <div class="user-right">{{ user.departmentName }}</div></li>
                            <li><svg-icon icon-class="email" /> {{ $t('common.userEmail') }} <div class="user-right">{{ user.email }}</div></li>
                            <li><svg-icon icon-class="date" /> {{ $t('common.createTime') }} <div class="user-right">{{ user.createTime }}</div></li>
                            <li>
                                <svg-icon icon-class="password" /> {{ $t('common.securitySetting') }}
                                <div class="user-right">
                                    <a @click="changePassword">{{ $t('common.changePassword') }}</a>
                                </div>
                            </li>
                            <li>
                                <svg-icon icon-class="more" /> {{ $t('common.moreSetting') }}
                                <div class="user-right">
                                    <a @click="updateProfile">{{ $t('common.changeInfo') }}</a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </el-card>
            </el-col>
            <el-col :xs="24" :sm="24" :md="15" :lg="18" :xl="19" style="height: 100%;">
                <el-card class="box-card" style="height: 100%;" :body-style="{ height: '100%' }">
                    <div slot="header" class="clearfix">
                        <span>{{ $t('common.operateLog') }}</span>
                        <div style="display:inline-block;float: right;cursor: pointer" @click="handleSearch"><i :class="tableLoadingIcon"></i></div>
                    </div>
                    <div style="height: 100%;">
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
                </el-card>
            </el-col>
        </el-row>
        <!--弹出对话框，:visible控制其是否显示，fullscreen全屏,append-to-body自身是否插入至body元素上，close-on-click-modal是否可以通过点击modal关闭Dialog，label-width表单域标签的宽度，label-position表单域标签的位置-->
        <el-dialog :title="dialogProfileTitle" :visible.sync="dialogProfileVisible" append-to-body width="880px" >
            <!--ref用于之后引用此表单用于验证，inline一行可放多个表单，rules为验证规则，之后需要在el-form-item上用prop属性即可验证-->
            <el-form ref="dialogFormRef" :model="dialogProfileTemp" :rules="dialogProfileRules" :inline="true"  label-position="left" label-width="140px">
                <el-form-item label="用户名" prop="name">
                    <el-input v-model="dialogProfileTemp.name" disabled placeholder="请输入用户名" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="昵称" prop="nickName">
                    <el-input v-model="dialogProfileTemp.nickName" placeholder="请输入昵称" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="dialogProfileTemp.email" placeholder="请输入邮箱" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="电话号码" prop="phoneNumber">
                    <el-input v-model="dialogProfileTemp.phoneNumber" placeholder="请输入电话号码" style="width: 260px;"/>
                </el-form-item>
                <el-form-item label="性别">
                    <el-select v-model="dialogProfileTemp.sex" placeholder="请选择性别" style="width: 260px;">
                        <el-option
                            v-for="key in Object.keys(systemUserSexDictionary)"
                            :key="key"
                            :label="systemUserSexDictionary[key]"
                            :value="key">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="dialogProfileTemp.remark" type="textarea" max="255" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入备注" style="width: 675px;"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogProfileVisible = false">取消</el-button>
                <el-button v-if="dialogProfileStatus!==0" type="primary" :loading="dialogProfileConfirmButtonLoading" @click="handleDialogConfirm">确定</el-button>
            </div>
        </el-dialog>
        <!--弹出对话框，:visible控制其是否显示，fullscreen全屏,append-to-body自身是否插入至body元素上，close-on-click-modal是否可以通过点击modal关闭Dialog，label-width表单域标签的宽度，label-position表单域标签的位置-->
        <el-dialog :title="dialogChangePasswordTitle" :visible.sync="dialogChangePasswordVisible" append-to-body width="880px" >
            <!--ref用于之后引用此表单用于验证，inline一行可放多个表单，rules为验证规则，之后需要在el-form-item上用prop属性即可验证-->
            <el-form ref="dialogChangePasswordFormRef" :model="dialogChangePasswordTemp" :rules="dialogChangePasswordRules"  label-position="right" label-width="140px">
                <el-form-item label="原密码" prop="oldPassword">
                    <el-input v-model="dialogChangePasswordTemp.oldPassword"/>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                    <el-input v-model="dialogChangePasswordTemp.newPassword"/>
                </el-form-item>
                <el-form-item label="再次新密码" prop="againNewPassword">
                    <el-input v-model="dialogChangePasswordTemp.againNewPassword"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogChangePasswordVisible = false">取消</el-button>
                <el-button v-if="dialogChangePasswordStatus!==0" type="primary" @click="handleDialogChangePasswordConfirm" :loading="dialogChangePasswordConfirmButtonLoading">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    // 工具
    import {encryptMD5}from '@/utils/encryptUtil'

    // 组件
    import store from "@/store";
    import { mapGetters } from 'vuex'

    // API
    import * as SystemUserApi from '@/api/systemUser'
    import * as SystemLogApi from '@/api/systemLog'

    export default {
        name: "Profile",
        computed: {
            ...mapGetters([
                'user'
            ]),
            avatarUrl(){
                return this.$store.getters.user.avatarUrl
            },
        },
        data() {
            const validatePassword = (rule, value, callback) => {
                if(this.dialogChangePasswordTemp.newPassword!==this.dialogChangePasswordTemp.againNewPassword) {
                    callback(new Error("两次输入的密码不一致!"))
                } else {
                    callback()
                }
            }
            return{
                // 第三方

                // 表格上方的操作栏
                tableLoadingIcon:'el-icon-refresh-right',
                tableLoading: true,
                tableList: [], // 表格列表

                // 分页
                tableListTotal: 0, // 表格列表总数（为服务器总数不是list个数）
                tableListParam: { // 查询参数
                    pageNum: 1, // 当前页
                    pageSize: this.Config.pageSize // 页大小
                },

                // 对话框
                dialogProfileStatus:0, // 修改资料对话框状态，0为查看，1为增加，2为修改
                dialogProfileTitle: '', // 修改资料对话框标题,可为增加、修改2种
                dialogProfileVisible: false, // 修改资料对话框是否可见,一开始是关闭的,点击之后才打开
                dialogProfileTemp: { // 修改资料对话框表单中间数据
                    name: '',
                    nickName:'',
                    email: '',
                    phoneNumber: '',
                    sex:'',
                    remark: '',
                },
                dialogProfileRules: { // 修改资料对话框验证规则
                    name:[{ required: true, message: '不能为空!', trigger: 'blur' }, {max: 255, message: '最长255位!', trigger: 'change' }],
                    nickName:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    email: [{required:false, type: 'email', message: '格式错误!', trigger: 'blur' }],
                    phoneNumber:[{require:false,message:'最长255位!',trigger:'change',max:255}],
                    sex:[{ required: true, message: '不能为空!', trigger: 'blur' }],
                    remark:[{require:false,message:'最长255位!',trigger:'change',max:255}]
                },
                dialogProfileConfirmButtonLoading:false,
                dialogChangePasswordStatus:0, // 修改密码对话框状态，0为查看，1为增加，2为修改
                dialogChangePasswordTitle: '', // 修改密码对话框标题,可为增加、修改2种
                dialogChangePasswordVisible: false, // 修改对话框是否可见,一开始是关闭的,点击之后才打开
                dialogChangePasswordTemp: { // 修改对话框表单中间数据
                    oldPassword:'',
                    newPassword:'',
                    againNewPassword:''
                },
                dialogChangePasswordRules: { // 修改对话框验证规则
                    oldPassword:[{ required: true, message: '请输入原密码', trigger: 'blur' }],
                    newPassword:[{ required: true, message: '请输入新密码', trigger: 'blur' }],
                    againNewPassword:[{ required: true, message: '请输入再次新密码', trigger: 'blur' },{trigger: ['blur', 'change'], validator: validatePassword}],
                },
                dialogChangePasswordConfirmButtonLoading:false,

                // 辅助
                systemLogTypeDictionary: {},
                systemUserSexDictionary: {},
            }
        },
        created() {
            // 不能直接在初始化systemEnableDictionary时从Vuex拿，会造成Vuex没有数据时的报错
            this.systemLogTypeDictionary=this.$store.getters.dictionary.systemLogType
            this.systemUserSexDictionary=this.$store.getters.dictionary.systemUserSex
            // 一开始则获取数据
            this.handleSearch()
        },
        methods: {
            // 第三方
            // 表格上方的操作栏
            handleSearch() {
                this.tableLoadingIcon='el-icon-loading'
                this.tableLoading = true
                SystemLogApi.searchPage({
                    username: this.user.name,
                    logType: '',
                    pageNum: this.tableListParam.pageNum,
                    pageSize: this.tableListParam.pageSize
                }).then(res => {
                    this.tableList = res.records
                    this.tableListTotal = res.total
                    this.tableLoadingIcon='el-icon-refresh-right'
                    this.tableLoading = false
                }).catch(error => {
                    this.tableLoadingIcon='el-icon-refresh-right'
                    this.tableLoading = false
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

            // 对话框
            avatarUploadOnChange(file){
                let form = new FormData()
                form.append('userId',this.user.id)
                form.append('file', file.raw, file.name)
                SystemUserApi.uploadAvatar(
                    form
                ).then(res=>{
                    this.$store.getters.user.avatarUrl=res
                }).catch(error=>{
                })
            },

            updateProfile(){
                this.dialogProfileStatus=2 // 2为修改
                this.dialogProfileVisible = true
                this.dialogProfileTitle = "修改资料"
                this.dialogProfileTemp = { // 复制表格行数据到dialogTemp
                    name:this.user.name,
                    nickName:this.user.nickName,
                    email: this.user.email,
                    phoneNumber: this.user.phoneNumber,
                    sex:this.user.sex,
                    remark: this.user.remark,
                }

                this.$nextTick(() => {
                    this.$refs['dialogFormRef'].clearValidate() // 清除验证
                })
            },
            handleUpdateProfile() {
                this.$refs['dialogFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogProfileConfirmButtonLoading = true
                        SystemUserApi.updateProfile(
                            this.dialogProfileTemp
                        ).then(res => {
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            });
                            this.dialogProfileConfirmButtonLoading = false
                            this.dialogProfileVisible = false
                            // 修改个人信息之后需要重新获取用户信息
                            SystemUserApi.getCurrent().then(res=>{
                                store.dispatch('user/setUser',res)
                            }).catch(error=>{
                            })
                        }).catch(error => {
                            this.dialogProfileConfirmButtonLoading = false
                        })
                    }
                })
            },
            changePassword(){
                this.dialogChangePasswordStatus=2 // 2为修改
                this.dialogChangePasswordVisible = true
                this.dialogChangePasswordTitle = "修改密码"
                // 重置数据
                this.dialogChangePasswordTemp = {
                    oldPassword:'',
                    newPassword:'',
                    againNewPassword:''
                }

                this.$nextTick(() => {
                    this.$refs['dialogChangePasswordFormRef'].clearValidate() // 清除验证
                })
            },
            handleChangePassword() {
                this.$refs['dialogChangePasswordFormRef'].validate((valid) => {
                    if (valid) {
                        this.dialogChangePasswordConfirmButtonLoading = true
                        SystemUserApi.changePassword({
                            oldPassword:encryptMD5(this.dialogChangePasswordTemp.oldPassword),
                            newPassword:encryptMD5(this.dialogChangePasswordTemp.newPassword),
                            againNewPassword:encryptMD5(this.dialogChangePasswordTemp.againNewPassword)
                        }).then(res => {
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            });
                            this.dialogChangePasswordConfirmButtonLoading = false
                            this.dialogChangePasswordVisible = false
                        }).catch(error => {
                            this.dialogChangePasswordConfirmButtonLoading = false
                        })
                    }
                })
            },

            // 对话框
            handleDialogChangePasswordConfirm() {
                if(this.dialogChangePasswordStatus===2){
                    this.handleChangePassword()
                }
            },
            handleDialogConfirm() {
                if(this.dialogProfileStatus===2){
                    this.handleUpdateProfile()
                }
            },
        }
    }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
    .user-info {
        padding-left: 0px;
        list-style: none;
        li{
            border-bottom: 1px solid #F0F3F4;
            border-top: 1px solid #F0F3F4;
            padding: 11px 0px;
            font-size: 13px;
        }
        .user-right {
            float: right;
            a{
                color: #317EF3;
            }
        }
    }

    .avatar-upload-preview {
        position: absolute;
        top: 50%;
        transform: translate(50%, -50%);
        width: 180px;
        height: 180px;
        border-radius: 50%;
        box-shadow: 0 0 4px #ccc;
        overflow: hidden;
    }
</style>
