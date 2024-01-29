package com.taita.springboot.taxibookingcustomerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressDTO {
    private String verification;
    private String addressText;
    private String addressLat;
    private String addressLon;
    private int addressType;
}
