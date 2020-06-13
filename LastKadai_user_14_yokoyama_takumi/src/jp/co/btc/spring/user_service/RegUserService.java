package jp.co.btc.spring.user_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.btc.spring.user_dao.RegUserDao;
import jp.co.btc.spring.user_entity.RegInfoEntity;
import jp.co.btc.spring.user_model.RegFormModel;

@Service
public class RegUserService {

	private RegUserDao regUserDao;

	@Autowired
	public RegUserService(RegUserDao regUserDao) {
		this.regUserDao = regUserDao;
	}

	//会員登録（採番、登録をして、会員番号を返す）
	@Transactional(rollbackForClassName = "Exception")
	public int insertUser(RegFormModel regFormModel) throws Exception {

		RegInfoEntity rie = new RegInfoEntity();
		rie.setName(regFormModel.getName());
		rie.setPass(regFormModel.getPass());
		rie.setAge(regFormModel.getAge());
		rie.setGender(regFormModel.getGender());
		rie.setPostalCode(regFormModel.getPostalCode());
		rie.setAddr(regFormModel.getAddr());
		rie.setTel(regFormModel.getTel());

		int userNum;

		try {
			//採番
			userNum = regUserDao.getUserNum();

			//登録
			regUserDao.insertUser(rie,userNum);

			return userNum;

		} catch (Exception e) {
			throw e;
		}
	}

}
