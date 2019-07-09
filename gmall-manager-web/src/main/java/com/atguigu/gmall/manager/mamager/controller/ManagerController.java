package com.atguigu.gmall.manager.mamager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManagerController {

    @RequestMapping("main")
    public String index() {
        return "main";
    }
}
