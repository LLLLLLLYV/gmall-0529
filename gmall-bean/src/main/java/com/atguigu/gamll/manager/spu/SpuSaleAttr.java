package com.atguigu.gamll.manager.spu;

import com.atguigu.gamll.manager.SuperBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Spu销售属性
 */
@Data
public class SpuSaleAttr implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    //spu_id  sale_attr_id  sale_attr_name
    private Integer spuId;
    private Integer saleAttrId;
    private String saleAttrName;
    @TableField(exist = false)
    private List<SpuSaleAttrValue> saleAttrValues;
}
