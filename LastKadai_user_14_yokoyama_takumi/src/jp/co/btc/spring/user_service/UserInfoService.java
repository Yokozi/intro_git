package jp.co.btc.spring.user_service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.btc.spring.user_dao.UserInfoDao;
import jp.co.btc.spring.user_entity.RegInfoEntity;
import jp.co.btc.spring.user_model.ChangeUserInfoModel;
import jp.co.btc.spring.user_session.LoginSessionModel;

@Service
public class UserInfoService {

	private UserInfoDao userInfoDao;
	private LoginSessionModel loginSession;

	@Autowired
	public UserInfoService(UserInfoDao userInfoDao,LoginSessionModel loginSession) {
		this.userInfoDao = userInfoDao;
		this.loginSession = loginSession;
	}

	//会員情報を取得
	@Transactional(rollbackForClassName = "Exception")
	public RegInfoEntity getUserInfo(int userNo) throws Exception {

		try {
			List<RegInfoEntity> regInfo = userInfoDao.getUserInfo(userNo);
			if (regInfo.size() != 1) {
				throw new Exception();
			}
			return regInfo.get(0);
		} catch (Exception e) {
			throw e;
		}
	}

	//会員情報変更
	@Transactional(rollbackForClassName = "Exception")
	public void changeUserInfo(ChangeUserInfoModel cui) throws Exception {
		try {
			//パスワード以外変更
			int updateCount = userInfoDao.changeUserInfo(cui);
			if (updateCount == 0) {
				throw new Exception();
			}
			loginSession.setNewName(cui.getName());
			//パスワード変更あれば、処理
			if(cui.getPass()!=null&&!cui.getPass().isEmpty()) {

				updateCount = userInfoDao.changePass(cui);
				if (updateCount == 0) {
					throw new Exception();
				}
				loginSession.setNewPass(cui.getPass());
			}

		} catch (Exception e) {
			throw e;
		}

	}

	//会員情報削除
	@Transactional(rollbackForClassName = "Exception")
	public void delUserInfo(RegInfoEntity rie) throws Exception {
		int delCount = userInfoDao.delUserInfo(rie);
		if (delCount == 0) {
			throw new Exception();
		}

	}

}
