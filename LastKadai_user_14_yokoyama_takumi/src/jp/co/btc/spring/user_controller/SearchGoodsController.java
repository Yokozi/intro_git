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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.co.btc.spring.user_component.CategoryList;
import jp.co.btc.spring.user_constants.PageConstants;
import jp.co.btc.spring.user_model.AddCartFormModel;
import jp.co.btc.spring.user_model.Date;
import jp.co.btc.spring.user_model.GoodsInfoModel;
import jp.co.btc.spring.user_model.PaginationModel;
import jp.co.btc.spring.user_model.SearchGoodsModel;
import jp.co.btc.spring.user_service.SearchGoodsService;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Controller
public class SearchGoodsController {

	private LoginSessionModel loginSession;
	private SearchGoodsService searchGoodsService;
	private CategoryList categoryList;

	@Autowired
	public SearchGoodsController(LoginSessionModel loginSession, SearchGoodsService searchGoodsService,
			CategoryList categoryList) {
		this.loginSession = loginSession;
		this.searchGoodsService = searchGoodsService;
		this.categoryList = categoryList;
	}

	//商品検索画面no1-9のみ表示（初期表示）
	@RequestMapping({ "/SHO101.html" })
	public String showSHO101(Model model) {

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());
		model.addAttribute("categoryList", categoryList.getCategoryList());
		model.addAttribute("searchGoodsModel", new SearchGoodsModel());

