package com.youtube.microservices.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youtube.microservices.entity.Invoice;
import com.youtube.microservices.entity.InvoiceItem;
import com.youtube.microservices.model.Customer;
import com.youtube.microservices.model.Product;
import com.youtube.microservices.client.CustomerClient;
import com.youtube.microservices.client.ProductClient;
import com.youtube.microservices.repository.InvoiceItemsRepository;
import com.youtube.microservices.repository.InvoiceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvoiceServiceImpls implements InvoiceService{
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private InvoiceItemsRepository invoiceItemsRepository;
	
	@Autowired
	CustomerClient customerClient;
	
	@Autowired
	ProductClient productClient;
	
	@Override
	public List<Invoice> listAll() {
		return invoiceRepository.findAll();
	}

	@Override
	public Invoice createInvoice(Invoice invoice) {
		Invoice invoiceDB = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
		if(invoiceDB != null) {
			return invoiceDB;
		}
		invoice.setState("CREATED");
		invoiceDB = invoiceRepository.save(invoice);
		
		invoiceDB.getItems().forEach(invoiceItem -> {
			productClient.updateStockProduct(invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
		});
		
		return invoiceDB;
	}

	@Override
	public Invoice updateInvoice(Invoice invoice) {
		Invoice invoiceDB = getInvoice(invoice.getId());
		if(invoiceDB == null) {
			return null;
		}
		
		invoiceDB.setDescription(invoice.getDescription());
		invoiceDB.setCustomerId(invoice.getCustomerId());
		invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
		invoiceDB.getItems().clear();
		invoiceDB.setItems(invoice.getItems());
		
		return invoiceRepository.save(invoiceDB);
	}

	@Override
	public Invoice deleteInvoice(Invoice invoice) {
		Invoice invoiceDB = getInvoice(invoice.getId());
		if(invoiceDB == null) {
			return null;
		}
		
		invoiceDB.setState("DELETED");
		return invoiceRepository.save(invoiceDB);
	}

	@Override
	public Invoice getInvoice(Long id) {
		Invoice invoice = invoiceRepository.findById(id).orElse(null);
		if(invoice != null) {
			Customer customer = customerClient.getCustomer(invoice.getCustomerId()).getBody();
			invoice.setCustomer(customer);
			List<InvoiceItem> itemList = invoice.getItems().stream().map(invoiceItem -> {
				Product product = productClient.getProduct(invoiceItem.getProductId()).getBody();
				invoiceItem.setProduct(product);
				return invoiceItem;
			}).collect(Collectors.toList());
			invoice.setItems(itemList);
		}
		
		return invoice;
	}

}
