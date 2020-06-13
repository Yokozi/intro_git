package jp.co.btc.spring.user_model;

import java.util.LinkedHashMap;
import java.util.Map;

public class GenderList {

	private static Map<String, String> genderList = new LinkedHashMap<String, String>() {
		{
			put("M", "男性");
			put("F", "女性");
		}
	};

	public static Map<String, String> getGenderList() {
		return genderList;
	}

	public static void setGenderList(Map<String, String> genderList) {
		GenderList.genderList = genderList;
	}
}
