package jp.co.btc.spring.user_service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.btc.spring.user_dao.LoginUserDao;
import jp.co.btc.spring.user_entity.LoginInfoEntity;
import jp.co.btc.spring.user_model.LoginFormModel;

@Service
public class LoginUserService {

	private LoginUserDao loginUserDao;

	@Autowired
	public LoginUserService(LoginUserDao loginUserDao) {
		this.loginUserDao = loginUserDao;
	}

	@Transactional(rollbackForClassName = "Exception")
	public List<LoginInfoEntity> loginUser(LoginFormModel loginFormModel) throws Exception {

		LoginInfoEntity lie = new LoginInfoEntity();
		int userNo = Integer.parseInt(loginFormModel.getUserNo());

		lie.setUserNo(userNo);
		lie.setUserPass(loginFormModel.getUserPass());

		try {

			List<LoginInfoEntity> userInfo = loginUserDao.loginUser(lie);

			return userInfo;

		} catch (Exception e) {
			throw e;
		}
	}
}
