<!-- # https://element.eleme.cn/#/en-US/component/tree -->
<template>
    <div>
        <h1>三级分类维护</h1>
        <el-switch v-model="draggable" active-text="开启拖拽" inactive-text="关闭拖拽"></el-switch>
        <el-button v-if="draggable" @click="batchSave">批量保存</el-button>
        <el-button type="danger" @click="batchDelete">批量删除</el-button>
        <el-tree :data="data" 
        :props="defaultProps" 
        show-checkbox 
        node-key="catId"
        :draggable="draggable"
        :allow-drop="allowDrop"
        @node-drop="handleDrop"
        ref="menuTree"
        default-expanded-keys="expandedKey"
        :expand-on-click-node="false">
          <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
          <span>
            <el-button
              v-if="node.level <= 2"
              type="text"
              size="mini"
              @click="() => append(data)">
              Append
            </el-button>
            <el-button
              type="text"
              size="mini"
              @click="() => modify(data)">
              Modify
            </el-button>
            <el-button
              v-if="node.childNodes.length == 0"
              type="text"
              size="mini"
              @click="() => remove(node, data)">
              Delete
            </el-button>
          </span>
        </span>
        </el-tree>

        <el-dialog :title="title" :visible.sync="dialogFormVisible" width="30%">
          <el-form :model="category">
            <el-form-item label="分类名称">
              <el-input v-model="category.name" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="addCategory()">确 定</el-button>
          </div>
        </el-dialog>
    </div>
</template>

