package com.atguigu.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.member.entity.UmsMemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author jacklam
 * @email jacklam200@gmail.com
 * @date 2022-02-06 20:29:17
 */
public interface UmsMemberService extends IService<UmsMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

