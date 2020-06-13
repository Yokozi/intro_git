package jp.co.btc.spring.user_component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.btc.spring.user_model.CategoryModel;
import jp.co.btc.spring.user_service.CategoryService;

@Component
public class CategoryList {


	private CategoryService categoryService;

	@Autowired
	public CategoryList (CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	private List<CategoryModel> categoryList = new ArrayList<>();

	@PostConstruct
	private void init() {
		categoryList = categoryService.selectAllCategoryModel();
	}

	public List<CategoryModel> getCategoryList() {
		return categoryList;
	}
}
