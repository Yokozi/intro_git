package jp.co.btc.spring.user_model;

public class AddCartFormModel {

	private String[] checkGoods;

	private String[] count;

	private String[] code;

	public void resetModel() {
		this.checkGoods = null;
		this.count = null;
		this.code = null;
	}

	public String[] getCheckGoods() {
		return checkGoods;
	}

	public void setCheckGoods(String[] checkGoods) {
		this.checkGoods = checkGoods;
	}

	public String[] getCount() {
		return count;
	}

	public void setCount(String[] count) {
		this.count = count;
	}

	public String[] getCode() {
		return code;
	}

	public void setCode(String[] code) {
		this.code = code;
	}


}
