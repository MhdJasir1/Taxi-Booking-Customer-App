package com.taita.springboot.taxibookingcustomerapi.controller.auth;

import com.taita.springboot.taxibookingcustomerapi.dto.CustomerMobileDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.CustomerRequestDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.VerifyRequestDTO;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class AuthController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/auth/{app_id}")
    public ResponseEntity<?> registerAndLoginCustomer(@PathVariable("app_id") String appId, @RequestBody CustomerRequestDTO customerRequestDTO){
        if(appId.equals("novatechzone_customer_app")){
            return customerService.registerAndLoginCustomer(customerRequestDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PostMapping("/auth/otp/{app_id}")
    public ResponseEntity<?> getCustomerPinNumber(@PathVariable("app_id") String appId,@RequestBody CustomerMobileDTO customerMobileDTO){
        if(appId.equals("novatechzone_customer_app")){
            return customerService.getCustomerPinNumber(customerMobileDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PostMapping("/auth/verify/{app_id}")
    public ResponseEntity<?> verifyCustomerMobile(@PathVariable("app_id") String appId, @RequestBody VerifyRequestDTO verifyRequestDTO){
        if(appId.equals("novatechzone_customer_app")){
            return customerService.verifyCustomerMobile(verifyRequestDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }
}
