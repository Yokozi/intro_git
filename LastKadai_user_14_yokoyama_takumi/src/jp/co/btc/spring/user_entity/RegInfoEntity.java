package jp.co.btc.spring.user_entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegInfoEntity {

	private int num;
	private String name;
	private String pass;
	private String age;
	private String gender;
	private String postalCode;
	private String addr;
	private String tel;
	private String regDate;

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getRegDate() {

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = null;
		try {
			Date date = sdFormat.parse(regDate);
			str = new SimpleDateFormat("yyyy/MM/dd").format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;

	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

}
