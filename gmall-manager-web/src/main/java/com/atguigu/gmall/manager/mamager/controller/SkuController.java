package com.atguigu.gmall.manager.mamager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.sku.SkuInfo;
import com.atguigu.gamll.manager.spu.SpuImage;
import com.atguigu.gamll.manager.spu.SpuSaleAttr;
import com.atguigu.gmall.manager.manager.SkuEsService;
import com.atguigu.gmall.manager.manager.SkuService;
import com.atguigu.gmall.manager.manager.SpuInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@RequestMapping("/sku")
@ResponseBody
@Controller
public class SkuController {
    @Reference
    SkuService skuService;
    @Reference
    SpuInfoService spuInfoService;

    @Reference
    SkuEsService skuEsService;
    /**
     * 传入skuId,将商品上架(存入es中)
     * @param skuId
     * @return
     */
    @ResponseBody
    @RequestMapping("/onSale")
    public String onSale(@RequestParam("skuId") Integer skuId) {
        skuEsService.onSale(skuId);
        return "ok";
    }

    @RequestMapping("/skuinfo")
    public List<SkuInfo> getSkuInfoBySpuId(@RequestParam("id") Integer spuId) {
        return skuService.getSkuInfoBySpuId(spuId);
    }


    @RequestMapping("/skuBigSave")
    public String skuBigSave(@RequestBody SkuInfo skuinfo) {
        skuService.saveBigSkuInfo(skuinfo);
        return "Ok";
    }


    /**
     * 查询spu下的所有图片
     * @param spuId
     * @return
     */
    @RequestMapping("/spuImges")
    public List<SpuImage> getSpuImages(@RequestParam("id") Integer spuId) {
        return  spuInfoService.getSpuImages(spuId);
    }
    /**
     * 根据catalog3Id查询平台属性名，及其值
     * @param catalog3Id
     * @return
     */
    @RequestMapping("/base_attr_info.json")
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(@RequestParam("id") Integer catalog3Id) {
        return skuService.getBaseAttrInfoByCatalog3Id(catalog3Id);
    }
    @RequestMapping("/spu_sale_attr.json")
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(@RequestParam("id") Integer spuId) {
        return skuService.getSpuSaleAttrBySpuId(spuId);
    }
}
