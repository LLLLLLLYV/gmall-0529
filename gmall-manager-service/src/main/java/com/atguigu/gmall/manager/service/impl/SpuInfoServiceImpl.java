package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gamll.manager.spu.*;
import com.atguigu.gmall.manager.manager.SpuInfoService;
import com.atguigu.gmall.manager.mapper.spu.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class SpuInfoServiceImpl implements SpuInfoService {
    @Autowired
    SpuInfoMapper spuInfoMapper;
    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Override
    public List<SpuInfo> getSpuInfoList(Integer catalog3Id) {
        QueryWrapper<SpuInfo> spuInfowrapper =
                new QueryWrapper<SpuInfo>().eq("catalog3_id", catalog3Id);
        return spuInfoMapper.selectList(spuInfowrapper);
    }

    /**
     * 查询基本销售属性
     * @return
     */
    @Override
    public List<BaseSaleAttr> getBaseSaleAttr() {
        return baseSaleAttrMapper.selectList(null);
    }

    /**
     * spuInfo的保存
     * @param spuInfo
     */
    @Override
    public void saveBigSpuInfo(SpuInfo spuInfo) {
        //1保存spu的基本信息
        spuInfoMapper.insert(spuInfo);
        //获取刚才保存到spu的id
        Integer spuId = spuInfo.getId();
        //2.获取spu的图片信息
        List<SpuImage> spuImages = spuInfo.getSpuImages();
        //图片信息中添加spuId,并保存
        for (SpuImage spuImage : spuImages) {
            spuImage.setSpuId(spuId);
            spuImageMapper.insert(spuImage);
        }
        //3.保存spu的所有销售属性信息
        List<SpuSaleAttr> spuSaleAttrs = spuInfo.getSpuSaleAttrs();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrs) {
            spuSaleAttr.setSpuId(spuId);
            spuSaleAttrMapper.insert(spuSaleAttr);
            //4.保存spu的销售属性值的信息
            List<SpuSaleAttrValue> saleAttrValue = spuSaleAttr.getSaleAttrValues();
            for (SpuSaleAttrValue spuSaleAttrValue : saleAttrValue) {
                //设置spuId
                spuSaleAttrValue.setSpuId(spuId);
                //设置saleAttrId
                spuSaleAttrValue.setSaleAttrId(spuSaleAttr.getSaleAttrId());
                spuSaleAttrValueMapper.insert(spuSaleAttrValue);
            }

        }


    }

    @Override
    public List<SpuImage> getSpuImages(Integer spuId) {
        return spuImageMapper.selectList(new QueryWrapper<SpuImage>().eq("spu_id",spuId));
    }
}
