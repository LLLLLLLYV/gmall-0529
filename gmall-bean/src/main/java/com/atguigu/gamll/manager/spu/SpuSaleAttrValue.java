package com.atguigu.gamll.manager.spu;

import com.atguigu.gamll.manager.SuperBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * Spu销售属性的值
 */
@Data
public class SpuSaleAttrValue implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //spu_id  sale_attr_id  sale_attr_value_name
    private Integer spuId;
    private Integer saleAttrId;
    private String  saleAttrValueName;
}
