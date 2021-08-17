package com.youtube.microservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youtube.microservices.Entity.Customer;
import com.youtube.microservices.Entity.Region;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	public Customer findByNumberID(String numberID);
	public List<Customer> findByLastName(String lastName);
	public List<Customer> findByRegion(Region region);
}
