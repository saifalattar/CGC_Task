package com.example.CGC;

import com.example.CGC.Shared.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AbyadApplicationTests {

	@Autowired
    Properties properties;
	@Test
	void contextLoads() {
		System.out.println(properties);
	}

}
