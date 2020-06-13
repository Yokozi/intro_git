package jp.co.btc.spring.user_dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.btc.spring.user_entity.LoginInfoEntity;

@Repository
public class LoginUserDao {

	private NamedParameterJdbcTemplate namedParameterTemplate;

	@Autowired
	public LoginUserDao(NamedParameterJdbcTemplate namedParameterTemplate) {
		this.namedParameterTemplate = namedParameterTemplate;
	}

	private class LoginInfoEntityRowMapper extends BeanPropertyRowMapper<LoginInfoEntity> {

		@Override
		public LoginInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			LoginInfoEntity le = new LoginInfoEntity();
			le.setUserNo(rs.getInt("MEMBER_NO"));
			le.setUserPass(rs.getString("PASSWORD"));
			le.setName(rs.getString("NAME"));
			return le;
		}
	}

	public List<LoginInfoEntity> loginUser(LoginInfoEntity lie) throws Exception {
		String sql = "select * from ONLINE_MEMBER where MEMBER_NO = :no AND PASSWORD = :pass AND DELETE_FLG = :flg";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("no", lie.getUserNo());
		param.addValue("pass", lie.getUserPass());
		param.addValue("flg", "0");
		List<LoginInfoEntity> loginEntiy = this.namedParameterTemplate.query(sql, param, new LoginInfoEntityRowMapper());

		return loginEntiy;

	}


}
