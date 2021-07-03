import requestUtil from '@/utils/requestUtil'

export function add(data) {
    return requestUtil({
        url: '/system/role/add',
        method: 'post',
        data: data
    })
}

export function _delete(data) {
    return requestUtil({
        url: '/system/role/delete',
        method: 'delete',
        data: data
    })
}

export function update(data) {
    return requestUtil({
        url: '/system/role/update',
        method: 'put',
        data: data
    })
}

export function searchPage(data) {
    return requestUtil({
        url: '/system/role/search/page',
        method: 'post',
        data: data
    })
}

export function getCurrentRoleIdList() {
    return requestUtil({
        url: '/system/role/get/current/roleIdList',
        method: 'get'
    })
}
