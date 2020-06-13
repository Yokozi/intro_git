package jp.co.btc.spring.user_dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.btc.spring.user_entity.GoodsInfoEntity;
import jp.co.btc.spring.user_model.GoodsInfoModel;
import jp.co.btc.spring.user_model.PriceInfoModel;

@Repository
public class OrderDao {

	private NamedParameterJdbcTemplate namedtemplate;

	@Autowired
	public OrderDao(NamedParameterJdbcTemplate namedParameterTemplate) {
		this.namedtemplate = namedParameterTemplate;
	}

	private class GoodsInfoEntityRowMapper extends BeanPropertyRowMapper<GoodsInfoEntity> {
		@Override
		public GoodsInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			GoodsInfoEntity entity = new GoodsInfoEntity();
			entity.setCode(rs.getString("PRODUCT_CODE"));
			entity.setCategory(rs.getInt("CATEGORY_ID"));
			entity.setName(rs.getString("PRODUCT_NAME"));
			entity.setMaker(rs.getString("MAKER"));
			entity.setStock(rs.getInt("STOCK_COUNT"));
			entity.setPrice(rs.getInt("UNIT_PRICE"));
			entity.setPicture(rs.getString("PICTURE_NAME"));
			entity.setMemo(rs.getString("MEMO"));
			return entity;
		}
	}

	public List<GoodsInfoEntity> checkDelFlg(String code) {
		String sql = "select * from ONLINE_PRODUCT where PRODUCT_CODE = :c and DELETE_FLG = 0";

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("c", code);
		List<GoodsInfoEntity> list = this.namedtemplate.query(sql, param, new GoodsInfoEntityRowMapper());
		return list;
	}

	public int searchCollectNo() {
		String sql = "select MAX(CAST(`COLLECT_NO` AS SIGNED)) from ONLINE_ORDER";
		MapSqlParameterSource param = new MapSqlParameterSource();
		Integer maxOrderNum = this.namedtemplate.queryForObject(sql, param, Integer.class);

		//何も登録されていないとき
		if (maxOrderNum == null) {
			return maxOrderNum = 1;
		}

		return maxOrderNum + 1;
	}

	public int insertOrder(int userNo, PriceInfoModel priceInfo, String strCollNo) {
		String sql = "insert into ONLINE_ORDER (MEMBER_NO,TOTAL_MONEY,TOTAL_TAX,ORDER_DATE,COLLECT_NO,LAST_UPD_DATE)values (:memNo , :toMoney , :tax , CURDATE() , :collectNo , NOW())";

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("memNo", userNo);
		param.addValue("toMoney", priceInfo.getSumP());
		param.addValue("tax", priceInfo.getTaxP());
		param.addValue("collectNo", strCollNo);

		int result = this.namedtemplate.update(sql, param);
		return result;

	}

	public int insertOrderList(GoodsInfoModel m, String strCollNo) {
		String sql = "insert into ONLINE_ORDER_LIST (COLLECT_NO,PRODUCT_CODE,ORDER_COUNT,ORDER_PRICE)values (:collectNo , :pCode , :pCount , :pPrice )";

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("collectNo", strCollNo);
		param.addValue("pCode", m.getCode());
		param.addValue("pCount", m.getCount());
		param.addValue("pPrice", m.getP());


		int result = this.namedtemplate.update(sql, param);
		return result;

	}

	public int stockEdit(GoodsInfoModel m) {
		String sql = "UPDATE ONLINE_PRODUCT SET STOCK_COUNT = STOCK_COUNT - :count , LAST_UPD_DATE = now() where PRODUCT_CODE = :pCode";

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("count", m.getCount());
		param.addValue("pCode", m.getCode());


		int result = this.namedtemplate.update(sql, param);
		return result;


	}

}
