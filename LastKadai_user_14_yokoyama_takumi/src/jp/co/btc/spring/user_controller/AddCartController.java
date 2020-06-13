package jp.co.btc.spring.user_controller;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.co.btc.spring.user_component.CategoryList;
import jp.co.btc.spring.user_constants.PageConstants;
import jp.co.btc.spring.user_model.AddCartFormModel;
import jp.co.btc.spring.user_model.Date;
import jp.co.btc.spring.user_model.GoodsInfoModel;
import jp.co.btc.spring.user_model.PaginationModel;
import jp.co.btc.spring.user_model.SearchGoodsModel;
import jp.co.btc.spring.user_service.AddCartService;
import jp.co.btc.spring.user_service.SearchGoodsService;
import jp.co.btc.spring.user_service.ngPatternException;
import jp.co.btc.spring.user_service.nonCheckException;
import jp.co.btc.spring.user_service.nonStockException;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Controller
public class AddCartController {

	private LoginSessionModel loginSession;
	private AddCartService addCartService;
	private SearchGoodsService searchGoodsService;
	private CategoryList categoryList;

	@Autowired
	public AddCartController(LoginSessionModel loginSession, AddCartService addCartService,
			SearchGoodsService searchGoodsService, CategoryList categoryList) {
		this.loginSession = loginSession;
		this.addCartService = addCartService;
		this.searchGoodsService = searchGoodsService;
		this.categoryList = categoryList;

	}

	@RequestMapping(path = { "/SHO103.html" }, method = RequestMethod.POST)
	public ModelAndView addCart(@ModelAttribute AddCartFormModel formModel, HttpSession session) throws Exception {

		ModelAndView mav = new ModelAndView();
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> cart = (LinkedHashMap<String, Integer>) session.getAttribute("cart");
		if (cart == null) {
			cart = new LinkedHashMap<String, Integer>();
		}

		//何も選択されていない場合
		if (formModel.getCheckGoods().length == 0) {
			SearchGoodsModel search = (SearchGoodsModel) session.getAttribute("search");
			PaginationModel page = (PaginationModel) session.getAttribute("page");
			if (page == null) {
				page = new PaginationModel(PageConstants.MAX_DISP_GOODS_LIST);
			}
			if (search == null) {
				search = new SearchGoodsModel();
			}

			List<GoodsInfoModel> gimList = searchGoodsService.findGoodsInfoList(search, page);

			mav.setViewName("SHO10/SHO101.html");
			mav.addObject("goodsList", gimList);
			mav.addObject("searchGoodsModel", search);
			mav.addObject("paginationModel", page);
			mav.addObject("addCartFormModel", formModel);
			mav.addObject("categoryList", categoryList.getCategoryList());
			mav.addObject("message", "商品を選択してください。");
			return mav;
		}

		List<GoodsInfoModel> goodsList = null;

		try {
			goodsList = addCartService.addCart(formModel, cart);
		} catch (nonCheckException e) {
			SearchGoodsModel search = (SearchGoodsModel) session.getAttribute("search");
			PaginationModel page = (PaginationModel) session.getAttribute("page");
			if (page == null) {
				page = new PaginationModel(PageConstants.MAX_DISP_GOODS_LIST);
			}
			if (search == null) {
				search = new SearchGoodsModel();
			}

			List<GoodsInfoModel> gimList = searchGoodsService.findGoodsInfoList(search, page);

			mav.setViewName("SHO10/SHO101.html");
			mav.addObject("goodsList", gimList);
			mav.addObject("searchGoodsModel", search);
			mav.addObject("paginationModel", page);
			mav.addObject("addCartFormModel", formModel);
			mav.addObject("categoryList", categoryList.getCategoryList());
			mav.addObject("message", "購入数は1～999の数値で入力してください。");
			return mav;
		} catch (ngPatternException e) {
			SearchGoodsModel search = (SearchGoodsModel) session.getAttribute("search");
			PaginationModel page = (PaginationModel) session.getAttribute("page");
			if (page == null) {
				page = new PaginationModel(PageConstants.MAX_DISP_GOODS_LIST);
			}
			if (search == null) {
				search = new SearchGoodsModel();
			}

			List<GoodsInfoModel> gimList = searchGoodsService.findGoodsInfoList(search, page);

			mav.setViewName("SHO10/SHO101.html");
			mav.addObject("goodsList", gimList);
			mav.addObject("searchGoodsModel", search);
			mav.addObject("paginationModel", page);
			mav.addObject("addCartFormModel", formModel);
			mav.addObject("categoryList", categoryList.getCategoryList());
			mav.addObject("message", "購入数は1～999の数値で入力してください。");
			return mav;
		} catch (nonStockException e) {
			SearchGoodsModel search = (SearchGoodsModel) session.getAttribute("search");
			PaginationModel page = (PaginationModel) session.getAttribute("page");
			if (page == null) {
				page = new PaginationModel(PageConstants.MAX_DISP_GOODS_LIST);
			}
			if (search == null) {
				search = new SearchGoodsModel();
			}

			List<GoodsInfoModel> gimList = searchGoodsService.findGoodsInfoList(search, page);

			mav.setViewName("SHO10/SHO101.html");
			mav.addObject("goodsList", gimList);
			mav.addObject("searchGoodsModel", search);
			mav.addObject("paginationModel", page);
			mav.addObject("addCartFormModel", formModel);
			mav.addObject("categoryList", categoryList.getCategoryList());
			mav.addObject("message", "在庫数が足りません。購入数を変更してください。");
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}

		session.setAttribute("cart", cart);
		formModel.resetModel();

		mav.setViewName("SHO10/SHO103.html");
		mav.addObject("goodsList", goodsList);
		session.setAttribute("cartGoodsList", goodsList);
		return mav;
	}

