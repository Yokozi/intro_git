package jp.co.btc.spring.user_service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.btc.spring.user_dao.SearchGoodsDao;
import jp.co.btc.spring.user_entity.GoodsInfoEntity;
import jp.co.btc.spring.user_model.GoodsInfoModel;
import jp.co.btc.spring.user_model.PaginationModel;
import jp.co.btc.spring.user_model.SearchGoodsModel;

@Service
public class SearchGoodsService {

	private SearchGoodsDao searchGoodsDao;

	@Autowired
	public SearchGoodsService(SearchGoodsDao searchGoodsDao) {
		this.searchGoodsDao = searchGoodsDao;
	}

	public long countGoods;

	@Transactional(rollbackForClassName = "Exception")
	public List<GoodsInfoModel> findGoodsInfoList(SearchGoodsModel search, PaginationModel page) {

		//表示するためのモデル
		List<GoodsInfoModel> gimList = new ArrayList<GoodsInfoModel>();

		//総レコード数取得
		long count = searchGoodsDao.countSearch(search);

		if(count==0) {
			return gimList;
		}

		// 総レコード数からページングでの検索を行う
		page.setResultsSum(count);

		int startNum = (page.getPage() - 1) * page.getMaxDisp();
		int dispNum = page.getMaxDisp();

		List<GoodsInfoEntity> gieList = searchGoodsDao.findSearchResult(search, startNum, dispNum);

		for (GoodsInfoEntity gie : gieList) {

			GoodsInfoModel gim = new GoodsInfoModel();

			gim.setCode(gie.getCode());

			gim.setName(gie.getName());

			gim.setMaker(gie.getMaker());

			gim.setPrice(gie.getPrice());

			gim.setMemo(gie.getMemo());

			gimList.add(gim);
		}
		return gimList;
	}

	@Transactional(rollbackForClassName = "Exception")
	public List<GoodsInfoModel> findGoodsDetail(String c) {
		//表示するためのモデル
		List<GoodsInfoModel> gimList = new ArrayList<GoodsInfoModel>();

		List<GoodsInfoEntity> gieList = searchGoodsDao.findSearchDetail(c);

		for (GoodsInfoEntity gie : gieList) {

			GoodsInfoModel gim = new GoodsInfoModel();

			gim.setCode(gie.getCode());

			gim.setName(gie.getName());

			gim.setPicture(gie.getPicture());

			gim.setPrice(gie.getPrice());

			gim.setMemo(gie.getMemo());

			gimList.add(gim);
		}
		return gimList;
	}
}
