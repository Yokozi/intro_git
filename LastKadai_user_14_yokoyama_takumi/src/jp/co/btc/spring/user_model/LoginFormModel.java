package jp.co.btc.spring.user_model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginFormModel {

	@NotEmpty(message = "会員Noは必ず入力してください。")
	@Pattern(regexp = "^[0-9]*$", message = "会員Noは半角数字で入力してください。")
	private String userNo;

	@NotEmpty(message = "パスワードは必ず入力してください。")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "パスワードは半角英数字で入力してください。")
	@Size(min=1,max=8,message = "パスワードは8桁以下で入力してください。")
	private String userPass;

	//hiddenから受け取る遷移先を変えるフラッグ
	//KGO102→KGO102
	//MEN101→MEM201
	//ELSE→MEN101
	private String tranFlg;

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getTranFlg() {
		return tranFlg;
	}

	public void setTranFlg(String tranFlg) {
		this.tranFlg = tranFlg;
	}


}
