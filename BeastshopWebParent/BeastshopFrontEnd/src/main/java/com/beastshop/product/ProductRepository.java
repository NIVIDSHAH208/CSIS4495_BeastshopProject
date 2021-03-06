package com.beastshop.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.beastshop.common.entity.product.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
	
	@Query("SELECT p FROM Product p WHERE p.enabled=true AND (p.category.id=?1 OR p.category.allParentIDs LIKE %?2%) ORDER BY p.name ASC")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);
	
	//Spring data jpa will take care {findBy+(entity name)}
	public Product findByAlias(String alias);
	
	//Method to implement fulltext search in the database
	@Query(value="SELECT * FROM products WHERE enabled = true AND MATCH(name, short_description, full_description) against (?1)", nativeQuery = true)
	public Page<Product> search(String keyword, Pageable pageable);
}
