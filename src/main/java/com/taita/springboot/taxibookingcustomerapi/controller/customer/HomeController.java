package com.taita.springboot.taxibookingcustomerapi.controller.customer;

import com.taita.springboot.taxibookingcustomerapi.dto.CustomerAddressDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.CustomerProfileUpdateDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.RemoveAddressDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.VerificationCodeDTO;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerService;
import com.taita.springboot.taxibookingcustomerapi.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    HomeService homeService;

    @GetMapping("/ongoing_trips/{app_id}")
    public ResponseEntity<?> checkCustomerOnGoingTrip(@PathVariable("app_id") String appId, @RequestBody VerificationCodeDTO verificationCodeDTO){
        if (appId.equals("novatechzone_customer_app")){
            return homeService.checkCustomerOnGoingTrip(verificationCodeDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PutMapping("/update_address/{app_id}")
    public ResponseEntity<?> updateHomeOfficeAddress(@PathVariable("app_id") String appId, @RequestBody CustomerAddressDTO customerAddressDTO){
        if (appId.equals("novatechzone_customer_app")){
            return homeService.updateHomeOfficeAddress(customerAddressDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @DeleteMapping("/remove_address/{app_id}")
    public ResponseEntity<?> removeHomeOfficeAddress(@PathVariable("app_id") String appId, @RequestBody RemoveAddressDTO removeAddressDTO){
        if (appId.equals("novatechzone_customer_app")){
            return homeService.removeHomeOfficeAddress(removeAddressDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

//    @GetMapping("/recent_trips/{app_id}")
//    public ResponseEntity<?> getCustomerRecentVisitedDestination(@PathVariable("app_id") String appId, @RequestBody VerificationCodeDTO verificationCodeDTO){
//        if (appId.equals("novatechzone_customer_app")){
//            return homeService.getCustomerRecentVisitedDestination(verificationCodeDTO);
//        }else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
//        }
//    }

}
