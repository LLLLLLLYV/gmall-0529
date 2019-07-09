package com.atguigu.gmall.manager.mamager.vo;

import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.BaseAttrValue;
import lombok.Data;

import java.util.List;

@Data
public class BaseAttrInfoAndAttrValueVo {
    private Integer id;

    private String attrName;

    private Integer catalog3Id;

    private List<BaseAttrValue> attrValues;

    private List<BaseAttrInfo> attrInfos;
}
