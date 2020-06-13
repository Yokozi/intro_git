package jp.co.btc.spring.user_model;

import jp.co.btc.spring.user_entity.CategoryEntity;

public class CategoryModel {

	private int id;
	private String name;

	public CategoryModel() {
		super();
	}

	public CategoryModel(CategoryEntity ce) {
		this.id = ce.getId();
		this.name = ce.getName();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
