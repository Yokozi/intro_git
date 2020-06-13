package jp.co.btc.spring.user_dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.btc.spring.user_entity.GoodsInfoEntity;
import jp.co.btc.spring.user_model.GoodsInfoModel;
import jp.co.btc.spring.user_model.SearchGoodsModel;

@Repository
public class SearchGoodsDao {

	private NamedParameterJdbcTemplate namedParameterTemplate;

	@Autowired
	public SearchGoodsDao(NamedParameterJdbcTemplate namedParameterTemplate) {

		this.namedParameterTemplate = namedParameterTemplate;
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
			//			entity.setRegDate(rs.getString("REGISTER_DATE"));
			entity.setPrice(rs.getInt("UNIT_PRICE"));
			entity.setPicture(rs.getString("PICTURE_NAME"));
			entity.setMemo(rs.getString("MEMO"));
			return entity;
		}
	}

	public long countSearch(SearchGoodsModel search) {

		StringBuilder sb = new StringBuilder("select count(*) from ONLINE_PRODUCT ");

		MapSqlParameterSource paramObj = new MapSqlParameterSource();

		sb.append(createQueryStrAndParamObj(search, paramObj));

		long count = namedParameterTemplate.queryForObject(sb.toString(), paramObj, Long.class);

		return count;
	}

	public List<GoodsInfoEntity> findSearchResult(SearchGoodsModel search, int startNum, int dispNum) {

		StringBuilder sb = new StringBuilder("select * from ONLINE_PRODUCT ");

		MapSqlParameterSource paramObj = new MapSqlParameterSource();

		sb.append(createQueryStrAndParamObj(search, paramObj));

		sb.append("limit :start , :max ;");
		paramObj.addValue("start", startNum);
		paramObj.addValue("max", dispNum);

		List<GoodsInfoEntity> list = this.namedParameterTemplate.query(sb.toString(), paramObj,
				new GoodsInfoEntityRowMapper());
		return list;
	}

	private String createQueryStrAndParamObj(SearchGoodsModel search, MapSqlParameterSource paramObj) {
		StringBuilder sb = new StringBuilder();

		if (search.getCategory() != 0) {

			sb.append(" CATEGORY_ID   = :category ");
			paramObj.addValue("category", search.getCategory());
		}

		if (search.getName() != null && !search.getName().isEmpty()) {
			if (0 < sb.length()) {
				sb.append("and ");
			}
			sb.append(" PRODUCT_NAME like :name ");
			paramObj.addValue("name", "%" + search.getName() + "%");
		}

		if (search.getMaker() != null && !search.getMaker().isEmpty()) {
			if (0 < sb.length()) {
				sb.append("and ");
			}

			sb.append(" MAKER like :maker ");
			paramObj.addValue("maker", "%" + search.getMaker() + "%");
		}

		if (search.getMinP() != null && !search.getMinP().isEmpty()) {
			if (0 < sb.length()) {
				sb.append("and ");
			}

			sb.append(" UNIT_PRICE >= :min ");
			int minP = Integer.parseInt(search.getMinP());
			paramObj.addValue("min", minP);
		}

		if (search.getMaxP() != null && !search.getMaxP().isEmpty()) {
			if (0 < sb.length()) {
				sb.append("and ");
			}

			sb.append(" UNIT_PRICE <= :maxP ");
			int maxP = Integer.parseInt(search.getMaxP());
			paramObj.addValue("maxP", maxP);
		}

		if (0 < sb.length()) {
			sb.append("and ");
		}

		sb.append(" DELETE_FLG = 0 ");

		if (0 < sb.length()) {
			return "where " + sb.toString();
		} else {
			return "";
		}
	}

	public List<GoodsInfoEntity> findSearchDetail(String c) {

		String sql = "select * from ONLINE_PRODUCT where PRODUCT_CODE = :c";

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("c", c);
		List<GoodsInfoEntity> list = this.namedParameterTemplate.query(sql, param, new GoodsInfoEntityRowMapper());
		return list;
	}

	public List<GoodsInfoEntity> findByNumSet(Set<String> keySet) {

		String sqlIn = "select * from ONLINE_PRODUCT where PRODUCT_CODE in (";
		String sqlOr = "order by field (PRODUCT_CODE, ";
		StringBuilder sbIn = new StringBuilder(sqlIn);
		StringBuilder sbOr = new StringBuilder(sqlOr);
		MapSqlParameterSource paramMap = new MapSqlParameterSource();

		int i = 1;
		for( String code : keySet ) {
			sbIn.append( ":num" + i + ", ");
			sbOr.append( ":num" + i + ", ");
			paramMap.addValue("num" + i, code);
			i++;
		}

		if ( i > 1 ) {
			sbIn.delete(sbIn.length() - ", ".length(), sbIn.length());
			sbOr.delete(sbOr.length() - ", ".length(), sbOr.length());
		}
		sbIn.append(") ");
		sbOr.append(");");

		sbIn.append(sbOr.toString());
		String sql = sbIn.toString();


		List<GoodsInfoEntity> memberList = this.namedParameterTemplate.query(sql, paramMap, new GoodsInfoEntityRowMapper());
		return memberList;
	}

	public List<GoodsInfoEntity> stockCheck(GoodsInfoModel model) {
		String sql = "select * from ONLINE_PRODUCT where PRODUCT_CODE = :c";

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("c", model.getCode());
		List<GoodsInfoEntity> list = this.namedParameterTemplate.query(sql, param, new GoodsInfoEntityRowMapper());
		return list;

	}

}
