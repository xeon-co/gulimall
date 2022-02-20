<!-- # https://element.eleme.cn/#/en-US/component/tree -->
<template>
    <div>
        <h1>三级分类维护</h1>
        <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
    </div>
</template>

<script>
  export default {
    data() {
      return {
        data: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      };
    },
    methods: {
      handleNodeClick(data) {
        console.log(data);
      },
      getMenu(){
        this.$http({
            url: this.$http.adornUrl("/product/category/list/tree"),
            method: "get"
            }
        ).then((data)=>{
            console.log("数据:"+data)
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