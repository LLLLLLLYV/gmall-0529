package com.atguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.es.SkuBaseAttrEsVo;
import com.atguigu.gamll.manager.es.SkuInfoEsVo;
import com.atguigu.gamll.manager.es.SkuSearchParamEsVo;
import com.atguigu.gamll.manager.es.SkuSearchResultEsVo;
import com.atguigu.gamll.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.manager.SkuEsService;
import com.atguigu.gmall.manager.manager.SkuService;
import com.atguigu.gmall.search.constant.EsConstant;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SkuEsServiceImpl implements SkuEsService {

    @Reference
    SkuService skuService;
    @Autowired
    JestClient jestClient;
    @Override
    public void onSale(Integer skuId) {
        //1.查出这个sku对应的详细信息
        try {
            SkuInfo skuInfo = skuService.getSkuInfoBySkuId(skuId);

            SkuInfoEsVo skuInfoEsVo = new SkuInfoEsVo();
            //将查处的skuInfo的信息拷贝到Vo中
            BeanUtils.copyProperties(skuInfo,skuInfoEsVo);
            //查询当前skuId对应的平台属性值id
            List<SkuBaseAttrEsVo> vos= skuService.getSkuBaseAttrValueIds(skuId);
            skuInfoEsVo.setBaseAttrEsVos(vos);

            Index build = new Index.Builder(skuInfoEsVo).index(EsConstant.GMALL_INDEX)
                    .type(EsConstant.GMALL_SKU_TYPE).id(skuInfoEsVo.getId() + "").build();
            try {
                jestClient.execute(build);
            } catch (IOException e) {
                log.error("es保存出问题：{}",e);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按照查询参数查出页面需要的数据
     * @param paramEsVo
     * @return
     */
    @Override
    public SkuSearchResultEsVo searchSkuFromES(SkuSearchParamEsVo paramEsVo) {
        SkuSearchResultEsVo resultEsVo = null;
        //0.dsl语句拼串
        String queryDsl = buildSkuSearchQuaryDSL(paramEsVo);
        //1.传入dsl语句
        Search build = new Search.Builder(queryDsl).addIndex(EsConstant.GMALL_INDEX)
                .addType(EsConstant.GMALL_SKU_TYPE).build();
        try {
            //2.查询
            SearchResult result =  jestClient.execute(build);
            //3.将查询出来的result封装成能给页面返回的SkuSearchResultEsVo对象
            resultEsVo = buildSkuSearchResult(result);
            resultEsVo.setPageNo(paramEsVo.getPageNo());
            return resultEsVo;
        } catch (IOException e) {
            log.error("es查询出现问题：{}",e);
        }
        return resultEsVo;
    }
    @Async
    @Override
    public void updateHotScore(Integer skuId, Long hincrBy) {
        String updateHotScote = "{\"doc\": {\"hotScore\":"+hincrBy+"}}";
        Update build = new Update.Builder(updateHotScote)
                .index(EsConstant.GMALL_INDEX)
                .type(EsConstant.GMALL_SKU_TYPE)
                .id(skuId+"")
                .build();
        try {
            jestClient.execute(build);
        } catch (IOException e) {
            log.error("hotSroce热度更新，出现问题：{}",e);
        }
    }

    /**
     * 构造queryDSL语句字符串
     * @param paramEsVo
     * @return
     */
    private String buildSkuSearchQuaryDSL(SkuSearchParamEsVo paramEsVo) {
        //创建一个搜索数据的构造器，能帮我们创造出dsl
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if(paramEsVo.getCatalog3Id()!=null) {
            //过滤三级分类信息
            TermQueryBuilder termCatalog3 = new TermQueryBuilder("catalog3Id",paramEsVo.getCatalog3Id());
            boolQuery.filter(termCatalog3);
        }

        if(paramEsVo.getValueId()!=null && paramEsVo.getValueId().length>0) {
            for (Integer vid : paramEsVo.getValueId()) {
                //过滤所有的valueId
                TermQueryBuilder termValueId = new TermQueryBuilder("baseAttrEsVos.valueId",vid);
                boolQuery.filter(termValueId);
            }
        }
        if(!StringUtils.isEmpty(paramEsVo.getKeyword())) {
            //搜索
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",paramEsVo.getKeyword());
            boolQuery.must(matchQueryBuilder);

            //高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuName");
            highlightBuilder.preTags("<span style='color:red' >");
            highlightBuilder.postTags("</span>");
            sourceBuilder.highlight(highlightBuilder);
        }
        sourceBuilder.query(boolQuery);

        


        //排序
        if(!StringUtils.isEmpty(paramEsVo.getSortField())) {
            SortOrder sortOrder =null;
            switch (paramEsVo.getSortOrder()) {
                case "desc":sortOrder = SortOrder.DESC;break;
                case "ads":sortOrder = SortOrder.ASC;break;
                default:sortOrder = SortOrder.DESC;
            }
            sourceBuilder.sort(paramEsVo.getSortField(),sortOrder);
        }


        //分页
        sourceBuilder.from((paramEsVo.getPageNo()-1)*paramEsVo.getPageSize());
        sourceBuilder.size(paramEsVo.getPageSize());

        //聚合
        TermsBuilder termsBuilder = new TermsBuilder("valueIdAggs");
        termsBuilder.field("baseAttrEsVos.valueId");
        sourceBuilder.aggregation(termsBuilder);


        //这个构造器的toString方法能返回DSL语句
        String dsl = sourceBuilder.toString();
        return dsl;
    }

    /**
     * 将查出的结果构造为页面能用的对象数据
     * @param result
     * @return
     */
    private SkuSearchResultEsVo buildSkuSearchResult(SearchResult result) {
        SkuSearchResultEsVo resultEsVo = new SkuSearchResultEsVo();
        List<SkuInfoEsVo> skuInfoEsVoList = null;

        //拿到命中的所有记录
        List<SearchResult.Hit<SkuInfoEsVo, Void>> hits = result.getHits(SkuInfoEsVo.class);
        if(hits == null || hits.size() == 0) {
            return  null;
        } else {
            skuInfoEsVoList= new ArrayList<>(hits.size());
            //遍历所有命中的信息，并存在list中，并设置高亮
            for (SearchResult.Hit<SkuInfoEsVo, Void> hit : hits) {
                SkuInfoEsVo source = hit.source;

                Map<String, List<String>> highlight = hit.highlight;
                //普通非全文搜索无高亮
                if(highlight != null) {
                    String highText = highlight.get("skuName").get(0);
                    //替换为高亮
                    source.setSkuName(highText);
                }
                skuInfoEsVoList.add(source);
            }
        }
        //1.从es搜索的结果中找到所有的SkuInfo信息
        resultEsVo.setSkuInfoEsVos(skuInfoEsVoList);
        //2.设置总记录数
        resultEsVo.setTotal(result.getTotal().intValue());
        //3.从聚合的数据中取出所有的平台属性及其值
        List<BaseAttrInfo> baseAttrInfos = getBaseAttrInfoGroupByValueId(result);
        resultEsVo.setBaseAttrInfos(baseAttrInfos);
        return resultEsVo;
    }

    /**
     * 根据es中查询到的es的结果，找到涉及的平台属性
     * @param result
     * @return
     */
    private List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(SearchResult result) {
        MetricAggregation aggregations = result.getAggregations();
        //获取terms聚合出来的数据
        TermsAggregation valueIdAggs = aggregations.getTermsAggregation("valueIdAggs");
        List<TermsAggregation.Entry> buckets = valueIdAggs.getBuckets();

        List<Integer> valueIds = new ArrayList<>();
        //遍历buckets中的数据
        for (TermsAggregation.Entry bucket : buckets) {
            String key = bucket.getKey();
            valueIds.add(Integer.valueOf(key));
        }
        //查询所有平台属性及其值
        return skuService.getBaseAttrInfoGroupByValueId(valueIds);

    }
}