	//SHO103戻るボタン押下
	@RequestMapping(value = "SHO104.html", params = "back", method = RequestMethod.POST)
	public ModelAndView backSHO101(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		mav.addObject("categoryList", categoryList.getCategoryList());
		mav.addObject("searchGoodsModel", new SearchGoodsModel());
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		mav.setViewName("SHO10/SHO101");
		return mav;
	}

	//SHO103お買い物ボタン押下
	@RequestMapping(value = "SHO104.html", params = "cart", method = RequestMethod.POST)
	public ModelAndView showKGO104(HttpSession session) {

		ModelAndView mav = new ModelAndView();

		@SuppressWarnings("unchecked")
		List<GoodsInfoModel> goodsList =  (List<GoodsInfoModel>) session.getAttribute("cartGoodsList");
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> cart = (LinkedHashMap<String, Integer>) session.getAttribute("cart");

		mav.addObject("goodsList",goodsList);
		mav.addObject("cart",cart);
		mav.addObject("addCartFormModel",new AddCartFormModel());
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		mav.setViewName("KGO10/KGO101");
		return mav;
	}

	//SHO102お買い物かごに入れるボタン押下
	@RequestMapping(value = "SHO102.html", params = "add", method = RequestMethod.POST)
	public ModelAndView addGoods(@ModelAttribute AddCartFormModel formModel, HttpSession session) throws Exception {

		ModelAndView mav = new ModelAndView();

		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> cart = (LinkedHashMap<String, Integer>) session.getAttribute("cart");
		if (cart == null) {
			cart = new LinkedHashMap<String, Integer>();
		}

		List<GoodsInfoModel> goodsList = null;

		try {
			goodsList = addCartService.addCartSHO102(formModel, cart);
		} catch (ngPatternException e) {

			GoodsInfoModel goodsInfo = (GoodsInfoModel) session.getAttribute("GoodsInfo");

			mav.setViewName("SHO10/SHO102.html");

			mav.addObject("goods",goodsInfo);
			mav.addObject("addCartFormModel", formModel);
			mav.addObject("message", "購入数は1～999の数値で入力してください。");
			return mav;
		} catch (nonStockException e) {

			GoodsInfoModel goodsInfo = (GoodsInfoModel) session.getAttribute("GoodsInfo");
			mav.setViewName("SHO10/SHO102.html");
			mav.addObject("goods",goodsInfo);
			mav.addObject("addCartFormModel", formModel);
			mav.addObject("message", "在庫数が足りません。購入数を変更してください。");

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("ERR101");
			return mav;
		}


		session.setAttribute("cart", cart);
		formModel.resetModel();

		mav.setViewName("SHO10/SHO103.html");
		mav.addObject("goodsList", goodsList);
		session.setAttribute("cartGoodsList", goodsList);

		return mav;
	}
}
