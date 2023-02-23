package com.online.shop.external;


import com.online.shop.request.UserResponse;
import com.online.shop.dto.ProductDTO;
import com.online.shop.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.LinkedList;
import java.util.List;

@FeignClient(url="https://jsonplaceholder.typicode.com",name="USER-CLIENT")
public interface OrderFulfillmentService {

	// here we have JUST on example to show calls to another service
	@GetMapping("/users")
	public List<UserResponse> getUsers();

	// By this method ( in reality not exist ) we can prepare the order
	// @PostMapping("/prepare-order")
	// public void prepareOrder(UserDTO userDto, LinkedList<ProductDTO> allProducts);

}
