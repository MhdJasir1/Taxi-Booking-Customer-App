package com.taita.springboot.taxibookingcustomerapi.repository;

import com.taita.springboot.taxibookingcustomerapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByMobile1(String mobile);

}
