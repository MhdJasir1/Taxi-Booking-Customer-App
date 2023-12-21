package com.taita.springboot.taxibookingcustomerapi.service.serviceImpl;


import com.taita.springboot.taxibookingcustomerapi.dto.*;
import com.taita.springboot.taxibookingcustomerapi.entity.Customer;
import com.taita.springboot.taxibookingcustomerapi.entity.CustomerContact;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerMessageRepository;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerNotificationRepository;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerRepository;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerNotificationService;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerService;
import com.taita.springboot.taxibookingcustomerapi.util.JwtUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMessageRepository customerMessageRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    CustomerNotificationService customerNotificationService;
    @Autowired
    CustomerNotificationRepository customerNotificationRepository;
    @Autowired
    RequestMetaDTO requestMetaDTO;

    public static final String UPLOAD_DIR = "upload";
    @Override
    public ResponseEntity<?> registerAndLoginCustomer(CustomerRequestDTO customerRequestDTO) {
            if(customerRequestDTO.getMobile().equals("")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter mobile");
            }else if(customerRequestDTO.getName().equals("")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter name");
            }else if(customerRequestDTO.getNotificationKey().equals("")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter notification key");
            }else if(customerRequestDTO.getCurrentLat().equals("")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter current latitude");
            }else if(customerRequestDTO.getCurrentLon().equals("")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter current longitude");
            }else {
                Customer customerDetails = customerRepository.findByMobile1(customerRequestDTO.getMobile());
                if(customerDetails != null){
                    return ResponseEntity.status(HttpStatus.OK).body("Account already exist!");
                }
                if (customerDetails == null){
                    //register
                    Customer customer = new Customer();
                    customer.setMobile1(customerRequestDTO.getMobile());
                    customer.setUsername(customerRequestDTO.getName());
                    customer.setNotificationKey(customerRequestDTO.getNotificationKey());
                    customer.setCurrentLat(customerRequestDTO.getCurrentLat());
                    customer.setCurrentLon(customerRequestDTO.getCurrentLon());

                    customerRepository.save(customer);

                    customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(customerRequestDTO.getMobile()).getCustomerId(),
                            "Account created successfully",
                            "new customer Registration",
                            0));


                }else if(String.valueOf(customerDetails.getStatus()).equals("0")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your account blacklisted. Cannot login to account");
                }

                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(customerRequestDTO.getMobile()).getCustomerId(),
                        "Login success",
                        "Customer logged in",
                        1));

                return ResponseEntity.status(HttpStatus.OK).body("success!");
        }
    }

    @Override
    public ResponseEntity<?> getCustomerPinNumber(CustomerMobileDTO customerMobileDTO) {
        if(customerMobileDTO.getMobile().equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not registered");
        }else{
            Customer customerDetails = customerRepository.findByMobile1(customerMobileDTO.getMobile());
            if (customerDetails != null){
                String pinNumber = String.format("%06d", new Random().nextInt(999999));
                customerDetails.setVerification(pinNumber);
                customerDetails.setLastOtpDate(String.valueOf(LocalDate.now()));
                customerDetails.setLastOtpTime(String.valueOf(LocalTime.now()));
                customerRepository.save(customerDetails);

                return ResponseEntity.status(HttpStatus.OK).body(pinNumber);
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid mobile!");
            }

        }
    }

    @Override
    public ResponseEntity<?> verifyCustomerMobile(VerifyRequestDTO verifyRequestDTO) {
        if(verifyRequestDTO.getMobile().equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not registered");
        }else if(verifyRequestDTO.getOtp().equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid OTP!");
        }else{
            Customer customerDetails = customerRepository.findByMobile1(verifyRequestDTO.getMobile());
            if (customerDetails == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not registered!");
            }else{
                if (!customerDetails.getVerification().equals(verifyRequestDTO.getOtp())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Entered OTP is wrong, try again");
                }
                customerDetails.setStatus(1);
                customerRepository.save(customerDetails);

                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(verifyRequestDTO.getMobile()).getCustomerId(),
                        "Verification key is ",
                        "Verification send to your phone",
                        1));

                String accessToken = jwtUtil.generateAccessToken(customerDetails);
                Map<String, String> data = new HashMap<>();
                data.put("message","Good to go");
                data.put("accessToken", accessToken);
                return ResponseEntity.status(HttpStatus.OK).body(data);

            }
        }
    }

    @Override
    public ResponseEntity<?> updateProfile(CustomerProfileUpdateDTO customerProfileUpdateDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        }else if(customerProfileUpdateDTO.getFullName().equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter customer name!");
        }else if (customerProfileUpdateDTO.getEmergencyNumber().equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter birthday!");
        }else if(customerProfileUpdateDTO.getEmail().equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter email!");
        }else if(customerProfileUpdateDTO.getNic().equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter nic!");
        }else{
            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            customer.setFullName(customerProfileUpdateDTO.getFullName());
            customer.setMobile2(customerProfileUpdateDTO.getEmergencyNumber());
            customer.setBirthday(customerProfileUpdateDTO.getBirthday());
            customer.setEmail(customerProfileUpdateDTO.getEmail());
            customer.setNic(customerProfileUpdateDTO.getNic());
            customerRepository.save(customer);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                    "Profile updated successfully",
                    "Profile details updated successfully",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("success");
        }
    }

    @Override
    public ResponseEntity<?> sendMessage(SendMessageDTO sendMessageDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        } else if (sendMessageDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(sendMessageDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid verification!");
        }else if (sendMessageDTO.getEmail().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter email!");
        } else if (sendMessageDTO.getMessage().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter message!");
        }  else{
            CustomerContact customerContact = new CustomerContact();
            customerContact.setCustomerId(requestMetaDTO.getCustomerId());
            customerContact.setEmail(sendMessageDTO.getEmail());
            customerContact.setMessage(sendMessageDTO.getMessage());
            customerContact.setDate(String.valueOf(LocalDate.now()));
            customerContact.setTime(String.valueOf(LocalTime.now()));
            customerContact.setStatus(1);
            customerMessageRepository.save(customerContact);

            return ResponseEntity.status(HttpStatus.OK).body("success!");
        }
    }

    @Override
    public ResponseEntity<?> updateProfileImage(String verification, MultipartFile file) throws IOException {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        } else if (verification.equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verification)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid verification!");
        } else if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please select an image!");
        } else{
            Path path = Paths.get(UPLOAD_DIR);
            if (!Files.exists(path)){
                Files.createDirectory(path);
            }
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = System.currentTimeMillis() +"."+extension;

            Path filePath = path.resolve(fileName);
            Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);

            String appUrl = String.format("http://%s:%s", InetAddress.getLocalHost(),8080);
            String url = UPLOAD_DIR +"/"+fileName;
            String fullUrl = appUrl + "/" + url;

            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            customer.setProfileImage(url);
            customerRepository.save(customer);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                    "Profile pic updated successfully",
                    "Profile details updated successfully",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("success");

        }
    }

}
