package com.taita.springboot.taxibookingcustomerapi.service;

import com.taita.springboot.taxibookingcustomerapi.dto.CustomerAddressDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.RemoveAddressDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.VerificationCodeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface HomeService {
    ResponseEntity<?> checkCustomerOnGoingTrip(VerificationCodeDTO verificationCodeDTO);
    ResponseEntity<?> updateHomeOfficeAddress(CustomerAddressDTO customerAddressDTO);
    ResponseEntity<?> removeHomeOfficeAddress(RemoveAddressDTO removeAddressDTO);
//    ResponseEntity<?> getCustomerRecentVisitedDestination(VerificationCodeDTO verificationCodeDTO);

}
