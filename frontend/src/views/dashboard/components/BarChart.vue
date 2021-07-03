<template>
    <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
/*//全部引入
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme*/
// 按需引入
import echarts from 'echarts/lib/echarts';// 引入 ECharts 主模块
require('echarts/lib/chart/bar');// 引入柱状图
require('echarts/theme/macarons') // Echarts主题

import resize from './mixins/resize'

const animationDuration = 6000

export default {
    mixins: [resize],
    props: {
        className: {
            type: String,
            default: 'chart'
        },
        width: {
            type: String,
            default: '100%'
        },
        height: {
            type: String,
            default: '300px'
        },
        chartData: {
            type: Object,
            required: true
        }
    },
    watch: {
        chartData: {// chartData必须要watch，因为第一次加载的时候chartData没有值后面才传入，必须监视它
            deep: true,
            handler(val) {
                this.setOptions(val)// 设置Echarts选项
            }
        }
    },
    data() {
        return {
            chart: null // 柱状图
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.initChart()
        })
    },
    beforeDestroy() {
        if (!this.chart) {
            return
        }
        this.chart.dispose() // 销毁
        this.chart = null // 置空
    },
    methods: {
        initChart() {
            this.chart = echarts.init(this.$el, 'macarons')

            this.setOptions(this.chartData)// 设置Echarts选项
        },
        setOptions({fixedAssetsProjectQuotaMonthSumList, investmentFilingProjectQuotaMonthSumList}={}){// 设置Echarts选项
            this.chart.setOption({
                title: {// 标题
                    text: '元/月',
                    textStyle:{
                        color:'#67C23A'
                    }
                },
                grid: {// 表格设置
                    left: '2%',
                    right: '2%',
                    bottom: '3%',
                    top: 30,
                    containLabel: true,// 是否包含坐标轴的刻度标签
                },
                xAxis: [{// 横坐标
                    type: 'category',// 坐标轴类型，类目轴，适用于离散的类目数据，为该类型时必须通过data设置类目数据
                    data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
                    axisTick: {// 是否显示坐标刻度
                        show: true,// 显示
                        inside:true// 刻度朝内
                    }
                }],
                yAxis: [{// 纵坐标
                    type: 'value',// 坐标轴类型，数值轴，适用于连续数据
                    axisTick: {// 是否显示坐标刻度
                        show: true,// 显示
                        inside:true// 刻度朝内
                    }
                }],
                tooltip: {// 提示框，鼠标放到表格上面时的提示
                    trigger: 'axis',// 触发类型:坐标轴触发，鼠标在坐标轴中移动时会显示提示框，主要在柱状图，折线图等会使用类目轴的图表中使用
                    axisPointer: { // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow' // 有阴影效果
                    },
                    formatter:function(params,ticket,callback){// 提示框的格式化
                        return params[0].name+'月</br>'
                            + echarts.format.getTooltipMarker(params[0].color)+params[0].seriesName+':'+params[0].value+'元</br>'
                            + echarts.format.getTooltipMarker(params[1].color)+params[1].seriesName+':'+params[1].value+'元</br>'
                    }
                },
                series: [{// 系列列表，显示的数据系列
                    name: '固定资产',// 显示名称
                    type: 'bar',// 组件 ID
                    stack: 'vistors',// 数据堆叠，同个类目轴上系列配置相同的stack值可以堆叠放置
                    barWidth: '60%',// 柱宽度
                    data: fixedAssetsProjectQuotaMonthSumList,// 数据列表
                    animationDuration: 6000,// 图例翻页时的动画时长
                }, {
                    name: '投资备案',
                    type: 'bar',
                    stack: 'vistors',
                    barWidth: '60%',
                    data: investmentFilingProjectQuotaMonthSumList,
                    animationDuration: 6000,// 图例翻页时的动画时长
                }]
            })
        }
    }
}
</script>
