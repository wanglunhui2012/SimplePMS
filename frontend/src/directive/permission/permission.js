import store from '@/store'

function checkPermission(el, binding) {
    const {value} = binding
    const permissions = store.getters.permissions

    if (value && value instanceof Array) {
        if (value.length > 0) {
            const hasAuthority = permissions.some(permission => {
                return value.includes(permission)
            })

            if (!hasAuthority) {
                el.parentNode && el.parentNode.removeChild(el)
            }
        }
    } else {
        throw new Error(`need roles! Like v-permission="['admin','editor']"`)
    }
}

export default {
    inserted(el, binding) {
        checkPermission(el, binding)
    },
    update(el, binding) {
        checkPermission(el, binding)
    }
}
