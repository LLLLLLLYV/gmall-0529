package com.atguigu.gmall.manager.manager;

import com.atguigu.gamll.manager.BaseCatalog1;
import com.atguigu.gamll.manager.BaseCatalog2;
import com.atguigu.gamll.manager.BaseCatalog3;

import java.util.List;

/**
 *操作分类的接口
 */
public interface CatalogService {

    /**
     * 获取所有一级分类
     * @return
     */
    public List<BaseCatalog1> getAllBaseCatalog1();

    /**
     * 根据一级分类id获取相对应的所有二级分类
     * @param C1Id 一级分类id
     * @return
     */
    public List<BaseCatalog2> getAllBaseCatalog2ByC1Id(Integer C1Id);

    /**
     * 根据二级分类id获取相对应的所有三级分类
     * @param C2Id 一级分类id
     * @return
     */
    public List<BaseCatalog3> getAllBaseCatalog3(Integer C2Id);

}
