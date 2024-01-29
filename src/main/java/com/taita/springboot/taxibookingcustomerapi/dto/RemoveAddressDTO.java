package com.taita.springboot.taxibookingcustomerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveAddressDTO {
    private String verification;
    private int addressType;
}
