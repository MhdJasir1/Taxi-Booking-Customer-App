package com.taita.springboot.taxibookingcustomerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNewTripDTO {
    private String verification;
    private int vehicleTypeId;
    private String startLat;
    private String startLon;
    private String endLat;
    private String endLon;
    private String customerNote;
    private String distance;
    private String startPoint;
    private String endPoint;

}
