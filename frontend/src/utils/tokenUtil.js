import Cookies from 'js-cookie'
import Config from '@/config'

const tokenKey = Config.tokenKey

export function getToken() {
    return Cookies.get(tokenKey)
}

export function setToken(token) {
    return Cookies.set(tokenKey, token)
}

export function removeToken() {
    return Cookies.remove(tokenKey)
}
