package jp.co.btc.spring.user_controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.co.btc.spring.user_entity.RegInfoEntity;
import jp.co.btc.spring.user_model.ChangeUserInfoModel;
import jp.co.btc.spring.user_model.Date;
import jp.co.btc.spring.user_model.GenderList;
import jp.co.btc.spring.user_service.UserInfoService;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Controller
public class UserInfoController {

	private LoginSessionModel loginSession;
	private UserInfoService userInfoService;

	@Autowired
	public UserInfoController(LoginSessionModel loginSession, UserInfoService userInfoService) {
		this.loginSession = loginSession;
		this.userInfoService = userInfoService;
	}

	//MEM201表示
	@RequestMapping(path = { "/MEM201.html" }, method = RequestMethod.GET)
	public ModelAndView showUserInfo( HttpSession session) {

		ModelAndView mav = new ModelAndView();

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
		return mav;
	}

	//MEM201修正ボタン押下
	@RequestMapping(value = "MEM201.html", params = "change", method = RequestMethod.POST)
	public ModelAndView changeUserInfo(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		try {
			RegInfoEntity currentInfo = userInfoService.getUserInfo(loginSession.getUserNo());
			ChangeUserInfoModel changeInfo = new ChangeUserInfoModel();
			changeInfo.setNum(currentInfo.getNum());
			changeInfo.setName(currentInfo.getName());
			changeInfo.setAge(currentInfo.getAge());
			changeInfo.setGender(currentInfo.getGender());
			changeInfo.setPostalCode(currentInfo.getPostalCode());
			changeInfo.setAddr(currentInfo.getAddr());
			changeInfo.setTel(currentInfo.getTel());
			changeInfo.setRegDate(currentInfo.getRegDate());

			mav.addObject("changeUserInfoModel", changeInfo);
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.addObject("genderList", GenderList.getGenderList());
		mav.setViewName("MEM20/MEM202");
		return mav;
	}

	//MEM201削除ボタン押下
	@RequestMapping(value = "MEM201.html", params = "del", method = RequestMethod.POST)
	public ModelAndView delUserInfo(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		try {
			mav.addObject("regInfoEntity", userInfoService.getUserInfo(loginSession.getUserNo()));
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.setViewName("MEM30/MEM301");
		return mav;
	}

	//MEM201戻るボタン押下
	@RequestMapping(value = "MEM201.html", params = "back", method = RequestMethod.POST)
	public ModelAndView backMEN101(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.addObject("loginCheck", loginSession.isLogin());
		mav.setViewName("MEN101");
		return mav;
	}

	//MEM202確認ボタン押下
	@RequestMapping(value = "MEM202.html", params = "check", method = RequestMethod.POST)
	public ModelAndView checkUserInfo(@Valid @ModelAttribute ChangeUserInfoModel changeUserInfoModel,
			BindingResult errors, HttpSession session) {

		ModelAndView mav = new ModelAndView();

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		if (errors.hasErrors()) {
			mav.setViewName("MEM20/MEM202");
			mav.addObject("genderList", GenderList.getGenderList());
			return mav;
		}

		try {
			if (changeUserInfoModel.getPass() != null && !changeUserInfoModel.getPass().isEmpty()) {
				RegInfoEntity rie = userInfoService.getUserInfo(loginSession.getUserNo());
				String currPass = rie.getPass();
				if (currPass.equals(changeUserInfoModel.getPass())) {
					mav.setViewName("MEM20/MEM202");
					mav.addObject("genderList", GenderList.getGenderList());
					mav.addObject("changeUserInfoModel", changeUserInfoModel);
					mav.addObject("message", "変更前と同じパスワードには変更はできません。");
					return mav;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}
		session.setAttribute("changeUserInfo", changeUserInfoModel);
		mav.addObject("changeUserInfoModel", changeUserInfoModel);
		mav.setViewName("MEM20/MEM203");
		return mav;
	}

	//MEM202戻るボタン押下
	@RequestMapping(value = "MEM202.html", params = "back", method = RequestMethod.POST)
	public ModelAndView backMEM201( HttpSession session) {

		ModelAndView mav = new ModelAndView();

		try {
			RegInfoEntity currentInfo = userInfoService.getUserInfo(loginSession.getUserNo());
			mav.addObject("regInfoEntity", currentInfo);
			session.setAttribute("userInfo", currentInfo);
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName( "ERR101");
			return mav;
		}

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.setViewName("MEM20/MEM201");
		return mav;
	}

	//MEM202クリアボタン押下
	@RequestMapping(value = "MEM202.html", params = "clear", method = RequestMethod.POST)
	public ModelAndView clearMEM202(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		try {
			RegInfoEntity currentInfo = (RegInfoEntity) session.getAttribute("userInfo");
			ChangeUserInfoModel changeInfo = new ChangeUserInfoModel();
			changeInfo.setNum(currentInfo.getNum());
			changeInfo.setName(currentInfo.getName());
			changeInfo.setAge(currentInfo.getAge());
			changeInfo.setGender(currentInfo.getGender());
			changeInfo.setPostalCode(currentInfo.getPostalCode());
			changeInfo.setAddr(currentInfo.getAddr());
			changeInfo.setTel(currentInfo.getTel());
			changeInfo.setRegDate(currentInfo.getRegDate());

			mav.addObject("changeUserInfoModel", changeInfo);
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.addObject("genderList", GenderList.getGenderList());
		mav.setViewName("MEM20/MEM202");

		return mav;
	}

	//MEM203実行ボタン押下
	@RequestMapping(value = "MEM203.html", params = "change", method = RequestMethod.POST)
	public ModelAndView change(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		try {
			ChangeUserInfoModel cui = (ChangeUserInfoModel) session.getAttribute("changeUserInfo");
			//DB処理
			userInfoService.changeUserInfo(cui);

		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.setViewName("MEM20/MEM204");
		return mav;
	}

	//MEM203戻るボタン押下
	@RequestMapping(value = "MEM203.html", params = "back", method = RequestMethod.POST)
	public ModelAndView backMEM202(HttpSession session) {

		ModelAndView mav = new ModelAndView();



		try {

			RegInfoEntity currentInfo = userInfoService.getUserInfo(loginSession.getUserNo());
			ChangeUserInfoModel changeInfo = new ChangeUserInfoModel();
			changeInfo.setNum(currentInfo.getNum());
			changeInfo.setName(currentInfo.getName());
			changeInfo.setAge(currentInfo.getAge());
			changeInfo.setGender(currentInfo.getGender());
			changeInfo.setPostalCode(currentInfo.getPostalCode());
			changeInfo.setAddr(currentInfo.getAddr());
			changeInfo.setTel(currentInfo.getTel());
			changeInfo.setRegDate(currentInfo.getRegDate());

			mav.addObject("changeUserInfoModel", changeInfo);
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.addObject("genderList", GenderList.getGenderList());
		mav.setViewName("MEM20/MEM202");
		return mav;
	}

	//MEM301実行ボタン押下
	@RequestMapping(value = "MEM301.html", params = "del", method = RequestMethod.POST)
	public ModelAndView del(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		try {
			RegInfoEntity rie = (RegInfoEntity) session.getAttribute("userInfo");
			userInfoService.delUserInfo(rie);

		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}
		loginSession.doLogout();
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.setViewName( "MEM30/MEM302");
		return mav;
	}

	//MEM301戻るボタン押下
	@RequestMapping(value = "MEM301.html", params = "back", method = RequestMethod.POST)
	public ModelAndView backTo201( HttpSession session) {

		ModelAndView mav = new ModelAndView();

		try {
			RegInfoEntity rie = (RegInfoEntity) session.getAttribute("userInfo");
			mav.addObject("regInfoEntity",rie);
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.setViewName( "MEM20/MEM201");
		return mav;
	}
}
