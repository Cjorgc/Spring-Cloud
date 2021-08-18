package com.youtube.microservices.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Positive;

import com.youtube.microservices.model.Product;

import lombok.Data;

@Entity
@Table(name = "tbl_invoice_items")
@Data
public class InvoiceItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Positive(message = "El stock debe ser mayor que cero")
	private Double quantity;
	
	private Double price;
	
	@Column(name = "product_id")
	private Long productId;
	
	@Transient
	private Double subTotal;
	
	@Transient
	private Product product;
	
	public Double getSubTotal() {
		if(this.quantity > 0 && this.price > 0) {
			return this.quantity * this.price;
		}
		else {
			return (double) 0;
		}
	}
	
	public InvoiceItem() {
		this.price = (double)0;
		this.quantity = (double)0;
	}

}
