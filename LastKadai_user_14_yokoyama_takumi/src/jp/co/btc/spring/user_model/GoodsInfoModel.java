package jp.co.btc.spring.user_model;

import jp.co.btc.spring.user_entity.GoodsInfoEntity;

public class GoodsInfoModel {

	private String code;

	private String name;

	private String maker;

	private int price;

	private String memo;

	private String picture;

	private int count;

	public GoodsInfoModel() {

	}

	public GoodsInfoModel(GoodsInfoEntity entity, Integer integer) {
		this.code = entity.getCode();
		this.name = entity.getName();
		this.maker = entity.getMaker();
		this.price = entity.getPrice();
		this.memo = entity.getMemo();
		this.picture = entity.getPicture();
		this.count = integer;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}


	public String getPrice() {

		String str = String.format("%,d", price);
		String price = ("¥" + str);
		return price;
	}

	public int getP() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	//２０文字以上で特殊にする
	public String getMemo20() {
		if(memo.length()>20) {
			memo= memo.substring(0, 20)+"・・・";
		}

		return memo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPicture() {
		picture = "/img/" + picture + ".png";

		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


}
