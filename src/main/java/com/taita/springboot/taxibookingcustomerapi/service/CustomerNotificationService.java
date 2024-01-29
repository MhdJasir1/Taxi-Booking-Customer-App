package com.taita.springboot.taxibookingcustomerapi.service;

import com.taita.springboot.taxibookingcustomerapi.dto.CustomerNotificationDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.VerificationCodeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CustomerNotificationService {
    void saveNotification(CustomerNotificationDTO customerNotificationDTO);

    ResponseEntity<?> getCustomerNotification(VerificationCodeDTO customerNotificationDTO);

}
