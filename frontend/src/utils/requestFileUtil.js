import axios from 'axios'
import router from '@/router'
import {Notification, MessageBox,Message} from 'element-ui'
import store from '@/store'
import {getToken,removeToken} from '@/utils/tokenUtil'
import Config from '@/config'


const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,

    timeout: Config.timeout,
    headers: {
        'Content-Type': 'multipart/form-data' // contentType不一样而已
    }
})


service.interceptors.request.use(
    config => {
        config.headers[Config.jwtHeader] = 'Bearer ' + (getToken()||'')

        return config
    },
    error => {
        console.log(error)

        return Promise.reject(error)
    }
)


service.interceptors.response.use(
    response => {
        const code = response.status
        if(code===200){
            return response.data
        }else {
            return Promise.reject('不支持的响应状态!')
        }
    },
    error => {
        let code = 0
        try {
            code = error.response.status
        } catch (e) {
            Notification({
                message: error.toString(),
                type: 'error',
                duration: 2500
            })
            return Promise.reject(error)
        }
        if (code === 401) {
            MessageBox.confirm(
                '登录状态已过期，请重新登录!',
                '系统提示',
                {
                    confirmButtonText: '重新登录',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                store.dispatch('user/logout').finally(res=>{
                    location.reload()
                })
            })
        } else if (code === 403) {
            router.push({path: '/403'})

        } else {
            Notification({
                title: error.response.data.message||'网络或服务器错误!',
                type: 'error',
                duration: 2500
            })
        }
        return Promise.reject(error)
    }
)
export default service
