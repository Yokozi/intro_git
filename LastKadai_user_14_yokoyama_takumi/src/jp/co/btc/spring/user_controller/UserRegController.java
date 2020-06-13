package jp.co.btc.spring.user_controller;

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

import jp.co.btc.spring.user_model.Date;
import jp.co.btc.spring.user_model.GenderList;
import jp.co.btc.spring.user_model.RegFormModel;
import jp.co.btc.spring.user_service.RegUserService;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Controller
public class UserRegController {

	private LoginSessionModel loginSession;
	private RegUserService regUserService;

	@Autowired
	public UserRegController(LoginSessionModel loginSession,RegUserService regUserService) {
		this.loginSession = loginSession;
		this.regUserService = regUserService;
	}

	//MEM101表示
	@RequestMapping(path = { "/MEM101.html" }, method = RequestMethod.GET)
	public String showRegForm(Model model) {

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());

		model.addAttribute("genderList", GenderList.getGenderList());
		model.addAttribute("regFormModel", new RegFormModel());

		return "MEM10/MEM101";
	}

	//MEM101確認ボタン押下
	@RequestMapping(value = "MEM101.html", params = "check", method = RequestMethod.POST)
	public ModelAndView CheckRegForm(@Valid @ModelAttribute RegFormModel regFormModel, BindingResult errors,HttpSession session) {

		ModelAndView mav = new ModelAndView();

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		if (errors.hasErrors()) {
			mav.setViewName("MEM10/MEM101");
			mav.addObject("genderList", GenderList.getGenderList());
			return mav;
		}

		session.setAttribute("regFormModel",regFormModel);
		mav.setViewName("MEM10/MEM102");
		return mav;
	}

	//MEM101クリアボタン押下
	@RequestMapping(value = "MEM101.html", params = "clear", method = RequestMethod.POST)
	public String clearRegForm(Model model) {

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName",loginSession.isGestOrUserName());

		model.addAttribute("genderList", GenderList.getGenderList());
		model.addAttribute("regFormModel", new RegFormModel());
		return "MEM10/MEM101";
	}

	//MEM101戻るボタン押下
	@RequestMapping(value = "MEM101.html", params = "back", method = RequestMethod.POST)
	public String backMEN101(Model model) {

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());

		//ログインとログアウトの切り替え用
		model.addAttribute("loginCheck", loginSession.isLogin());
		return "MEN101";
	}

	//MEM102新規登録処理
	@RequestMapping(value = "MEM102.html", params = "reg", method = RequestMethod.POST)
	public String reg(Model model,HttpSession session)  {

		try {
			int userNo = regUserService.insertUser((RegFormModel) session.getAttribute("regFormModel"));

			model.addAttribute("userNo",userNo);

			model.addAttribute("date", Date.getDate());
			model.addAttribute("userName",loginSession.isGestOrUserName());

			return "MEM10/MEM103";

		} catch (Exception e) {
			e.printStackTrace();
			//共通エラーページへ遷移させる
			return "ERR101";
		}
	}

	//MEM102戻るボタン押下
	@RequestMapping(value = "MEM102.html", params = "back", method = RequestMethod.POST)
	public String backMEM101(Model model,HttpSession session) {

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());

		model.addAttribute("genderList", GenderList.getGenderList());
		model.addAttribute("regFormModel", session.getAttribute("regFormModel"));
		return "MEM10/MEM101";
	}
}
