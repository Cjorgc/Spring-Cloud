package com.youtube.microservices.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youtube.microservices.entity.Category;
import com.youtube.microservices.entity.Product;
import com.youtube.microservices.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
	
	
	private final ProductRepository repository;

	@Override
	public List<Product> listAllProduct() {
		return repository.findAll();
	}

	@Override
	public Product getProduct(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Product createProduct(Product product) {
		product.setStatus("Created");
		product.setCreatedAt(new Date());
		
		return repository.save(product);
	}

	@Override
	public Product updateProduct(Product product) {
		Product productDB = getProduct(product.getId());
		if(productDB == null) {
			return null;
		}
		
		productDB.setCategory(product.getCategory());
		productDB.setDescription(product.getDescription());
		productDB.setName(product.getName());
		productDB.setPrice(product.getPrice());
		productDB.setStock(product.getStock());
		
		return repository.save(productDB);
	}

	@Override
	public Product deleteProduct(Long id) {
		Product eliminatedProduct = getProduct(id);
		if(eliminatedProduct == null) {
			return null;
		}
		
		eliminatedProduct.setStatus("Eliminated");
		return repository.save(eliminatedProduct);
	}

	@Override
	public List<Product> findByCategory(Category category) {
		return repository.findByCategory(category);
	}

	@Override
	public Product updateStock(Long id, Double quantity) {
		Product productDB = getProduct(id);
		if(productDB == null) {
			return null;
		}
		
		Double stock = productDB.getStock() + quantity;
		productDB.setStock(stock);
		return repository.save(productDB);
	}
	
}
