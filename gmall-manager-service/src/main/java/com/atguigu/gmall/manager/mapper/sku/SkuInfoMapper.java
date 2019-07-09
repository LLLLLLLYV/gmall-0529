package com.atguigu.gmall.manager.mapper.sku;

import com.atguigu.gamll.manager.sku.SkuAttrValueMappingTo;
import com.atguigu.gamll.manager.sku.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SkuInfoMapper  extends BaseMapper<SkuInfo> {

    List<SkuAttrValueMappingTo> getSkuAttrValueMapping(Integer spuId);
}
