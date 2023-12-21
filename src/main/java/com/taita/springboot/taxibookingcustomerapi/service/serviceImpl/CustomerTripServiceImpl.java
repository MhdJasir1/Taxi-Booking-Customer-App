package com.taita.springboot.taxibookingcustomerapi.service.serviceImpl;

import com.taita.springboot.taxibookingcustomerapi.dto.*;
import com.taita.springboot.taxibookingcustomerapi.entity.Customer;
import com.taita.springboot.taxibookingcustomerapi.entity.CustomerTrip;
import com.taita.springboot.taxibookingcustomerapi.entity.RiderRating;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerRepository;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerTripRepository;
import com.taita.springboot.taxibookingcustomerapi.repository.RiderRepository;
import com.taita.springboot.taxibookingcustomerapi.repository.VehicleRepository;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerNotificationService;
import com.taita.springboot.taxibookingcustomerapi.service.CustomerTripService;
import com.taita.springboot.taxibookingcustomerapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerTripServiceImpl implements CustomerTripService {
    @Autowired
    VehicleRepository vehicleTypeRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerTripRepository customerTripRepository;

    @Autowired
    RiderRepository riderRepository;

    @Autowired
    RequestMetaDTO requestMetaDTO;
    @Autowired
    CustomerNotificationService customerNotificationService;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public ResponseEntity<?> requestNewTripByCustomer(CustomerNewTripDTO customerNewTripDTO) {
        if (customerNewTripDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (String.valueOf(customerNewTripDTO.getVehicleTypeId()).equals("0")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter vehicle type id!");
        } else if (customerNewTripDTO.getStartLat().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter start latitude!");
        } else if (customerNewTripDTO.getStartLon().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter start longitude!");
        } else if (customerNewTripDTO.getEndLat().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter end latitude!");
        } else if (customerNewTripDTO.getEndLon().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter end longitude!");
        } else if (customerNewTripDTO.getCustomerNote().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter customer note!");
        } else if (customerNewTripDTO.getDistance().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter distance!");
        } else if (customerNewTripDTO.getStartPoint().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter start point!");
        } else if (customerNewTripDTO.getEndPoint().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter end point!");
        } else {
            Optional<Customer> optionalCustomer = customerRepository.findById(requestMetaDTO.getCustomerId());
            Customer customer = optionalCustomer.get();
            if (!customer.getVerification().equals(customerNewTripDTO.getVerification())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification");
            }

            if (optionalCustomer.isPresent()) {
                CustomerTrip customerTrip = new CustomerTrip();
                customerTrip.setCustomerId(customer.getCustomerId());
                customerTrip.setVehicleTypeId(customerNewTripDTO.getVehicleTypeId());
                customerTrip.setStartLat(customerNewTripDTO.getStartLat());
                customerTrip.setStartLon(customerNewTripDTO.getStartLon());
                customerTrip.setEndLat(customerNewTripDTO.getEndLat());
                customerTrip.setEndLon(customerNewTripDTO.getEndLon());
                customerTrip.setTripDistance(Double.parseDouble(customerNewTripDTO.getDistance()));
                customerTrip.setCustomerNote(customerNewTripDTO.getCustomerNote());
                customerTrip.setStartPoint(customerNewTripDTO.getStartPoint());
                customerTrip.setEndPoint(customerNewTripDTO.getEndPoint());
                customerTrip.setTripDate(String.valueOf(LocalDate.now()));
                customerTrip.setTripTime(String.valueOf(LocalTime.now()));
                customerTripRepository.save(customerTrip);

                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                        "New trip requested successfully",
                        "New trip requested",
                        1));

                return ResponseEntity.status(HttpStatus.OK).body("success");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customer id!");
            }

        }
    }

    @Override
    public ResponseEntity<?> customerRejectTrip(int tripId, CustomerRejectTripDTO customerRejectTripDTO) {
        if (String.valueOf(tripId).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip id not found!");
        } else if (customerRejectTripDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (customerRejectTripDTO.getReason().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter reason!");
        } else if (customerTripRepository.findById(tripId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid trip id!");
        } else {
            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            if (!customer.getVerification().equals(customerRejectTripDTO.getVerification())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification");
            }
            CustomerTrip customerTrip = customerTripRepository.findById(tripId).get();
            customerTrip.setCustomerId(customer.getCustomerId());
            customerTrip.setRejectReason(customerRejectTripDTO.getReason());
            customerTrip.setRejectDate(String.valueOf(LocalDate.now()));
            customerTrip.setRejectTime(String.valueOf(LocalTime.now()));
            customerTrip.setRejectUserType(1);
            customerTrip.setTripStatus(6);
            customerTripRepository.save(customerTrip);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                    "Trip reject successfully",
                    "Trip reject request",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("success");

        }
    }

    @Override
    public ResponseEntity<?> checkCustomerTripAcceptedByRider(int tripId, VerificationCodeDTO verificationCodeDTO) {
        if (String.valueOf(tripId).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip id not found!");
        } else if (verificationCodeDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (customerTripRepository.findById(tripId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid trip id!");
        } else {
            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            if (!customer.getVerification().equals(verificationCodeDTO.getVerification())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification");
            }

            CustomerTrip customerTrip = customerTripRepository.findById(tripId).get();
            if (String.valueOf(verificationCodeDTO.getVerification()).equals("0")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                        "Riders not found (Trip ID:"+customerTrip.getCustomerTripId()+")",
                        "All riders are busy for the newly requested trip",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("pending");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("1")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                        "Looking for new rider (Trip ID:"+customerTrip.getCustomerTripId()+")",
                        "Search for rider",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("search for rider");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("2")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                        "Rider assigned to the trip (Trip ID:"+customerTrip.getCustomerTripId()+")",
                        "Rider assigned",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("rider assigned");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("3")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                        "Trip started successfully (Trip ID:"+customerTrip.getCustomerTripId()+")",
                        "Trip started",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("trip start");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("4")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                        "Trip ended successfully (Trip ID:"+customerTrip.getCustomerTripId()+")",
                        "Trip ended",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("trip end");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("5")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                        "Trip canceled by rider for some reason.. please retry (Trip ID:"+customerTrip.getCustomerTripId()+")",
                        "Trip cancel by rider",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("trip cancel by rider");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("6")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                        "Trip canceled by customer for some reason (Trip ID:"+customerTrip.getCustomerTripId()+")",
                        "Trip cancel by customer",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("trip cancel by customer");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("7")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                        "Rider not found for the trip. please try again (Trip ID:"+customerTrip.getCustomerTripId()+")",
                        "Rider not found for trip",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("rider not found for trip");
            }

            return ResponseEntity.status(HttpStatus.OK).body("Something went wrong!");
        }
    }

    @Override
    public ResponseEntity<?> getTripDataCustomerById(int tripId, VerificationCodeDTO verificationCodeDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        } else if (String.valueOf(tripId).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip id not found!");
        } else if (verificationCodeDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (customerTripRepository.findById(tripId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid trip id!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verificationCodeDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid verification!");
        } else {
            CustomerTrip customerTrip = customerTripRepository.findById(tripId).get();
            return ResponseEntity.status(HttpStatus.OK).body(customerTrip);
        }
    }

    @Override
    public ResponseEntity<?> rateRider(int tripId, RateRiderDTO rateRiderDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        } else if (String.valueOf(tripId).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip id not found!");
        } else if (customerTripRepository.findById(tripId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid trip id!");
        } else if (rateRiderDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(rateRiderDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid verification!");
        } else if (rateRiderDTO.getStarRate().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter star rating!");
        } else if (rateRiderDTO.getComment().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter comment!");
        } else {
            RiderRating riderRating = new RiderRating();
            riderRating.setCustomerTripId(tripId);
            riderRating.setDate(String.valueOf(LocalDate.now()));
            riderRating.setTime(String.valueOf(LocalTime.now()));
            riderRating.setStarRate(rateRiderDTO.getStarRate());
            riderRating.setComment(rateRiderDTO.getComment());
            riderRepository.save(riderRating);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(customerRepository.findByMobile1(requestMetaDTO.getMobile()).getCustomerId(),
                    "Raider rated successfully",
                    "Rider rating",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("success!");

        }
    }

    @Override
    public ResponseEntity<?> getTripHistoryByCustomer(CustomerTripHistoryDTO customerTripHistoryDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid customer id!");
        } else if (customerTripHistoryDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter verification!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(customerTripHistoryDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid verification!");
        } else {
            if (customerTripHistoryDTO.getDate().equals("")) {
                //date selected
                List<CustomerTrip> todayTrips = customerTripRepository.findAllByCustomerIdAndTripDate(requestMetaDTO.getCustomerId(), customerTripHistoryDTO.getDate());
                return ResponseEntity.status(HttpStatus.OK).body(todayTrips);
            } else {
                //date not selected
                String tripHistoryString = customerTripHistoryDTO.getDate();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                List<CustomerTrip> customerTrips = new ArrayList<>();

                try {
                    Date date = simpleDateFormat.parse(tripHistoryString);
                    List<CustomerTrip> allTrips = customerTripRepository.findAllByCustomerId(requestMetaDTO.getCustomerId());
                    allTrips.forEach(trips -> {
                        try {
                            Date tripDate = simpleDateFormat.parse(trips.getTripDate());

                            if (tripDate.equals(date) || tripDate.after(date)) {
                                customerTrips.add(trips);
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customerTrips);
            }
        }
    }

}
