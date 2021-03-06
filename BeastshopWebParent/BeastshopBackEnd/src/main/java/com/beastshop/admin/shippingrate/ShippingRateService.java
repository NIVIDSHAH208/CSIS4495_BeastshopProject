package com.beastshop.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beastshop.admin.paging.PagingAndSortingHelper;
import com.beastshop.admin.product.ProductRepository;
import com.beastshop.admin.setting.country.CountryRepository;
import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.ShippingRate;
import com.beastshop.common.entity.product.Product;

@Service
@Transactional
public class ShippingRateService {
	public static final int RATES_PER_PAGE=10;
	private static final int DIM_DIVISOR=139;
	
	@Autowired private ShippingRateRepository shipRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private ProductRepository productRepo;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
	}
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDb = shipRepo.findByCountryAndState(rateInForm.getCountry().getId(), rateInForm.getState());
		
		boolean foundExistingRateInNewMode = rateInForm.getId()==null&&rateInDb!=null;
		
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId()!=null&&rateInDb!=null;
		
		if(foundExistingRateInNewMode||!foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("There is already a rate for destination "+rateInForm.getCountry().getName()+","
					+rateInForm.getState());
		}
		shipRepo.save(rateInForm);
	}
	
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException{
		try {
			return shipRepo.findById(id).get();
		}catch (NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with id: "+id);
		}
	}
	
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if(count==null||count==0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with id: "+id);
		}
		shipRepo.updateCODSupport(id, codSupported);
		
	}
	
	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if(count==null||count==0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with id: "+id);
		}
		shipRepo.deleteById(id);
		
	}
	
	//method to calculate the shipping cost
	public float calculateShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException {
		ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);
		if(shippingRate==null) {
			throw new ShippingRateNotFoundException("No shipping rate found, please enter the cost manually");
		}
		Product product = productRepo.findById(productId).get();
		float dimWeight = (product.getLength()*product.getWidth()*product.getHeight())/DIM_DIVISOR;
		
		float finalWeight = product.getWeight()>dimWeight?product.getWeight():dimWeight;
		
		
		return finalWeight*shippingRate.getRate();
	}
	
	
}
