package com.taita.springboot.taxibookingcustomerapi.service;

import com.taita.springboot.taxibookingcustomerapi.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CustomerTripService {
    ResponseEntity<?> requestNewTripByCustomer(CustomerNewTripDTO customerNewTripDTO);

    ResponseEntity<?> customerRejectTrip(int tripId, CustomerRejectTripDTO customerRejectTripDTO);

    ResponseEntity<?> checkCustomerTripAcceptedByRider(int tripId, VerificationCodeDTO verificationCodeDTO);

    ResponseEntity<?> getTripDataCustomerById(int tripId, VerificationCodeDTO verificationCodeDTO);

    ResponseEntity<?> rateRider(int tripId, RateRiderDTO rateRiderDTO);

    ResponseEntity<?> getTripHistoryByCustomer(CustomerTripHistoryDTO customerTripHistoryDTO);

}
