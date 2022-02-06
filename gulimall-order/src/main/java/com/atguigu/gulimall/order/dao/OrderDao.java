package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author jacklam
 * @email jacklam200@gmail.com
 * @date 2022-02-06 20:37:04
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
