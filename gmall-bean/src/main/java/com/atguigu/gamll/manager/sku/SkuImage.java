package com.atguigu.gamll.manager.sku;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkuImage implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    //sku_id  img_name       img_url  spu_img_id  is_default
    private Integer skuId;//当前图片对应的skuId
    private String imgName;//图片的名字
    private String imgUrl;//图片的url
    private Integer spuImgId;//图片对应的spu_image表中的id
    private String isDefault;//是否默认图片


}
