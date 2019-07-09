package com.atguigu.gamll.manager.es;

import lombok.Data;

import java.io.Serializable;
@Data
public class SkuSearchParamEsVo implements Serializable{
    String keyword;  //关键字搜索
    Integer catalog3Id; //根据3及分类id搜索
    Integer[] valueId;//根据平台属性值搜索，可能有多个
    Integer pageNo = 1;//页码，默认打开第一页
    Integer pageSize = 12;//每页显示个数
    String sortField = "hotScore"; //排序方式：热度评分
    String sortOrder = "desc";  //降序
}
