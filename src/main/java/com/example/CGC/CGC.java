package com.example.CGC;

import com.example.CGC.CGCExceptions.CGCErrorMapping;
import com.example.CGC.Shared.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({CGCErrorMapping.class, Properties.class})
public class CGC {
	public static void main(String[] args) {
		SpringApplication.run(CGC.class, args);
	}
}