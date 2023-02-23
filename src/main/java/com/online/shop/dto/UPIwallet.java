package com.online.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UPIwallet {

    private Integer upiId;
    private String nameUPI;
    private Integer amount;

}
