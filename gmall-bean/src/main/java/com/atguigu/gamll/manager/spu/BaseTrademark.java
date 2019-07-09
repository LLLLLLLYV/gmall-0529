package com.atguigu.gamll.manager.spu;

import com.atguigu.gamll.manager.SuperBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 品牌表
 */
@Data
public class BaseTrademark implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //tm_name  logo_url  is_enable
    private String  tmName; //品牌名字
    private String logoUrl;//品牌url地址
    private String isEnable;//是否启用

}
