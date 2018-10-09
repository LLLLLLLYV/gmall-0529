package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.BaseAttrValue;
import com.atguigu.gmall.manager.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAllBaseAttrInfoByC3Id(Integer C3Id) {
        QueryWrapper<BaseAttrInfo> queryWrapper = new QueryWrapper<BaseAttrInfo>().eq("catalog3_id", C3Id);
        return baseAttrInfoMapper.selectList(queryWrapper);
    }

    @Override
    public List<BaseAttrValue> getBaseAttrValueByAttrId(Integer BaseAttrInfoid) {
        QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<BaseAttrValue>().eq("attr_id", BaseAttrInfoid);
        return baseAttrValueMapper.selectList(queryWrapper);
    }
}
