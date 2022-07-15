package com.beastshop.shoppingcart;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.CartItem;
import com.beastshop.common.entity.Customer;
import com.beastshop.common.entity.product.Product;
import com.beastshop.common.exception.ShoppingCartException;
import com.beastshop.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {
	@Autowired private CartItemRepository cartRepo;
	@Autowired private ProductRepository productRepo;
	
	
	//add product to the database
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		int maxAllowedQuantity = 5;
		Product product = new Product(productId);
		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
		if(cartItem!=null) {
			updatedQuantity = cartItem.getQuantity()+quantity;
			if(updatedQuantity>maxAllowedQuantity) {
				throw new ShoppingCartException("Could not add "+quantity+" item(s). Maximum allowed quantity is "+maxAllowedQuantity);
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
	
	
	//list cart item for the customer
	public List<CartItem> listCartItems(Customer customer){
		return cartRepo.findByCustomer(customer);
	}

	//method to update the product quantity from the cart module
	public float updateuantity(Integer productId, Integer quantity, Customer customer) {
		cartRepo.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepo.findById(productId).get();
		
		float subtotal = product.getDiscountPrice()*quantity;
		return subtotal;
	}
	
	public void removeProduct(Integer productId, Customer customer) {
		cartRepo.deleteByCustomerAndProduct(customer.getId(), productId);
	}
	
	public void deleteByCustomer(Customer customer) {
		cartRepo.deleteByCustomer(customer.getId());
	}
	
}
