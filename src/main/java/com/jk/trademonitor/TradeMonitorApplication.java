package com.jk.trademonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@PropertySource("classpath:private.properties")
@EnableScheduling
@SpringBootApplication
public class TradeMonitorApplication {
	public static void main(String[] args) {
		SpringApplication.run(TradeMonitorApplication.class, args);
	}
}
