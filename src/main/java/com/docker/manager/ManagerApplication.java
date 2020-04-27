package com.docker.manager;

import com.docker.manager.common.utils.DigestUtils;
import org.apache.shiro.util.ByteSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.docker.manager.dao")
public class ManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
		ByteSource username = ByteSource.Util.bytes("admin");
//		System.out.println("username:" + username);
//		System.out.println("password:"+ DigestUtils.Md5("admin","123456"));
	}
}
