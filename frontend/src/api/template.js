import requestUtil from '@/utils/requestUtil'

export function fetchList(query) {
    return requestUtil({
        url: '/vue-element-admin/article/list',
        method: 'get',
        params: query
    })
}

export function fetchArticle(id) {
    return requestUtil({
        url: '/vue-element-admin/article/detail',
        method: 'get',
        params: {id}
    })
}

export function updateRole(id, data) {
    return requestUtil({
        url: `/vue-element-admin/role/${id}`,
        method: 'put',
        data
    })
}

export function getToken() {
    return requestUtil({
        url: '/qiniu/upload/token', // 假地址 自行替换
        method: 'get'
    })
}
