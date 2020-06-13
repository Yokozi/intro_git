package jp.co.btc.spring.user_service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.btc.spring.user_dao.SearchGoodsDao;
import jp.co.btc.spring.user_entity.GoodsInfoEntity;
import jp.co.btc.spring.user_model.AddCartFormModel;
import jp.co.btc.spring.user_model.GoodsInfoModel;
import jp.co.btc.spring.user_model.PriceInfoModel;

@Service
public class AddCartService {

	private SearchGoodsDao searchGoodsDao;

	@Autowired
	public AddCartService(SearchGoodsDao searchGoodsDao) {
		this.searchGoodsDao = searchGoodsDao;
	}

	@Transactional(rollbackForClassName = "Exception")
	public List<GoodsInfoModel> addCart(AddCartFormModel formModel, LinkedHashMap<String, Integer> cart)
			throws Exception {

		if (cart != null) {
			String[] itemNums = formModel.getCode();
			String[] countItem = formModel.getCount();
			String[] checkItem = formModel.getCheckGoods();

			Map<String, String> nums = new HashMap<>();

			if (itemNums != null && countItem != null) {
				for (int i = 0; i < itemNums.length; i++) {
					if (countItem[i] != null && !countItem[i].isEmpty()) {
						nums.put(itemNums[i], countItem[i]);
					}
				}
			}

			for (String itemNum : checkItem) {
				if (!nums.containsKey(itemNum)) {
					throw new nonCheckException();
				}
			}

			for (String itemNum : checkItem) {
				Pattern p = Pattern.compile("^[0-9]*$");
				Matcher m = p.matcher(nums.get(itemNum));

				if (!m.find()) {
					throw new ngPatternException();
				}

				int i = Integer.parseInt(nums.get(itemNum));
				if (i <= 0 || i > 999) {
					throw new ngPatternException();
				}

			}

			for (String itemNum : checkItem) {
				if (cart.get(itemNum) != null) {
					cart.put(itemNum, cart.get(itemNum) + Integer.parseInt(nums.get(itemNum)));
				} else {
					cart.put(itemNum, Integer.parseInt(nums.get(itemNum)));
				}
			}

			// カートのkey(MEM_NUM)でメンバー情報を取得
			List<GoodsInfoEntity> goodsEntityList = searchGoodsDao.findByNumSet(cart.keySet());

			// 表示用bean（MenberEntityにフィールド個数を追加した物）に詰め替える
			List<GoodsInfoModel> modelList = new ArrayList<GoodsInfoModel>();
			for (GoodsInfoEntity entity : goodsEntityList) {
				modelList.add(new GoodsInfoModel(entity, cart.get(entity.getCode())));
			}

			//在庫チェック
			for (GoodsInfoModel model : modelList) {
				List<GoodsInfoEntity> list = searchGoodsDao.stockCheck(model);
				if (list.get(0).getStock() < model.getCount()) {
					cart.replace(model.getCode(), 0);
					model.setCount(0);
					throw new nonStockException();
				}
			}

			return modelList;
		}
		return null;
	}

	@Transactional(rollbackForClassName = "Exception")
	public List<GoodsInfoModel> addCartSHO102(AddCartFormModel formModel, LinkedHashMap<String, Integer> cart)
			throws Exception {

		if (cart != null) {
			String[] itemNums = formModel.getCode();
			String[] countItem = formModel.getCount();

			Map<String, String> nums = new HashMap<>();

			if (itemNums != null && countItem != null) {
				for (int i = 0; i < itemNums.length; i++) {
					if (countItem[i] != null && !countItem[i].isEmpty()) {
						nums.put(itemNums[i], countItem[i]);
					}
				}
			}

			for (String count : countItem) {
				Pattern p = Pattern.compile("^[0-9]*$");
				Matcher m = p.matcher(count);

				if (!m.find()) {
					throw new ngPatternException();
				}

				int i = Integer.parseInt(count);
				if (i <= 0 || i > 999) {
					throw new ngPatternException();
				}

			}

			for (String itemNum : itemNums) {
				if (cart.get(itemNum) != null) {
					cart.put(itemNum, cart.get(itemNum) + Integer.parseInt(nums.get(itemNum)));
				} else {
					cart.put(itemNum, Integer.parseInt(nums.get(itemNum)));
				}
			}

			// カートのkey(MEM_NUM)でメンバー情報を取得
			List<GoodsInfoEntity> goodsEntityList = searchGoodsDao.findByNumSet(cart.keySet());

			// 表示用bean（MenberEntityにフィールド個数を追加した物）に詰め替える
			List<GoodsInfoModel> modelList = new ArrayList<GoodsInfoModel>();
			for (GoodsInfoEntity entity : goodsEntityList) {
				modelList.add(new GoodsInfoModel(entity, cart.get(entity.getCode())));
			}

			//在庫チェック
			for (GoodsInfoModel model : modelList) {
				List<GoodsInfoEntity> list = searchGoodsDao.stockCheck(model);
				if (list.get(0).getStock() < model.getCount()) {
					cart.replace(model.getCode(), 0);
					model.setCount(0);
					throw new nonStockException();
				}
			}

			return modelList;
		}
		return null;
	}

	@Transactional(rollbackForClassName = "Exception")
	public List<GoodsInfoModel> removeGoods(LinkedHashMap<String, Integer> cart) {

		List<GoodsInfoEntity> goodsEntityList = searchGoodsDao.findByNumSet(cart.keySet());

		// 表示用bean（MenberEntityにフィールド個数を追加した物）に詰め替える
		List<GoodsInfoModel> modelList = new ArrayList<GoodsInfoModel>();
		for (GoodsInfoEntity entity : goodsEntityList) {
			modelList.add(new GoodsInfoModel(entity, cart.get(entity.getCode())));
		}

		return modelList;
	}

	@Transactional(rollbackForClassName = "Exception")
	public List<GoodsInfoModel> stockCheck(AddCartFormModel formModel, LinkedHashMap<String, Integer> cart)
			throws Exception {

		List<GoodsInfoEntity> goodsEntityList = searchGoodsDao.findByNumSet(cart.keySet());
		int i = 0;
		String[] arr = formModel.getCount();
		// 表示用bean（MenberEntityにフィールド個数を追加した物）に詰め替える
		List<GoodsInfoModel> modelList = new ArrayList<GoodsInfoModel>();
		for (GoodsInfoEntity entity : goodsEntityList) {
			int count = Integer.parseUnsignedInt(arr[i]);
			modelList.add(new GoodsInfoModel(entity, count));
			i++;
		}

		//在庫チェック
		for (GoodsInfoModel model : modelList) {
			List<GoodsInfoEntity> list = searchGoodsDao.stockCheck(model);
			if (list.get(0).getStock() < model.getCount()) {
				throw new nonStockException();
			}
		}
		return modelList;
	}

	@Transactional(rollbackForClassName = "Exception")
	public PriceInfoModel getPriceInfo(List<GoodsInfoModel> goodsList) {

		List<Integer> priceList = new ArrayList<Integer>();
		Integer sumPrice = 0;
		int tax=0;

		for (GoodsInfoModel goods : goodsList) {
//			int price = Integer.parseInt(goods.getPrice());
			priceList.add(goods.getP() * goods.getCount());
		}

		for (Integer price : priceList) {
			sumPrice += price;
		}

		tax = sumPrice/10;

		int totalPrice = sumPrice+tax;

		PriceInfoModel priceInfo = new PriceInfoModel();

		priceInfo.setSumPrice(sumPrice);
		priceInfo.setTax(tax);
		priceInfo.setTotalPrice(totalPrice);

		return priceInfo;
	}

}
