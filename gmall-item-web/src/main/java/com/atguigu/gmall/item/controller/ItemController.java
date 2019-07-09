package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gamll.manager.sku.SkuAttrValueMappingTo;
import com.atguigu.gamll.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.manager.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
public class ItemController {

    @Reference
    SkuService skuService;



    /**
     * 商品信息的详情
     * @param skuId
     * @param model
     * @return
     */
    @RequestMapping("/{skuId}.html" )
    public String itemPage(@PathVariable("skuId")Integer skuId, Model model) {
        //查出sku的详细信息
        SkuInfo skuInfo = null;
        try {
            skuInfo = skuService.getSkuInfoBySkuId(skuId);
        } catch (InterruptedException e) {
            log.error("com.atguigu.gmall.item.controller.ItemController的itemPage出错");
        }
        model.addAttribute("skuInfo",skuInfo);
        Integer spuId = skuInfo.getSpuId();
        //查出sku对应的spu下的所有spu销售属性组合
        List<SkuAttrValueMappingTo> valueMappingTos = skuService.getSkuAttrValueMapping(spuId);
        model.addAttribute("skuValueMapping",valueMappingTos);

        //增加点击率，更新es的hotScore值
        //用redis把商品热度保存起来，到达一定数量增加到es
        skuService.incrSkuHotScore(skuId);
        return "item";
    }

}
