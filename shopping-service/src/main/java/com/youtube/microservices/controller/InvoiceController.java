package com.youtube.microservices.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youtube.microservices.entity.Invoice;
import com.youtube.microservices.service.InvoiceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/invoices")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@GetMapping
	public ResponseEntity<List<Invoice>> listAllInvoices(){
		List<Invoice> invoices = invoiceService.listAll();
		if(invoices.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(invoices);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Invoice> getInvoice(@PathVariable Long id){
		log.info("Fetching invoice with id {} " , id);
		Invoice invoiceDB = invoiceService.getInvoice(id);
		if(invoiceDB == null) {
			log.error("Invoice with id {} not found", id);
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(invoiceDB);
	}
	
	@PostMapping
	public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice, BindingResult result){
		log.info("Creating invoice {}" , invoice);
		
		if(result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,this.formatMessage(result));
		}
		
		Invoice invoiceDB = invoiceService.createInvoice(invoice);
		return ResponseEntity.status(HttpStatus.CREATED).body(invoiceDB);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice invoice, @PathVariable("id") Long id){
		log.info("Updating invoice {} ",invoice);
		
		invoice.setId(id);
		Invoice invoiceDB = invoiceService.updateInvoice(invoice);
		
		if(invoiceDB == null) {
			log.error("Unable to update invoice ", invoice);
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(invoiceDB);

	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Invoice> deleteInvoice(@PathVariable Long id){
		log.info("Deleting invoice with id {} ", id);
		Invoice invoiceDB = invoiceService.getInvoice(id);
		if(invoiceDB == null) {
			log.error("Unable to delete invoice with id {} , not found", id);
			return ResponseEntity.notFound().build();
		}
		
		invoiceDB = invoiceService.deleteInvoice(invoiceDB);
		
		return ResponseEntity.ok(invoiceDB);
	}
	
	public String formatMessage(BindingResult result) {
		List<Map<String,String>> errors = result.getFieldErrors().stream()
				.map(err -> {
					Map<String,String> error = new HashMap<>();
					error.put(err.getField(), err.getDefaultMessage());
					return error;
				}).collect(Collectors.toList());
		ErrorMessage errorMessage = ErrorMessage.builder()
				.code("01")
				.errorMessage(errors)
				.build();
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(errors);
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return jsonString;
		
	}
	
}
