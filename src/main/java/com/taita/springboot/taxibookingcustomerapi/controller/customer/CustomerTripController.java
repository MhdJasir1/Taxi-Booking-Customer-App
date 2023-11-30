package com.taita.springboot.taxibookingcustomerapi.controller.customer;

import com.taita.springboot.taxibookingcustomerapi.dto.*;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trip")
public class CustomerTripController {
    @Autowired
    CustomerTripService customerTripService;

    @PostMapping("/new_trip/{app_id}")
    public ResponseEntity<?> requestNewTripByCustomer(@PathVariable("app_id") String appId, @RequestBody CustomerNewTripDTO customerNewTripDTO){
        if(appId.equals("novatechzone_customer_app")){
            return customerTripService.requestNewTripByCustomer(customerNewTripDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PostMapping("/reject_trip/{trip_id}/{app_id}")
    public ResponseEntity<?> customerRejectTrip(@PathVariable("trip_id") int tripId, @PathVariable("app_id") String appId, @RequestBody CustomerRejectTripDTO customerRejectTripDTO){
        if(appId.equals("novatechzone_customer_app")){
            return customerTripService.customerRejectTrip(tripId,customerRejectTripDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PostMapping("/trip_status/{trip_id}/{app_id}")
    public ResponseEntity<?> checkCustomerTripAcceptedByRider(@PathVariable("trip_id") int tripId, @PathVariable("app_id") String appId, @RequestBody VerificationCodeDTO verificationCodeDTO){
        if(appId.equals("novatechzone_customer_app")){
            return customerTripService.checkCustomerTripAcceptedByRider(tripId, verificationCodeDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PostMapping("/trip_auto_cancel/{trip_id}/{app_id}")
    public ResponseEntity<?> autoCancelTripByCustomer(@PathVariable("trip_id") int tripId, @PathVariable("app_id") String appId, @RequestBody VerificationCodeDTO verificationCodeDTO){
        if(appId.equals("novatechzone_customer_app")){
            //return customerTripService.autoCancelTripByCustomer(tripId, verificationCodeDTO);
            return null;
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PostMapping("/trip_data/{trip_id}/{app_id}")
    public ResponseEntity<?> getTripDataCustomerById(@PathVariable("trip_id") int tripId, @PathVariable("app_id") String appId, @RequestBody VerificationCodeDTO verificationCodeDTO){
        if (appId.equals("novatechzone_customer_app")){
            return customerTripService.getTripDataCustomerById(tripId,verificationCodeDTO);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PostMapping("/rate_rider/{trip_id}/{app_id}")
    public ResponseEntity<?> rateRider(@PathVariable("trip_id") int tripId, @PathVariable("app_id") String appId, @RequestBody RateRiderDTO rateRiderDTO){
        if (appId.equals("novatechzone_customer_app")){
            return customerTripService.rateRider(tripId,rateRiderDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @GetMapping("/trip_history/{app_id}")
    public ResponseEntity<?> getTripHistoryByCustomer(@PathVariable("app_id") String appId, @RequestBody CustomerTripHistoryDTO customerTripHistoryDTO){
        if (appId.equals("novatechzone_customer_app")){
            return customerTripService.getTripHistoryByCustomer(customerTripHistoryDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }


}