		return "SHO10/SHO101";
	}

	//検索ボタン押下
	@RequestMapping(value = "SHO101.html", params = "search", method = RequestMethod.POST)
	public ModelAndView searchGoods(@Valid @ModelAttribute SearchGoodsModel search, BindingResult errors,
			HttpSession session) {

		PaginationModel page = new PaginationModel(PageConstants.MAX_DISP_GOODS_LIST);

		ModelAndView mav = new ModelAndView();
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.addObject("categoryList", categoryList.getCategoryList());
		mav.setViewName("SHO10/SHO101");

		if (errors.hasErrors()) {
			mav.addObject("paginationModel", page);
			mav.addObject("addCartFormModel", new AddCartFormModel());
			if (session.getAttribute("goodsList") != null) {
				mav.addObject("goodsList", session.getAttribute("goodsList"));
			}
			return mav;
		}

		//商品リスト取得
		List<GoodsInfoModel> gimList = searchGoodsService.findGoodsInfoList(search, page);

		if (gimList.size() == 0) {
			mav.addObject("searchGoodsModel", search);

			session.setAttribute("search", search);
			mav.addObject("message", "条件に該当する商品は０件です。");
			return mav;
		}
		// 検索条件、ページング情報をsessionに保持
		session.setAttribute("search", search);
		session.setAttribute("page", page);
		session.setAttribute("goodsList", gimList);

		mav.addObject("goodsList", gimList);
		mav.addObject("searchGoodsModel", search);
		mav.addObject("paginationModel", page);
		mav.addObject("addCartFormModel", new AddCartFormModel());

		return mav;
	}

	//戻るボタン押下
	@RequestMapping(value = "SHO101.html", params = "back", method = RequestMethod.POST)
	public String backToMEN101(Model model) {

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());

		//ログインとログアウトの切り替え用
		model.addAttribute("loginCheck", loginSession.isLogin());
		return "MEN101";

	}

	//クリアボタン押下
	@RequestMapping(value = "SHO101.html", params = "clear", method = RequestMethod.POST)
	public String clearSHO101(Model model, HttpSession session) {

		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());
		model.addAttribute("categoryList", categoryList.getCategoryList());
		model.addAttribute("searchGoodsModel", new SearchGoodsModel());
		model.addAttribute("addCartFormModel", new AddCartFormModel());

		//表示しているリストはそのまま
		PaginationModel page;

		if (session.getAttribute("page") != null) {
			page = (PaginationModel) session.getAttribute("page");
		} else {
			page = new PaginationModel(PageConstants.MAX_DISP_GOODS_LIST);
		}
		model.addAttribute("paginationModel", page);

		if (session.getAttribute("goodsList") != null) {
			model.addAttribute("goodsList", session.getAttribute("goodsList"));
		}

		return "SHO10/SHO101";
	}

	//商品のリンク
	@RequestMapping(value = "SHO102.html", method = RequestMethod.GET)
	public String showDetail(Model model, HttpSession session,
			@RequestParam("code") String c) {

		List<GoodsInfoModel> gimList = searchGoodsService.findGoodsDetail(c);
		GoodsInfoModel goodsInfo = gimList.get(0);
		model.addAttribute("goods", goodsInfo);
		model.addAttribute("addCartFormModel", new AddCartFormModel());
		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());
		session.setAttribute("goodsInfo", goodsInfo);

		return "SHO10/SHO102.html";
	}

	//商品のリンク(ページング後)
	@RequestMapping(value = "SHO101/SHO102.html", method = RequestMethod.GET)
	public String showDetail2(Model model, HttpSession session,
			@RequestParam("code") String c) {

		List<GoodsInfoModel> gimList = searchGoodsService.findGoodsDetail(c);
		GoodsInfoModel goodsInfo = gimList.get(0);
		model.addAttribute("goods", goodsInfo);
		model.addAttribute("addCartFormModel", new AddCartFormModel());
		model.addAttribute("date", Date.getDate());
		model.addAttribute("userName", loginSession.isGestOrUserName());
		session.setAttribute("goods", goodsInfo);

		return "SHO10/SHO102.html";
	}

	//SHO102戻る押下
	@RequestMapping(value = "SHO102.html", params = "back", method = RequestMethod.POST)
	public ModelAndView backToSHO101(HttpSession session) {

		SearchGoodsModel search = (SearchGoodsModel) session.getAttribute("search");
		PaginationModel page = (PaginationModel) session.getAttribute("page");
		if (page == null) {
			page = new PaginationModel(PageConstants.MAX_DISP_GOODS_LIST);
		}
		if (search == null) {
			search = new SearchGoodsModel();
		}

		List<GoodsInfoModel> gimList = searchGoodsService.findGoodsInfoList(search, page);

		session.setAttribute("search", search);
		session.setAttribute("page", page);

		ModelAndView mav = new ModelAndView("SHO10/SHO101");

		mav.addObject("goodsList", gimList);
		mav.addObject("searchGoodsModel", search);
		mav.addObject("paginationModel", page);
		mav.addObject("addCartFormModel", new AddCartFormModel());
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.addObject("categoryList", categoryList.getCategoryList());

		return mav;
	}

	//ページング処理
	@RequestMapping(path = "/SHO101/page.html", method = RequestMethod.POST)
	public ModelAndView transitionPage(@RequestParam String pageNum, HttpSession session) {

		SearchGoodsModel search = (SearchGoodsModel) session.getAttribute("search");
		PaginationModel page = (PaginationModel) session.getAttribute("page");
		if (page == null) {
			page = new PaginationModel(PageConstants.MAX_DISP_GOODS_LIST);
		}
		if (search == null) {
			search = new SearchGoodsModel();
		}

		try {
			page.setPage(Integer.parseInt(pageNum));
		} catch (NumberFormatException e) {

		}

		List<GoodsInfoModel> gimList = searchGoodsService.findGoodsInfoList(search, page);

		session.setAttribute("search", search);
		session.setAttribute("page", page);

		ModelAndView mav = new ModelAndView("SHO10/SHO101");

		mav.addObject("goodsList", gimList);
		mav.addObject("searchGoodsModel", search);
		mav.addObject("paginationModel", page);
		mav.addObject("addCartFormModel", new AddCartFormModel());
		mav.addObject("date", Date.getDate());
		mav.addObject("userName", loginSession.isGestOrUserName());
		mav.addObject("categoryList", categoryList.getCategoryList());

		return mav;
	}
}
