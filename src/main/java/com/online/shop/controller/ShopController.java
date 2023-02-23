package com.online.shop.controller;


import com.online.shop.dto.ProductDTO;
import com.online.shop.dto.Response;
import com.online.shop.dto.UPIwallet;
import com.online.shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping( "/api" )
@Slf4j
public class ShopController
{

    @Autowired
    private ShopService shopService;


    @Operation( summary =  "Show all products in the cart" )
    @GetMapping( value = "/show-cart" )
    public ResponseEntity<Response> showCart( @RequestParam( name = "userId",  required = true ) final Integer userId )
    {
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .data(Map.of("show-cart", shopService.showCart(userId) ))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @Operation( summary =  "Add a product to the cart, TypeProduct can be:  \n" +
            "    SHOES,\n" +
            "    TROUSERS,\n" +
            "    SLIPPERS,\n" +
            "    GLOVES,\n" +
            "    BELT,\n" +
            "    GLASSES,\n" +
            "    BRACELET    UserId can be: 1, 2, 3" )
    @PutMapping( value = "/add-product" )
    public ResponseEntity<Response> addProduct(@RequestBody final ProductDTO product,
                                               @RequestParam( name = "userId",  required = true ) final Integer userId  )
    {
        shopService.addProductToCart(product,  userId );

        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message(" Product added correctly!")
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @Operation( summary =  "Select a payment-method " )
    @PostMapping( value = "/select-payment-method" )
    public ResponseEntity<Response> selectPaymentMethod(@RequestParam( name = "upiId",  required = true ) final Integer upiId,
                                               @RequestParam( name = "userId",  required = true ) final Integer userId
    )
    {
        shopService.selectPaymentMethod(upiId,  userId );

        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message(" Payments method selected correctly"  )
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @Operation( summary =  "See User all Payment Methods" )
    @PostMapping( value = "/all-payments-method" )
    public ResponseEntity<Response> getAllPaymentsMethod(@RequestParam( name = "userId",  required = true ) final Integer userId
    )
    {

        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .data(  shopService.getAllPaymentsMethod( userId ) )
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @Operation( summary =  "Select a payment-method " )
    @PostMapping( value = "/check-and-pay" )
    public ResponseEntity<Response> checkAndPay(@RequestParam( name = "userId",  required = true ) final Integer userId
    )
    {
        shopService.checkAndPay( userId );

        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message(" Cart verification and successful payment "  )
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

}
