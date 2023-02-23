package com.online.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer id;
    private String userName;
    private String address;
    private UPIwallet UPIselectedForPayment;
    private HashMap< Integer, UPIwallet> walletUPI = new HashMap<>();

}
