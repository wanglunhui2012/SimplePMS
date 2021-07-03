<template>
    <div class="dashboard-editor-container">
        <!--<panel-group @handleSetLineChartData="handleSetLineChartData"/>-->
        <panel-group :userCount="userCount" :roleCount="roleCount" :departmentCount="departmentCount" :jobCount="jobCount"/>

        <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
            <line-chart :chart-data="thisWeekLogCountList"/>
        </el-row>

        <!--<el-row :gutter="32">
            <el-col :xs="24" :sm="24" :lg="8">
                <div class="chart-wrapper">
                    <raddar-chart/>
                </div>
            </el-col>
            <el-col :xs="24" :sm="24" :lg="8">
                <div class="chart-wrapper">
                    <pie-chart :chart-data="pieChartData"/>
                </div>
            </el-col>
            <el-col :xs="24" :sm="24" :lg="8">
                <div class="chart-wrapper">
                    <bar-chart :chart-data="barChartData"/>
                </div>
            </el-col>
        </el-row>-->

        <!--<el-row :gutter="8">
            <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" :lg="{span: 12}" :xl="{span: 12}"
                    style="padding-right:8px;margin-bottom:30px;">
                <transaction-table/>
            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}"
                    style="margin-bottom:30px;">
                <todo-list/>
            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}"
                    style="margin-bottom:30px;">
                <box-card/>
            </el-col>
        </el-row>-->
    </div>
</template>

<script>
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
//import RaddarChart from './components/RaddarChart'
//import PieChart from './components/PieChart'
//import BarChart from './components/BarChart'
//import { mapGetters } from 'vuex'

import * as systemCommonApi from '@/api/systemCommon'

export default {
    name: 'Dashboard',
    components: {
        PanelGroup,
        LineChart,
        //RaddarChart,
        //PieChart,
        //BarChart
    },
    data() {
        return {
            userCount:0,
            roleCount:0,
            departmentCount:0,
            jobCount:0,
            thisWeekLogCountList:[]
        }
    },
    created() {
        this.handleSearch()
    },
    methods: {
        /*handleSetLineChartData(name) {// 点击选项时切换数据
            this.lineChartData = this.dashboardDisplayData[name]
        },*/
        handleSearch(){
            systemCommonApi.getDashboard().then(res=>{
                this.userCount=res.userCount
                this.roleCount=res.roleCount
                this.departmentCount=res.departmentCount
                this.jobCount=res.jobCount
                this.thisWeekLogCountList=res.thisWeekLogCountList
            })
        }
    }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .github-corner {
        position: absolute;
        top: 0px;
        border: 0;
        right: 0;
    }

    .chart-wrapper {
        background: #fff;
        padding: 16px 16px 0;
        margin-bottom: 32px;
    }
}

@media (max-width: 1024px) {
    .chart-wrapper {
        padding: 8px;
    }
}
</style>
