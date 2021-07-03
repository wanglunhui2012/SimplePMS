import requestUtil from '@/utils/requestUtil'

export function add(data) {
    return requestUtil({
        url: '/system/dictionaryDetail/add',
        method: 'post',
        data: data
    })
}

export function _delete(data) {
    return requestUtil({
        url: '/system/dictionaryDetail/delete',
        method: 'delete',
        data: data
    })
}

export function update(data) {
    return requestUtil({
        url: '/system/dictionaryDetail/update',
        method: 'put',
        data: data
    })
}

export function searchPage(data) {
    return requestUtil({
        url: '/system/dictionaryDetail/search/page',
        method: 'post',
        data: data
    })
}

export function searchByNameList(data) {
    return requestUtil({
        url: '/system/dictionaryDetail/search/byNameList',
        method: 'post',
        data: data
    })
}
