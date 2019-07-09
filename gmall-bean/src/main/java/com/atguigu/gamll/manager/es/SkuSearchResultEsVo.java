package com.atguigu.gamll.manager.es;

import com.atguigu.gamll.manager.BaseAttrInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SkuSearchResultEsVo implements Serializable {
    //skuInfo的搜索结果
    private List<SkuInfoEsVo> skuInfoEsVos;
    //总记录数
    private Integer total;
    //当前是第几页数据
    private Integer pageNo;
    //所有可供筛选的平台属性
    private List<BaseAttrInfo> baseAttrInfos;

}
