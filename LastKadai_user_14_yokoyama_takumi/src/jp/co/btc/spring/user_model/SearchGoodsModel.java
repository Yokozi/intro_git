package jp.co.btc.spring.user_model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;

public class SearchGoodsModel {

	private int category;

	private String name;

	private String maker;

	@Pattern(regexp = "^[0-9]*$", message = "金額上限は半角数字で入力してください。")
	private String maxP;


	@Pattern(regexp = "^[0-9]*$", message = "金額下限は半角数字で入力してください。")
	private String minP;

	@AssertTrue(message = "金額上限は金額下限より大きい値を入力してください。")
	public boolean isPriceCheck() {

		int min = 0;
		int max = 1;

		try {

			if (minP != null && !minP.isEmpty()) {
				min = Integer.parseInt(minP);
				max = min + 1;

				if (maxP != null && !maxP.isEmpty()) {
					max = Integer.parseInt(maxP);
					return min <= max;
				}

			}

		} catch (NumberFormatException e) {

		}

		return true;

	}

	@AssertTrue(message = "金額下限には正の数で入力してください。")
	public boolean isMinPOver0() {

		int min = 0;

		try {

			if (minP != null && !minP.isEmpty()) {
				min = Integer.parseInt(minP);
			}

			return min >= 0;

		} catch (NumberFormatException e) {

		}

		return true;

	}

	@AssertTrue(message = "金額上限には正の数で入力してください。")
	public boolean isMaxPOver0() {

		int max = 0;

		try {

			if (maxP != null && !maxP.isEmpty()) {
				max = Integer.parseInt(maxP);
			}

			return max >= 0;

		} catch (NumberFormatException e) {

		}

		return true;

	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
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

	public String getMaxP() {
		return maxP;
	}

	public void setMaxP(String maxP) {
		this.maxP = maxP;
	}

	public String getMinP() {
		return minP;
	}

	public void setMinP(String minP) {
		this.minP = minP;
	}


}
