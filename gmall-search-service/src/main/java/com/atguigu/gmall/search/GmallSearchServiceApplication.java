package com.atguigu.gmall.search;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableDubbo
@SpringBootApplication
public class GmallSearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallSearchServiceApplication.class, args);
	}
}
