package com.example.OrderService;

import com.example.OrderService.client.ProductServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class OrderServiceApplicationTests {

	@MockBean
	private ProductServiceClient productServiceClient;

	@Test
	void contextLoads() {
	}
}
