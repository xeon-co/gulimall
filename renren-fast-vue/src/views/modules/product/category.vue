<!-- # https://element.eleme.cn/#/en-US/component/tree -->
<template>
    <div>
        <h1>三级分类维护</h1>
        <el-tree :data="data" 
        :props="defaultProps" 
        show-checkbox 
        node-key="catId"
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
              v-if="node.childNodes.length == 0"
              type="text"
              size="mini"
              @click="() => remove(node, data)">
              Delete
            </el-button>
          </span>
        </span>
        </el-tree>
    </div>
</template>

<script>
  export default {
    data() {
      return {
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      };
    },
    methods: {
      handleNodeClick(data) {
        console.log(data);
      },
      append(data){
        console.log("数据append:", data)
      },
      remove(node, data){
        console.log("数据remove node:", node)
        console.log("数据remove data:", data)
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