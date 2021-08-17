package com.youtube.microservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youtube.microservices.entity.Category;
import com.youtube.microservices.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	public List<Product> findByCategory(Category category);
}
