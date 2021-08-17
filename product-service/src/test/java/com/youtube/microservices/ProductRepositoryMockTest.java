package com.youtube.microservices;

import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.youtube.microservices.entity.Category;
import com.youtube.microservices.entity.Product;
import com.youtube.microservices.repository.ProductRepository;

@DataJpaTest
public class ProductRepositoryMockTest {
	
	@Autowired
	private ProductRepository repository;
	
	@Test
	public void whenFfindByCategory_thenReturnListProduct() {
		
		Product product01 = Product.builder()
							.name("computer")
							.category(Category.builder().id(1L).build())
							.description("")
							.stock(Double.parseDouble("1"))
							.price(Double.parseDouble("1231.51"))
							.status("Created")
							.createdAt(new Date())
							.build();
		
		repository.save(product01);
							
		List<Product> founds = repository.findByCategory(product01.getCategory());
		
		Assertions.assertThat(founds.size()).isEqualTo(3);
	}
}
