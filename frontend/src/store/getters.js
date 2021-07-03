const getters = {
    sidebar: state => state.app.sidebar,
    language: state => state.app.language,
    size: state => state.app.size,
    device: state => state.app.device,
    visitedViews: state => state.tagsView.visitedViews,
    cachedViews: state => state.tagsView.cachedViews,
    errorLogs: state => state.errorLog.logs,

    token: state => state.user.token,
    user: state => state.user.user,
    permissions: state => state.user.permissions,
    menus: state => state.user.menus,
    routes:state => state.user.routes,
    hasGetAllInfo: state => state.user.hasGetAllInfo,
    dictionary: state => state.user.dictionary,

    dashboardDisplayData: state=>state.other.dashboardDisplayData,
    projectToDoCount: state=>state.other.projectToDoCount
}
export default getters
