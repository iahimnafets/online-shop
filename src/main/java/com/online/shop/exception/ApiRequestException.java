package com.online.shop.exception;



public class ApiRequestException extends RuntimeException
{

    public ApiRequestException(String message ){
     super(message);
   }

}
