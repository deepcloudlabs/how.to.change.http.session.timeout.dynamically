package com.example.infrastructure.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.infrastructure.service.HttpSessionService;

/**
 * 
 * @author Binnur Kurt <binnur.kurt@gmail.com>
 *
 */
@Configuration
@ComponentScan("com.example.infrastructure.service")
public class HttpSessionConfig {

	@Bean
	public HttpSessionListener httpSessionListener(HttpSessionService httpSessionService) {
		return new HttpSessionListener() {
			@Override
			public void sessionCreated(HttpSessionEvent httpSessionEvent) {
				httpSessionService.createSession(httpSessionEvent);
			}

			@Override
			public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
				httpSessionService.removeSession(httpSessionEvent);
			}
		};
	}
}
