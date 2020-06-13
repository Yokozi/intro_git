package jp.co.btc.spring.user_dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.btc.spring.user_entity.RegInfoEntity;
import jp.co.btc.spring.user_model.ChangeUserInfoModel;

@Repository
public class UserInfoDao {

	private NamedParameterJdbcTemplate namedParameterTemplate;

	@Autowired
	public UserInfoDao(NamedParameterJdbcTemplate namedParameterTemplate) {
		this.namedParameterTemplate = namedParameterTemplate;
	}

	private class RegInfoEntityRowMapper extends BeanPropertyRowMapper<RegInfoEntity> {
		@Override
		public RegInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			RegInfoEntity regInfo = new RegInfoEntity();
			regInfo.setNum(rs.getInt("MEMBER_NO"));
			regInfo.setPass(rs.getString("PASSWORD"));
			regInfo.setName(rs.getString("NAME"));
			regInfo.setAge(rs.getString("AGE"));
			regInfo.setGender(rs.getString("SEX"));
			regInfo.setPostalCode(rs.getString("ZIP"));
			regInfo.setAddr(rs.getString("ADDRESS"));
			regInfo.setTel(rs.getString("TEL"));
			regInfo.setRegDate(rs.getString("REGISTER_DATE"));

			return regInfo;
		}
	}

	public List<RegInfoEntity> getUserInfo(int userNo) {
		String sql = "select * from ONLINE_MEMBER where MEMBER_NO = :userNo";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userNo", userNo);
		List<RegInfoEntity> regInfo = this.namedParameterTemplate.query(sql, param, new RegInfoEntityRowMapper());
		return regInfo;

	}

	//会員情報（パスワード以外）を変更
	public int changeUserInfo(ChangeUserInfoModel cui) {
		String sql = "UPDATE ONLINE_MEMBER SET NAME = :name , AGE = :age ,SEX = :sex , ZIP = :zip ,ADDRESS = :addr ,TEL = :tel , LAST_UPD_DATE = now() where MEMBER_NO = :userNo";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userNo", cui.getNum());
		param.addValue("name", cui.getName());
		param.addValue("age", cui.getAge());
		param.addValue("sex", cui.getGender());
		param.addValue("zip", cui.getPostalCode());
		param.addValue("addr", cui.getAddr());
		param.addValue("tel", cui.getTel());

		int updateCount = this.namedParameterTemplate.update(sql, param);
		return updateCount;
	}

	//パスワードを変更
	public int changePass(ChangeUserInfoModel cui) {
		String sql = "UPDATE ONLINE_MEMBER SET PASSWORD = :newPass where MEMBER_NO = :userNo ";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userNo", cui.getNum());
		param.addValue("newPass",cui.getPass());

		int updateCount = this.namedParameterTemplate.update(sql, param);
		return updateCount;
	}

	//会員情報削除
	public int delUserInfo(RegInfoEntity rie) {
		String sql = "UPDATE ONLINE_MEMBER SET DELETE_FLG = '1' , LAST_UPD_DATE = now() where MEMBER_NO = :userNo ";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userNo", rie.getNum());

		int updateCount = this.namedParameterTemplate.update(sql, param);
		return updateCount;
	}
}
