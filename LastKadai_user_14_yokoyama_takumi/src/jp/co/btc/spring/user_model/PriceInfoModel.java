package jp.co.btc.spring.user_model;

public class PriceInfoModel {

	private int sumPrice;

	private int tax;

	private int totalPrice;

	public String getSumPrice() {
		String str = String.format("%,d", sumPrice);
		String price = ("¥" + str);
		return price;

	}

	public int getSumP() {

		return sumPrice;

	}

	public void setSumPrice(int sumPrice) {
		this.sumPrice = sumPrice;
	}

	public String getTax() {
		String str = String.format("%,d", tax);
		String price = ("¥" + str);
		return price;
	}

	public int getTaxP() {

		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public String getTotalPrice() {
		String str = String.format("%,d", totalPrice);
		String price = ("¥" + str);
		return price;
	}

	public int getTotalP() {

		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
