<template>
    <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
/*全部引入
import echarts from 'echarts'// 引入Echarts
require('echarts/theme/macarons') // Echarts主题*/
// 按需引入
import echarts from 'echarts/lib/echarts';// 引入 ECharts 主模块
require('echarts/lib/chart/line');// 引入柱状图
require('echarts/lib/component/tooltip');// 引入提示框
require('echarts/lib/component/title');// 引入标题组件
require('echarts/lib/component/legend');// 引入图标组件
require('echarts/theme/macarons') // Echarts主题

import resize from './mixins/resize'

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
            default: '350px'
        },
        autoResize: {
            type: Boolean,
            default: true
        },
        chartData: {
            //type: Object,
            type:Array,
            required: true
        }
    },
    data() {
        return {
            chart: null // 线型表
        }
    },
    watch: {
        chartData: { // chartData必须要watch，因为第一次加载的时候chartData没有值后面才传入，必须监视它
            deep: true,
            handler(val) {
                this.setOptions(val) // 设置Echarts选项
            }
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.initChart() // 初始化Echarts，必须在加载完成之后才初始化
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
            this.chart = echarts.init(this.$el, 'macarons') // 初始化Echarts
            this.setOptions(this.chartData)// 设置Echarts选项
        },
        //({valueList, averageList} = {}) {// 设置Echarts选项，{}为获取直接获取里面的数据
        setOptions(chartData) {
            this.chart.setOption({// 指定图表的配置项和数据
                title: {// 标题
                    text: this.$t('common.numberPerMonth'),
                    textStyle:{
                        color:'#67C23A'
                    }
                },
                grid: {// 表格设置
                    left: 10,
                    right: 10,
                    bottom: 20,
                    top: 30,
                    containLabel: true// grid区域是否包含坐标轴的刻度标签
                },
                xAxis: {// 横坐标
                    /*data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11',{
                        value:'12',
                        textStyle: {// 单个样式
                            align: 'right'// 最后一个右对齐，默认的居中对齐的话右边会不显示
                        }
                    }],*/
                    data: [this.$t('common.monday'), this.$t('common.tuesday'), this.$t('common.wednesday'), this.$t('common.thursday'), this.$t('common.friday'), this.$t('common.saturday'), this.$t('common.sunday')],
                    boundaryGap: false,// 两坐标轴起点是否有空隙，默认true，为false则横坐标部署从0开始而是从data的第一个1开始
                    axisTick: {// 是否显示坐标刻度
                        show: true,// 显示
                        //inside:true// 刻度朝内
                    }
                },
                yAxis: {// 纵坐标
                    axisTick: {// 是否显示坐标刻度
                        show: true,// 显示
                        //inside:true// 刻度朝内
                    }
                },
                tooltip: {// 提示框，鼠标放到表格上面时的提示
                    trigger: 'axis',// 触发类型:坐标轴触发，鼠标在坐标轴中移动时会显示提示框，主要在柱状图，折线图等会使用类目轴的图表中使用
                    axisPointer: {// 坐标轴指示器
                        type: 'cross',// 十字准星指示器，即启用两个正交的轴的 axisPointer
                        /*label:{// 横纵坐标以及提示框标题的格式化
                          show:true,
                          formatter: '{value}'// formatter: 'some text {value} some text，其中 {value} 会被自动替换为轴的值。
                        }*/
                    },
                    padding: [5, 10],
                    /*formatter:function(params,ticket,callback){// 提示框的格式化
                        return params[0].name+'月</br>'
                            + echarts.format.getTooltipMarker(params[0].color)+params[0].seriesName+':'+params[0].value+'</br>'
                            + echarts.format.getTooltipMarker(params[1].color)+params[1].seriesName+':'+params[1].value+'</br>'
                    }*/
                },
                legend: {// 图上面的通过点击图例控制哪些系列不显示
                    //data: ['值', '平均'],// 这里的值必须和series中的name对应
                    data:[this.$t('common.totalLog')]
                },
                /*series: [{// 系列列表，显示的数据系列
                    name: '值',// 显示名称
                    itemStyle: {// valueList系列
                        normal: {
                            color: '#FF005A',
                            lineStyle: {
                                color: '#FF005A',
                                width: 2
                            }
                        }
                    },
                    smooth: true,// 开启平滑处理
                    type: 'line',// 线条类型
                    data: valueList,// 数据内容数组
                    animationDuration: 2800,// 初始动画的时长
                    animationEasing: 'cubicInOut',// 初始动画的缓动效果
                    label:{// 坐标曲线上的值
                        show:false
                    }
                },
                    {
                        name: '平均',// averageList系列
                        smooth: true,
                        type: 'line',
                        itemStyle: {
                            normal: {
                                color: '#3888fa',
                                lineStyle: {
                                    color: '#3888fa',
                                    width: 2
                                },
                                areaStyle: {
                                    color: '#f3f8ff'
                                }
                            }
                        },
                        data: averageList,
                        animationDuration: 2800,
                        animationEasing: 'quadraticOut'
                    }]*/
                series: [{
                    name: this.$t('common.totalLog'), itemStyle: {
                        normal: {
                            color: '#FF005A',
                            lineStyle: {
                                color: '#FF005A',
                                width: 2
                            }
                        }
                    },
                    smooth: true,
                    type: 'line',
                    data: chartData,
                    animationDuration: 2800,
                    animationEasing: 'cubicInOut'
                }]
            })
        }
    }
}
</script>
