package com.atguigu.gmall.manager.mamager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.BaseAttrValue;
import com.atguigu.gmall.manager.mamager.vo.BaseAttrInfoAndAttrValueVo;
import com.atguigu.gmall.manager.manager.BaseAttrInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/attr")
@Controller
@Slf4j
public class AttrManagerController {

    @Reference
    BaseAttrInfoService baseAttrInfoService;

    @RequestMapping(value = "/updates",method = RequestMethod.POST)
    @ResponseBody
    public void saveOrUpdateToAttrInfoAndAttrValue(@RequestBody BaseAttrInfoAndAttrValueVo bav) {
        /*判断是添加info还是修改info，如果bav的id去空格后为null或空字符串则是添加*/
        if(bav.getId()!=null) {
            /*将vo数据复制到bean中*/
            BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
            BeanUtils.copyProperties(bav,baseAttrInfo);
            baseAttrInfoService.saveOrUpdateToAttrInfoAndAttrValue(baseAttrInfo);
        } else {

        }

    }


   /* 获取传入的info的id对应的value信息*/
   @RequestMapping("/attrValue/{id}")
   @ResponseBody
    public List<BaseAttrValue> getAttrValueByAttrInfoId(@PathVariable("id") Integer attrInfoId){
        return  baseAttrInfoService.getBaseAttrValueByAttrId(attrInfoId);
    }

    /**
     * 去往平台属性页面
     * @return
     */

    @RequestMapping("/attrListPage.html")
    public String attrListPage() {
        return "attr/attrListPage";
    }

    /**
     * 去往平台属性添加，修改，的对话框页面
     * @return
     */
    @RequestMapping("/arrtEdit")
    public String attrEdit() {
        return "attr/attrEdit";
    }
}
