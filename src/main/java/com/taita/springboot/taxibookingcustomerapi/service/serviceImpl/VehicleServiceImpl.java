package com.taita.springboot.taxibookingcustomerapi.service.serviceImpl;

import com.taita.springboot.taxibookingcustomerapi.dto.RequestMetaDTO;
import com.taita.springboot.taxibookingcustomerapi.entity.CustomerTrip;
import com.taita.springboot.taxibookingcustomerapi.entity.VehicleType;
import com.taita.springboot.taxibookingcustomerapi.repository.CustomerRepository;
import com.taita.springboot.taxibookingcustomerapi.repository.VehicleRepository;
import com.taita.springboot.taxibookingcustomerapi.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;
    @Override
    public ResponseEntity<?> getVehicleTypes() {
        List<VehicleType> vehicleTypes = vehicleRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(vehicleTypes);
    }
}
