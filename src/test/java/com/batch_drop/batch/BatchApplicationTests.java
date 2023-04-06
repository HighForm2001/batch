package com.batch_drop.batch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BatchApplicationTests {

	@Test
	void contextLoads() {
		System.setProperty("Password","1234");
		System.out.println(System.getProperty("Password"));
	}

}
