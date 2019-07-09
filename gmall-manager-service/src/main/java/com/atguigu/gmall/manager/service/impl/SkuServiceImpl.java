package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.es.SkuBaseAttrEsVo;
import com.atguigu.gamll.manager.sku.*;
import com.atguigu.gamll.manager.spu.SpuSaleAttr;
import com.atguigu.gmall.manager.constant.RedisCacheKeyConst;
import com.atguigu.gmall.manager.manager.SkuEsService;
import com.atguigu.gmall.manager.manager.SkuService;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuAttrValueMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuImageMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuSaleAttrValueMapper;
import com.atguigu.gmall.manager.mapper.spu.SpuSaleAttrMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SkuServiceImpl implements SkuService{
    @Reference
    SkuEsService skuEsService;
    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    JedisPool jedisPool;
    @Override
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id) {

        return baseAttrInfoMapper.getBaseAttrInfoByCatalog3Id(catalog3Id);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(Integer spuId) {
        return spuSaleAttrMapper.getSpuSaleAttrBySpuId(spuId);
    }

    @Override
    public void saveBigSkuInfo(SkuInfo skuinfo) {
        //先保存info的基本信息
        skuInfoMapper.insert(skuinfo);
        //再保存提交的图片，平台属性，销售属性
        List<SkuImage> skuImages = skuinfo.getSkuImages();
        for (SkuImage skuImage : skuImages) {
            //设置sku的id
            skuImage.setSkuId(skuinfo.getId());
            skuImageMapper.insert(skuImage);
        }


        List<SkuAttrValue> skuAttrValues = skuinfo.getSkuAttrValues();
        for (SkuAttrValue skuAttrValue : skuAttrValues) {
            //设置sku的id
            skuAttrValue.setSkuId(skuinfo.getId());
            skuAttrValueMapper.insert(skuAttrValue);
        }

        List<SkuSaleAttrValue> skuSaleAttrValues = skuinfo.getSkuSaleAttrValues();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValues) {
            //设置sku的id
            skuSaleAttrValue.setSkuId(skuinfo.getId());
            skuSaleAttrValueMapper.insert(skuSaleAttrValue);
        }

    }

    @Override
    public List<SkuInfo> getSkuInfoBySpuId(Integer spuId) {
        return skuInfoMapper.selectList(
                new QueryWrapper<SkuInfo>().eq("spu_id",spuId));
    }

    @Override
    public SkuInfo getSkuInfoBySkuId(Integer skuId) throws InterruptedException {
        Jedis jedis = jedisPool.getResource();
        String key = RedisCacheKeyConst.SKU_INFO_PREFIX+skuId+RedisCacheKeyConst.SKU_INFO_SUFFIX;
        SkuInfo result = null;
        String s = jedis.get(key);
        if(s!=null) {//如果缓存中有这个key
            log.debug("缓存中找到了skuid为{}的数据",skuId);
            result = JSON.parseObject(s,SkuInfo.class);
        } else {
            log.debug("缓存中没找到");
            String token = UUID.randomUUID().toString();
            //加锁
            String lock = jedis.set(RedisCacheKeyConst.LOCK_SKU_INFO+":"+skuId, token, "NX", "EX", RedisCacheKeyConst.LOCK_TIMEOUT);
            if(lock == null) { //没拿到锁
                log.debug("没获取到锁");
                Thread.sleep(1000);
                getSkuInfoBySkuId(skuId);
            } else if("OK".equals(lock)){//如果拿到锁
                log.debug("获取到锁");
                //从数据库查找该sku
                result = getFromDb(skuId);
                //将对象转为json
                String skuInfoJson = JSON.toJSONString(result);
                //将skuInfoJson存入redis
                //sku:1:info ---  xxxx
                jedis.setex(key,RedisCacheKeyConst.SKU_INFO_TIMEOUT,skuInfoJson);

                //判断是否还是我的锁，如果是才删
                if(token.equals(jedis.get(RedisCacheKeyConst.LOCK_SKU_INFO))) {
                    //释放锁
                    jedis.del(RedisCacheKeyConst.LOCK_SKU_INFO);
                }

            }

        }
        jedis.close();

        return result;
    }

    private SkuInfo getFromDb(Integer skuId) {
        log.debug("从数据准备获取skuId是：{}的商品信息",skuId);
        //查出sku的基本信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if(skuInfo == null){
            //即使没有数据也返回出去放在缓存
            return null;
        }
        //查出sku的图片信息
        List<SkuImage> skuImages = skuImageMapper.selectList(new QueryWrapper<SkuImage>().eq("sku_id", skuInfo.getId()));
        skuInfo.setSkuImages(skuImages);
        //3、查出整个skuAttrValue信息
        List<SkuAllSaveAttrAndValueTo> skuAllSaveAttrAndValueTos = skuImageMapper.getSkuAllSaveAttrAndValue(skuInfo.getId(),skuInfo.getSpuId());
        skuInfo.setSkuAllSaveAttrAndValueTos(skuAllSaveAttrAndValueTos);
        return skuInfo;
    }

    @Override
    public List<SkuAttrValueMappingTo> getSkuAttrValueMapping(Integer spuId) {
        return skuInfoMapper.getSkuAttrValueMapping(spuId);
    }

    @Override
    public List<SkuBaseAttrEsVo> getSkuBaseAttrValueIds(Integer skuId) {
        List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.selectList(new QueryWrapper<SkuAttrValue>()
                .eq("sku_id", skuId));

        List<SkuBaseAttrEsVo> result = new ArrayList<>();
        for (SkuAttrValue skuAttrValue : skuAttrValues) {
            Integer valueId = skuAttrValue.getValueId();
            SkuBaseAttrEsVo vo = new SkuBaseAttrEsVo();
            vo.setValueId(valueId);
            result.add(vo);
        }


        return result;
    }

    @Override
    public List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(List<Integer> valueIds) {

        return baseAttrInfoMapper.getBaseAttrInfoGroupByValueId(valueIds);
    }

    /**
     * 增加商品热度
     * @param skuId
     */
    @Async
    @Override
    public void incrSkuHotScore(Integer skuId) {
        Jedis jedis = jedisPool.getResource();
        Long hincrBy = jedis.hincrBy(RedisCacheKeyConst.SKU_HOT_SCORE, skuId + "", 1);
        if(hincrBy % 3 == 0) {
            //更新es的热度
            skuEsService.updateHotScore(skuId,hincrBy);
        }
    }


}
