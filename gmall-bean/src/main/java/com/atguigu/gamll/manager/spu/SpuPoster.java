package com.atguigu.gamll.manager.spu;

import com.atguigu.gamll.manager.SuperBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * Spu海报
 */
@Data
public class SpuPoster implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //spu_id  img_name  img_url
    private Integer spuId;//关联的商品id
    private String imgName;//海报图片的名字
    private String imgUrl;//海报图片的url地址
}
