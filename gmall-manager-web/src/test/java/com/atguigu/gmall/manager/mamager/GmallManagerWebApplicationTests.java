package com.atguigu.gmall.manager.mamager;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.service.EchoService;
import com.atguigu.gmall.manager.order.OrderService;
import com.atguigu.gmall.manager.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManagerWebApplicationTests {
	@Reference
	UserService userService;
  	@Reference
	OrderService orderService;
	@Test
	public void contextLoads() {

		EchoService echoService=(EchoService)userService;
		EchoService echoService1=(EchoService)orderService;
		System.out.println(echoService.$echo("hehe"));
	}

	class Person{

	}
	class Student extends Person{

	}
	class Teacher extends Person{

	}
}
