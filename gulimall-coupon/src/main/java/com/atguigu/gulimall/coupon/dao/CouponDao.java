package com.atguigu.gulimall.coupon.dao;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author jacklam
 * @email jacklam200@gmail.com
 * @date 2022-02-06 20:20:46
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
