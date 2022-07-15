package com.beastshop.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.beastshop.common.entity.Brand;
import com.beastshop.common.entity.Category;
import com.beastshop.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
	@Autowired
	private ProductRepository repo;
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateProduct() {
		//Passing the ID=10 for Samsung brand (checked in the database)
		Brand brand = entityManager.find(Brand.class, 37);
		
		//Passing the ID=15 for unlocked cellphones (checked in the database)
		Category category = entityManager.find(Category.class, 5);
		
		//Creating new product object
		Product product = new Product();
		product.setName("Acer Aspire Desktop");
		product.setAlias("acer_aspire");
		product.setShortDescription("Desktop made to inspire everyone");
		product.setFullDescription("no one wants to waste time writing full description");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(1221);
		product.setCost(678);
		product.setEnabled(true);
		product.setInStock(true);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product savedProduct = repo.save(product);
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	
	//List all products from the database
	@Test
	public void testListAllProducts() {
		Iterable<Product> findAllProducts = repo.findAll();
		//Sweet lambda function
		findAllProducts.forEach(System.out::println);
	}
	
	
	@Test
	public void testGetProduct() {
		Integer id=1;
		Product productById = repo.findById(id).get();
		System.out.println(productById);
		assertThat(productById).isNotNull();
	}
	
	
	@Test
	public void testUpdateProduct() {
		Integer id=1;
		Product productById = repo.findById(id).get();
		productById.setPrice(1212);
		repo.save(productById);
		Product updatedProduct = entityManager.find(Product.class, id);
		assertThat(updatedProduct.getPrice()).isEqualTo(1212);
	}
	
	@Test
	public void testDeleteProduct() {
		Integer id=3;
		repo.deleteById(id);
		 Optional<Product> productById = repo.findById(id);
		assertThat(!productById.isPresent());
	}
	
	@Test
	public void testSaveProductWithDetails() {
		Integer productId=1;
		Product product = repo.findById(productId).get();
		

		product.addDetail("Device memory", "128 GB");
		product.addDetail("OS", "Windows");
		product.addDetail("Processor", "Intel i7");
		
		Product savedProduct = repo.save(product);
		assertThat(savedProduct.getDetails()).isNotEmpty();
	}
	
	@Test
	public void testSaveProductWithImages() {
		Integer productId=1;
		Product product = repo.findById(productId).get();
		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image1.png");
		product.addExtraImage("extra_image2.png");
		product.addExtraImage("extra-image3.png");
		Product savedProduct = repo.save(product);
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}
	
}
