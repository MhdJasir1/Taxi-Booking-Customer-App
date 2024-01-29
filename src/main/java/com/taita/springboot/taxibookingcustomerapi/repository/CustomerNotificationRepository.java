package com.taita.springboot.taxibookingcustomerapi.repository;

import com.taita.springboot.taxibookingcustomerapi.entity.CustomerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerNotificationRepository extends JpaRepository<CustomerNotification, Integer> {
    List<CustomerNotification> findAllByCustomerIdAndShowToUser(int customerId, int i);

}
