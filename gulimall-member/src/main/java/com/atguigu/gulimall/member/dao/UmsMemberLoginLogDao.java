package com.atguigu.gulimall.member.dao;

import com.atguigu.gulimall.member.entity.UmsMemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author jacklam
 * @email jacklam200@gmail.com
 * @date 2022-02-06 20:29:17
 */
@Mapper
public interface UmsMemberLoginLogDao extends BaseMapper<UmsMemberLoginLogEntity> {
	
}
