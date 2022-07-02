package com.beastshop.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.Product;
import com.beastshop.common.exception.ProductNotFoundException;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE=10;
	public static final int PRODUCTS_SEARCH_RESULTS_PAGE=10;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	//Method to list all products by category
	public Page<Product> listByCategory(int pageNum, Integer categoryId){
		String categoryIDMatch = "-"+String.valueOf(categoryId)+"-";
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE);
		
		return productRepository.listByCategory(categoryId, categoryIDMatch, pageable);
	}
	
	
	//Method to get product by alias
	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product = productRepository.findByAlias(alias);
		if(product==null) {
			throw new ProductNotFoundException("Could not find any product with alias "+alias);
		}
		return product;
	}
	
	
	//Method to search products based on full text search for better results
	public Page<Product> search(String keyword, int pageNum){
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_SEARCH_RESULTS_PAGE);
		return productRepository.search(keyword, pageable);
	}
	
}
