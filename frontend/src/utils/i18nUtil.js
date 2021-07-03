import i18n from '@/lang'

export function getInternational(text) {
    if(!text){
        return ''
    }

    const hasKey = i18n.te(text)

    if (hasKey) {
        const translatedText = i18n.t(text)
        return translatedText
    }

    return text
}

export function getInternationalFromRoute(s){ // 获取路由router.meta.title的国际化
    if(!s){
        return ''
    }

    const hasKey = i18n.te('route.'+s)

    if (hasKey) {
        // $t来自vue-i18n，在@/lang/index.js中注入
        const translatedTitle = i18n.t('route.'+s)

        return translatedTitle
    }

    return s
}
