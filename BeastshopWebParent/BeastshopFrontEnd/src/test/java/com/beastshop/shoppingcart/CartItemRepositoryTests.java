package com.beastshop.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.beastshop.common.entity.CartItem;
import com.beastshop.common.entity.Customer;
import com.beastshop.common.entity.product.Product;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {
	@Autowired private CartItemRepository repo;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testSaveItem() {
		Integer custId = 1;
		Integer prodId = 1;
		
		Customer customer = entityManager.find(Customer.class, custId);
		Product product = entityManager.find(Product.class, prodId);
		
		CartItem newItem = new CartItem();
		newItem.setCustomer(customer);
		newItem.setProduct(product);
		newItem.setQuantity(1);
		
		CartItem savedItem = repo.save(newItem);
		
		assertThat(savedItem.getId()).isGreaterThan(0);
	}
	
	
	@Test
	public void testSaveTwoItem() {
		Integer custId = 5;
		Integer prodId = 5;
		
		Customer customer = entityManager.find(Customer.class, custId);
		Product product = entityManager.find(Product.class, prodId);
		
		CartItem item1 = new CartItem();
		item1.setCustomer(customer);
		item1.setProduct(product);
		item1.setQuantity(2);
		
		CartItem item2 = new CartItem();
		item2.setCustomer(new Customer(custId));
		item2.setProduct(new Product(8));
		item2.setQuantity(5);
		
		Iterable<CartItem> iterable = repo.saveAll(List.of(item1,item2));
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomer() {
		Integer custId=5;
		List<CartItem> listItems = repo.findByCustomer(new Customer(custId));
		listItems.forEach(System.out::println);
		
		assertThat(listItems).size().isEqualTo(2);
	}
	
	@Test
	public void testFindByCustomerAndProduct() {
		Integer custId = 5;
		Integer prodId = 5;
		
		CartItem cartItem = repo.findByCustomerAndProduct(new Customer(custId), new Product(prodId));
		
		System.out.println(cartItem);
		assertThat(cartItem).isNotNull();
	}
	
	@Test
	public void testUpdateQuantity() {
		Integer custId = 5;
		Integer prodId = 5;
		Integer quantity = 4;
		
		repo.updateQuantity(quantity, custId, prodId);
		
		CartItem cartItem = repo.findByCustomerAndProduct(new Customer(custId), new Product(prodId));
		assertThat(cartItem.getQuantity()).isEqualTo(4);
		
	}
	
	
	@Test
	public void testDeleteByCustomerAndProduct() {
		Integer custId = 5;
		Integer prodId = 5;
		
		repo.deleteByCustomerAndProduct(custId, prodId);
		
		CartItem cartItem = repo.findByCustomerAndProduct(new Customer(custId), new Product(prodId));
		assertThat(cartItem).isNull();
	}
	
	
}
