<template>
    <el-row :gutter="40" class="panel-group">
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
            <!--<div class="card-panel" @click="handleSetLineChartData('newVisitis')">-->
            <div class="card-panel">
                <div class="card-panel-icon-wrapper icon-people">
                    <!--这里的svg-icon中使用的svg图片下载之后需要编辑，将里面的fill值去掉，因为它会填充颜色从而这里无法用css为其设置颜色-->
                    <svg-icon icon-class="user" class-name="card-panel-icon"/>
                </div>
                <div class="card-panel-description">
                    <div class="card-panel-text">
                        {{ $t('common.user') }}
                    </div>
                    <count-to :start-val="0" :end-val="userCount" :duration="2600" class="card-panel-num"/>
                </div>
            </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
            <!--<div class="card-panel" @click="handleSetLineChartData('messages')">-->
            <div class="card-panel">
                <div class="card-panel-icon-wrapper icon-message">
                    <svg-icon icon-class="role" class-name="card-panel-icon"/>
                </div>
                <div class="card-panel-description">
                    <div class="card-panel-text">
                        {{ $t('common.role') }}
                    </div>
                    <count-to :start-val="0" :end-val="roleCount" :duration="3000" class="card-panel-num"/>
                </div>
            </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
            <!--<div class="card-panel" @click="handleSetLineChartData('purchases')">-->
            <div class="card-panel">
                <div class="card-panel-icon-wrapper icon-money">
                    <svg-icon icon-class="department" class-name="card-panel-icon"/>
                </div>
                <div class="card-panel-description">
                    <div class="card-panel-text">
                        {{ $t('common.department') }}
                    </div>
                    <count-to :start-val="0" :end-val="departmentCount" :duration="3200" class="card-panel-num"/>
                </div>
            </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
            <!--<div class="card-panel" @click="handleSetLineChartData('shoppings')">-->
            <div class="card-panel">
                <div class="card-panel-icon-wrapper icon-shopping">
                    <svg-icon icon-class="job" class-name="card-panel-icon"/>
                </div>
                <div class="card-panel-description">
                    <div class="card-panel-text">
                        {{ $t('common.job') }}
                    </div>
                    <count-to :start-val="0" :end-val="jobCount" :duration="3600" class="card-panel-num"/>
                </div>
            </div>
        </el-col>
    </el-row>
</template>

<script>
import CountTo from 'vue-count-to'

export default {
    props:{
        userCount:{
            type: Number,
            default:0
        },
        roleCount:{
            type: Number,
            default:0
        },
        departmentCount:{
            type: Number,
            default:0
        },
        jobCount:{
            type: Number,
            default:0
        },
    },
    components: {
        CountTo
    },
    methods: {
        /*handleSetLineChartData(type) { // 4个图标的点击事件
            this.$emit('handleSetLineChartData', type) // 发送事件给父级组件，父子间通信
        }*/
    },
    /*computed:{
        fixedAssetsProjectMonthCountListSum() {
            return this.$store.getters.dashboardDisplayData.fixedAssetsProjectMonthCount.valueList.reduce((prev, next, index, array) => prev + next,0)//  reduce()方法从数组的第一项开始，逐个遍历到最后，数组为空则返回0
        },
        investmentFilingProjectMonthCountListSum() {
            return this.$store.getters.dashboardDisplayData.investmentFilingProjectMonthCount.valueList.reduce((prev, next, index, array) => prev + next,0)
        },
        projectProcessMonthCountListSum() {
            return this.$store.getters.dashboardDisplayData.projectProcessMonthCount.valueList.reduce((prev, next, index, array) => prev + next,0)
        },
        projectAttachmentMonthCountListSum() {
            return this.$store.getters.dashboardDisplayData.projectAttachmentMonthCount.valueList.reduce((prev, next, index, array) => prev + next,0)
        }
    }*/
}
</script>

<style lang="scss" scoped>
.panel-group {
    margin-top: 18px;

    .card-panel-col {
        margin-bottom: 32px;
    }

    .card-panel {
        height: 108px;
        cursor: pointer;
        font-size: 12px;
        position: relative;
        overflow: hidden;
        color: #666;
        background: #fff;
        box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
        border-color: rgba(0, 0, 0, .05);

        &:hover {
            .card-panel-icon-wrapper {
                color: #fff;
            }

            .icon-people {
                background: #40c9c6;
            }

            .icon-message {
                background: #36a3f7;
            }

            .icon-money {
                background: #f4516c;
            }

            .icon-shopping {
                background: #34bfa3
            }
        }

        .icon-people {
            color: #40c9c6;
        }

        .icon-message {
            color: #36a3f7;
        }

        .icon-money {
            color: #f4516c;
        }

        .icon-shopping {
            color: #34bfa3
        }

        .card-panel-icon-wrapper {
            float: left;
            margin: 14px 0 0 14px;
            padding: 16px;
            transition: all 0.38s ease-out;
            border-radius: 6px;
        }

        .card-panel-icon {
            float: left;
            font-size: 48px;
        }

        .card-panel-description {
            float: right;
            font-weight: bold;
            margin: 26px;
            margin-left: 0px;

            .card-panel-text {
                line-height: 18px;
                color: rgba(0, 0, 0, 0.45);
                font-size: 16px;
                margin-bottom: 12px;
            }

            .card-panel-num {
                font-size: 20px;
            }
        }
    }
}

@media (max-width: 550px) {
    .card-panel-description {
        display: none;
    }

    .card-panel-icon-wrapper {
        float: none !important;
        width: 100%;
        height: 100%;
        margin: 0 !important;

        .svg-icon {
            display: block;
            margin: 14px auto !important;
            float: none !important;
        }
    }
}
</style>
