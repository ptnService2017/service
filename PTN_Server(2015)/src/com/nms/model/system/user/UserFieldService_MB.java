package com.nms.model.system.user;


import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.system.user.UserField;
import com.nms.db.dao.system.user.UserFieldMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ExceptionManage;


public class UserFieldService_MB extends ObjectService_Mybatis {

    private UserFieldMapper mapper = null;
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	public UserFieldMapper getMapper() {
		return mapper;
	}

	public void setMapper(UserFieldMapper mapper) {
		this.mapper = mapper;
	}


	/**
	 * 根据user_id查询
	 * 
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public List<UserField> query(int user_id) throws Exception {
		List<UserField> userFields = null;
		try {
			userFields = new ArrayList<UserField>();
			userFields = this.mapper.queryByCondition(user_id);

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return userFields;
	}
	/**
	 * 根据user_id查询
	 * 
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public List<UserField> queryUserField(UserField userField) throws Exception {
		List<UserField> userFields = null;
		try {
			userFields = new ArrayList<UserField>();
			userFields = this.mapper.queryByUserField(userField);

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return userFields;
	}



	public void delete(int userId) throws Exception {
		try {
			this.mapper.deleteByUserId(userId);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	

}
