package com.atguigu.gamll.manager.es;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuInfoEsVo implements Serializable{

    Integer id;//skuInfo的id

    BigDecimal price;//sku的价格

    String skuName;//sku的名字 //全文检索，分词

    String skuDesc;//sku的描述 //全文检索，分词

    Integer catalog3Id;//sku的三级分类id

    String skuDefaultImg;//sku的默认图片

    Long hotScore=0L;//热度评分


    List<SkuBaseAttrEsVo> baseAttrEsVos;//把sku的平台属性的值保存下来，只需要平台属性的id
}
