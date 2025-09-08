package com.oneplane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling	// 스케쥴링 활성화
public class OneplaneApplication {

	public static void main(String[] args) {

		SpringApplication.run(OneplaneApplication.class, args);
	}

}
