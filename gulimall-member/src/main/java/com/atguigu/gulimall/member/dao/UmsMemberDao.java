package com.atguigu.gulimall.member.dao;

import com.atguigu.gulimall.member.entity.UmsMemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author jacklam
 * @email jacklam200@gmail.com
 * @date 2022-02-06 20:29:17
 */
@Mapper
public interface UmsMemberDao extends BaseMapper<UmsMemberEntity> {
	
}
