package io.pivotal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisSinkApplication.class, args);
	}
}
