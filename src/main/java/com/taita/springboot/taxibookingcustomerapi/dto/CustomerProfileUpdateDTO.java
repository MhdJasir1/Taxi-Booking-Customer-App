package com.taita.springboot.taxibookingcustomerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileUpdateDTO {
    private String verification;
    private String fullName;
    private String emergencyNumber;
    private String birthday;
    private String email;
    private String nic;
}
