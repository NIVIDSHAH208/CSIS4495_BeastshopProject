package com.beastshop.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.CartItem;
import com.beastshop.common.entity.Customer;
import com.beastshop.common.entity.Product;
import com.beastshop.common.exception.ShoppingCartException;

@Service
public class ShoppingCartService {
	@Autowired private CartItemRepository cartRepo;
	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);
		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
		if(cartItem!=null) {
			updatedQuantity = cartItem.getQuantity()+quantity;
			if(updatedQuantity>5) {
				throw new ShoppingCartException("Could not add "+quantity+" item(s). Maximum allowed quantity is 5.");
			}
			
		}else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		cartItem.setQuantity(updatedQuantity);
		cartRepo.save(cartItem);
		
		return updatedQuantity;
	}
}
