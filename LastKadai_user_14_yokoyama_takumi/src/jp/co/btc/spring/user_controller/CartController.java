package jp.co.btc.spring.user_controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.co.btc.spring.user_model.AddCartFormModel;
import jp.co.btc.spring.user_model.Date;
import jp.co.btc.spring.user_model.GoodsInfoModel;
import jp.co.btc.spring.user_model.PriceInfoModel;
import jp.co.btc.spring.user_service.AddCartService;
import jp.co.btc.spring.user_service.nonStockException;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Controller
public class CartController {

	private LoginSessionModel loginSession;
	private AddCartService addCartService;

	@Autowired
	public CartController(LoginSessionModel loginSession, AddCartService addCartService) {
		this.loginSession = loginSession;
		this.addCartService = addCartService;
	}

	//KGO101表示
	@RequestMapping({ "/KGO101.html" })
	public ModelAndView showKGO101(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		@SuppressWarnings("unchecked")
		List<GoodsInfoModel> goodsList = (List<GoodsInfoModel>) session.getAttribute("cartGoodsList");
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> cart = (LinkedHashMap<String, Integer>) session.getAttribute("cart");

		mav.addObject("goodsList", goodsList);
		mav.addObject("cart", cart);
		mav.addObject("addCartFormModel", new AddCartFormModel());
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		mav.setViewName("KGO10/KGO101");
		return mav;
	}

	//KGO101取り消し押下
	@RequestMapping(value = "KGO101.html", params = "del", method = RequestMethod.POST)
	public ModelAndView delGoods(@ModelAttribute AddCartFormModel formModel, HttpSession session) {

		ModelAndView mav = new ModelAndView();

		mav.addObject("addCartFormModel", new AddCartFormModel());
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.setViewName("KGO10/KGO101");

		@SuppressWarnings("unchecked")
		List<GoodsInfoModel> goodsList = (List<GoodsInfoModel>) session.getAttribute("cartGoodsList");
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> cart = (LinkedHashMap<String, Integer>) session.getAttribute("cart");

		String[] checkItem = formModel.getCheckGoods();

		//何も選択されていない場合
		if (checkItem != null && checkItem.length == 0) {
			mav.addObject("message", "取り消し対象の商品を選択してください。");
		} else {
			for (String code : checkItem) {
				cart.remove(code);
				if (cart.size() == 0) {
					session.removeAttribute("cart");
					session.removeAttribute("cartGoodsList");
					return mav;
				} else {
					goodsList = addCartService.removeGoods(cart);
				}
			}

			session.setAttribute("cart", cart);
			session.setAttribute("cartGoodsList", goodsList);
		}

		mav.addObject("goodsList", goodsList);
		mav.addObject("cart", cart);

		return mav;
	}

	//KGO101買い物をやめる押下
	@RequestMapping(value = "KGO101.html", params = "cancel", method = RequestMethod.POST)
	public ModelAndView cancelOrder(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		session.removeAttribute("cartGoodsList");
		session.removeAttribute("cart");

		mav.addObject("loginCheck", loginSession.isLogin());
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		mav.setViewName("MEN101");
		return mav;
	}

	//KGO101注文する押下
	@RequestMapping(value = "KGO101.html", params = "order", method = RequestMethod.POST)
	public ModelAndView orderCheck(@ModelAttribute AddCartFormModel formModel, HttpSession session) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		@SuppressWarnings("unchecked")
		List<GoodsInfoModel> goodsList = (List<GoodsInfoModel>) session.getAttribute("cartGoodsList");
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> cart = (LinkedHashMap<String, Integer>) session.getAttribute("cart");

		String[] count = formModel.getCount();

		for (String c : count) {
			Pattern p = Pattern.compile("^[0-9]*$");
			Matcher m = p.matcher(c);

			if (c.isEmpty() || !m.find()) {
				mav.addObject("goodsList", goodsList);
				mav.addObject("cart", cart);
				mav.addObject("addCartFormModel", formModel);
				mav.addObject("message", "購入数は1～999の数値で入力してください。");
				mav.setViewName("KGO10/KGO101");
				return mav;
			}

			int intC = Integer.parseInt(c);

			if (intC < 1 || intC > 999) {
				mav.addObject("goodsList", goodsList);
				mav.addObject("cart", cart);
				mav.addObject("addCartFormModel", formModel);
				mav.addObject("message", "購入数は1～999の数値で入力してください。");
				mav.setViewName("KGO10/KGO101");
				return mav;
			}
		}

		try {
			goodsList = addCartService.stockCheck(formModel, cart);
		} catch (nonStockException e) {
			mav.addObject("goodsList", goodsList);
			mav.addObject("cart", cart);
			mav.addObject("addCartFormModel", formModel);
			mav.addObject("message", "在庫が足りません。購入数を変更してください。");
			mav.setViewName("KGO10/KGO101");
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}

		PriceInfoModel priceInfo = addCartService.getPriceInfo(goodsList);

		mav.addObject("goodsList", goodsList);
		mav.addObject("priceInfo", priceInfo);
		session.setAttribute("cartGoodsList", goodsList);
		session.setAttribute("orderFormModel", formModel);
		session.setAttribute("priceInfo", priceInfo);
		mav.setViewName("KGO10/KGO102");
		return mav;
	}

	//KGO101メニューへ押下
	@RequestMapping(value = "KGO101.html", params = "menu", method = RequestMethod.POST)
	public String toMenu(Model model) {

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());

		//ログインとログアウトの切り替え用
		model.addAttribute("loginCheck", loginSession.isLogin());
		return "MEN101";
	}

	//KGO102戻る押下
	@RequestMapping(value = "KGO101.html", params = "backKGO101", method = RequestMethod.POST)
	public ModelAndView backToKGO101(Model model,HttpSession session) {

		ModelAndView mav = new ModelAndView();

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.addObject("goodsList",session.getAttribute("cartGoodsList"));
		mav.addObject("addCartFormModel",session.getAttribute("orderFormModel"));
		mav.setViewName("KGO10/KGO101");
		return mav;
	}
}
