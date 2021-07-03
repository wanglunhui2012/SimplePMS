import requestUtil from '@/utils/requestUtil'

export function add(data) {
    return requestUtil({
        url: '/system/menu/add',
        method: 'post',
        data: data
    })
}

export function _delete(data) {
    return requestUtil({
        url: '/system/menu/delete',
        method: 'delete',
        data: data
    })
}

export function update(data) {
    return requestUtil({
        url: '/system/menu/update',
        method: 'put',
        data: data
    })
}

export function getNavigation() {
    return requestUtil({
        url: '/system/menu/get/navigation',
        method: 'get'
    })
}

export function searchTree(data) {
    return requestUtil({
        url: '/system/menu/search/tree',
        method: 'post',
        data:data
    })
}
