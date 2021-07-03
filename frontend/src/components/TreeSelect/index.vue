<template>
    <!--remove-tags删除标签时的事件，clear点击右边全清时的事件，collapse-tags当标签过多容不下时显示+剩余个数-->
    <el-select
            ref="selectRef"
            v-model="selectShowValue"
            @visible-change="selectVisibleChange"
            @remove-tag="selectRemoveTag"
            @clear="selectClear"
            :loading="selectLoading"
            :loading-text="selectLoadingText"
            :placeholder="selectPlaceholder"
            multiple
            clearable
            :collapse-tags="true"
            :class="selectClass"
            :style="{width:selectWidth+'px'}">
        <!--el-select必须有el-option否则下拉都不会显示，这里写上它但让其不显示-->
        <el-option value="0" v-show="false"></el-option>
        <!--node-key作为唯一标识的属性，show-checkbox节点是否可被选择，props配置显示值，default-checked-keys默认勾选的节点的key的数组，check当复选框被点击的时候触发-->
        <el-tree
                ref="treeRef"
                :data="treeData"
                :node-key="treeNodeKey"
                show-checkbox
                :props="treeProps"
                :default-checked-keys="treeRealValue"
                @check="treeCheck">
        </el-tree>
    </el-select>
</template>

<script>
    export default {
        name: "TreeSelect",
        props:{
            // 选择组件
            selectLoadingText:{// 选择组件正在加载值时的提示文件
                type:String,
                default:'Loading...'
            },
            selectPlaceholder:{// 选择组件的placeholder
                type:String,
                default:'Input Content'
            },
            selectClass:{// 选择组件样式class
                type:String,
                require:false
            },
            selectWidth:{// 选择组件宽度
                type:Number,
                default:180
            },

            // 树形组件
            treeNodeKey:{// 树形组件标识，默认为树形组件数据的id
                type:String,
                default: 'id'// 有default则require默认为false
            },
            treeProps: {// 树形组件配置显示值，因为有时默认显示属性是label，而传入的是name
                type:Object,
                default:function () {// 传入属性为数组或对象时，默认值必须是一个函数返回
                    return {
                        children: 'children',
                        label: 'name'
                    }
                }
            },
            getTreeData:{// 树形数据获取函数
                type:Function,
                require:true
            },
            getTreeParam:{// 树形数据获取函数参数
                type:Object,
                default:null
            },
            parentChildrenGetType:{// 选中父节点时父节点与子节点的取舍类型，0：仅需要子节点不需要父节点，1：仅需要父节点不需要子节点，2：子节点父节点都需要
                type:Number,
                default:0
            }
        },
        data(){
            return {
                // 选择组件
                selectLoading:false,// 是否正在加载选择组件下拉
                selectShowValue:[],// 显示的值，为选择组件的值

                // 树形组件
                treeData:null,// 树形组件数据
                treeRealValue:[],// 实际的值，为树形组件的值

                // 辅助
                flatTreeDataMap:new Map(),// 扁平化数据Map，键为树形数据标识id，值为树形数据
                flatTreeDataSet:new Set(),// 扁平化数据Set，存放要删除的节点和它的子节点的Key，因为删除父节点时子节点也需要删除
                keyValueMap:new Map(),// 键为id，值为value对象，空间换时间，不需要再取遍历了
                valueKeyMap:new Map()// 键为value对象（名称唯一，所以可作为键），值为id，空间换时间，不需要再取遍历了
            }
        },
        methods:{
            // 选择组件
            selectVisibleChange(show){// 选择组件的下拉选项显示/隐藏时触发，这里获取树组件的数据
                const _this=this
                if(show){// 显示
                    this.selectLoading=true
                    this.getTreeData(this.getTreeParam).then(res=>{// 从服务端获取数据
                        _this.treeData=res
                        _this.keepHelpData()// 维持辅助数据
                        _this.selectLoading=false
                    }).catch((error) => {
                        console.error(error)
                        _this.selectLoading=false
                    })
                }
            },
            selectRemoveTag(value){// 选择组件多选模式下移除tag时触发，这里也将树形组件上的值移除，如果选择的是父节点则子节点也需要移除
                const _this=this
                let node=null
                this.valueKeyMap.forEach((k,v,map)=>{// name->value，name为显示值，value为具体的对象
                    if(v.name===value){
                        node=v
                        // map遍历不能退出
                    }
                })

                if(node!=null){
                    // 因为删除子节点后，其父节点应该为半选状态，所以应该删除其父节点否则删除子节点由于其父节点还存在它还是被选中的
                    let set=new Set()// 存放当前节点极其不断往上递归的父节点id
                    this.getDeleteParent(node,set)// 递归获取父节点id

                    for(let i=this.treeRealValue.length -1; i >= 0 ; i--){
                        if(set.has(this.treeRealValue[i])){// 如果包含在被删除的id数组里面，则需要将其删除
                            this.treeRealValue.splice(i, 1);
                        }
                    }

                    if(node.children!=null&&node.children.length>0){// 如果为父节点，则需要删除其子节点
                        this.deleteParentChildren(node,this.treeRealValue)// 递归删除当前节点node的子节点
                    }
                }

                this.selectShowValue=[]
                this.treeRealValue.forEach(item=>{// 添加对应显示值
                    _this.selectShowValue.push(_this.keyValueMap.get(item).name)
                })

                this.$refs.treeRef.setCheckedKeys(this.treeRealValue)// 需要手动设置才会显示，因为treeRealValue只是树组件的默认值不是v-model绑定的
                this.$emit('onReturnValue',{'treeRealValue':this.treeRealValue,'selectShowValue':this.selectShowValue})// 回传值给父组件
            },
            selectClear(){// 选择组件点击右边的清除按钮时触发
                //this.selectShowValue=[]// 这个不需要删除，选择组件在点删除时自动帮我们删除了
                this.treeRealValue=[]
                this.$emit('onReturnValue',{'treeRealValue':this.treeRealValue,'selectShowValue':this.selectShowValue})// 回传值给父组件
            },

            // 树形组件
            treeCheck(treeData,checkData){// 当树形组件复选框被点击的时候触发，这里获取树形组件上的选中的值并添加到选择组件，treeData为传给树形组件的原始树形数据，checkData为选择的扁平化数据
                const _this=this
                // 重置数据
                _this.treeRealValue=[]
                _this.selectShowValue=[]

                switch (this.parentChildrenGetType) {// 选中父节点时父节点与子节点的取舍类型
                    case 0:// 仅需要子节点不需要父节点
                        // getCheckedNodes第1个参数勾选父节点而自动勾选子节点时是否只需要叶子节点不需要父节点，第2个参数是否包含半选节点
                        this.$refs.treeRef.getCheckedNodes(true,false).forEach(function (item, index, array) {// 遍历选中的值
                            _this.treeRealValue.push(item.id)// 虽然勾选父节点会自动勾选子节点（反过来也如此），但treeRealValue还是为空的，需要手动设置
                            _this.selectShowValue.push(item.name);// 显示值为name
                        })
                        break;
                    case 1:// 仅需要父节点不需要子节点
                        _this.treeRealValue=Object.assign(checkData.checkedKeys)// 直接复制原值（不能直接用等号=否则会造成原值的变化）
                        checkData.checkedKeys.forEach(item=>{
                            let node=_this.keyValueMap.get(item)// key->value

                            if(node.children!=null&&node.children.length>0){// 如果为父节点，则需要删除其子节点
                                _this.deleteParentChildren(node,_this.treeRealValue)// 递归删除当前节点node的子节点
                            }
                        })

                        this.treeRealValue.forEach(item=>{// 添加对应显示值
                            _this.selectShowValue.push(_this.keyValueMap.get(item).name)
                        })
                        break;
                    case 2:// 子节点父节点都需要
                        // 也可以用this.$refs.treeRef.getCheckedNodes(false,false)获取所有节点，注意第1个参数为false和前面第1种情况（为true）不一样
                        checkData.checkedNodes.forEach(function (item, index, array) {// 遍历选中的值
                            _this.treeRealValue.push(item.id)// 虽然勾选父节点会自动勾选子节点（反过来也如此），但treeRealValue还是为空的，需要手动设置
                            _this.selectShowValue.push(item.name);// 显示值为name
                        });
                        break;
                }

                this.$emit('onReturnValue',{'treeRealValue':this.treeRealValue,'selectShowValue':this.selectShowValue})// 回传值给父组件
            },

            // 辅助
            keepState(treeRealValue){// 保持状态同步，传入树形组件真实值需要设置选择组件显示值
                const _this=this
                switch (this.parentChildrenGetType) {// 选中父节点时父节点与子节点的取舍类型
                    case 0:// 仅需要子节点不需要父节点
                        if(treeRealValue.length!==0){// 有值才需要处理
                            this.getTreeData(this.getTreeParam).then(res=>{// 从服务端获取数据
                                this.treeData=res
                                this.keepHelpData()// 维持辅助数据

                                this.selectShowValue=[]
                                this.treeRealValue=[]
                                treeRealValue.forEach(item=>{
                                    let node=_this.keyValueMap.get(item)
                                    if(node.children==null||node.children.length==0){// 仅需要子节点不需要父节点时，只设置子节点
                                        _this.treeRealValue.push(item)
                                        _this.selectShowValue.push(node.name)
                                    }
                                })
                            }).catch((error) => {
                                console.error(error)
                            })
                        }else{// 没有值直接置[]
                            this.selectShowValue=[]
                            this.treeRealValue=[]
                        }
                        break;
                    case 1:// 仅需要父节点不需要子节点
                        if(treeRealValue.length!==0){// 有值才需要处理
                            this.getTreeData(this.getTreeParam).then(res=>{// 从服务端获取数据
                                this.treeData=res
                                this.keepHelpData()// 维持辅助数据

                                this.selectShowValue=[]
                                this.treeRealValue=Object.assign(treeRealValue)// 直接复制原值（不能直接用等号=否则会造成原值的变化），之后如果勾选子节点则再将子节点删除，因为仅需要父节点不需要子节点

                                treeRealValue.forEach(item=>{
                                    let node=_this.keyValueMap.get(item)// key->value

                                    if(node.children!=null&&node.children.length>0){// 如果为父节点，则需要删除其子节点
                                        _this.deleteParentChildren(node,_this.treeRealValue)// 递归删除当前节点node的子节点
                                    }
                                })

                                this.treeRealValue.forEach(item=>{// 添加对应显示值
                                    _this.selectShowValue.push(_this.keyValueMap.get(item).name)
                                })
                            }).catch((error) => {
                                console.error(error)
                            })
                        }else{// 没有值直接置[]
                            this.selectShowValue=[]
                            this.treeRealValue=[]
                        }
                        break;
                    case 2:// 子节点父节点都需要
                        if(treeRealValue.length!==0){// 有值才需要处理
                            this.getTreeData(this.getTreeParam).then(res=>{// 从服务端获取数据
                                this.treeData=res
                                this.keepHelpData()// 维持辅助数据

                                this.selectShowValue=[]
                                this.treeRealValue=Object.assign(treeRealValue)// // 直接复制原值

                                this.treeRealValue.forEach(item=>{// 添加对应显示值
                                    _this.selectShowValue.push(_this.keyValueMap.get(item).name)
                                })
                            }).catch((error) => {
                                console.error(error)
                            })
                        }else{// 没有值直接置[]
                            this.selectShowValue=[]
                            this.treeRealValue=[]
                        }
                        break;
                }
            },
            getDeleteParent(parent,set){// 递归添加父节点
                if(parent==null){
                    return
                }

                set.add(parent.id)

                this.getDeleteParent(parent.parent,set)
            },
            getObjectType(obj) {// 获取对象类型
                let type = Object.prototype.toString.call(obj).match(/^\[object (.*)\]$/)[1].toLowerCase();
                if(type === 'string' && typeof obj === 'object') return 'object'; // Let "new String('')" return 'object'
                if (obj === null) return 'null'; // PhantomJS has type "DOMWindow" for null
                if (obj === undefined) return 'undefined'; // PhantomJS has type "DOMWindow" for undefined
                return type;
            },
            fillHelpTreeData(currentData,parentData,flatTreeDataMap,flatTreeDataSet){// 填充辅助数据
                if(currentData==null){
                    return
                }

                const _this=this
                if(this.getObjectType(currentData)==='array'){// 为数组时[{},{}]不能直接获取它需要遍历才能拿到，为对象{}时可以直接获取它，所以为数组的话还需要遍历，因为最终为对象时才能放数据，Set也是Array
                    currentData.forEach(item=>{
                        _this.fillHelpTreeData(item,parentData,flatTreeDataMap,flatTreeDataSet)
                    })
                }else if(this.getObjectType(currentData)==='object') {// 为对象，可以直接放入数据
                    flatTreeDataMap.set(currentData.id,currentData)
                    flatTreeDataSet.add(currentData.id)
                    currentData.parent=parentData// 填充对象currentData的parent属性为parentData

                    currentData.children.forEach(function (item, index, array) {
                        _this.fillHelpTreeData(item,currentData,flatTreeDataMap,flatTreeDataSet)
                    })
                }
            },
            keepHelpData(){// 维持辅助数据
                this.flatTreeDataSet=new Set()
                this.flatTreeDataMap=new Map()
                this.fillHelpTreeData(this.treeData,null,this.flatTreeDataMap,this.flatTreeDataSet)// 填充辅助数据，null是因为顶级数据的父节点为null
                this.keyValueMap=new Map()
                this.valueKeyMap=new Map()
                const _this=this
                this.flatTreeDataMap.forEach((value,key,map)=>{
                    _this.keyValueMap.set(value.id,value)
                    _this.valueKeyMap.set(value,value.id)
                })

            },
            deleteParentChildren(node,treeRealValue){// 递归在treeRealValue中删除当前节点node的子节点
                const _this=this
                if(node.children!=null&&node.children.length>0){
                    node.children.forEach(item=>{
                        let i=treeRealValue.indexOf(item.id)
                        if(i!=-1){
                            treeRealValue.splice(i,1)
                        }

                        _this.deleteParentChildren(item,treeRealValue)// 递归删除子节点
                    })
                }
            }
        }
    }
</script>

<style scoped>

</style>
