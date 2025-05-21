package com.app.crm.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.app.crm.enums.LoanStatus;
import com.app.crm.model.Customer;
import com.app.crm.model.Enquiry;
import com.app.crm.model.LocalAddress;
import com.app.crm.service.CustomerService;
import com.app.crm.service.EnquiryService;

@CrossOrigin("*")
@RestController
public class CustomerController {

	@Autowired
	RestTemplate rt;
	@Autowired
	EnquiryService enquiryService;

	@Autowired
	private CustomerService customerService;
	
	
	
	@GetMapping("/get_All_Enquiry")
	public ResponseEntity<List<Enquiry>> getALLEnquiredCustomer()
	{
		List<Enquiry> getAllEnquiries=enquiryService.findAllEnquiry();
		return new ResponseEntity<List<Enquiry>>(getAllEnquiries,HttpStatus.OK);
	}
	
	
	@GetMapping("/forwordingtoOe/{customerId}")
	public ResponseEntity<Customer> forwordingtoOe(@PathVariable("customerId")int customerId)
	{
		
		Customer customer=customerService.forwardToOe(customerId);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	
	
	
	
	

	@PostMapping("/saveCustomer/{customerId}")
	public ResponseEntity<Customer> onSaveCustomerDetails(@PathVariable("customerId") int customerId,
			@RequestPart("customer") String customer, @RequestPart("addressProof") MultipartFile addressProof,
			@RequestPart("panCard") MultipartFile panCard, @RequestPart("incomeTax") MultipartFile incomeTax,
			@RequestPart("adharCard") MultipartFile adharCard, @RequestPart("photo") MultipartFile photo,
			@RequestPart("signature") MultipartFile signature, @RequestPart("bankCheque") MultipartFile bankCheque,
			@RequestPart("salarySlip") MultipartFile salarySlip) {
		Customer savecustomer=null;
		String url="http://localhost:7070/getenquirybyenquiryId/"+customerId;
		Enquiry enquiry = rt.getForObject(url,Enquiry.class);
		System.out.println(enquiry.getEnquiryStatus());
		if(enquiry.getEnquiryStatus().equalsIgnoreCase("Applicable_for_loan"))
		{
//			System.out.println("Controller");
//			System.out.println(enquiry.getEnquiryStatus());
			 savecustomer = customerService.saveCustomerDetails(enquiry, customer, addressProof, panCard, incomeTax,
					adharCard, photo, signature, bankCheque, salarySlip);
			return new ResponseEntity<Customer>(savecustomer, HttpStatus.CREATED);
		}else
		{
			Customer c=new Customer();
			return new ResponseEntity<Customer>(c, HttpStatus.CREATED);
		}
				
	//return new ResponseEntity<Customer>(savecustomer, HttpStatus.CREATED);
	}

		
	@GetMapping("/loanStatusPending/{loanStatus}")
	public ResponseEntity<List< Customer>> loanStatusPending(@PathVariable("loanStatus")LoanStatus loanStatus)
	{
		List< Customer> customer=customerService.getCustomerByLoanStatus(loanStatus);
		return new ResponseEntity<List<Customer>>(customer,HttpStatus.OK);
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/save")
	public ResponseEntity<Customer> onSaveCustomerDetails(@RequestBody Customer c) {
		Customer saveCustomer = customerService.saveCustomerDetails(c);
		return new ResponseEntity<Customer>(saveCustomer, HttpStatus.CREATED);
	}


	
	
	int checkOtp = 0;
	Customer customer = null;

	@GetMapping("/otp_genratation/{customerEmail}/{password}")
	public ResponseEntity<Object> getByEmailAndPassword(@PathVariable("customerEmail") String customerEmail,
			@PathVariable("password") String password) {
		customer = customerService.findByEmailAndPassword(customerEmail, password);
		Random random = new Random();
		checkOtp = random.nextInt(1000) + 9000;
		String s = checkOtp + "  Please Login With This Opt";
		return new ResponseEntity<Object>(s, HttpStatus.OK);
	}

	@GetMapping("/otplogin/{otp}")
	public ResponseEntity<Customer> otpLogin(@PathVariable("otp") int otp) {
		
		if(checkOtp==0)
		{
			return new ResponseEntity<Customer>(new Customer(), HttpStatus.OK);
		}
		if (otp == checkOtp) {
			checkOtp=0;
			return new ResponseEntity<Customer>(customer, HttpStatus.OK);
		} else {
			return new ResponseEntity<Customer>(new Customer(), HttpStatus.OK);
		}
	}
	
	
	
	StringBuilder captcha = new StringBuilder();
	Customer customer1 = null;
	@GetMapping("/captch_genratation/{customerEmail}/{password}")
	public ResponseEntity<Object> captchaLogin(@PathVariable("customerEmail") String customerEmail,
			@PathVariable("password") String password) {
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@#$%&*".toCharArray();
	    char[] numbers = "0123456789".toCharArray();

		Random random = new Random();
		customer1 = customerService.findByEmailAndPassword(customerEmail, password);

		for (int i = 0; i < 6; i++) {  
	        if (random.nextBoolean()) {
	            captcha.append(chars[random.nextInt(chars.length)]);
	        } else {
	            captcha.append(numbers[random.nextInt(numbers.length)]);
	        }
	    }		return new ResponseEntity<Object>(captcha+ "  this is your captch", HttpStatus.CREATED);
	}
	
	@GetMapping("/captchalogin/{captcha}")
	public ResponseEntity<Customer> captchaLogin(@PathVariable("captcha") String captcha1) {
		
		if(captcha==null)
		{
			return new ResponseEntity<Customer>(new  Customer() , HttpStatus.OK);
		}
		else	if (captcha.toString().equals(captcha1.toString())){
			captcha=null;
			return new ResponseEntity<Customer>(customer1, HttpStatus.OK);
		} else {
			return new ResponseEntity<Customer>(new Customer(), HttpStatus.OK);
		}
		
	}
	

	@GetMapping("/findAllCustomer")
	public ResponseEntity<List<Customer>> getAllCustomerData() {
		List<Customer> allCustomer = customerService.findAllCustomer();
		return new ResponseEntity<List<Customer>>(allCustomer, HttpStatus.OK);
	}

	
	@GetMapping("/getBycustomerId/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId")int customerId) {
		Customer customer=customerService.findByCustomerId(customerId);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

	@PutMapping("/update/{property}/{customerId}")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer,
			@PathVariable("customerId") int customerId, @PathVariable("property") String property) {
		Customer cust = customerService.updateCustomer(customerId, property, customer);
		return new ResponseEntity<Customer>(cust, HttpStatus.OK);
	}

	@PutMapping("/updateAllData/{customerId}")
	public ResponseEntity<Customer> updateAllCustomer(@PathVariable("customerId") int customerId,
			@RequestBody Customer customer) {
		Customer cust = customerService.updateCustomer(customerId, customer);
		return new ResponseEntity<>(cust, HttpStatus.OK);
	}

	@PutMapping("/updateDocument/{customerId}")
	public ResponseEntity<Customer> updateAllDocument(@PathVariable("customerId") int customerId,
			@RequestPart("addressProof") MultipartFile addressProof, @RequestPart("panCard") MultipartFile panCard,
			@RequestPart("incomeTax") MultipartFile incomeTax, @RequestPart("adharCard") MultipartFile adharCard,
			@RequestPart("photo") MultipartFile photo, @RequestPart("signature") MultipartFile signature,
			@RequestPart("bankCheque") MultipartFile bankCheque, @RequestPart("salarySlip") MultipartFile salarySlip) {

		Customer customers = customerService.updateDocument(customerId, addressProof, panCard, incomeTax, adharCard,
				photo, signature, bankCheque, salarySlip);
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	
	@PutMapping("/updateAddress/{customerId}")
	public ResponseEntity<Customer> updateLocalAddress(@PathVariable("customerId")int customerId,@RequestBody LocalAddress localAddress){
		Customer customer=customerService.updateLocalAddress(customerId,localAddress);
		return new ResponseEntity<Customer>(customer,HttpStatus.OK);
	}
	
	
	

	
	
	
	
}
