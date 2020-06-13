package jp.co.btc.spring.user_dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.btc.spring.user_entity.RegInfoEntity;

@Repository
public class RegUserDao {

	private NamedParameterJdbcTemplate namedParameterTemplate;

	@Autowired
	public RegUserDao(NamedParameterJdbcTemplate namedParameterTemplate) {
		this.namedParameterTemplate = namedParameterTemplate;
	}

	public int getUserNum() {
		String sql = "select MAX(MEMBER_NO) from ONLINE_MEMBER";
		MapSqlParameterSource param = new MapSqlParameterSource();
		Integer userNum = this.namedParameterTemplate.queryForObject(sql, param, Integer.class);

		//何も登録されていないとき
		if (userNum == null) {
			return userNum = 1;
		}

		return userNum + 1;
	}

	public void insertUser(RegInfoEntity rie, int userNum) throws Exception {
		String sql = "insert into ONLINE_MEMBER values (:memNum, :pass, :name, :age, :sex, :zip, :addr, :tel,curdate() , :delFlg ,NOW())";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("memNum", userNum);
		param.addValue("pass", rie.getPass());
		param.addValue("name", rie.getName());
		param.addValue("age", rie.getAge());
		param.addValue("sex", rie.getGender());
		param.addValue("zip", rie.getPostalCode());
		param.addValue("addr", rie.getAddr());
		param.addValue("tel", rie.getTel());
		param.addValue("delFlg", "0");

		int result = this.namedParameterTemplate.update(sql, param);
		if (result != 1) {
			throw new Exception();
		}
	}

}
