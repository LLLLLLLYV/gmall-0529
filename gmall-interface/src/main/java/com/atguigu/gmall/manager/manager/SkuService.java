package com.atguigu.gmall.manager.manager;

import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.es.SkuBaseAttrEsVo;
import com.atguigu.gamll.manager.sku.SkuAttrValueMappingTo;
import com.atguigu.gamll.manager.sku.SkuInfo;
import com.atguigu.gamll.manager.spu.SpuSaleAttr;

import java.util.List;

/**
 * sku服务组件
 */
public interface SkuService {

    /**
     * 根据catalog3Id查询对应的平台属性名和值
     * @param catalog3Id
     * @return
     */
    List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id);

    /**
     * 根据spuId查询对应的销售属性的名和值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrBySpuId(Integer spuId);

    void saveBigSkuInfo(SkuInfo skuinfo);

    /**
     * 获取sku对应的skuinfo
     * @param skuId
     * @return
     */
    List<SkuInfo> getSkuInfoBySpuId(Integer skuId);

    /**
     * 查询某个sku的详细信息
     * @param skuId
     * @return
     */
    SkuInfo getSkuInfoBySkuId(Integer skuId) throws InterruptedException;

    /**
     * 传入spuId查询对应的销售属性值及其名的映射
     * @param spuId
     * @return
     */
    List<SkuAttrValueMappingTo> getSkuAttrValueMapping(Integer spuId);

    /**
     * 获取sku所有平台属性值得id
     * @param skuId
     * @return
     */
    List<SkuBaseAttrEsVo> getSkuBaseAttrValueIds(Integer skuId);

    /**
     * 查询涉及到的平台属性及其值
     * @param valueIds 平台属性值的集合
     * @return
     */
    List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(List<Integer> valueIds);

    /**
     * 增加某个商品的热度
     * @param skuId
     */
    void incrSkuHotScore(Integer skuId);
}
