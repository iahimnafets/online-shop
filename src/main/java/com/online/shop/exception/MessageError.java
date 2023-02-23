package com.online.shop.exception;

public enum MessageError {

    NO_PRODUCT_IN_CARD(1, "There are no products selected in the cart"),
    NO_PAYMENT_SELECT(2, "Please select a payment method before proceeding (select UPI) "),
    NO_ITEMS_IN_CART( 3, "No items in the cart" ),
    PRODUCTS_EXCEEDS_THE_NUMBER_IN_STOCK ( 4, "The number of selected products exceeds the number in stock"),
    USER_NOT_EXIST(5, "User does not exist in our system" ),
    DONT_HAVE_ENOUGH_MONEY ( 6, "We don't have enough money in the selected Method, so we can't proceed"),
    UPI_NOT_EXIST(7, "UPI not exist in your wallet")
    ;

    private final int id;
    private final String message;

    MessageError(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() { return id; }
    public String getMessage() { return message; }

}
