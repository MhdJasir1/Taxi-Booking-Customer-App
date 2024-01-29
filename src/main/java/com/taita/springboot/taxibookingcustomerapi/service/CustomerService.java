package com.taita.springboot.taxibookingcustomerapi.service;


import com.taita.springboot.taxibookingcustomerapi.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface CustomerService {

    ResponseEntity<?> registerAndLoginCustomer(CustomerRequestDTO customerRequestDTO);

    ResponseEntity<?> getCustomerPinNumber(CustomerMobileDTO customerMobileDTO);

    ResponseEntity<?> verifyCustomerMobile(VerifyRequestDTO verifyRequestDTO);

    ResponseEntity<?> updateProfile(CustomerProfileUpdateDTO customerProfileUpdateDTO);

    ResponseEntity<?> sendMessage(SendMessageDTO sendMessageDTO);

    ResponseEntity<?> updateProfileImage(String verification, MultipartFile image) throws IOException;

}
