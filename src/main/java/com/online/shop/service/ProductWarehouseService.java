package com.online.shop.service;


import com.online.shop.dto.TypeProduct;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;

@org.springframework.stereotype.Service
@Slf4j
public class ProductWarehouseService {

    // I have declared a map where I have various products
    // and the quantity of these products
    // and are automatically added once the class is
    // instantiated ( i.e. the first time )
    private static HashMap<TypeProduct,Integer> products = new HashMap<>();

    static {
        products.put(TypeProduct.BELT, 10 );
        products.put(TypeProduct.BRACELET, 20 );
        products.put(TypeProduct.GLASSES, 15 );
        products.put(TypeProduct.GLOVES, 5 );
        products.put(TypeProduct.SLIPPERS, 10 );
        products.put(TypeProduct.TROUSERS, 20 );
        products.put(TypeProduct.SHOES, 15 );
    }


    // We can have acces from diferent areas for this reason I synchronized this method
    // the method always updates the number of products left
    public synchronized boolean removeFromWarehouse(TypeProduct typeProduct, Integer numbers){
        Integer numbersOfProduct =  products.get(typeProduct);
        if(numbers > numbersOfProduct){
            return false;
        }
        numbersOfProduct = numbersOfProduct-numbers;
        products.put(typeProduct,numbersOfProduct);
        return true;
    }



}
