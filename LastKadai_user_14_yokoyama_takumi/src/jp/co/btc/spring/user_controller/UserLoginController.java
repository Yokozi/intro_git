package jp.co.btc.spring.user_controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.co.btc.spring.user_entity.LoginInfoEntity;
import jp.co.btc.spring.user_entity.RegInfoEntity;
import jp.co.btc.spring.user_model.Date;
import jp.co.btc.spring.user_model.LoginFormModel;
import jp.co.btc.spring.user_service.LoginUserService;
import jp.co.btc.spring.user_service.UserInfoService;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Controller
public class UserLoginController {

	private LoginSessionModel loginSession;
	private LoginUserService loginUserService;
	private UserInfoService userInfoService;

	@Autowired
	public UserLoginController(LoginSessionModel loginSession,LoginUserService loginUserService,UserInfoService userInfoService) {
		this.loginSession = loginSession;
		this.loginUserService = loginUserService;
		this.userInfoService = userInfoService;
	}

	@RequestMapping(path={"/LOG101.html"},method = RequestMethod.GET)
	public String showLoginForm(Model model) {
		model.addAttribute("loginFormModel",new LoginFormModel());

		model.addAttribute("date",Date.getDate());
		model.addAttribute("userName",loginSession.isGestOrUserName());

		LoginFormModel loginFormModel = new LoginFormModel();
		loginFormModel.setTranFlg("ELSE");
		model.addAttribute("loginFormModel",loginFormModel);

		return "LOG101";
	}

	//ログインボタン押下
	@RequestMapping(value="LOG101.html", params = "login", method = RequestMethod.POST)
	public ModelAndView login(@Valid @ModelAttribute LoginFormModel loginFormModel, BindingResult errors,HttpSession session) {

		ModelAndView mav = new ModelAndView();

		mav.addObject("date",Date.getDate());

		if (errors.hasErrors()) {
			mav.setViewName("/LOG101");
			mav.addObject("userName",loginSession.isGestOrUserName());
			return mav;
		}

		try {
			List<LoginInfoEntity> userInfo = loginUserService.loginUser(loginFormModel);

			if(userInfo.size()==1) {
				loginSession.doLogin(userInfo.get(0));
				mav.addObject("userName",loginSession.isGestOrUserName());
			}else {
				mav.addObject("userName",loginSession.isGestOrUserName());
				mav.addObject("message","ログインできませんでした。");
				mav.addObject("loginFormModel",loginFormModel);
				mav.setViewName("/LOG101");
				return mav;
			}

		} catch (Exception e) {
			e.printStackTrace();
			//共通エラーページへ遷移させる
			mav.setViewName("/ERR101");
			return mav;
		}

		//tranFragによって遷移先を決める
		if(loginFormModel.getTranFlg().equals("KGO102")) {
			//カート情報を渡すなど
			mav.addObject("goodsList",session.getAttribute("cartGoodsList"));
			mav.addObject("priceInfo", session.getAttribute("priceInfo"));
			mav.addObject("userName",loginSession.isGestOrUserName());
			mav.setViewName("KGO10/KGO102");

		}else if(loginFormModel.getTranFlg().equals("MEN101")){

			try {
				RegInfoEntity currentInfo = userInfoService.getUserInfo(loginSession.getUserNo());
				mav.addObject("regInfoEntity", currentInfo);
				session.setAttribute("userInfo", currentInfo);
			} catch (Exception e) {
				e.printStackTrace();
				mav.setViewName("ERR101");
				return mav;
			}

			mav.addObject("date", Date.getDate());
			mav.addObject("userName", loginSession.isGestOrUserName());
			mav.setViewName("MEM20/MEM201");

		}else if(loginFormModel.getTranFlg().equals("ELSE")){
			mav.setViewName("/MEN101");
			mav.addObject("loginCheck", loginSession.isLogin());
		}
		return mav;
	}

	//クリアボタン押下
	@RequestMapping(value="LOG101.html", params = "clear", method = RequestMethod.POST)
	public String clearLoginForm(@ModelAttribute LoginFormModel loginFormModel,Model model) {

		//遷移先指定フラグは残す
		LoginFormModel loginForm = new LoginFormModel();
		loginForm.setTranFlg(loginFormModel.getTranFlg());

		model.addAttribute("date",Date.getDate());
		model.addAttribute("userName",loginSession.isGestOrUserName());

		model.addAttribute("loginFormModel",loginForm);
		return "LOG101";
	}
}
