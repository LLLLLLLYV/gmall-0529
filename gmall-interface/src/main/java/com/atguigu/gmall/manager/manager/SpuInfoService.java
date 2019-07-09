package com.atguigu.gmall.manager.manager;

import com.atguigu.gamll.manager.spu.BaseSaleAttr;
import com.atguigu.gamll.manager.spu.SpuImage;
import com.atguigu.gamll.manager.spu.SpuInfo;

import java.util.List;

public interface SpuInfoService {

    List<SpuInfo> getSpuInfoList (Integer catalog3Id);

    List<BaseSaleAttr> getBaseSaleAttr();

    void saveBigSpuInfo(SpuInfo spuInfo);

    /**
     * 查询spu的所有图片
     * @param spuId
     * @return
     */
    List<SpuImage> getSpuImages(Integer spuId);
}
