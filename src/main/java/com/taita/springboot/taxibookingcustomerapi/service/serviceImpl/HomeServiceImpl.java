package com.taita.springboot.taxibookingcustomerapi.service.serviceImpl;

import com.taita.springboot.taxibookingcustomerapi.dto.*;
import com.taita.springboot.taxibookingcustomerapi.entity.Customer;
import com.taita.springboot.taxibookingcustomerapi.entity.CustomerTrip;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerRepository;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerTripRepository;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerNotificationService;
import com.taita.springboot.taxibookingcustomerapi.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RequestMetaDTO requestMetaDTO;
    @Autowired
    CustomerTripRepository customerTripRepository;
    @Autowired
    CustomerNotificationService customerNotificationService;

    @Override
    public ResponseEntity<?> checkCustomerOnGoingTrip(VerificationCodeDTO verificationCodeDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        } else if (verificationCodeDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verificationCodeDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification");
        } else {

            List<CustomerTrip> customerTripList = customerTripRepository.findAllByCustomerId(requestMetaDTO.getCustomerId());

            List<CustomerTrip> onGoingTrips = new ArrayList<>();
            customerTripList.forEach(trips -> {
                if (String.valueOf(trips.getTripStatus()).equals("0") ||
                        String.valueOf(trips.getTripStatus()).equals("1") ||
                        String.valueOf(trips.getTripStatus()).equals("2") ||
                        String.valueOf(trips.getTripStatus()).equals("3")) {
                    onGoingTrips.add(trips);
                } else if (String.valueOf(trips.getTripStatus()).equals("4") ||
                        String.valueOf(trips.getTripStatus()).equals("5") ||
                        String.valueOf(trips.getTripStatus()).equals("6") ||
                        String.valueOf(trips.getTripStatus()).equals("7")) {
//                    return ResponseEntity.status(HttpStatus.OK).body("results not available!");
                }
            });
            return ResponseEntity.status(HttpStatus.OK).body(onGoingTrips);
        }
    }

    @Override
    public ResponseEntity<?> updateHomeOfficeAddress(CustomerAddressDTO customerAddressDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        } else if (customerAddressDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(customerAddressDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification");
        } else if (customerAddressDTO.getAddressText().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter address!");
        } else if (customerAddressDTO.getAddressLat().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter address latitude!");
        } else if (customerAddressDTO.getAddressLon().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter address longitude!");
        } else if (String.valueOf(customerAddressDTO.getAddressType()).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid address type!");
        } else {
            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            if (String.valueOf(customerAddressDTO.getAddressType()).equals("1")) {
                //home address
                customer.setHomeAddress(customerAddressDTO.getAddressText());
                customer.setHomeAddressLat(customerAddressDTO.getAddressLat());
                customer.setHomeAddressLon(customerAddressDTO.getAddressLon());
                customer.setAddressType(1);
            }else if (String.valueOf(customerAddressDTO.getAddressType()).equals("2")) {
                //office address
                customer.setOfficeAddress(customerAddressDTO.getAddressText());
                customer.setOfficeAddressLat(customerAddressDTO.getAddressLat());
                customer.setOfficeAddressLon(customerAddressDTO.getAddressLon());
                customer.setAddressType(2);
            }
            customerRepository.save(customer);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                    "Customer address updated successfully",
                    "Customer address update",
                    1));
        }
        return ResponseEntity.status(HttpStatus.OK).body("success!");
    }

    @Override
    public ResponseEntity<?> removeHomeOfficeAddress(RemoveAddressDTO removeAddressDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        } else if (removeAddressDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(removeAddressDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification");
        } else if (String.valueOf(removeAddressDTO.getAddressType()).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid address type!");
        }else{
            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            if (String.valueOf(removeAddressDTO.getAddressType()).equals("1")){
                customer.setHomeAddress("");
                customer.setHomeAddressLat("");
                customer.setHomeAddressLon("");
                customer.setAddressType(0);
            } else if (String.valueOf(removeAddressDTO.getAddressType()).equals("2")){
                customer.setOfficeAddress("");
                customer.setOfficeAddressLat("");
                customer.setOfficeAddressLon("");
                customer.setAddressType(0);
            }
            customerRepository.save(customer);
            customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                    "Customer address removed successfully",
                    "Customer address remove",
                    1));
            return ResponseEntity.status(HttpStatus.OK).body("success!");
        }
    }

//    @Override
//    public ResponseEntity<?> getCustomerRecentVisitedDestination(VerificationCodeDTO verificationCodeDTO) {
//        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
//        } else if (verificationCodeDTO.getVerification().equals("")) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
//        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verificationCodeDTO.getVerification())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification!");
//        }else{
//            List<CustomerTrip> trips = customerTripRepository.findAllByCustomerId(requestMetaDTO.getCustomerId());
//            trips.forEach(trip -> {
//                try {
//                    Date tripDate = simpleDateFormat.parse(trips.getTripDate());
//
//                    if (tripDate.equals(date) || tripDate.after(date)) {
//                        customerTrips.add(trips);
//                    }
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//
//            return ResponseEntity.status(HttpStatus.OK).body(null);
//        }
//    }
}
