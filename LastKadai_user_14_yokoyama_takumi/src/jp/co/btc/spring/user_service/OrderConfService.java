package jp.co.btc.spring.user_service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.btc.spring.user_dao.OrderDao;
import jp.co.btc.spring.user_entity.GoodsInfoEntity;
import jp.co.btc.spring.user_model.GoodsInfoModel;
import jp.co.btc.spring.user_model.PriceInfoModel;

@Service
public class OrderConfService {

	private OrderDao orderDao;

	@Autowired
	public OrderConfService(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Transactional(rollbackForClassName = "Exception")
	public String checkDelFlg(List<GoodsInfoModel> goodsList) {

		for (GoodsInfoModel m : goodsList) {
			List<GoodsInfoEntity> eList = orderDao.checkDelFlg(m.getCode());
			if (eList.size() == 0) {
				return m.getCode();
			}
		}

		return "";
	}

	@Transactional(rollbackForClassName = "Exception")
	public void order(List<GoodsInfoModel> goodsList, PriceInfoModel priceInfo, int userNo) throws Exception {

		//取りまとめ番号取得
		int collectNo = orderDao.searchCollectNo();
		String strCollNo = String.valueOf(collectNo);

		//注文台帳
		int result = orderDao.insertOrder(userNo,priceInfo,strCollNo);
		if(result==0) {
			throw new Exception();
		}

		//注文詳細台帳
		for(GoodsInfoModel m : goodsList) {
			result = orderDao.insertOrderList(m,strCollNo);
			if(result==0) {
				throw new Exception();
			}
		}

		//在庫調整
		for(GoodsInfoModel m : goodsList) {
			result = orderDao.stockEdit(m);
			if(result==0) {
				throw new Exception();
			}
		}



	}
}
