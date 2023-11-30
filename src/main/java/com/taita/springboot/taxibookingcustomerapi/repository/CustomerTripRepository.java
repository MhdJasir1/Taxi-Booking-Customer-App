package com.taita.springboot.taxibookingcustomerapi.repository;

import com.taita.springboot.taxibookingcustomerapi.entity.CustomerTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerTripRepository extends JpaRepository<CustomerTrip, Integer> {
    List<CustomerTrip> findAllByCustomerId(int customerId);
    List<CustomerTrip> findAllByCustomerIdAndTripDate(int customerId, String tripDate);
}
