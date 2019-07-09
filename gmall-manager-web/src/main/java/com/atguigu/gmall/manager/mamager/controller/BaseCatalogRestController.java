package com.atguigu.gmall.manager.mamager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.BaseCatalog1;
import com.atguigu.gamll.manager.BaseCatalog2;
import com.atguigu.gamll.manager.BaseCatalog3;
import com.atguigu.gmall.manager.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.manager.CatalogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 这个Controller用来给easyui提供json数据
 */
@RequestMapping("/basecatalog")
@RestController
public class BaseCatalogRestController {

    @Reference
    CatalogService catalogService;

    @Reference
    BaseAttrInfoService baseAttrInfoService;

    @RequestMapping("/1/list.json")
    public List<BaseCatalog1> listBaseCatalog1() {
        return catalogService.getAllBaseCatalog1();
    }
    @RequestMapping("/2/list.json")
    public List<BaseCatalog2> listBaseCatalog2(Integer id) {
        return catalogService.getAllBaseCatalog2ByC1Id(id);
    }
    @RequestMapping("/3/list.json")
    public List<BaseCatalog3> listBaseCatalog3(Integer id) {
        return catalogService.getAllBaseCatalog3(id);
    }
    @RequestMapping("/attrsInfo.json")
    public List<BaseAttrInfo> listBaseAttrInfoByC3Id(Integer id) {
        return baseAttrInfoService.getAllBaseAttrInfoByC3Id(id);
    }
}
