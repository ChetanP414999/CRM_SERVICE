package com.app.crm.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.crm.enums.LoanStatus;
import com.app.crm.model.Customer;
import com.app.crm.model.Enquiry;
import com.app.crm.model.LocalAddress;

public interface CustomerService {
	public Customer saveCustomerDetails(Enquiry enquiry, String customer, MultipartFile addressProof, MultipartFile panCard,
			MultipartFile incomeTax, MultipartFile adharCard, MultipartFile photo, MultipartFile signature,
			MultipartFile bankCheque, MultipartFile salarySlip);

	public Customer saveCustomerDetails(Customer c);

	public Customer findByEmailAndPassword(String customerEmail, String password);

	public List<Customer> findAllCustomer();

	public Customer findByCustomerId(int customerId);

	public Customer updateCustomer(int customerId, String property, Customer customer);

	public Customer updateCustomer(int customerId, Customer customer);
	
	
	public Customer updateDocument(int customerId, MultipartFile addressProof, MultipartFile panCard,
			MultipartFile incomeTax, MultipartFile adharCard, MultipartFile photo, MultipartFile signature,
			MultipartFile bankCheque, MultipartFile salarySlip);

	public Customer forwardToOe(int customerId);

	public Customer updateLocalAddress(int customerId, LocalAddress localAddress);

	public List<Customer> getCustomerByLoanStatus(LoanStatus loanStatus);

	//public Customer updateSanctionStatus(int customerId);
}
