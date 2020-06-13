package jp.co.btc.spring.user_service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.btc.spring.user_dao.CategoryDao;
import jp.co.btc.spring.user_entity.CategoryEntity;
import jp.co.btc.spring.user_model.CategoryModel;

@Service
public class CategoryService {

	CategoryDao categoryDao;

	@Autowired
	public CategoryService(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public List<CategoryModel> selectAllCategoryModel() {
		List<CategoryEntity> categoryEntityList = categoryDao.selectAllCategory();
		List<CategoryModel> categoryList = new ArrayList<>();
		for(CategoryEntity ce: categoryEntityList) {
			CategoryModel cm = new CategoryModel(ce);
			categoryList.add(cm);
		}
		return categoryList;
	}

}
