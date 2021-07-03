import store from '@/store'

/**
 * 判断是否有权限，见@/views/permission/directive.vue
 */
export default function hasAuthority(value) {
    if (value) {
        const permissions = store.getters.permissions

        const hasAuthority = permissions.some(permission => {
            return permission===value
        })

        return hasAuthority
    } else {
        console.error(`need roles! Like v-permission="'USER_ADD'"`)
        return false
    }
}
