package com.example.lottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.infrastructure.config.HttpSessionConfig;

/**
 * 
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 *
 */
@SpringBootApplication
@EnableScheduling
@Import(HttpSessionConfig.class)
public class LotteryMicroservice {

	public static void main(String[] args) {
		SpringApplication.run(LotteryMicroservice.class, args);
	}

}
