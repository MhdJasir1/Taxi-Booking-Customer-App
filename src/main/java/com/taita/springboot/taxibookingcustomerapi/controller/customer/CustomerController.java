package com.taita.springboot.taxibookingcustomerapi.controller.customer;

import com.taita.springboot.taxibookingcustomerapi.dto.*;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/profile/update_profile/{app_id}")
    public ResponseEntity<?> updateProfile(@PathVariable("app_id") String appId, @RequestBody CustomerProfileUpdateDTO customerProfileUpdateDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.updateProfile(customerProfileUpdateDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PostMapping("/send_message/{app_id}")
    public ResponseEntity<?> sendMessage(@PathVariable("app_id") String appId, @RequestBody SendMessageDTO sendMessageDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.sendMessage(sendMessageDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

    @PostMapping("/update_profile_pic/{app_id}")
    public ResponseEntity<?> updateProfileImage(@PathVariable("app_id") String appId, @RequestParam String verification, @RequestParam("image") MultipartFile file) throws IOException {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.updateProfileImage(verification,file);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }
    }

}
