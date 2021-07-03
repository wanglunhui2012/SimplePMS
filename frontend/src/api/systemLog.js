import requestUtil from '@/utils/requestUtil'

export function searchPage(data) {
    return requestUtil({
        url: '/system/log/search/page',
        method: 'post',
        data:data
    })
}
