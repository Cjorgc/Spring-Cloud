package com.youtube.microservices.services;

import java.util.List;

import com.youtube.microservices.Entity.Customer;
import com.youtube.microservices.Entity.Region;

public interface CustomerService {
	
	public List<Customer> findAll();
	public List<Customer> findCustomerByRegion(Region region);
	
	public Customer createCustomer(Customer customer);
	public Customer updateCustomer(Customer customer);
	public Customer deleteCustomer(Customer customer);
	public Customer getCustomer(Long id); 

}
