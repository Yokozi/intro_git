package jp.co.btc.spring.user_controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.co.btc.spring.user_model.Date;
import jp.co.btc.spring.user_model.GoodsInfoModel;
import jp.co.btc.spring.user_model.PriceInfoModel;
import jp.co.btc.spring.user_service.OrderConfService;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Controller
public class OrderConfController {

	private LoginSessionModel loginSession;
	private OrderConfService orderConfService;

	@Autowired
	public OrderConfController(LoginSessionModel loginSession,OrderConfService orderConfService) {
		this.loginSession = loginSession;
		this.orderConfService = orderConfService;
	}

	//注文処理
	@RequestMapping(path = { "/KGO103.html" }, method = RequestMethod.POST)
	public ModelAndView order( HttpSession session) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		@SuppressWarnings("unchecked")
		List<GoodsInfoModel> goodsList = (List<GoodsInfoModel>) session.getAttribute("cartGoodsList");
		PriceInfoModel priceInfo  = (PriceInfoModel) session.getAttribute("priceInfo");



		try {
			String checkDelFlg = orderConfService.checkDelFlg(goodsList);

			if(!checkDelFlg.equals("")) {
				mav.setViewName("KGO10/KGO102");
				mav.addObject("goodsList", goodsList);
				mav.addObject("priceInfo", priceInfo);
				mav.addObject("goodsNo",checkDelFlg);
				return mav;
			}

			orderConfService.order(goodsList,priceInfo,loginSession.getUserNo());

		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}


		session.removeAttribute("cart");
		session.removeAttribute("cartGoodsList");
		session.removeAttribute("priceInfo");

		mav.setViewName("KGO10/KGO103");
		return mav;
	}

}
