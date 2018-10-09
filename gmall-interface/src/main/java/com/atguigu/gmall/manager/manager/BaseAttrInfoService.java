package com.atguigu.gmall.manager.manager;

import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.BaseAttrValue;

import java.util.List;

/**
 * 平台属性
 */
public interface BaseAttrInfoService {

    public List<BaseAttrInfo> getAllBaseAttrInfoByC3Id(Integer C3Id);

    public List<BaseAttrValue> getBaseAttrValueByAttrId(Integer BaseAttrInfoid);
}
