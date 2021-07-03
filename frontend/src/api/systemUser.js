import requestUtil from '@/utils/requestUtil'
import requestFileUtil from '@/utils/requestFileUtil'

export function add(data) {
    return requestUtil({
        url: '/system/user/add',
        method: 'post',
        data: data
    })
}

export function _delete(data) {
    return requestUtil({
        url: '/system/user/delete',
        method: 'delete',
        data: data
    })
}

export function update(data) {
    return requestUtil({
        url: '/system/user/update',
        method: 'put',
        data: data
    })
}

export function searchPage(data) {
    return requestUtil({
        url: '/system/user/search/page',
        method: 'post',
        data: data
    })
}

export function changePassword(data) {
    return requestUtil({
        url: '/system/user/changePassword',
        method: 'put',
        data: data
    })
}

export function uploadAvatar(form) {
    return requestFileUtil({
        url: '/system/user/upload/avatar',
        method: 'post',
        data: form
    })
}

export function updateProfile(data) {
    return requestUtil({
        url: '/system/user/update/profile',
        method: 'post',
        data: data
    })
}

export function getCurrent() {
    return requestUtil({
        url: '/system/user/get/current',
        method: 'get'
    })
}

export function getTreeDepartmentJob(data) {
    return requestUtil({
        url: '/system/user/get/tree/departmentJob',
        method: 'post',
        data: data
    })
}
