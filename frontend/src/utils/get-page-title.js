import Config from '@/config'
import i18n from '@/lang'

const title = Config.systemName || 'Vue Element Admin'

export default function getPageTitle(key) {
    const hasKey = i18n.te(`route.${key}`)
    if (hasKey) {
        const pageName = i18n.t(`route.${key}`)
        return `${pageName} - ${title}`
    }
    return `${title}`
}
