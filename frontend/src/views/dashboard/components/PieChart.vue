<template>
    <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
/*全部引入
    import echarts from 'echarts'
    require('echarts/theme/macarons') // echarts theme*/
// 按需引入
import echarts from 'echarts/lib/echarts';// 引入 ECharts 主模块
require('echarts/lib/chart/pie');// 引入饼图
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
            default: '300px'
        },
        chartData: {
            type: Array,
            required: true
        }
    },
    data() {
        return {
            chart: null // 饼图表
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
        setOptions(chartData=[]){// 设置Echarts选项
            this.chart.setOption({
                title: {// 标题
                    text: '额度',
                    textStyle:{
                        color:'#67C23A'
                    }
                },
                grid: {// 表格设置
                    left: 10,
                    right: 10,
                    bottom: 20,
                    top: 30,
                },
                tooltip: {// 提示框组件
                    trigger: 'item',// 触发类型：数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用
                    formatter: '{a} <br/>{b} : {c} ({d}%)'// 提示框浮层内容格式器，{a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
                },
                legend: {// 图上面的通过点击图例控制哪些系列不显示
                    left: 'center',// 图例组件离容器左侧的距离，center为居中对齐
                    bottom: '10',// 图例组件离容器下侧的距离
                },
                series: [
                    {
                        name: '额度情况',// 名称
                        type: 'pie',// 组件ID
                        roseType: 'radius',// 是否展示成南丁格尔图，通过半径区分数据大小，'radius' 扇区圆心角展现数据的百分比，半径展现数据的大小。'area' 所有扇区圆心角相同，仅通过半径展现数据大小。
                        radius: [15, 95],// 扇区圆心角展现数据的百分比，半径展现数据的大小
                        center: ['50%', '40%'],// 饼图的中心（圆心）坐标
                        data: chartData,// 数据数组
                        animationEasing: 'cubicInOut',// 初始动画的缓动效果
                        animationDuration: 2600,// 图例翻页时的动画时长
                    }
                ]
            })
        }
    }
}
</script>
