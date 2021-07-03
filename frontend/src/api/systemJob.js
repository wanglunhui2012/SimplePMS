import requestUtil from '@/utils/requestUtil'

export function add(data) {
    return requestUtil({
        url: '/system/job/add',
        method: 'post',
        data: data
    })
}

export function _delete(data) {
    return requestUtil({
        url: '/system/job/delete',
        method: 'delete',
        data: data
    })
}

export function update(data) {
    return requestUtil({
        url: '/system/job/update',
        method: 'put',
        data: data
    })
}

export function searchPage(data) {
    return requestUtil({
        url: '/system/job/search/page',
        method: 'post',
        data: data
    })
}

export function getCurrentJobIdList() {
    return requestUtil({
        url: '/system/job/get/current/jobIdList',
        method: 'get'
    })
}
