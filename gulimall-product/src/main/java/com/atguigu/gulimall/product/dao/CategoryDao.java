package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author jacklam
 * @email jacklam200@gmail.com
 * @date 2022-02-05 16:15:37
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
