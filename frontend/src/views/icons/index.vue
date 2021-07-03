<template>
    <div class="icons-container">
        <el-tabs type="border-card">
            <el-tab-pane label="Icons">
                <div class="grid">
                    <div v-for="item of svgIcons" :key="item" @click="handleClipboard('svgIcons',item)">
                        <div class="icon-item">
                            <svg-icon :icon-class="item" class-name="disabled"/>
                            <span>{{ item }}</span>
                        </div>
                    </div>
                </div>
            </el-tab-pane>
            <el-tab-pane label="Element-UI Icons">
                <div class="grid">
                    <div v-for="item of elementIcons" :key="item" @click="handleClipboard('elementIcons',item)">
                        <div class="icon-item">
                            <i :class="'el-icon-' + item"/>
                            <span>{{ item }}</span>
                        </div>
                    </div>
                </div>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
import svgIcons from './svg-icons'
import elementIcons from './element-icons'
import {getInternational} from "@/utils/i18nUtil";

export default {
    name: 'Icons',
    data() {
        return {
            svgIcons,
            elementIcons
        }
    },
    methods: {
        handleClipboard(type,text) {
            if(type==='svgIcons'){
                this.$emit('select',text)
            }else{
                this.$message.error('暂只能选择svgIcons');
            }
        }
    }
}
</script>

<style lang="scss" scoped>
.icons-container {
    margin: 0;
    overflow: hidden;

    .grid {
        position: relative;
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(60px, 1fr));
    }

    .icon-item {
        margin: 0;
        height: 100px;
        text-align: center;
        width: 60px;
        float: left;
        font-size: 30px;
        color: #24292e;
        cursor: pointer;
    }

    span {
        display: block;
        font-size: 16px;
        margin-top: 0;
    }

    .disabled {
        pointer-events: none;
    }
}
</style>
