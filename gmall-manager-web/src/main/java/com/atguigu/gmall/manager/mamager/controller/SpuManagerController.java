package com.atguigu.gmall.manager.mamager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gamll.manager.spu.BaseSaleAttr;
import com.atguigu.gamll.manager.spu.SpuInfo;
import com.atguigu.gmall.manager.manager.SpuInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/spu")
public class SpuManagerController {

    @Reference
    SpuInfoService spuInfoService;

    @ResponseBody
    @RequestMapping(value = "/bigSave")
    public String saveAllSpuInfos(@RequestBody SpuInfo spuInfo) {
        spuInfoService.saveBigSpuInfo(spuInfo);
        return "ok";
    }
    @RequestMapping("/base_sale_attr")
    @ResponseBody
    public List<BaseSaleAttr> getBaseSaleAttr() {
        System.out.println(spuInfoService.getBaseSaleAttr());
        return spuInfoService.getBaseSaleAttr();
    }

    /**
     * 加载spu内容
     * @return
     */
    @RequestMapping("/listPage.html")
    public String spuListPage() {
        return "spu/spuListPage";
    }

    /**
     * 加载catalog3对应的spuInfo信息
     * @return
     */
    @RequestMapping("/spuInfoList")
    @ResponseBody
    public List<SpuInfo> getSpuInfoList(Integer id) {
        return spuInfoService.getSpuInfoList(id);
    }
}
