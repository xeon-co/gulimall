<!-- # https://element.eleme.cn/#/en-US/component/tree -->
<template>
    <div>
        <h1>三级分类维护</h1>
        <el-tree :data="data" 
        :props="defaultProps" 
        show-checkbox 
        node-key="catId"
        draggable=true
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        @node-drop="handleDrop"
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
        updateNodes:[],
        dialogType:"", // edit add
        data: [],
        itemData:{},
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
        this.countNodeLevel(draggingNode.data);
        let deep = (this.maxLevel - draggingNode.data.catLevel) + 1;
        if(type !== 'inner'){
          return (deep + dropNode.parent.level) <= 3;
        }
        else{
          return (deep + dropNode.level) <= 3;
        }
      
        
      },

      handleDrop(draggingNode, dropNode, dropType, ev) {
        console.log('tree drop: ', dropNode.label, dropType);
        // 当前父节点
        let pCid = 0;
        let siblings = null;
        if(dropType == "before" || dropType == "after"){
          pCid = dropNode.parent.data.catId == undefined ? 0 : dropNode.parent.data.catId;
          siblings = dropNode.parent.childNodes;
        }
        else{
          pCid = dropNode.data.catId;
          siblings = dropNode.childNodes;
        }
        // 当前拖拽节点最新顺序
        for(let i = 0; i < siblings.length; i++){
          if(siblings[i].data.catId == draggingNode.data.catId){
            // 如果遍历得是当前正在拖拽得节点
            let catLevel = draggingNode.level;
            if(siblings[i].level != draggingNode.level){
              // 当前节点得层级发生变化
               catLevel = siblings[i].level;
              // 修改其他子节点得层级
              this.updateChildNodeLevel(siblings[i])
            }
            this.updateNodes.push({catId:siblings[i].data.catId, sort:i, parentCid:pCid, catLevel:catLevel});

          }
          else{
              this.updateNodes.push({catId:siblings[i].data.catId, sort:i});
          }
          
        }
        if(dropType == "inner"){

        }
        else{

        }
        // 当前拖拽节点最新层级
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
      allowDrag(draggingNode) {
        return true;
      },

      countNodeLevel(node){
        if(node.children != null && node.children.length > 0){
          for(let i =0; i < node.children.length; i++){
            if(node.children[i].catLevel > this.maxLevel){
              this.maxLevel = node.children[i].catLevel;
            }
            this.countNodeLevel(node.children[i]);
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