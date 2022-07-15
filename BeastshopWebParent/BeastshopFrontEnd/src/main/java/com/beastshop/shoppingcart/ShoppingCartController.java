package com.beastshop.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.beastshop.Utility;
import com.beastshop.address.AddressService;
import com.beastshop.common.entity.Address;
import com.beastshop.common.entity.CartItem;
import com.beastshop.common.entity.Customer;
import com.beastshop.common.entity.ShippingRate;
import com.beastshop.customer.CustomerService;
import com.beastshop.shipping.ShippingRateService;

@Controller
public class ShoppingCartController {
	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	@Autowired private AddressService addressService;
	@Autowired private ShippingRateService shipService;
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItems = cartService.listCartItems(customer);
		float estimatedTotal = 0.0F;
		for(CartItem item: cartItems) {
			estimatedTotal+=item.getSubtotal();
		}
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		boolean usePrimaryAddressAsDefault=false;
		
		if(defaultAddress!=null) {
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		}else {
			usePrimaryAddressAsDefault=true;
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}
		
		model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
		model.addAttribute("shippingSupported", shippingRate!=null);
		model.addAttribute("estimatedTotal",estimatedTotal);
		model.addAttribute("cartItems",cartItems);
		return "cart/shopping_cart";
	}
	
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		return customerService.getCustomerByEmail(email);
	}
}
