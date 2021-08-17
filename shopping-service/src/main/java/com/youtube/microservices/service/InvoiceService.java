package com.youtube.microservices.service;

import java.util.List;

import com.youtube.microservices.entity.Invoice;

public interface InvoiceService {
	List<Invoice> listAll();
	
	Invoice createInvoice(Invoice invoice);
	Invoice updateInvoice(Invoice invoice);
	Invoice deleteInvoice(Invoice invoice);
	
	Invoice getInvoice (Long id);
}
