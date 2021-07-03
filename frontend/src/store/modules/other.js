const other={
    namespaced: true,
    state:{
        dashboardDisplayData:{
            monthList:[],
            fixedAssetsProjectMonthCount:{
                valueList:[],
                averageList:[],
            },
            investmentFilingProjectMonthCount:{
                valueList:[],
                averageList:[],
            },
            projectProcessMonthCount:{
                valueList:[],
                averageList:[],
            },
            projectAttachmentMonthCount:{
                valueList:[],
                averageList:[],
            },
            projectTotalRestQuotaList:[
                {
                    value:0,
                    name:'总额度'
                },
                {
                    value:0,
                    name:'剩余额度'
                }
            ],
            fixedAssetsProjectQuotaMonthSumList:[],
            investmentFilingProjectQuotaMonthSumList:[],
        },
        projectToDoCount:{
            fixedAssetsApply:0,
            fixedAssetsReview:0,
            fixedAssetsFillSchedule:0,
            investmentFilingApply:0,
            investmentFilingReview:0,
            investmentFilingFillSchedule:0
        }
    },
    mutations:{
        SETDASHBOARDDISPLAYDATA: (state, dashboardDisplayData) => {
            state.dashboardDisplayData=dashboardDisplayData
        },
        SETPTOJECTTODOCOUNT:(state, projectToDoCount) => {
            state.projectToDoCount=projectToDoCount
        }
    },
    actions:{
        setDashboardDisplayData({commit}, dashboardDisplayData) {
            commit('SETDASHBOARDDISPLAYDATA', dashboardDisplayData)
        },
        setProjectToDoCount({commit}, projectToDoCount){
            commit('SETPTOJECTTODOCOUNT', projectToDoCount)
        }
    }
}

export default other
