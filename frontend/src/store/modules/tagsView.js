import router from '@/router'

const tagsView={
    namespaced:true,
    state:{
        visitedViews: [],
        cachedViews: []
    },
    mutations:{
        ADD_VISITED_VIEW: (state, view) => {
            if (state.visitedViews.some(v => v.path === view.path)) return
            state.visitedViews.push(
                Object.assign({}, view, {
                    title: view.meta.title || 'no-name'
                })
            )
        },
        ADD_CACHED_VIEW: (state, view) => {
            if (state.cachedViews.includes(view.name)) return
            if (!view.meta.noCache) {
                state.cachedViews.push(view.name)
            }
        },

        DEL_VISITED_VIEW: (state, view) => {
            for (const [i, v] of state.visitedViews.entries()) {
                if (v.path === view.path) {
                    state.visitedViews.splice(i, 1)
                    break
                }
            }
        },
        DEL_CACHED_VIEW: (state, view) => {
            const index = state.cachedViews.indexOf(view.name)
            index > -1 && state.cachedViews.splice(index, 1)
        },

        DEL_OTHERS_VISITED_VIEWS: (state, view) => {
            state.visitedViews = state.visitedViews.filter(v => {
                return v.meta.affix || v.path === view.path
            })
        },
        DEL_OTHERS_CACHED_VIEWS: (state, view) => {
            const index = state.cachedViews.indexOf(view.name)
            if (index > -1) {
                state.cachedViews = state.cachedViews.slice(index, index + 1)
            } else {
                // if index = -1, there is no cached tags
                state.cachedViews = []
            }
        },

        DEL_ALL_VISITED_VIEWS: state => {
            // keep affix tags
            const affixTags = state.visitedViews.filter(tag => tag.meta.affix)
            state.visitedViews = affixTags
        },
        DEL_ALL_CACHED_VIEWS: state => {
            state.cachedViews = []
        },

        UPDATE_VISITED_VIEW: (state, view) => {
            for (let v of state.visitedViews) {
                if (v.path === view.path) {
                    v = Object.assign(v, view)
                    break
                }
            }
        }
    },
    actions:{
        addView({dispatch}, view) {
            dispatch('addVisitedView', view)
            dispatch('addCachedView', view)
        },
        addVisitedView({commit}, view) {
            commit('ADD_VISITED_VIEW', view)
        },
        addCachedView({commit}, view) {
            /*console.log(view)
            if (view.matched && view.matched.length >= 3) {
                commit('ADD_CACHED_VIEW', view.matched[1])
            }
            commit('ADD_CACHED_VIEW', view)*/
            if (view.matched && view.matched.length > 1) {
                for (let i = 1; i < view.matched.length; i++) { // ???0??????Layout?????????
                    commit('ADD_CACHED_VIEW', view.matched[i])
                }
            } else {
                commit('ADD_CACHED_VIEW', view)
            }
            //commit('ADD_CACHED_VIEW', view)
        },

        delView({dispatch, state}, view) {
            return new Promise(resolve => {
                dispatch('delVisitedView', view)
                dispatch('delCachedView', view)
                resolve({
                    visitedViews: [...state.visitedViews],
                    cachedViews: [...state.cachedViews]
                })
            })
        },
        delVisitedView({commit, state}, view) {
            return new Promise(resolve => {
                commit('DEL_VISITED_VIEW', view)
                resolve([...state.visitedViews])
            })
        },
        async delCachedView({dispatch,commit, state, rootState }, view) {
            /*console.log(view)
            return new Promise(resolve => {
                if (view.matched && view.matched.length >= 3) {
                    commit('DEL_CACHED_VIEW', view.matched[1])
                }
                commit('DEL_CACHED_VIEW', view)
                resolve([...state.cachedViews])
            })*/
            if (view.matched && view.matched.length > 1) {
                const deleteView = view.matched[view.matched.length - 1]
                console.log('delete ' + deleteView.name)
                commit('DEL_CACHED_VIEW', deleteView)
                await dispatch('recursionDeleteParent', { routes: rootState.user.routes, view: deleteView.parent, excludeView: view.matched[0] })
            } else {
                commit('DEL_CACHED_VIEW', view)
            }
            return [...state.cachedViews]
            /*return new Promise(resolve => {
                commit('DEL_CACHED_VIEW', view)
                resolve([...state.cachedViews])
            })*/
        },

        delOthersViews({dispatch, state}, view) {
            return new Promise(resolve => {
                dispatch('delOthersVisitedViews', view)
                dispatch('delOthersCachedViews', view)
                resolve({
                    visitedViews: [...state.visitedViews],
                    cachedViews: [...state.cachedViews]
                })
            })
        },
        delOthersVisitedViews({commit, state}, view) {
            return new Promise(resolve => {
                commit('DEL_OTHERS_VISITED_VIEWS', view)
                resolve([...state.visitedViews])
            })
        },
        delOthersCachedViews({commit, state}, view) {
            return new Promise(resolve => {
                commit('DEL_OTHERS_CACHED_VIEWS', view)
                resolve([...state.cachedViews])
            })
        },

        delAllViews({dispatch, state}, view) {
            return new Promise(resolve => {
                dispatch('delAllVisitedViews', view)
                dispatch('delAllCachedViews', view)
                resolve({
                    visitedViews: [...state.visitedViews],
                    cachedViews: [...state.cachedViews]
                })
            })
        },
        delAllVisitedViews({commit, state}) {
            return new Promise(resolve => {
                commit('DEL_ALL_VISITED_VIEWS')
                resolve([...state.visitedViews])
            })
        },
        delAllCachedViews({commit, state}) {
            return new Promise(resolve => {
                commit('DEL_ALL_CACHED_VIEWS')
                resolve([...state.cachedViews])
            })
        },

        updateVisitedView({commit}, view) {
            commit('UPDATE_VISITED_VIEW', view)
        },

        getRouteByName({ dispatch }, { routes, name }) {
            let stark = []
            stark = stark.concat(routes)
            while (stark.length) {
                const temp = stark.shift()
                if (temp.children) {
                    stark = stark.concat(temp.children)
                }
                if (temp.name === name) {
                    return temp
                }
            }
            return null
        },
        async recursionDeleteParent({ dispatch, commit, state }, { routes, view, excludeView }) {
            const route = await dispatch('getRouteByName', { routes: routes, name: view.name })
            if (route && route.children) {
                const childrenNames = route.children.map(r => r.name).filter(Boolean)
                const visitedViewNames = state.visitedViews.map(s => s.name)

                const needDelete = !(childrenNames.some(item => visitedViewNames.includes(item)) || view === excludeView)
                if (needDelete) {
                    console.log('delete ' + view.name)
                    commit('DEL_CACHED_VIEW', view)
                    await dispatch('recursionDeleteParent', { routes: routes, view: view.parent, excludeView: excludeView })
                }
            }
        }
    }
}

export default tagsView
