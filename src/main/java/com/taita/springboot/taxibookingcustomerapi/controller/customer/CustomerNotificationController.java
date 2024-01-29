package com.taita.springboot.taxibookingcustomerapi.controller.customer;

import com.taita.springboot.taxibookingcustomerapi.dto.CustomerNotificationDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.VerificationCodeDTO;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerNotificationService;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class CustomerNotificationController {
    @Autowired
    CustomerNotificationService customerNotificationService;

    @GetMapping("/customer_notification/{app_id}")
    public ResponseEntity<?> getCustomerNotification(@PathVariable("app_id") String appId, @RequestBody VerificationCodeDTO verificationCodeDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerNotificationService.getCustomerNotification(verificationCodeDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid app id!");
        }

    }
}
