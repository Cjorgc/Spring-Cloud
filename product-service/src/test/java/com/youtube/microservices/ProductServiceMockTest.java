package com.youtube.microservices;

import java.util.Date;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.youtube.microservices.entity.Category;
import com.youtube.microservices.entity.Product;
import com.youtube.microservices.repository.ProductRepository;
import com.youtube.microservices.service.ProductService;
import com.youtube.microservices.service.ProductServiceImpl;

@SpringBootTest
public class ProductServiceMockTest {
	
	@Mock
	private ProductRepository repository;
	
	private ProductService productService;
	
	@BeforeEach
	public void setup() {
		
		MockitoAnnotations.openMocks(this);
		
		productService = new ProductServiceImpl(repository);
		
		Product computer = Product.builder()
				.id(1L)
				.name("computer")
				.category(Category.builder().id(1L).build())
				.description("")
				.stock(Double.parseDouble("1"))
				.price(Double.parseDouble("1231.51"))
				.status("Created")
				.createdAt(new Date())
				.build();
		
		Mockito.when(repository.findById(1L)).thenReturn(Optional.of(computer));
		Mockito.when(repository.save(computer)).thenReturn(computer);
		
	}
	
	@Test
	public void whenValidGetId_ThenReturnProduct() {
		Product found = productService.getProduct(1L);
		Assertions.assertThat(found.getName()).isEqualTo("computer");
	}
	
	@Test
	public void whenValidUpdateStock_thenReturnNewStock() {
		Product newStock = productService.updateStock(1L, Double.parseDouble("8"));
		Assertions.assertThat(newStock.getStock()).isEqualTo(9);
	}
	
}
