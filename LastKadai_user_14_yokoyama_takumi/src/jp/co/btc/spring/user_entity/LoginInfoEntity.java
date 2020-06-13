package jp.co.btc.spring.user_entity;

public class LoginInfoEntity {

	private int userNo;
	private String userPass;
	private String name;

	public LoginInfoEntity(int userNo, String userPass,String name) {
		this.userNo = userNo;
		this.userPass = userPass;
		this.name=name;
	}

	public LoginInfoEntity() {

	}
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
