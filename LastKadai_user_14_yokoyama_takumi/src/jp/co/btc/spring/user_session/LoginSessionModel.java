package jp.co.btc.spring.user_session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import jp.co.btc.spring.user_entity.LoginInfoEntity;

@SessionScope
@Component
public class LoginSessionModel {

	private LoginInfoEntity loginInfo;

	public boolean isLogin() {
		if (loginInfo != null) {
			return true;
		}
		return false;
	}

	public String isGestOrUserName() {
		if (loginInfo == null) {
			return "ゲスト";
		}
		return loginInfo.getName();
	}

	public LoginInfoEntity getLoginInfo() {
		return new LoginInfoEntity(loginInfo.getUserNo(), loginInfo.getUserPass(), loginInfo.getName());
	}

	public void setNewPass(String newPass) {
		this.loginInfo.setUserPass(newPass);
	}

	public void doLogin(LoginInfoEntity loginInfo) {
		this.loginInfo = loginInfo;
	}

	public void doLogout() {
		this.loginInfo = null;
	}

	public int getUserNo() {
		return loginInfo.getUserNo();
	}

	public void setNewName(String name) {
		this.loginInfo.setName(name);

	}
}
