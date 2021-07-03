import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'
import Config from '@/config'
import {Message} from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken} from '@/utils/tokenUtil'
import {getInternationalFromRoute} from '@/utils/i18nUtil'

import * as SystemUserApi from '@/api/systemUser'
import * as SystemPermissionApi from '@/api/systemPermission'
import * as SystemMenuApi from '@/api/systemMenu'
import * as SystemDictionaryDetailApi from '@/api/systemDictionaryDetail'

Vue.use(Router)

import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * 固定路由
 */
export const constantRoutes = [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('@/views/redirect/index')
            }
        ]
    },
    {
        path: '/login',
        component: () => import('@/views/login/index'),
        meta: {title: 'login'},
        hidden: true,
    },
    {
        path: '/auth-redirect',
        component: () => import('@/views/redirect'),
        hidden: true
    },
    {
        path: '/404',
        component: () => import('@/views/error-page/404'),
        hidden: true
    },
    {
        path: '/401',
        component: () => import('@/views/error-page/401'),
        hidden: true
    },
    {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        children: [
            {
                path: 'dashboard',
                component: () => import('@/views/dashboard/index'),
                name: 'Dashboard',
                meta: {title: 'dashboard', icon: 'dashboard', affix: true}
            }
        ]
    },
    {
        path: '/profile',
        component: Layout,
        redirect: '/profile/index',
        hidden: true,
        children: [
            {
                path: 'index',
                component: () => import('@/views/profile/index'),
                name: 'Profile',
                meta: {title: 'profile', icon: 'user', noCache: false}
            }
        ]
    }
]

const createRouter = () => new Router({
    // mode: 'history', // 需要服务支持
    scrollBehavior: () => ({y: 0}),
    routes: constantRoutes // 默认固定路由
})

const router = createRouter()

// 详见https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher // reset router
}

NProgress.configure({showSpinner: false}) // 配置NProgress

const whiteList = ['/login', '/auth-redirect'] // 非redirect白名单

router.beforeEach(async (to, from, next) => {
    document.title = getInternationalFromRoute(to.meta.title) + ' - ' + Config.systemName
    // 开始加载
    NProgress.start()

    const hasToken = getToken()
    if (hasToken) {
        if (to.path === '/login') {
            // 如果已登录，重定向到主页
            next({path: '/'})
            NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
        } else {
            if(store.getters.hasGetAllInfo){ // 判断是否已经获取用户信息
                next()
            }else{
                try{
                    await store.dispatch('user/getInfo')

                    next({...to, replace: true})
                }catch (error) {
                    await store.dispatch('user/logout')
                    Message.error(error || 'Has Error')
                    next(`/login?redirect=${to.path}`)
                    NProgress.done()
                }
            }
        }
    } else {
        if (whiteList.indexOf(to.path) !== -1) { // 访问白名单，直接可以前往
            next()
        } else { // 重定向到登录页面
            next(`/login?redirect=${to.path}`)
            NProgress.done()
        }
    }
})

router.afterEach(() => {
    NProgress.done()
})

export const generateAsyncRoutes = (routers) => {
    const asyncRouters = routers.filter(router => {
        if (router.component) {
            if (router.component === 'Layout') {
                router.component = Layout
            } else {
                const component = router.component
                //router.component = () => import(`@/views/${component}`)
                router.component = (resolve) => require([`@/views/${component}`], resolve)
            }
        }
        if (router.children && router.children.length) {
            router.children = generateAsyncRoutes(router.children)
        }
        return true
    })

    // 404页面必须放在最后!!!
    asyncRouters.push({ path: '*', redirect: '/404', hidden: true })
    return asyncRouters
}

export default router
