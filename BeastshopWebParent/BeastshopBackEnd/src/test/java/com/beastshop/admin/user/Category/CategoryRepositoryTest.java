package com.beastshop.admin.user.Category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.beastshop.admin.category.CategoryRepository;
import com.beastshop.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	@Autowired
	private CategoryRepository repo;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory =repo.save(category);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	
	@Test
	public void testCreateSubCategory() {
		//Get the parent category
		Category parentCategory  = new Category(7);
		//Create and save new category
		Category subCategory = new Category("iPhone", parentCategory);
//		Category smartPhone = new Category("Smart Phone", parentCategory);
//		repo.saveAll(List.of(cameras, smartPhone));
		Category savedCat=repo.save(subCategory);
		assertThat(savedCat.getId()).isGreaterThan(0);		
	}
	
	@Test
	public void testGetCategory() {
		Category category = repo.findById(2).get();
		System.out.println(category.getName());
		Set<Category> childreCategories=category.getChildren();
		for(Category subCat:childreCategories) {
			System.out.println(subCat.getName());
		}
		assertThat(childreCategories.size()).isGreaterThan(0);
	}
	
	//Check the code in category repository
	@Test
	public void testListRootCategories() {
		List<Category> rootCategories = repo.findRootCategories();
		rootCategories.forEach(cat->{
			System.out.println(cat.getName());
		});
	}
	
	
	@Test
	public void testPrintHierarchicalCategories() {
		 Iterable<Category> categories = repo.findAll();
		 for(Category category: categories) {
			 if(category.getParent()==null) {
				 System.out.println(category.getName());
				 Set<Category> children = category.getChildren();
				 for(Category subCat: children) {
					 System.out.println("--"+subCat.getName());
					 printChildren(subCat, 1);
				 }
			 }
		 }
	}
	
	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel+1;
		Set<Category> children = parent.getChildren();
		for(Category subCat : children) {
			for(int i=0;i<newSubLevel; i++ ) {
				System.out.print("--");
			}
			System.out.println(subCat.getName());
			printChildren(subCat, newSubLevel);
		}
	}
	
	@Test
	public void testFindByName() {
		String name="Computers";
		Category category = repo.findByName(name);
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}
	
	@Test
	public void testFindByAlias() {
		String aliasName="Electronics";
		Category category = repo.findByAlias(aliasName);
		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(aliasName);
	}
	
}
