package com.taita.springboot.taxibookingcustomerapi.repository;

import com.taita.springboot.taxibookingcustomerapi.entity.Customer;
import com.taita.springboot.taxibookingcustomerapi.entity.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMessageRepository extends JpaRepository<CustomerContact, Integer> {
}
