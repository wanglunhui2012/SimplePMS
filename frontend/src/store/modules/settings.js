import variables from '@/styles/element-variables.scss'
import Config from '@/config'

const {menuOpenUnique, showSettings, tagsView, fixedHeader, sidebarLogo, supportPinyinSearch} = Config

const settings={
    namespaced: true,
    state:{
        theme: variables.theme,
        menuOpenUnique:menuOpenUnique,
        showSettings: showSettings,
        tagsView: tagsView,
        fixedHeader: fixedHeader,
        sidebarLogo: sidebarLogo,
        supportPinyinSearch: supportPinyinSearch
    },
    mutations:{
        CHANGE_SETTING: (state, {key, value}) => {
            // eslint-disable-next-line no-prototype-builtins
            if (state.hasOwnProperty(key)) {
                state[key] = value
            }
        }
    },
    actions:{
        changeSetting({commit}, data) {
            commit('CHANGE_SETTING', data)
        }
    }
}

export default settings

