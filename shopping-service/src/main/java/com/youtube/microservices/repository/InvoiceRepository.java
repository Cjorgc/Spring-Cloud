package com.youtube.microservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youtube.microservices.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>{

	List<Invoice> findByCustomerId(Long id);
	Invoice findByNumberInvoice(String numberInvoice);
}
