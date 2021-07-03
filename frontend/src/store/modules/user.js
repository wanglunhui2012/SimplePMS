import { getToken,setToken,removeToken} from '@/utils/tokenUtil'
import router, {constantRoutes, generateAsyncRoutes, resetRouter} from '@/router'
import * as SystemAuthenticationApi from '@/api/systemAuthentication'
import * as SystemUserApi from "@/api/systemUser";
import store from "@/store";
import * as SystemPermissionApi from "@/api/systemPermission";
import * as SystemMenuApi from "@/api/systemMenu";
import * as SystemDictionaryDetailApi from "@/api/systemDictionaryDetail";
import axios from 'axios'

const initState=()=>{
    return {
        token:getToken(),
        user:{
        },
        permissions:[],
        menus:[],
        routes:[].concat(constantRoutes), // Router默认有固定路由，所以Store中的路由一开始也应该有固定路由（Router中用于路由，Store中用于搜索）
        dictionary:{
        },
        hasGetAllInfo:false
    }
}

const user={
    namespaced: true,
    state: initState(),
    mutations: {
        SETTOKEN:(state, token) => {
            state.token = token
        },
        SETUSER: (state, user) => {
            state.user = user
        },
        SETPERMISSIONS: (state, permissions) => {
            state.permissions = permissions
        },
        SETMENUS: (state, menus) => {
            state.menus = menus
        },
        ADDROUTES: (state, routes) => {
            state.routes=state.routes.concat(routes)
        },
        RESETROUTES: (state) => {
            state.routes=[].concat(constantRoutes)
        },
        SETDICTIONARY: (state, dictionary) => {
            //state.dictionary[obj.key]=obj.value
            state.dictionary=dictionary
        },
        SETHASGETALLINFO: (state, hasGetAllInfo) => {
            state.hasGetAllInfo = hasGetAllInfo
        },
        RESET: (state) => {
            Object.assign(state,initState())
        },
    },
    actions:{
        setToken({commit}, token) {
            commit('SETTOKEN', token)
        },
        setUser({commit}, user) {
            commit('SETUSER', user)
        },
        setPermissions({commit}, permissions) {
            commit('SETPERMISSIONS', permissions)
        },
        setMenus({commit}, menus) {
            commit('SETMENUS', menus)
        },
        addRoutes({ commit }, routes) {
            commit('ADDROUTES', routes)
        },
        resetRoutes({ commit }) {
            commit('RESETROUTES')
        },
        setDictionary({commit}, obj) {
            commit('SETDICTIONARY', obj)
        },
        setHasGetAllInfo({commit}, hasGetAllInfo) {
            commit('SETHASGETALLINFO', hasGetAllInfo)
        },
        reset({commit}) {
            commit('RESET')
        },
        login({commit},loginForm){
            return new Promise((resolve, reject) => {
                SystemAuthenticationApi.login(loginForm).then(res=>{
                    commit('SETTOKEN', res.token)
                    setToken(res.token)
                    resolve()
                }).catch(error=>{
                    reject(error)
                })
            })
        },
        logout({commit, state, dispatch}) {
            return new Promise((resolve, reject) => {
                SystemAuthenticationApi.logout().then(res=>{
                    commit('RESET')
                    removeToken()
                    resetRouter()
                    // 重置访问过的和缓存的页面，修复https://github.com/PanJiaChen/vue-element-admin/issues/2485
                    dispatch('tagsView/delAllViews', null, { root: true })
                    resolve(res)
                }).catch((error)=>{
                    commit('RESET')
                    removeToken()
                    resetRouter()
                    // 重置访问过的和缓存的页面，修复https://github.com/PanJiaChen/vue-element-admin/issues/2485
                    dispatch('tagsView/delAllViews', null, { root: true })
                    reject(error)
                })
            })
        },
        getInfo({commit,state}) {
            return new Promise((resolve, reject) => {
                /*SystemUserApi.getCurrent().then(res=>{
                    commit('SETUSER',res)
                }).catch(error=>{
                    reject(error)
                })

                SystemPermissionApi.getCurrent().then(res=>{
                    commit('SETPERMISSIONS',res)
                }).catch(error=>{
                    reject(error)
                })

                SystemMenuApi.getNavigation().then(res =>{
                    commit('SETMENUS',res)
                    const asyncRouters=generateAsyncRoutes(res)
                    commit('ADDROUTES',asyncRouters)
                    router.addRoutes(asyncRouters)
                }).catch(error=>{
                    reject(error)
                })

                SystemDictionaryDetailApi.searchByNameList([]).then(res=>{
                    commit('SETDICTIONARY',res)
                }).catch(error=>{
                    reject(error)
                })

                commit('SETHASGETALLINFO',true)

                resolve()*/

                // 改为所有信息都获取完成才进行下一步，防止vuex一些变量初始状态为undefined
                Promise.all([
                    SystemUserApi.getCurrent(),
                    SystemPermissionApi.getCurrent(),
                    SystemMenuApi.getNavigation(),
                    SystemDictionaryDetailApi.searchByNameList([])
                ]).then(res=>{
                    commit('SETUSER',res[0])

                    commit('SETPERMISSIONS',res[1])

                    commit('SETMENUS',res[2])
                    const asyncRouters=generateAsyncRoutes(res[2])
                    commit('ADDROUTES',asyncRouters)
                    router.addRoutes(asyncRouters)

                    commit('SETDICTIONARY',res[3])

                    commit('SETHASGETALLINFO',true)

                    resolve()
                }).catch(error=>{
                    reject(error)
                })
            })
        },
    }
}

export default user
