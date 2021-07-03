<template>
    <div class="login-container">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">
            <div class="title-container">
                <h3 class="title">{{ $t('login.title') }}</h3>
                <lang-select class="set-language"/>
            </div>
            <el-form-item prop="username">
                <el-row>
                    <el-col :span="2">
                        <span class="svg-container"><svg-icon icon-class="user"/></span>
                    </el-col>
                    <el-col :span="22">
                        <el-input
                            ref="username"
                            v-model="loginForm.username"
                            :placeholder="$t('login.username')"
                            name="username"
                            type="text"
                            tabindex="1"
                            autocomplete="on"/>
                    </el-col>
                </el-row>
            </el-form-item>
            <el-tooltip v-model="showCapsLockTip" :content="$t('login.capsLock')" placement="right" manual>
                <el-form-item prop="password">
                    <el-row>
                        <el-col :span="2">
                            <span class="svg-container"><svg-icon icon-class="password"/></span>
                        </el-col>
                        <el-col :span="20">
                            <el-input
                                :key="passwordType"
                                ref="password"
                                v-model="loginForm.password"
                                :type="passwordType"
                                :placeholder="$t('login.password')"
                                name="password"
                                tabindex="2"
                                autocomplete="on"
                                @keyup.native="checkCapsLock"
                                @keyup.enter.native="handleLogin"/>
                        </el-col>
                        <el-col :span="2">
                            <span class="show-password" @click="setPasswordType"><svg-icon :icon-class="passwordType === 'password' ? 'eye-close' : 'eye-open'"/></span>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-tooltip>
            <el-form-item prop="captcha">
                <el-row>
                    <el-col :span="2">
                        <span class="svg-container"><svg-icon icon-class="captcha"/></span>
                    </el-col>
                    <el-col :span="16">
                        <el-input
                            v-model="loginForm.captcha"
                            ref="captcha"
                            :placeholder="$t('login.captcha')"
                            name="captcha"
                            type="text"
                            tabindex="3"
                            autocomplete="off"
                            @keyup.enter.native="handleLogin"/>
                    </el-col>
                    <el-col :span="6">
                        <img :src="captchaUrl" @click="getCaptchaInfo" style="cursor: pointer;vertical-align:middle">
                    </el-col>
                </el-row>
            </el-form-item>
            <el-form-item style="width:100%;">
                <el-button :loading="loading" size="medium" type="primary" tabindex="4" style="width:100%;" @click.native.prevent="handleLogin">
                    <span v-if="!loading">{{ $t('login.login') }}</span>
                    <span v-else>{{ $t('login.logining') }}</span>
                </el-button>
            </el-form-item>
        </el-form>

        <div id="el-login-footer">
            <span v-html="footerHtml"/>
        </div>
    </div>
</template>

