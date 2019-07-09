package com.atguigu.gmall.manager.manager;

import com.atguigu.gamll.manager.es.SkuSearchParamEsVo;
import com.atguigu.gamll.manager.es.SkuSearchResultEsVo;

public interface SkuEsService {

    /**
     * 商品的上架
     * @param skuId
     */
    void onSale(Integer skuId);

    SkuSearchResultEsVo searchSkuFromES(SkuSearchParamEsVo paramEsVo);

    /**
     * 更新es中商品的热度值
     * @param skuId
     * @param hincrBy
     */
    void updateHotScore(Integer skuId, Long hincrBy);
}
