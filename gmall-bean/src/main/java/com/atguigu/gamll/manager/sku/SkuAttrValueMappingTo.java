package com.atguigu.gamll.manager.sku;

import lombok.Data;

import java.io.Serializable;

/**
 * 保存销售属性值和skuId的映射关系
 */
@Data
public class SkuAttrValueMappingTo implements Serializable {
    private Integer skuId;
    private Integer spuId;
    private String skuName;
    private String saleAttrValueIdMapping;//销售属性值的映射
    private String saleAttrValueNameMapping;//销售属性值的名的映射
}
