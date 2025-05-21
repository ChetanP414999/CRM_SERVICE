package com.app.crm.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.crm.model.Enquiry;
import com.app.crm.repository.EnquiryRepository;
import com.app.crm.service.EnquiryService;

@Service
public class EnquiryServiceImpl  implements EnquiryService{

	@Autowired
	EnquiryRepository enquiryRepository;
	
	@Override
	public Enquiry findByCustomerId(int customerId) {
		// TODO Auto-generated method stub
		return enquiryRepository.findByCustomerId(customerId);
	}

	@Override
	public List<Enquiry> findAllEnquiry() {
	
		List<Enquiry> enquiries=enquiryRepository.findAll();
		
		return enquiries;
	}

}
