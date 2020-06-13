package jp.co.btc.spring.user_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.btc.spring.user_model.Date;
import jp.co.btc.spring.user_model.LoginFormModel;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Controller
public class UserMenuController {

	private LoginSessionModel loginSession;

	@Autowired
	public UserMenuController(LoginSessionModel loginSession) {
		this.loginSession = loginSession;
	}

	@RequestMapping({ "/MEN101.html" })
	public String showMEN101(Model model) {

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());

		//ログインとログアウトの切り替え用
		model.addAttribute("loginCheck", loginSession.isLogin());
		return "MEN101";
	}

	@RequestMapping(value = "LOG101.html", params = "logout", method = RequestMethod.POST)
	public String Logout(Model model) {

		loginSession.doLogout();

		LoginFormModel loginFormModel = new LoginFormModel();
		loginFormModel.setTranFlg("ELSE");

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());

		model.addAttribute("loginFormModel", loginFormModel);

		return "LOG101";
	}


	//共通エラー用
	@RequestMapping({ "/ERR101.html" })
	public String showERR101() {
		return "ERR101";
	}
}
