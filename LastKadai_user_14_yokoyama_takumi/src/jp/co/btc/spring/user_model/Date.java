package jp.co.btc.spring.user_model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date {

	public static String getDate() {

		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		//

		return sdf.format(calendar.getTime());
	}



}
