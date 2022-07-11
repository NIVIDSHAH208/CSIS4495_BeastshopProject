package com.beastshop.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beastshop.Utility;
import com.beastshop.common.entity.Customer;
import com.beastshop.common.exception.CustomerNotFoundException;
import com.beastshop.common.exception.ShoppingCartException;
import com.beastshop.customer.CustomerService;

@RestController
public class ShoppingCartRestController {
	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name="productId") Integer productId,
			@PathVariable(name="quantity") Integer quantity,HttpServletRequest request) {
		
		try {
			Customer authenticatedCustomer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, authenticatedCustomer);
			
			return updatedQuantity+" item(s) of this product were added to your shopping cart";
		} catch (CustomerNotFoundException e) {
			return "You must login to add this product to cart";
		} catch (ShoppingCartException ex) {
			return ex.getMessage();
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if(email==null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);
	}
	
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateProductToCart(@PathVariable(name="productId") Integer productId,
			@PathVariable(name="quantity") Integer quantity,HttpServletRequest request) {
		
		try {
			Customer authenticatedCustomer = getAuthenticatedCustomer(request);
			float subtotal = cartService.updateuantity(productId, quantity, authenticatedCustomer);
			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException e) {
			return "You must login to change quantity of product in cart";
		} 
	}
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request){
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer);
			return "Product has been removed from your shopping cart";
			
		} catch (CustomerNotFoundException e) {
			return "You must login to remove product";
		}
	}
	
}