<script>
  export default {
    data() {
      return {
        title:"edit",
        maxLevel:0,
        draggable: false,
        updateNodes:[],
        dialogType:"", // edit add
        data: [],
        itemData:{},
        pCid: [],
        category:{name:'', parentCid:0, catLevel:0, showStatus:1, sort:0, catId:null},
        dialogFormVisible: false,
        expandedKey:[],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      };
    },
    methods: {
      allowDrop(draggingNode, dropNode, type) {
        //1、被拖动的当前节点以及所在的父节点总层数不能大于3

          //1）、被拖动的当前节点总层数
          console.log("allowDrop:", draggingNode, dropNode, type);
          //
          this.countNodeLevel(draggingNode);
          //当前正在拖动的节点+父节点所在的深度不大于3即可
          let deep = Math.abs(this.maxLevel - draggingNode.level) + 1;
          console.log("深度：", deep);

          //   this.maxLevel
          if (type == "inner") {
            // console.log(
            //   `this.maxLevel：${this.maxLevel}；draggingNode.data.catLevel：${draggingNode.data.catLevel}；dropNode.level：${dropNode.level}`
            // );
            var allow = deep + dropNode.level <= 3;
            console.log("allowDrop ：", allow + " deep:" + deep + " dropNode.level:" + dropNode.level + " draggable:"+this.draggable);
            return allow;
          } else {
            var allow = deep + dropNode.parent.level <= 3;
            console.log("allowDrop1 ：", allow + " deep:" + deep + " dropNode.parent.level:" + dropNode.parent.level+ " draggable:"+this.draggable);
            return allow;
          }
      
        
      },

      batchSave() {
        this.$http({
          url: this.$http.adornUrl("/product/category/update/sort"),
          method: "post",
          data: this.$http.adornData(this.updateNodes, false)
        }).then(({ data }) => {
          this.$message({
            message: "菜单顺序等修改成功",
            type: "success"
          });
          //刷新出新的菜单
          this.getMenus();
          //设置需要默认展开的菜单
          this.expandedKey = this.pCid;
          this.updateNodes = [];
          this.maxLevel = 0;
          // this.pCid = 0;
        });
      },
	  
	  batchDelete() {
		  let catIds = [];
		  let checkedNodes = this.$refs.menuTree.getCheckedNodes();
		  console.log("被选中的元素", checkedNodes);
		  for (let i = 0; i < checkedNodes.length; i++) {
			catIds.push(checkedNodes[i].catId);
		  }
		  this.$confirm(`是否批量删除【${catIds}】菜单?`, "提示", {
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			type: "warning"
		  })
			.then(() => {
			  this.$http({
				url: this.$http.adornUrl("/product/category/delete"),
				method: "post",
				data: this.$http.adornData(catIds, false)
			  }).then(({ data }) => {
				this.$message({
				  message: "菜单批量删除成功",
				  type: "success"
				});
				this.getMenus();
			  });
			})
			.catch(() => {});
	  },
      handleDrop(draggingNode, dropNode, dropType, ev) {
        console.log("handleDrop: ", draggingNode, dropNode, dropType);
        //1、当前节点最新的父节点id
        let pCid = 0;
        let siblings = null;
        if (dropType == "before" || dropType == "after") {
          pCid =
            dropNode.parent.data.catId == undefined
              ? 0
              : dropNode.parent.data.catId;
          siblings = dropNode.parent.childNodes;
        } else {
          pCid = dropNode.data.catId;
          siblings = dropNode.childNodes;
        }
        this.pCid.push(pCid);

        //2、当前拖拽节点的最新顺序，
        for (let i = 0; i < siblings.length; i++) {
          if (siblings[i].data.catId == draggingNode.data.catId) {
            //如果遍历的是当前正在拖拽的节点
            let catLevel = draggingNode.level;
            if (siblings[i].level != draggingNode.level) {
              //当前节点的层级发生变化
              catLevel = siblings[i].level;
              //修改他子节点的层级
              this.updateChildNodeLevel(siblings[i]);
            }
            this.updateNodes.push({
              catId: siblings[i].data.catId,
              sort: i,
              parentCid: pCid,
              catLevel: catLevel
            });
          } else {
            this.updateNodes.push({ catId: siblings[i].data.catId, sort: i });
          }
        }

        //3、当前拖拽节点的最新层级
        console.log("updateNodes", this.updateNodes);
      },

      updateChildNodeLevel(node){
        if(node.childNodes.length > 0){
          for(let i = 0; i < node.childNodes.length; i++){
            var cNode = node.childNodes[i].data;
            this.updateNodes.push({catId:cNode.catId, catLevel:node.childNodes[i].level});
            this.updateChildNodeLevel(node.childNodes[i]);
          }
        }

      },
    

      countNodeLevel(node){
      

        //找到所有子节点，求出最大深度
        if (node.childNodes != null && node.childNodes.length > 0) {
          for (let i = 0; i < node.childNodes.length; i++) {
            if (node.childNodes[i].level > this.maxLevel) {
              this.maxLevel = node.childNodes[i].level;
            }
            this.countNodeLevel(node.childNodes[i]);
          }
        }
      },


      handleNodeClick(data) {
        console.log(data);
      },
      append(data){
        this.itemData = data;
        console.log("数据append:", data)
        this.dialogType = "add";
        this.title = "add";
        this.dialogFormVisible = true
        this.category.parentCid = data.catId;
        this.category.catLevel = data.catLevel*1 +1
      },

      modify(data) {
      this.itemData = data;
      this.dialogType = "edit";
       this.title = "edit";
       this.dialogFormVisible = true
       this.category.name=data.name
       this.category.catId = data.catId
      },

      addCategory(){
        if(this.dialogType == "add"){
          console.log("addCategory:", this.categroy);
          this.$http({
                url: this.$http.adornUrl("/product/category/save"),
                method: "post",
                data:this.$http.adornData(this.category, false)
                }
            ).then(({data})=>{
                console.log("保存 数据:", data)
                this.dialogFormVisible = false
                this.$message({
                  type: 'success',
                  message: '保存成功!'
                });
                this.getMenu();
                this.expandedKey=[this.category.parentCid]
            }).catch(() => {
              this.dialogFormVisible = false
            this.$message({
              type: 'info',
              message: '保存失败'
            });          
          });
        }
        else{
            this.itemData.name = this.category.name;
            this.edit(this.itemData);
        }
        
      },

      edit(data){
          console.log("modify:", this.itemData);
           this.$http({
                url: this.$http.adornUrl("/product/category/update"),
                method: "post",
                data:this.$http.adornData(this.itemData, false)
                }
            ).then(({data})=>{
                console.log("更新 数据:", data)
                this.dialogFormVisible = false
                this.$message({
                  type: 'success',
                  message: '更新成功!'
                });
                this.getMenu();
                this.expandedKey=[this.itemData.catId]
            }).catch(() => {
              this.dialogFormVisible = false
            this.$message({
              type: 'info',
              message: '更新失败'
            });          
          });
      },
      remove(node, data){
        console.log("数据remove node:", node)
        console.log("数据remove data:", data)
        this.$confirm('此操作将删除该菜单, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {

          var ids = [data.catId]
          this.$http({
              url: this.$http.adornUrl("/product/category/delete"),
              method: "post",
              data:this.$http.adornData(ids, false)
              }
          ).then(({data})=>{
              console.log("remove 数据:", data)
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
          });
          
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
        
        console.log("remove 数据1")
      },
      getMenu(){
        this.$http({
            url: this.$http.adornUrl("/product/category/list/tree"),
            method: "get"
            }
        ).then(({data})=>{
            console.log("数据:", data.data)
            this.data = data.data;
            }
        );
      }
    },
    created() {
        this.getMenu();
    }
  };

 
</script>

<style>
    
</style>