package com.taita.springboot.taxibookingcustomerapi.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface VehicleService {
    ResponseEntity<?> getVehicleTypes();
}
