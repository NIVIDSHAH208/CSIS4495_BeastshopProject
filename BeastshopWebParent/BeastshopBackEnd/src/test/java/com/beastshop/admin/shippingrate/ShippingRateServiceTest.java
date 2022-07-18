package com.beastshop.admin.shippingrate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.beastshop.admin.product.ProductRepository;
import com.beastshop.common.entity.ShippingRate;
import com.beastshop.common.entity.product.Product;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServiceTest {
	@MockBean private ShippingRateRepository shipRepo;
	@MockBean private ProductRepository productRepo;
	@InjectMocks private ShippingRateService shipService;
	
	
	//Testing if the method throws an exception
	@Test
	public void testCalculateShippingCostNoRateFound() {
		Integer productId=1;
		Integer countryId=234;
		String state="ABCDEF";
		
		Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(null);
		
		assertThrows(ShippingRateNotFoundException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
				// TODO Auto-generated method stub
				shipService.calculateShippingCost(productId, countryId, state);
			}
		});
	}
	
	
	//now testing if shipping rate is found 
	@Test
	public void testCalculateShippingCostRateFound() throws ShippingRateNotFoundException {
		Integer productId=1;
		Integer countryId=234;
		String state="New York";
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(10);
		
		Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(shippingRate);
		Product product = new Product();
		product.setWeight(5);
		product.setWidth(4);
		product.setHeight(3);
		product.setLength(8);
		Mockito.when(productRepo.findById(productId)).thenReturn(Optional.of(product));
		float shippingCost = shipService.calculateShippingCost(productId, countryId, state);
		
		assertEquals(50, shippingCost);
	}
	
}
