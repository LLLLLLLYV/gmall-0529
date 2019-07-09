package com.atguigu.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gamll.manager.es.SkuSearchParamEsVo;
import com.atguigu.gamll.manager.es.SkuSearchResultEsVo;
import com.atguigu.gmall.annotation.LoginRequired;
import com.atguigu.gmall.manager.manager.SkuEsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ListController {
    @Reference
    SkuEsService skuEsService;


    @LoginRequired
    @RequestMapping("/hehe")
    public String hehe() {
        return "hehe";
    }



    /**
     * 将页面可能用到的数据封装入参
     * @param paramEsVo
     * @return
     */
    @RequestMapping("/list.html")
    public String listPage(SkuSearchParamEsVo paramEsVo, Model model) {

        //搜索完成后返回的数据
        SkuSearchResultEsVo searchResult = skuEsService.searchSkuFromES(paramEsVo);
        model.addAttribute("searchResult",searchResult);

        return "list";
    }
}
