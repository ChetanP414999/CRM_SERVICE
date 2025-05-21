package com.app.crm.serviceImpl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.crm.enums.CibilStatus;
import com.app.crm.enums.LoanStatus;
import com.app.crm.exceptions.CustomerObjectNotFoundExceptions;
import com.app.crm.model.AccountDetails;
import com.app.crm.model.AllPersonalDocuments;
import com.app.crm.model.Customer;
import com.app.crm.model.Enquiry;
import com.app.crm.model.LocalAddress;
import com.app.crm.model.SanctionLetter;
import com.app.crm.repository.CustomerRepository;
import com.app.crm.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class CustomerServiceImpl implements CustomerService {

//	@Autowired
//	private JavaMailSender javaMailSender;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer saveCustomerDetails(Enquiry enquiry, String customer, MultipartFile addressProof,
			MultipartFile panCard, MultipartFile incomeTax, MultipartFile adharCard, MultipartFile photo,
			MultipartFile signature, MultipartFile bankCheque, MultipartFile salarySlip) {
		Customer savecustomer = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			savecustomer = mapper.readValue(customer, Customer.class);
			savecustomer.setCustomerId(enquiry.getCustomerId());
			savecustomer.setCustomerName(enquiry.getCustomerName());
			savecustomer.setCustomerAge(enquiry.getCustomerAge());
			savecustomer.setCustomerEmail(enquiry.getCustomerEmail());
			savecustomer.setCustomerMobileNumber(enquiry.getCustomerMobileNumber());
			savecustomer.setGender(enquiry.getGender());
			savecustomer.setUserName(enquiry.getUserName());
			savecustomer.setPassword(enquiry.getPassword());
			
			savecustomer.setAdharCard(enquiry.getAdharCard());
			savecustomer.setPancardNo(enquiry.getPancardNo());
		
			savecustomer.setLoanStatus(LoanStatus.Pending);
			
			AllPersonalDocuments doc = new AllPersonalDocuments();
			doc.setDocumentId(savecustomer.getCustomerId());
			doc.setAddressProof(addressProof.getBytes());
			doc.setPanCard(panCard.getBytes());
			doc.setIncomeTax(incomeTax.getBytes());
			
			doc.setAdharCard(adharCard.getBytes());
			doc.setPhoto(photo.getBytes());
			doc.setSignature(signature.getBytes());
			doc.setBankCheque(bankCheque.getBytes());
			doc.setSalarySlip(salarySlip.getBytes());

			savecustomer.setAllPersonalDocuments(doc);
			
			SanctionLetter letter=new SanctionLetter();
			letter.setSanctionId(savecustomer.getCustomerId());			
			
			savecustomer.setSanctionLetter(letter);
			
			
			
			AccountDetails accountDetails=new AccountDetails();
			accountDetails.setAccountId(savecustomer.getCustomerId());
			accountDetails.setAccountNumber(savecustomer.getAccountDetails().getAccountNumber());
			accountDetails.setIfscCode(savecustomer.getAccountDetails().getIfscCode());
			accountDetails.setBankName(savecustomer.getAccountDetails().getBankName());
			accountDetails.setAccountType(savecustomer.getAccountDetails().getAccountType());
			accountDetails.setAccountStatus(savecustomer.getAccountDetails().getAccountStatus());
			accountDetails.setAccountBalance(savecustomer.getAccountDetails().getAccountBalance());
			accountDetails.setAccountHolderName(savecustomer.getAccountDetails().getAccountHolderName());
			
			savecustomer.setAccountDetails(accountDetails);
			
							customerRepository.save(savecustomer);
			
		
			
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
//		MimeMessage message = javaMailSender.createMimeMessage();
//		MimeMessageHelper mailMessage;
//		try {
//			mailMessage = new MimeMessageHelper(message, true);
//			mailMessage.setFrom("rahuljadhao1904@gmail.com");
//			mailMessage.setTo(savecustomer.getCustomerEmail());
//			mailMessage.setSubject("Regarding Customer Registry");
//			mailMessage.setText("Dear " + savecustomer.getCustomerName() + ", your registration is successful!");
//			
//			javaMailSender.send(message);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}

		
		return savecustomer;
	}

	
	@Override
	public List< Customer> getCustomerByLoanStatus(LoanStatus loanStatus) {
		
		
		List< Customer> customer = customerRepository.findByloanStatus(loanStatus);
		
		
		
		return customer;
	}
	
	
	@Override
	public Customer saveCustomerDetails(Customer c) {
		Customer customer = customerRepository.save(c);
		return customer;
	}

	@Override
	public Customer findByEmailAndPassword(String customerEmail, String password) {
		Customer customer = customerRepository.findByCustomerEmailAndPassword(customerEmail, password);
		return customer;
	}

	@Override
	public List<Customer> findAllCustomer() {
		List<Customer> list = customerRepository.findAll();
		return list;
	}

	@Override
	public Customer findByCustomerId(int customerId) {
		Customer cust = customerRepository.findByCustomerId(customerId);
		if(cust==null)
		{
			throw new CustomerObjectNotFoundExceptions("the object for  customerId : "+customerId +" not found");
		}else{
			return cust;
		}
	}

	@Override
	public Customer updateCustomer(int customerId, String property, Customer customer) {

		Customer cust = customerRepository.findByCustomerId(customerId);
		if(cust==null)
		{
			throw new CustomerObjectNotFoundExceptions("the object for  customerId : "+customerId +" not found");
		}
		else
		{
		if (property.equalsIgnoreCase("customerName") || property.equalsIgnoreCase("name")) {
			cust.setCustomerName(customer.getCustomerName());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("customerDateOfBirth")) {
			cust.setCustomerDateOfBirth(customer.getCustomerDateOfBirth());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("customerAge")) {
			cust.setCustomerAge(customer.getCustomerAge());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("gendergender")) {
			cust.setGender(customer.getGender());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("customerEmail")) {
			cust.setCustomerEmail(customer.getCustomerEmail());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("gendergender")) {
			cust.setGender(customer.getGender());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("cutomerMobileNumber")) {
			cust.setCustomerMobileNumber(customer.getCustomerMobileNumber());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("customerAdditionalMobileNumber")) {
			cust.setCustomerAdditionalMobileNumber(customer.getCustomerAdditionalMobileNumber());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("customerAmmountPaidForCarloan")) {
			cust.setCustomerAmmountPaidForCarloan(customer.getCustomerAmmountPaidForCarloan());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("customerTotalLoanRequiredAmmount")) {
			cust.setCustomerTotalLoanRequiredAmmount(customer.getCustomerTotalLoanRequiredAmmount());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("password")) {
			cust.setPassword(customer.getPassword());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("numberOfFamilyMember")) {

			cust.getDependantInformation()
					.setNumberOfFamilyMember(customer.getDependantInformation().getNumberOfFamilyMember());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("maritalStatus")) {

			cust.getDependantInformation().setMaritalStatus(customer.getDependantInformation().getMaritalStatus());
			customerRepository.save(cust);
	
		} else if (property.equalsIgnoreCase("familyIncome")) {
			cust.getDependantInformation().setFamilyIncome(customer.getDependantInformation().getFamilyIncome());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("areaName")) {
			cust.getCustomerAddress().getPermanentAddress().setAreaName(customer.getCustomerAddress().getPermanentAddress().getAreaName());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("cityName")) {
			cust.getCustomerAddress().getPermanentAddress().setAreaName(customer.getCustomerAddress().getPermanentAddress().getAreaName());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("district")) {
			cust.getCustomerAddress().getPermanentAddress().setDistrict(customer.getCustomerAddress().getPermanentAddress().getDistrict());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("state")) {
			cust.getCustomerAddress().getPermanentAddress().setState(customer.getCustomerAddress().getPermanentAddress().getState());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("pincode")) {
			cust.getCustomerAddress().getPermanentAddress().setPincode(customer.getCustomerAddress().getPermanentAddress().getPincode());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("houseNumber")) {
			cust.getCustomerAddress().getPermanentAddress().setHouseNumber(customer.getCustomerAddress().getPermanentAddress().getHouseNumber());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("streetName")) {
			cust.getCustomerAddress().getPermanentAddress().setStreetName(customer.getCustomerAddress().getPermanentAddress().getStreetName());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("localArea")) {
			cust.getCustomerAddress().getLocalAddress().setAreaName(customer.getCustomerAddress().getLocalAddress().getAreaName());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("localCity")) {
			cust.getCustomerAddress().getLocalAddress().setAreaName(customer.getCustomerAddress().getLocalAddress().getAreaName());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("localDistrict")) {
			cust.getCustomerAddress().getLocalAddress().setDistrict(customer.getCustomerAddress().getLocalAddress().getDistrict());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("localState")) {
			cust.getCustomerAddress().getLocalAddress().setState(customer.getCustomerAddress().getLocalAddress().getState());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("localPincode")) {
			cust.getCustomerAddress().getLocalAddress().setPincode(customer.getCustomerAddress().getLocalAddress().getPincode());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("localHouseno")) {
			cust.getCustomerAddress().getLocalAddress().setHouseNumber(customer.getCustomerAddress().getLocalAddress().getHouseNumber());
			customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("localStreet")) {
			cust.getCustomerAddress().getLocalAddress().setStreetName(customer.getCustomerAddress().getLocalAddress().getStreetName());
			customerRepository.save(cust);
		}else if (property.equalsIgnoreCase("patientId")) {
			 cust.getMedicalInfo().setPatientId(customer.getMedicalInfo().getPatientId());
			customerRepository.save(cust);
		}else if (property.equalsIgnoreCase("patientName")) {
			   cust.getMedicalInfo().setProfessionPatientName(customer.getMedicalInfo().getProfessionPatientName());
			customerRepository.save(cust);
		}else if (property.equalsIgnoreCase("billingDate")) {
			 cust.getMedicalInfo().setBilingDate(customer.getMedicalInfo().getBilingDate());
			customerRepository.save(cust);
		}else if (property.equalsIgnoreCase("loanAmount")) {
			  cust.getMedicalInfo().setLoanAmount(customer.getMedicalInfo().getLoanAmount());
			customerRepository.save(cust);
		}else if (property.equalsIgnoreCase("treatment")) {
			 cust.getMedicalInfo().setTreatement(customer.getMedicalInfo().getTreatement());
			customerRepository.save(cust);
		}else if (property.equalsIgnoreCase("previousLoanAmount")) {
		    cust.getPreviousLoanDetails().setPreviousLoanAmount(customer.getPreviousLoanDetails().getPreviousLoanAmount());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("previousLoanTenure")) {
		    cust.getPreviousLoanDetails().setPreviousLoanTenure(customer.getPreviousLoanDetails().getPreviousLoanTenure());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("previousLoanPaid")) {
		    cust.getPreviousLoanDetails().setPreviousLoanPaid(customer.getPreviousLoanDetails().getPreviousLoanPaid());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("previousLoanRemainingAmount")) {
		    cust.getPreviousLoanDetails().setPreviousLoanRemeaningAmount(customer.getPreviousLoanDetails().getPreviousLoanRemeaningAmount());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("previousLoanDefaulterCount")) {
		    cust.getPreviousLoanDetails().setPreviousLoanDefaulterCount(customer.getPreviousLoanDetails().getPreviousLoanDefaulterCount());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("previousLoanRemark")) {
		    cust.getPreviousLoanDetails().setPreviousLoanRemark(customer.getPreviousLoanDetails().getPreviousLoanRemark());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("branchName")) {
		    cust.getPreviousLoanDetails().getPreviousLoanBank().setBranchName(customer.getPreviousLoanDetails().getPreviousLoanBank().getBranchName());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("branchCode")) {
		    cust.getPreviousLoanDetails().getPreviousLoanBank().setBranchCode(customer.getPreviousLoanDetails().getPreviousLoanBank().getBranchCode());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("branchType")) {
		    cust.getPreviousLoanDetails().getPreviousLoanBank().setBranchType(customer.getPreviousLoanDetails().getPreviousLoanBank().getBranchType());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("ifscCode")) {
		    cust.getPreviousLoanDetails().getPreviousLoanBank().setIfscCode(customer.getPreviousLoanDetails().getPreviousLoanBank().getIfscCode());
		    customerRepository.save(cust);
		
		} else if (property.equalsIgnoreCase("contactNumber")) {
		    cust.getPreviousLoanDetails().getPreviousLoanBank().setContactNumber(customer.getPreviousLoanDetails().getPreviousLoanBank().getContactNumber());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("bankAddress")) {
		    cust.getPreviousLoanDetails().getPreviousLoanBank().setBankAddress(customer.getPreviousLoanDetails().getPreviousLoanBank().getBankAddress());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("email")) {
		    cust.getPreviousLoanDetails().getPreviousLoanBank().setEmail(customer.getPreviousLoanDetails().getPreviousLoanBank().getEmail());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("status")) {
		    cust.getPreviousLoanDetails().getPreviousLoanBank().setStatus(customer.getPreviousLoanDetails().getPreviousLoanBank().getStatus());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("accountType")) {
		    cust.getAccountDetails().setAccountType(customer.getAccountDetails().getAccountType());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("accountBalance")) {
		    cust.getAccountDetails().setAccountBalance(customer.getAccountDetails().getAccountBalance());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("accountHolderName")) {
		    cust.getAccountDetails().setAccountHolderName(customer.getAccountDetails().getAccountHolderName());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("accountStatus")) {
		    cust.getAccountDetails().setAccountStatus(customer.getAccountDetails().getAccountStatus());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("accountNumber")) {
		    cust.getAccountDetails().setAccountNumber(customer.getAccountDetails().getAccountNumber());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("guarantorName")) {
		    cust.getGuarantorDetails().setGuarantorName(customer.getGuarantorDetails().getGuarantorName());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("guarantorDateOfBirth")) {
		    cust.getGuarantorDetails().setGuarantorDateOfBirth(customer.getGuarantorDetails().getGuarantorDateOfBirth());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("guarantorRelationshipwithCustomer")) {
		    cust.getGuarantorDetails().setGuarantorRelationshipwithCustomer(customer.getGuarantorDetails().getGuarantorRelationshipwithCustomer());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("guarantorMobileNumber")) {
		    cust.getGuarantorDetails().setGuarantorMobileNumber(customer.getGuarantorDetails().getGuarantorMobileNumber());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("guarantorAdharCardNo")) {
		    cust.getGuarantorDetails().setGuarantorAdharCardNo(customer.getGuarantorDetails().getGuarantorAdharCardNo());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("guarantorMortgageDetails")) {
		    cust.getGuarantorDetails().setGuarantorMortgageDetails(customer.getGuarantorDetails().getGuarantorMortgageDetails());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("guarantorJobDetails")) {
		    cust.getGuarantorDetails().setGuarantorjobDetails(customer.getGuarantorDetails().getGuarantorjobDetails());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("guarantorLocalAddress")) {
		    cust.getGuarantorDetails().setGuarantorLocalAddress(customer.getGuarantorDetails().getGuarantorLocalAddress());
		    customerRepository.save(cust);
		} else if (property.equalsIgnoreCase("guarantorPermanentAddress")) {
		    cust.getGuarantorDetails().setGuarantorPermanentAddress(customer.getGuarantorDetails().getGuarantorPermanentAddress());
		    customerRepository.save(cust);
		}
		}
		return cust;

	}

	@Override
	public Customer updateCustomer(int customerId, Customer customer) {
		Customer cust = customerRepository.findByCustomerId(customerId);
		//System.out.println(customer);
		if(cust==null)
		{
			throw new CustomerObjectNotFoundExceptions("the object for  customerId : "+customerId +" not found");
		}
		else {
		cust.setCustomerName(customer.getCustomerName());
	    cust.setCustomerDateOfBirth(customer.getCustomerDateOfBirth());
	    cust.setCustomerAge(customer.getCustomerAge());
	    cust.setGender(customer.getGender());
	    cust.setCustomerEmail(customer.getCustomerEmail());
	    cust.setCustomerMobileNumber(customer.getCustomerMobileNumber());
	    cust.setCustomerAdditionalMobileNumber(customer.getCustomerAdditionalMobileNumber());
	    cust.setCustomerAmmountPaidForCarloan(customer.getCustomerAmmountPaidForCarloan());
	    cust.setCustomerTotalLoanRequiredAmmount(customer.getCustomerTotalLoanRequiredAmmount());
	    cust.setPassword(customer.getPassword());
	   
	    cust.getDependantInformation().setNumberOfFamilyMember(customer.getDependantInformation().getNumberOfFamilyMember());
	    cust.getDependantInformation().setMaritalStatus(customer.getDependantInformation().getMaritalStatus());
	    cust.getDependantInformation().setFamilyIncome(customer.getDependantInformation().getFamilyIncome());

	    cust.getCustomerAddress().getPermanentAddress().setAreaName(customer.getCustomerAddress().getPermanentAddress().getAreaName());
	    cust.getCustomerAddress().getPermanentAddress().setCityName(customer.getCustomerAddress().getPermanentAddress().getCityName());
	    cust.getCustomerAddress().getPermanentAddress().setDistrict(customer.getCustomerAddress().getPermanentAddress().getDistrict());
	    cust.getCustomerAddress().getPermanentAddress().setState(customer.getCustomerAddress().getPermanentAddress().getState());
	    cust.getCustomerAddress().getPermanentAddress().setPincode(customer.getCustomerAddress().getPermanentAddress().getPincode());
	    cust.getCustomerAddress().getPermanentAddress().setHouseNumber(customer.getCustomerAddress().getPermanentAddress().getHouseNumber());
	    cust.getCustomerAddress().getPermanentAddress().setStreetName(customer.getCustomerAddress().getPermanentAddress().getStreetName());

	    cust.getCustomerAddress().getLocalAddress().setAreaName(customer.getCustomerAddress().getLocalAddress().getAreaName());
	    cust.getCustomerAddress().getLocalAddress().setCityName(customer.getCustomerAddress().getLocalAddress().getCityName());
	    cust.getCustomerAddress().getLocalAddress().setDistrict(customer.getCustomerAddress().getLocalAddress().getDistrict());
	    cust.getCustomerAddress().getLocalAddress().setState(customer.getCustomerAddress().getLocalAddress().getState());
	    cust.getCustomerAddress().getLocalAddress().setPincode(customer.getCustomerAddress().getLocalAddress().getPincode());
	    cust.getCustomerAddress().getLocalAddress().setHouseNumber(customer.getCustomerAddress().getLocalAddress().getHouseNumber());
	    cust.getCustomerAddress().getLocalAddress().setStreetName(customer.getCustomerAddress().getLocalAddress().getStreetName());
	  // System.out.println(cust.getMedicalInfo());
	    cust.getMedicalInfo().setPatientId(customer.getMedicalInfo().getPatientId());
	    cust.getMedicalInfo().setProfessionPatientName(customer.getMedicalInfo().getProfessionPatientName());
	    cust.getMedicalInfo().setBilingDate(customer.getMedicalInfo().getBilingDate());
	    cust.getMedicalInfo().setLoanAmount(customer.getMedicalInfo().getLoanAmount());
	    cust.getMedicalInfo().setTreatement(customer.getMedicalInfo().getTreatement());
	    
	    cust.getPreviousLoanDetails().setPreviousLoanAmount(customer.getPreviousLoanDetails().getPreviousLoanAmount());
	    cust.getPreviousLoanDetails().setPreviousLoanTenure(customer.getPreviousLoanDetails().getPreviousLoanTenure());
	    cust.getPreviousLoanDetails().setPreviousLoanPaid(customer.getPreviousLoanDetails().getPreviousLoanPaid());
	    cust.getPreviousLoanDetails().setPreviousLoanRemeaningAmount(customer.getPreviousLoanDetails().getPreviousLoanRemeaningAmount());
	    cust.getPreviousLoanDetails().setPreviousLoanDefaulterCount(customer.getPreviousLoanDetails().getPreviousLoanDefaulterCount());
	    cust.getPreviousLoanDetails().setPreviousLoanRemark(customer.getPreviousLoanDetails().getPreviousLoanRemark());

	    cust.getPreviousLoanDetails().getPreviousLoanBank().setBranchName(customer.getPreviousLoanDetails().getPreviousLoanBank().getBranchName());
	    cust.getPreviousLoanDetails().getPreviousLoanBank().setBranchCode(customer.getPreviousLoanDetails().getPreviousLoanBank().getBranchCode());
	    cust.getPreviousLoanDetails().getPreviousLoanBank().setBranchType(customer.getPreviousLoanDetails().getPreviousLoanBank().getBranchType());
	    cust.getPreviousLoanDetails().getPreviousLoanBank().setIfscCode(customer.getPreviousLoanDetails().getPreviousLoanBank().getIfscCode());
	    cust.getPreviousLoanDetails().getPreviousLoanBank().setContactNumber(customer.getPreviousLoanDetails().getPreviousLoanBank().getContactNumber());
	    cust.getPreviousLoanDetails().getPreviousLoanBank().setBankAddress(customer.getPreviousLoanDetails().getPreviousLoanBank().getBankAddress());
	    cust.getPreviousLoanDetails().getPreviousLoanBank().setEmail(customer.getPreviousLoanDetails().getPreviousLoanBank().getEmail());
	    cust.getPreviousLoanDetails().getPreviousLoanBank().setStatus(customer.getPreviousLoanDetails().getPreviousLoanBank().getStatus());

	    cust.getAccountDetails().setAccountType(customer.getAccountDetails().getAccountType());
	    cust.getAccountDetails().setAccountBalance(customer.getAccountDetails().getAccountBalance());
	    cust.getAccountDetails().setAccountHolderName(customer.getAccountDetails().getAccountHolderName());
	    cust.getAccountDetails().setAccountStatus(customer.getAccountDetails().getAccountStatus());
	    cust.getAccountDetails().setAccountNumber(customer.getAccountDetails().getAccountNumber());

	    cust.getGuarantorDetails().setGuarantorName(customer.getGuarantorDetails().getGuarantorName());
	    cust.getGuarantorDetails().setGuarantorDateOfBirth(customer.getGuarantorDetails().getGuarantorDateOfBirth());
	    cust.getGuarantorDetails().setGuarantorRelationshipwithCustomer(customer.getGuarantorDetails().getGuarantorRelationshipwithCustomer());
	    cust.getGuarantorDetails().setGuarantorMobileNumber(customer.getGuarantorDetails().getGuarantorMobileNumber());
	    cust.getGuarantorDetails().setGuarantorAdharCardNo(customer.getGuarantorDetails().getGuarantorAdharCardNo());
	    cust.getGuarantorDetails().setGuarantorMortgageDetails(customer.getGuarantorDetails().getGuarantorMortgageDetails());
	    cust.getGuarantorDetails().setGuarantorjobDetails(customer.getGuarantorDetails().getGuarantorjobDetails());
	    cust.getGuarantorDetails().setGuarantorLocalAddress(customer.getGuarantorDetails().getGuarantorLocalAddress());
	    cust.getGuarantorDetails().setGuarantorPermanentAddress(customer.getGuarantorDetails().getGuarantorPermanentAddress());

	    customerRepository.save(cust);
		}
	    return cust;
	}
	@SuppressWarnings("unused")
	@Override
	public Customer updateDocument(int customerId, MultipartFile addressProof, MultipartFile panCard,
			MultipartFile incomeTax, MultipartFile adharCard, MultipartFile photo, MultipartFile signature,
			MultipartFile bankCheque, MultipartFile salarySlip) {

		Customer cust = customerRepository.findByCustomerId(customerId);
		
		if(cust==null)
		{
		
			throw new CustomerObjectNotFoundExceptions("the object for  customerId : "+customerId +" not found");
		}
		
		else
		{
			AllPersonalDocuments documents = cust.getAllPersonalDocuments();
		try {
			if (addressProof != null) {
				documents.setAddressProof(addressProof.getBytes());
			}
			if (panCard != null) {
				
				documents.setPanCard(panCard.getBytes());
			}
			if (incomeTax != null) {
				documents.setIncomeTax(incomeTax.getBytes());
			}
			if (adharCard != null) {
				documents.setAdharCard(adharCard.getBytes());
			}
			if (photo != null) {
				documents.setPhoto(photo.getBytes());
			}
			if (signature != null) {
				documents.setSignature(signature.getBytes());
			}
			if (bankCheque != null) {
				documents.setBankCheque(bankCheque.getBytes());
			}
			if (salarySlip != null) {
				documents.setSalarySlip(salarySlip.getBytes());
			}
			cust.setAllPersonalDocuments(documents);
			customerRepository.save(cust);

		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		return cust;
	}

	@Override
	public Customer forwardToOe(int customerId) {
	
		Customer customer = customerRepository.findByCustomerId(customerId);
		customer.setLoanStatus(LoanStatus.ForwardToOE);
		customerRepository.save(customer);
		
		return customer;
	}

	@Override
	public Customer updateLocalAddress(int customerId, LocalAddress localAddress)
	{
		Customer customer = customerRepository.findByCustomerId(customerId);
		customer.getCustomerAddress().getLocalAddress().setAreaName(localAddress.getAreaName());
		customer.getCustomerAddress().getLocalAddress().setCityName(localAddress.getCityName());
		customer.getCustomerAddress().getLocalAddress().setDistrict(localAddress.getDistrict());
		customer.getCustomerAddress().getLocalAddress().setHouseNumber(localAddress.getHouseNumber());
		customer.getCustomerAddress().getLocalAddress().setPincode(localAddress.getPincode());
		customer.getCustomerAddress().getLocalAddress().setState(localAddress.getState());
		customer.getCustomerAddress().getLocalAddress().setStreetName(localAddress.getStreetName());
		customerRepository.save(customer);
		return customer;
	}

	


	

}
