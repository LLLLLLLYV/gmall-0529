package com.atguigu.gmall.manager.mapper.sku;

import com.atguigu.gamll.manager.sku.SkuAllSaveAttrAndValueTo;
import com.atguigu.gamll.manager.sku.SkuImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkuImageMapper extends BaseMapper<SkuImage> {

    List<SkuAllSaveAttrAndValueTo> getSkuAllSaveAttrAndValue(@Param("id") Integer id, @Param("spuId") Integer spuId);
}
