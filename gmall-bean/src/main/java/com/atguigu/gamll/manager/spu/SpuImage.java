package com.atguigu.gamll.manager.spu;

import com.atguigu.gamll.manager.SuperBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * Spu图片信息
 */
@Data
public class SpuImage implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    //spu_id  img_name  img_url
    private Integer spuId;//图片对应的商品id
    private String imgName;//图片的名字
    private String imgUrl;//图片的url地址

}
