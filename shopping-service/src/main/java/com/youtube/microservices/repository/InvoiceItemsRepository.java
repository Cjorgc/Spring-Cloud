package com.youtube.microservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youtube.microservices.entity.InvoiceItem;

public interface InvoiceItemsRepository extends JpaRepository<InvoiceItem, Long>{

}
