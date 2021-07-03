import requestUtil from '@/utils/requestUtil'

export function getCaptcha() {
    return requestUtil({
        url: '/system/authentication/captcha',
        method: 'get'
    })
}

export function login(data) {
    return requestUtil({
        url: '/system/authentication/login',
        method: 'post',
        data: data
    })
}

export function logout() {
    return requestUtil({
        url: '/system/authentication/logout',
        method: 'delete'
    })
}
