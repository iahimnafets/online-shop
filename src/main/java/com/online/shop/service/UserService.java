package com.online.shop.service;

import com.online.shop.dto.UPIwallet;
import com.online.shop.dto.UserDTO;
import com.online.shop.exception.ApiRequestException;
import com.online.shop.exception.MessageError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    private static HashMap<Integer, UserDTO> allUsers = new HashMap<>();


    public UserDTO getUserById(Integer userId ){
        if(!allUsers.containsKey(userId)){
            throw new ApiRequestException(MessageError.USER_NOT_EXIST.getMessage() + " userId:"+ userId  );
        }
        return allUsers.get(userId);
    }

    public void loadAllUsers() {

        HashMap<Integer, UPIwallet> walletUPI_user1 = new HashMap<>();
        walletUPI_user1.put( 1, new UPIwallet( 1,"UPI_A2", 200) );
        walletUPI_user1.put( 2, new UPIwallet( 2, "UPI_BR", 100) );
        walletUPI_user1.put( 3, new UPIwallet( 2, "UPI_CR", 10000) );

        HashMap<Integer, UPIwallet> walletUPI_user2 = new HashMap<>();
        walletUPI_user2.put(4, new UPIwallet( 4,"UPI_VB", 2000) );

        HashMap<Integer, UPIwallet> walletUPI_user3 = new HashMap<>();
        walletUPI_user3.put( 5, new UPIwallet( 5, "UPI_VC", 5000) );


        allUsers.put(1, new UserDTO(1, "User-Mihai", "address_User-Mihai" , null, walletUPI_user1)  );
        allUsers.put(2, new UserDTO(2, "User-Jack", "address_User-Jack"  ,null, walletUPI_user2 )   );
        allUsers.put(3, new UserDTO(3, "User-Marco", "address_User-Marco" , null, walletUPI_user3)   );

        log.info("Users Ready to be used");
        for (Map.Entry<Integer, UserDTO> entry : allUsers.entrySet() ) {
            log.info( "UserName : " + entry.getValue().getUserName()  + " userId: " + entry.getValue().getId() );
        }
    }


}
