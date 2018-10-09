package com.atguigu.gmall.manager;

import com.atguigu.gamll.manager.BaseAttrInfo;
import com.atguigu.gamll.manager.BaseCatalog1;
import com.atguigu.gmall.manager.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.manager.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManagerServiceApplicationTests {
	@Autowired
	CatalogService catalogService;
	@Autowired
	BaseAttrInfoService baseAttrInfoService;
	@Test
	public void contextLoads() {

		for (BaseCatalog1 baseCatalog1:catalogService.getAllBaseCatalog1()) {
			System.out.println(baseCatalog1);
		}
	}
	@Test
	public void test1() {
		for (BaseAttrInfo baseAttrInfo:baseAttrInfoService.getAllBaseAttrInfoByC3Id(1)) {
			System.out.println(baseAttrInfo);
		}
	}
}
