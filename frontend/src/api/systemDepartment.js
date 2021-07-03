import requestUtil from '@/utils/requestUtil'

export function add(data) {
    return requestUtil({
        url: '/system/department/add',
        method: 'post',
        data: data
    })
}

export function _delete(data) {
    return requestUtil({
        url: '/system/department/delete',
        method: 'delete',
        data: data
    })
}

export function update(data) {
    return requestUtil({
        url: '/system/department/update',
        method: 'put',
        data: data
    })
}

export function searchTree(data) {
    return requestUtil({
        url: '/system/department/search/tree',
        method: 'post',
        data:data
    })
}
