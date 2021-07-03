import requestUtil from '@/utils/requestUtil'

export function getDashboard() {
    return requestUtil({
        url: '/system/common/get/dashboard',
        method: 'get'
    })
}
