package com.youtube.microservices.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youtube.microservices.Entity.Customer;
import com.youtube.microservices.Entity.Region;
import com.youtube.microservices.services.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping
	public ResponseEntity<List<Customer>> listAllCustomer(@RequestParam(name = "regionId", required = false)Long regionId ){
		List<Customer> customers = new ArrayList<>();
		if(regionId == null) {
			customers = customerService.findAll();
			if(customers.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
		}else {
			Region region = new Region();
			region.setId(regionId);
			customers = customerService.findCustomerByRegion(region);
			if(customers.isEmpty()) {
				log.error("Customer with Region id {} not found",regionId);
				return ResponseEntity.noContent().build();
			}
		}
		
		return ResponseEntity.ok(customers);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id){
		log.info("Fetching Customer with id {}",id);
		Customer customer = customerService.getCustomer(id);
		if(customer == null) {
			log.error("Customer with id {} not found",id);
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(customer);
	}
	
	@PostMapping
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult result){
		log.info("Creating customer : {}" , customer);
		if(result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatMessage(result));
		}
		
		
		Customer customerDB = customerService.createCustomer(customer);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(customerDB);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer,@PathVariable(name = "id") Long id){
		log.info("Updating Customer with id {}",id);
		
		Customer customerDB = customerService.getCustomer(id);
		
		if(customerDB == null) {
			log.error("Unable to update, Customer with id {} not exist",id);
			return ResponseEntity.noContent().build();
		}
		
		customer.setId(id);
		customerDB = customerService.updateCustomer(customer);
		
		return ResponseEntity.ok(customerDB);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Customer> deleteCustomer(@PathVariable(name = "id")Long id){
		log.info("Deleting Customer with id {}", id);
		
		Customer customerDB = customerService.getCustomer(id);
		if(customerDB == null) {
			log.error("Unable to delete customer with id {}", id);
			return ResponseEntity.noContent().build();
		}
		
		customerDB = customerService.deleteCustomer(customerDB);
		
		return ResponseEntity.ok(customerDB);
	}
	
	
	private String formatMessage(BindingResult result) {
		List<Map<String,String>> errors = result.getFieldErrors().stream()
				.map(err -> {
					Map<String,String> error = new HashMap<>();
					error.put(err.getField(), err.getDefaultMessage());
					return error;
				}).collect(Collectors.toList());
		ErrorMessage errorMessage = ErrorMessage.builder()
				.code("01")
				.messages(errors)
				.build();
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(errorMessage);
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}
	
	
}
