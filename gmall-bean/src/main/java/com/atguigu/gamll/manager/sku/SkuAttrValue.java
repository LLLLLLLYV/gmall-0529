package com.atguigu.gamll.manager.sku;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Sku与平台属性关联信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkuAttrValue implements Serializable{
    @TableId(type = IdType.AUTO)
    private Integer id;
    //attr_id  value_id  sku_id
    private Integer attrId;//平台属性id
    private Integer valueId;//平台属性值id
    private Integer skuId;//对应的skuId

}
