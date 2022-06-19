package com.beastshop.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.Category;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository repo;

	public List<Category> listAll() {
		return (List<Category>) repo.findAll();
	}
	
	public Category save(Category category) {
		return repo.save(category);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDb = repo.findAll();

		for (Category category : categoriesInDb) {
			if (category.getParent() == null) {

				categoriesUsedInForm.add(Category.copyIdAndName(category));
				Set<Category> children = category.getChildren();
				for (Category subCat : children) {
					String catName ="--"+subCat.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCat.getId(), catName));
					listChildren(categoriesUsedInForm,subCat, 1);
				}
			}
		}

		return categoriesUsedInForm;
	}
	
	private void listChildren(List<Category> categoriesUsedInForm,Category parent, int subLevel) {
		int newSubLevel = subLevel+1;
		Set<Category> children = parent.getChildren();
		for(Category subCat : children) {
			String name = "";
			for(int i=0;i<newSubLevel; i++ ) {
				name+="--";
			}
			name+=subCat.getName();
			categoriesUsedInForm.add(Category.copyIdAndName(subCat.getId(), name));
			listChildren(categoriesUsedInForm, subCat, newSubLevel);
		}
	}
}
