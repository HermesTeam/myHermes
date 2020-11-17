package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//com.example.demo.DemoApplication
//nohup java -cp hermes.jar com.example.demo.DemoApplication > Log.log 2>&1 &
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//加载hadoop的一个客户端exe软件的
		System.setProperty("hadoop.home.dir", "c:/hadoop");
		SpringApplication.run(DemoApplication.class, args);
	}

}