<script>
    import Config from '@/config'
    import {encryptMD5}from '@/utils/encryptUtil'
    import LangSelect from '@/components/LangSelect'
    import * as SystemAuthenticationApi from '@/api/systemAuthentication'

    export default {
        name: 'Login',
        components: {LangSelect},
        data() {
            const validateUsername = (rule, value, callback) => { // 验证用户名
                if (!value||value.trim().length===0) {
                    callback(new Error(this.$t('login.usernameTip')))
                } else {
                    callback()
                }
            }
            const validatePassword = (rule, value, callback) => { // 验证密码
                if (!value||value.trim().length===0) {
                    callback(new Error(this.$t('login.passwordTip')))
                } else {
                    callback()
                }
            }
            const validateCaptcha = (rule, value, callback) => { // 验证验证码
                if (!value||value.trim().length!==4) {
                    callback(new Error(this.$t('login.captchaTip')))
                } else {
                    callback()
                }
            }
            return {
                loginForm: { // 登录表单
                    username: 'admin',
                    password: '123456',
                    captcha: '',
                    uuid: ''
                },
                loginRules: { // 登录验证规则
                    username: [{required: true, trigger: 'blur', validator: validateUsername}],
                    password: [{required: true, trigger: 'blur', validator: validatePassword}],
                    captcha: [{required: true, trigger: 'blur', validator: validateCaptcha}],
                },
                showCapsLockTip: false, // 是否显示大写锁定提示
                passwordType: 'password', // 密码类型，用于是否可以查看密码
                captchaUrl: Config.loadingImage, // 验证码Url
                loading: false, // 是否正在登录
                redirect: undefined, // 登录后的重定向地址
                otherQuery: {}, // 查询参数
                footerHtml:Config.footerHtml // 底部信息
            }
        },
        watch: {
            $route: {
                handler: function (route) { // 监听路由变化，获取redirect和otherQuery
                    const query = route.query
                    if (query) {
                        this.redirect = query.redirect
                        this.otherQuery = this.getOtherQuery(query)
                    }
                },
                immediate: true
            }
        },
        created() {
            this.getCaptchaInfo() // 获取验证码
        },
        mounted() {
            this.$refs.username.focus() // 用户名获取焦点
        },
        methods: {
            getOtherQuery(query) {
                return Object.keys(query).reduce((acc, cur) => {
                    if (cur !== 'redirect') {
                        acc[cur] = query[cur]
                    }
                    return acc
                }, {})
            },
            checkCapsLock({shiftKey, key} = {}) {
                if (key === 'CapsLock') { // 如果按下了CapsLock键
                    this.showCapsLockTip = !this.showCapsLockTip
                }
            },
            setPasswordType() {
                if (this.passwordType === 'password') {
                    this.passwordType = ''
                } else {
                    this.passwordType = 'password'
                }
                this.$nextTick(() => {
                    this.$refs.password.focus()
                })
            },
            getCaptchaInfo() {
                SystemAuthenticationApi.getCaptcha().then(res => {
                    this.captchaUrl = res.captcha
                    this.loginForm.uuid = res.uuid
                })
            },
            handleLogin() {
                this.$refs.loginForm.validate(valid => {
                    if (valid) {
                        this.loading = true

                        this.$store.dispatch('user/login',{
                            username:this.loginForm.username,
                            password:encryptMD5(this.loginForm.password),
                            uuid:this.loginForm.uuid,
                            captcha:this.loginForm.captcha
                        }).then(() => {
                            this.loading = false
                            this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
                        }).catch((error) => {
                            this.loading = false
                            this.loginForm.captcha=''
                            this.$refs.captcha.focus()
                            this.getCaptchaInfo()
                        })
                    }
                })
            }
        }
    }
</script>

<style lang="scss">
    $bg: #283443;
    $light_gray: #fff;
    $cursor: #fff;

    @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
        .login-container .el-input input {
            color: $cursor;
        }
    }


    .login-container {
        .el-input {
            display: inline-block;
            height: 47px;
            width: 85%;

            input {
                background: transparent;
                border: 0px;
                -webkit-appearance: none;
                border-radius: 0px;
                padding: 12px 5px 12px 15px;
                color: $light_gray;
                height: 47px;
                caret-color: $cursor;

                &:-webkit-autofill {
                    box-shadow: 0 0 0px 1000px $bg inset !important;
                    -webkit-text-fill-color: $cursor !important;
                }
            }
        }

        .el-form-item {
            border: 1px solid rgba(255, 255, 255, 0.1);
            background: rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            color: #454545;
        }
    }
</style>

<style lang="scss" scoped>
    $bg: #2d3a4b;
    $dark_gray: #889aa4;
    $light_gray: #eee;

    .login-container {
        min-height: 100%;
        width: 100%;
        background-color: $bg;
        overflow: hidden;

        .login-form {
            position: relative;
            width: 520px;
            max-width: 100%;
            padding: 160px 35px 0;
            margin: 0 auto;
            overflow: hidden;
        }

        .tips {
            font-size: 14px;
            color: #fff;
            margin-bottom: 10px;

            span {
                &:first-of-type {
                    margin-right: 16px;
                }
            }
        }

        .svg-container {
            padding: 6px 5px 6px 15px;
            color: $dark_gray;
            vertical-align: middle;
            width: 30px;
            display: inline-block;
        }

        .title-container {
            position: relative;

            .title {
                font-size: 26px;
                color: $light_gray;
                margin: 0px auto 40px auto;
                text-align: center;
                font-weight: bold;
            }

            .set-language {
                color: #fff;
                position: absolute;
                top: 3px;
                font-size: 18px;
                right: 0px;
                cursor: pointer;
            }
        }

        .show-password {
            position: absolute;
            right: 10px;
            top: 7px;
            font-size: 16px;
            color: $dark_gray;
            cursor: pointer;
            user-select: none;
        }

        .thirdparty-button {
            position: absolute;
            right: 0;
            bottom: 6px;
        }

        @media only screen and (max-width: 470px) {
            .thirdparty-button {
                display: none;
            }
        }

        #el-login-footer {
            height: 40px;
            line-height: 40px;
            position: fixed;
            bottom: 0;
            width: 100%;
            text-align: center;
            color: #fff;
            font-family: Arial;
            font-size: 12px;
            letter-spacing: 1px;
        }
    }
</style>
