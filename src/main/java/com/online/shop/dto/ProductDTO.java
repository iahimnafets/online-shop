package com.online.shop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO
{
    private Integer id;
    private TypeProduct typeProduct; // we have the type of product
    private Integer price;
    private Integer quantity;

}
