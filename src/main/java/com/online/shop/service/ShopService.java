package com.online.shop.service;

import com.online.shop.request.UserResponse;
import com.online.shop.dto.CartDTO;
import com.online.shop.dto.ProductDTO;
import com.online.shop.dto.UPIwallet;
import com.online.shop.dto.UserDTO;
import com.online.shop.exception.ApiRequestException;
import com.online.shop.exception.MessageError;
import com.online.shop.external.OrderFulfillmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


@org.springframework.stereotype.Service
@Slf4j
public class ShopService {

    @Autowired
    private ProductWarehouseService warehouseService;

    @Autowired
    private UserService  userService;

    @Autowired
    private OrderFulfillmentService orderFulfillmentService;

    private HashMap<Integer, CartDTO> userCarts =  new HashMap<>();

    /**
     * Add the product for the user who is shopping
     */
    public void addProductToCart(ProductDTO product, Integer userId) {
        log.info("addProductToCart-RUN  product: {} ", product);

        UserDTO user = userService.getUserById(userId);
        if(user == null){
            throw new ApiRequestException( MessageError.USER_NOT_EXIST.getMessage() );
        }
        if (userCarts.containsKey(userId)) {
            userCarts.get(userId).getAllProducts().add(product);
        } else {
            LinkedList<ProductDTO>  products =  new LinkedList<>();
            products.add( product );

            CartDTO cartDTO = CartDTO.builder()
                    .userDto( user )
                    .allProducts( products )
                    .build();
            userCarts.put(userId, cartDTO);
        }
    }


    public List<ProductDTO> showCart(Integer userId) {
        log.info("showCart-RUN  userId: {}", userId);
        if(! userCarts.containsKey( userId ) ){
            throw new ApiRequestException( MessageError.NO_ITEMS_IN_CART.getMessage() );
        }
        return userCarts.get(userId).getAllProducts();
    }

    /**
     *
     * @param upiId
     * @param userId
     */
    public void selectPaymentMethod(Integer upiId, Integer userId) {
        log.info("selectPaymentMethod-RUN  upiId: {}  userId: {}", upiId, userId);
        if(userCarts.containsKey(userId)){
            if(! userCarts.get(userId).getUserDto().getWalletUPI().containsKey(upiId)){
                throw new ApiRequestException(MessageError.UPI_NOT_EXIST.getMessage() );
            }
            userCarts.get(userId).getUserDto().setUPIselectedForPayment( userCarts.get(userId).getUserDto().getWalletUPI().get(upiId) );
        }else{
            UserDTO user = userService.getUserById(userId);
            if(! user.getWalletUPI().containsKey(upiId)){
                throw new ApiRequestException(MessageError.UPI_NOT_EXIST.getMessage() );
            }
            user.setUPIselectedForPayment(  user.getWalletUPI().get(upiId) );
            CartDTO cart = CartDTO.builder()
                    .userDto( user )
                    .allProducts( new LinkedList<>() )
                    .build();

            userCarts.put( userId ,cart );
        }
    }

    /**
     *
     * @param userId
     */
    public void checkAndPay(Integer userId) {

        log.info("checkAndPay-RUN  userId: {}  ", userId );

        // we have products for this user in the cart
        if(! userCarts.containsKey(userId) ){
            throw new ApiRequestException( MessageError.NO_PRODUCT_IN_CARD.getMessage() );
        }
        CartDTO cart = userCarts.get(userId);
        if(cart.getAllProducts().size() == 0){
            throw new ApiRequestException(MessageError.NO_PRODUCT_IN_CARD.getMessage() );
        }
        // we have a selected payment
        if( cart.getUserDto().getUPIselectedForPayment() == null ){
            throw new ApiRequestException( MessageError.NO_PAYMENT_SELECT.getMessage()  );
        }
        // all the selected products are still available in the warehouse (or the warehouse without any products)
        // because they are already out of stock while we are shopping...

        // Here if we have a REAL situation we use transaction and do ROLBACK in case we have an exception
        // to restore the situation of the products
        // and we need to synchronize this block so as not to buy something we do NOT have in stock

        Integer totalAmmount = 0;
        synchronized(this) {
            for (ProductDTO product : cart.getAllProducts()) {

                totalAmmount+=  applyDiscountIfExist( product );
                if (!warehouseService.removeFromWarehouse(product.getTypeProduct(), product.getQuantity())) {
                    // here I should restore all the products that
                    // I have scaled from the warehouse, I DO NOT do it because it is only a TEST
                    // and now I can throw an exception
                    throw new ApiRequestException(MessageError.PRODUCTS_EXCEEDS_THE_NUMBER_IN_STOCK.getMessage() + ": " + product.getTypeProduct());
                }
            }
            // If I'm here means NO issue regarding the products, now I need to verify:
            // We have enough money for payment
           if( totalAmmount > cart.getUserDto().getUPIselectedForPayment().getAmount()){
               throw new ApiRequestException(MessageError.DONT_HAVE_ENOUGH_MONEY.getMessage() + " amount due :"
                       + totalAmmount + " Your UPI has this balance: "+ cart.getUserDto().getUPIselectedForPayment().getAmount() );
           }
           // if we got here we can deduct the money
            Integer balance =  cart.getUserDto().getUPIselectedForPayment().getAmount() - totalAmmount;
            cart.getUserDto().getUPIselectedForPayment().setAmount(balance);

            // here we need to save this situation in real LIFE,
            // if something wrong happens in the "prepareOrder" method we know that the order
            // was successful but the "prepareOrder" service went wrong or other cases
            log.info("checkAndPay-OK  -> Payment of the order made");

            // clean the CART
            userCarts.get(userId).getAllProducts().clear();
        }
        // and send the order to the warehouse
        // orderFulfillmentService.prepareOrder( cart.getUserDto(), cart.getAllProducts() );
        log.info("checkAndPay-OK  -> order shipped to warehouse");


        // mock test for an external call to another service
        // just to show that the call works
        List<UserResponse> users =  orderFulfillmentService.getUsers();
        log.info(" Also called the external method -> result {} ", users );
        // if we don't have any error:
        log.info("checkAndPay-OK  -> Order received from warehouse");

    }

    private Integer applyDiscountIfExist(ProductDTO product) {
        log.info(" applyDiscount-RUN ->  TypeProduct: {}  price: {}", product.getTypeProduct(), product.getPrice() );

        // Here we can have a particular logic and we can check
        // if that particular product has:
        // discount = Yes/No
        // how much in percentage %  ( single product )
        // for what period dateBegin / dateEnd
        // if you buy more than 3 ( for example ) you can have other 5% of discount

        // for now we Don't have any logic for make discounts

        // We calculate only the numbers of product
        return product.getPrice() * product.getQuantity();
    }

    public HashMap< Integer, UPIwallet> getAllPaymentsMethod(Integer userId) {
        log.info("getAllPaymentsMethod-RUN  userId: {}", userId);
        UserDTO user = userService.getUserById(1);
        return user.getWalletUPI();
    }


}
