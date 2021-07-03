import requestUtil from '@/utils/requestUtil'

export function add(data) {
    return requestUtil({
        url: '/system/permission/add',
        method: 'post',
        data: data
    })
}

export function _delete(data) {
    return requestUtil({
        url: '/system/permission/delete',
        method: 'delete',
        data: data
    })
}

export function update(data) {
    return requestUtil({
        url: '/system/permission/update',
        method: 'put',
        data: data
    })
}

export function getCurrent() {
    return requestUtil({
        url: '/system/permission/get/current',
        method: 'get'
    })
}

export function searchPage(data) {
    return requestUtil({
        url: '/system/permission/search/page',
        method: 'post',
        data: data
    })
}
