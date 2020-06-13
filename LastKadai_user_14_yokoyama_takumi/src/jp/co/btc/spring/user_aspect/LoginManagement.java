package jp.co.btc.spring.user_aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import jp.co.btc.spring.user_model.Date;
import jp.co.btc.spring.user_model.LoginFormModel;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Aspect
@Component
public class LoginManagement {

	private LoginSessionModel loginSession;

	@Autowired
	public LoginManagement(LoginSessionModel loginSession) {
		this.loginSession = loginSession;
	}

	//会員情報修正系
	@Around("execution(* jp.co.btc.spring.user_controller.UserInfoController.*(..))")
	public ModelAndView userPageFilter(ProceedingJoinPoint pj) throws Throwable {

		// ログインしていない場合はlogin画面を表示
		if (!loginSession.isLogin()) {
			ModelAndView mav = new ModelAndView("/LOG101");
			mav.addObject("message", "ログインして下さい。");
			mav.addObject("date", Date.getDate());
			mav.addObject("userName", loginSession.isGestOrUserName());

			LoginFormModel loginFormModel = new LoginFormModel();
			loginFormModel.setTranFlg("MEN101");
			mav.addObject("loginFormModel", loginFormModel);

			return mav;
		}

		// ジョインポイントを呼び出す
		return (ModelAndView) pj.proceed();
	}

	//注文前確認
	@Around("execution(* jp.co.btc.spring.user_controller.OrderConfController.*(..))")
	public ModelAndView orderLoginFilter(ProceedingJoinPoint pj) throws Throwable {

		// ログインしていない場合はlogin画面を表示
		if (!loginSession.isLogin()) {
			ModelAndView mav = new ModelAndView("/LOG101");
			mav.addObject("message", "ログインして下さい。");
			mav.addObject("date", Date.getDate());
			mav.addObject("userName", loginSession.isGestOrUserName());

			LoginFormModel loginFormModel = new LoginFormModel();
			loginFormModel.setTranFlg("KGO102");
			mav.addObject("loginFormModel", loginFormModel);

			return mav;
		}

		// ジョインポイントを呼び出す
		return (ModelAndView) pj.proceed();
	}
}
