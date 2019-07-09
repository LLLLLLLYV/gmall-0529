package com.atguigu.gamll.manager.sku;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * sku销售属性值表
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkuSaleAttrValue implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    //sku_id  sale_attr_id  sale_attr_value_id  sale_attr_name  sale_attr_value_name
    private Integer skuId;//
    private Integer saleAttrId;//销售属性的id
    private String saleAttrName;//销售属性的名字（冗余） ===【颜色】

    private Integer saleAttrValueId;//销售属性值id
    private String saleAttrValueName;//销售属性值的名字  ====【红色】


}
