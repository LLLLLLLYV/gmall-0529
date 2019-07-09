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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManagerServiceApplicationTests {
	@Autowired
	CatalogService catalogService;
	@Autowired
	BaseAttrInfoService baseAttrInfoService;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Autowired
	JedisPool jedisPool;

	@Test
	public void testJedisPool() {
		Jedis jedis = jedisPool.getResource();
		jedis.set("myJedis","6666");
	}


	@Test
	public void textRedis() {
		//创建
		JedisPool jedisPool = new JedisPool();
		//从池中获取jedis客户端
		Jedis resource = jedisPool.getResource();


		ValueOperations<String, String> ofsForValue = stringRedisTemplate.opsForValue();
		ofsForValue.set("hello","world");

	}

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
