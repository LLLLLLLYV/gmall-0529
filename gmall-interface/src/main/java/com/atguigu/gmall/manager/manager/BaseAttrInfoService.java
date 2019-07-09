package com.atguigu.gmall.manager.manager;

import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.BaseAttrValue;

import java.util.List;

/**
 * 平台属性
 */
public interface BaseAttrInfoService {

    /**
     * 根据AttrInfo对象，保存或修改AttrInfo和AttrValue
     * @param baseAttrInfo
     */
    void saveOrUpdateToAttrInfoAndAttrValue(BaseAttrInfo baseAttrInfo);

    /**
     * 根据catalog3Id查询对应的info信息
     * @param C3Id
     * @return
     */
    List<BaseAttrInfo> getAllBaseAttrInfoByC3Id(Integer C3Id);

    /**
     * 根据attrInfoId查询对应的value信息
     * @param BaseAttrInfoid
     * @return
     */
    List<BaseAttrValue> getBaseAttrValueByAttrId(Integer BaseAttrInfoid);
}
