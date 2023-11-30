package com.taita.springboot.taxibookingcustomerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMetaDTO {
    private int customerId;
    private String username;
    private String mobile;
}
