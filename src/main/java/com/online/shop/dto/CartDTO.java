package com.online.shop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;

@Data
@Builder
public class CartDTO {

    UserDTO userDto;
    LinkedList<ProductDTO> allProducts;

}
