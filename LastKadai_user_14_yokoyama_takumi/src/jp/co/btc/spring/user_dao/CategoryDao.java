package jp.co.btc.spring.user_dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.btc.spring.user_entity.CategoryEntity;

@Repository
public class CategoryDao {

	private NamedParameterJdbcTemplate namedtemplate;

	@Autowired
	public CategoryDao(NamedParameterJdbcTemplate namedParameterTemplate) {
		this.namedtemplate =  namedParameterTemplate;
	}

	private class CategoryEntityRowMapper extends BeanPropertyRowMapper<CategoryEntity> {
		@Override
		public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			CategoryEntity entity = new CategoryEntity();
			entity.setId(rs.getInt("CTGR_ID"));
			entity.setName(rs.getString("NAME"));

			return entity;
		}
	}

	public List<CategoryEntity> selectAllCategory() {
		String sql = "select * from  ONLINE_CATEGORY";
		List<CategoryEntity> categoryEntityList = this.namedtemplate.query(sql, new CategoryEntityRowMapper());
		return categoryEntityList;
	}
}
