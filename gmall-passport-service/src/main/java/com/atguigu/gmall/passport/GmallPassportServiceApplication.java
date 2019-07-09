package com.atguigu.gmall.passport;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.atguigu.gmall.passport.mapper")
@EnableDubbo
@SpringBootApplication
public class GmallPassportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallPassportServiceApplication.class, args);
	}
}
