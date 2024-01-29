package com.taita.springboot.taxibookingcustomerapi.service.serviceImpl;

import com.taita.springboot.taxibookingcustomerapi.dto.CustomerNotificationDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.RequestMetaDTO;
import com.taita.springboot.taxibookingcustomerapi.dto.VerificationCodeDTO;
import com.taita.springboot.taxibookingcustomerapi.entity.CustomerNotification;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerNotificationRepository;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerRepository;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CustomerNotificationServiceImpl implements CustomerNotificationService {
    @Autowired
    CustomerNotificationRepository customerNotificationRepository;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RequestMetaDTO requestMetaDTO;

    @Override
    public void saveNotification(CustomerNotificationDTO customerNotificationDTO) {
        CustomerNotification customerNotification = new CustomerNotification();
        customerNotification.setCustomerId(customerNotificationDTO.getCustomerId());
        customerNotification.setNotificationTopic(customerNotificationDTO.getNotificationTopic());
        customerNotification.setNotification(customerNotificationDTO.getNotification());
        customerNotification.setDate(String.valueOf(LocalDate.now()));
        customerNotification.setTime(String.valueOf(LocalTime.now()));
        customerNotification.setShowToUser(customerNotificationDTO.getShowToUser());

        customerNotificationRepository.save(customerNotification);
    }

    @Override
    public ResponseEntity<?> getCustomerNotification(VerificationCodeDTO verificationCodeDTO) {
        if (customerNotificationRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        } else if (verificationCodeDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verificationCodeDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification");
        } else {

            List<CustomerNotification> customerNotifications = customerNotificationRepository.findAllByCustomerIdAndShowToUser(requestMetaDTO.getCustomerId(), 1);
            return ResponseEntity.status(HttpStatus.OK).body(customerNotifications);
        }
    }

}
