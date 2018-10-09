package com.atguigu.gmall.manager.mamager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/attr")
@Controller
public class AttrManagerController {

    @RequestMapping("/attrListPage.html")
    public String attrListPage() {
        return "attr/attrListPage";
    }
}
