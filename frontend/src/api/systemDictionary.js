import requestUtil from '@/utils/requestUtil'

export function add(data) {
    return requestUtil({
        url: '/system/dictionary/add',
        method: 'post',
        data: data
    })
}

export function _delete(data) {
    return requestUtil({
        url: '/system/dictionary/delete',
        method: 'delete',
        data: data
    })
}

export function update(data) {
    return requestUtil({
        url: '/system/dictionary/update',
        method: 'put',
        data: data
    })
}

export function searchPage(data) {
    return requestUtil({
        url: '/system/dictionary/search/page',
        method: 'post',
        data: data
    })
}
