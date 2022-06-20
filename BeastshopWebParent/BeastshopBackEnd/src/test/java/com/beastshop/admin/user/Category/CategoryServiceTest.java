package com.beastshop.admin.user.Category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.beastshop.admin.category.CategoryRepository;
import com.beastshop.admin.category.CategoryService;
import com.beastshop.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

	//We need to use few extensions for mockito and spring
	//This test class uses Mockito for testing
	//We only want to check the service layer and not the repository layer
	//Mockito inserts the fake object for testing purposes
	//To inject the new fake object we use the annotation mockbean
	
	@MockBean
	private CategoryRepository repo;
	
	//It will create the fake object of the category and inject in the runtime
	@InjectMocks
	private CategoryService service;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateName() {
		Integer id=null;
		String name="Computers";
		String alias="abc";
		
		Category category = new Category(id, name, alias);
		//Now comes the mockito part
		//This basically means, when the method is called, we return the fake object
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateAlias() {
		Integer id=null;
		String name="NameABC";
		String alias="Computers";
		
		Category category = new Category(id, name, alias);
		//Now comes the mockito part
		//This basically means, when the method is called, we return the fake object
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);

		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnReturnOk() {
		Integer id=null;
		String name="NameABC";
		String alias="Computers";
		
		//Now comes the mockito part
		//This basically means, when the method is called, we return the fake object
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateName() {
		Integer id=1;
		String name="Computers";
		String alias="abc";
		
		Category category = new Category(2, name, alias);
		//Now comes the mockito part
		//This basically means, when the method is called, we return the fake object
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateAlias() {
		Integer id=1;
		String name="NameABC";
		String alias="computers";
		
		Category category = new Category(2, name, alias);
		//Now comes the mockito part
		//This basically means, when the method is called, we return the fake object
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);

		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnReturnOk() {
		Integer id=1;
		String name="NameABC";
		
		String alias="Computers";
		Category category = new Category(id, name, alias);

		//Now comes the mockito part
		//This basically means, when the method is called, we return the fake object
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("OK");
	}
	
}
