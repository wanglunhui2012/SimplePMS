import Vue from 'vue'
import store from '@/store'
import {isString, isArray} from '@/utils/validateUtil'
import Config from '@/config/index'

// 可以在settings.js中设置
// errorLog:'production'或者['production', 'development']
const {errorLog: needErrorLog} = Config

function checkNeed() {
    const env = process.env.NODE_ENV
    if (isString(needErrorLog)) {
        return env === needErrorLog
    }
    if (isArray(needErrorLog)) {
        return needErrorLog.includes(env)
    }
    return false
}

if (checkNeed()) {
    Vue.config.errorHandler = function (err, vm, info, a) {
        // 别问为什么用Vue.nextTick,这只是个hack，详见https://forum.vuejs.org/t/dispatch-in-vue-config-errorhandler-has-some-problem/23500
        Vue.nextTick(() => {
            store.dispatch('errorLog/addErrorLog', {
                err,
                vm,
                info,
                url: window.location.href
            })
            console.error(err, info)
        })
    }
}
