package com.app.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.crm.model.Enquiry;
@Repository
public interface EnquiryRepository  extends JpaRepository<Enquiry, Integer>{

	
	public Enquiry findByCustomerId(int customerId);
	
	
	
}
