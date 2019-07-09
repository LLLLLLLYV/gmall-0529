package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.BaseAttrValue;
import com.atguigu.gmall.manager.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@Service
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    /**
     * 根据AttrInfo对象，保存或修改AttrInfo和AttrValue
     * @param baseAttrInfo
     */
    @Override
    public void saveOrUpdateToAttrInfoAndAttrValue(BaseAttrInfo baseAttrInfo) {
        if(baseAttrInfo.getId()!=null) {
            /*修改info的name*/
            baseAttrInfoMapper.updateById(baseAttrInfo);

            /*value数据的增删改*/
            List<BaseAttrValue> attrValues = baseAttrInfo.getAttrValues();
            /*删除没有提交的value数据
            * 如果数据库中，没有现在返回回来的value的id则证明此value已被删除
            * */

            List<Integer> ids = new ArrayList<>(); //提交的value的id
            for (BaseAttrValue attrValue : attrValues) {
                Integer id = attrValue.getId();
                /*将已提交的id保存到ids中，并修改其对应的数据*/
                if(id != null) {
                    ids.add(id);
                }
            }

            /*删除value*/
            QueryWrapper<BaseAttrValue> deleteValueWrapper = new QueryWrapper<BaseAttrValue>()
                    .notIn("id",ids).eq("attr_id", baseAttrInfo.getId());
            baseAttrValueMapper.delete(deleteValueWrapper);


            for (BaseAttrValue attrValue : attrValues) {
                /*将已提交的id保存到ids中，并修改其对应的数据*/
                if(attrValue.getId()!= null) {
                    baseAttrValueMapper.updateById(attrValue);
                } else {
                    /*新增value*/
                    baseAttrValueMapper.insert(attrValue);
                }
            }
        }
    }

    /**
     * 根据catalog3Id查询对应的info信息
     * @param C3Id
     * @return
     */
    @Override
    public List<BaseAttrInfo> getAllBaseAttrInfoByC3Id(Integer C3Id) {
        QueryWrapper<BaseAttrInfo> queryWrapper = new QueryWrapper<BaseAttrInfo>().eq("catalog3_id", C3Id);
        return baseAttrInfoMapper.selectList(queryWrapper);
    }

    /**
     * 根据attrInfoId查询对应的value信息
     * @param BaseAttrInfoid
     * @return
     */
    @Override
    public List<BaseAttrValue> getBaseAttrValueByAttrId(Integer BaseAttrInfoid) {
        QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<BaseAttrValue>().eq("attr_id", BaseAttrInfoid);
        return baseAttrValueMapper.selectList(queryWrapper);
    }
}
