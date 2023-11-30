package com.taita.springboot.taxibookingcustomerapi.repository;

import com.taita.springboot.taxibookingcustomerapi.entity.RiderRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface RiderRepository extends JpaRepository<RiderRating,Integer> {
}
