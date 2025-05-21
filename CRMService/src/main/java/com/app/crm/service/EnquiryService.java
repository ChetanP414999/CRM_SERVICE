package com.app.crm.service;

import java.util.List;

import com.app.crm.model.Enquiry;

public interface EnquiryService {

	public Enquiry findByCustomerId(int customerId);

	public List<Enquiry> findAllEnquiry();
	
	
}
