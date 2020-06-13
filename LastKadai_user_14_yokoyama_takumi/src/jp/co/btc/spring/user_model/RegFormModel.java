package jp.co.btc.spring.user_model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

public class RegFormModel {

	@NotEmpty(message = "氏名は必ず入力してください。")
	@Size(min=1,max=20,message = "氏名は20桁以下で入力してください。")
	private String name;

	@NotEmpty(message = "パスワードは必ず入力してください。")
	@Pattern(regexp = "[a-zA-Z0-9]*", message = "パスワードは半角英数字で入力してください。")
	@Size(min=1,max=8,message = "パスワードは8桁以下で入力してください。")
	private String pass;

	@NotEmpty(message = "パスワード(確認用)は必ず入力してください。")
	@Pattern(regexp = "[a-zA-Z0-9]*", message = "パスワード(確認用)は半角英数字で入力してください。")
	@Size(min=1,max=8,message = "パスワード(確認用)は8桁以下で入力してください。")
	private String CheckPass;

	@NotEmpty(message = "年齢は必ず入力してください。")
	@Pattern(regexp = "[0-9]*", message = "年齢は半角数字で入力してください。")
	@Range(min = 0,message = "年齢には正の数で入力してください。")
	private String age;

	@NotEmpty(message = "性別は必ず入力してください。")
	private String gender;

	@NotEmpty(message = "郵便番号は必ず入力してください。")
	@Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "郵便番号はXXX-XXXXの形式で入力してください。")
	private String postalCode;

	@NotEmpty(message = "住所は必ず入力してください。")
	@Size(min=1,max=50,message = "住所は50桁以下で入力してください。")
	private String addr;

	@NotEmpty(message = "電話番号は必ず入力してください。")
	@Pattern(regexp = "^[0-9\\-]+$", message = "電話番号は半角数字とハイフンで入力してください。")
	@Size(min=1,max=20,message = "電話番号は20桁以下で入力してください。")
	private String tel;

	@AssertTrue(message = "パスワードとパスワード(確認用)は同じ値を入力してください。")
	public boolean isPasswordConfirmed() {
		try {
			return this.pass.equals(this.CheckPass);
		} catch (Exception e) {
			return false;
		}
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

	public String getCheckPass() {
		return CheckPass;
	}

	public void setCheckPass(String checkPass) {
		CheckPass = checkPass;
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


}

