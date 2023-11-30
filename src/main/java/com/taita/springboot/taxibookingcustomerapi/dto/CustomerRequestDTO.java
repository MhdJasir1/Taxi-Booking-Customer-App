package com.taita.springboot.taxibookingcustomerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {
    private String mobile;
    private String name;
    private String notificationKey;
    private String currentLat;
    private String currentLon;
}
