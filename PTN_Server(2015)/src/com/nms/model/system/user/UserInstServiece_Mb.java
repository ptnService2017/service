package com.nms.model.system.user;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.system.NetWork;
import com.nms.db.bean.system.roleManage.RoleInfo;
import com.nms.db.bean.system.user.UserField;
import com.nms.db.bean.system.user.UserInst;
import com.nms.db.dao.system.NetWorkMapper;
import com.nms.db.dao.system.roleManage.RoleInfoMapper;
import com.nms.db.dao.system.user.UserFieldMapper;
import com.nms.db.dao.system.user.UserInstMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ExceptionManage;


public class UserInstServiece_Mb extends ObjectService_Mybatis {
    private UserInstMapper userInstMapper=null;
    
    public UserInstMapper getMapper() {
		return userInstMapper;
	}

	public void setMapper(UserInstMapper userInstMapper) {
		this.userInstMapper = userInstMapper;
	}

	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}

	

	
	/**
	 * 通过条件查询判断userid<>自己的
	 * @param userinst 查询条件
	 * @return List<UserInst>集合
	 * @throws Exception
	 */
	public List<UserInst> select(UserInst userinst) throws Exception {
		List<UserInst> userinstList = new ArrayList<UserInst>();
		try {
			userinstList =this.userInstMapper.selectUserList(userinst);		
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return userinstList;
	}
	
	/**
	 * 通过主键删除userInst
	 * 
	 * @param id
	 *            主键
	 * @return 删除的记录数
	 * @throws Exception
	 */
	public int delete(int User_Id) throws Exception {

		int result = 0;

		try {
			result = this.userInstMapper.deleteByUserId(User_Id);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return result;

	}
	
	
	/**
	 * 通过条件查询
	 * 
	 * @param userinst
	 *            查询条件
	 * @return List<UserInst> 集合
	 * @throws Exception
	 */
	public List<UserInst> selectuserid(UserInst userinst) throws Exception {
		List<UserInst> userinstList = null;

		try {
			userinstList= new ArrayList<UserInst>();
			userinstList = this.userInstMapper.queryByuserid(userinst);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return userinstList;
	}
	
	
	/**
	 * 新增或修改userInst对象，通过userInst.getUser_Id()来判断是修改还是新增
	 * 
	 * @param userInst
	 *            实体
	 * @return userinst主键
	 * @throws Exception
	 */
	public int saveOrUpdate(UserInst userInst) throws Exception {

		if (userInst == null) {
			throw new Exception("userInst is null");
		}

		int result = 0;
		NetWork field=null;
		UserField userField=null;
		UserFieldMapper userFieldMapper=null;
        RoleInfoMapper roleInfoMapper = null;
		List<RoleInfo> roleInfoList=null;
		RoleInfo roleInfo=null;
		try {
			this.sqlSession.getConnection().setAutoCommit(false);
			userFieldMapper=this.sqlSession.getMapper(UserFieldMapper.class);
			/**
			 * 通过   ：用户   角色标签（级  角色名）
			 * 			查找 角色ID
			 */
			roleInfoMapper = this.sqlSession.getMapper(RoleInfoMapper.class);
			roleInfo=new RoleInfo();
			roleInfo.setRoleName(userInst.getUser_Group());
			roleInfo.setRoleEnName(userInst.getUser_GroupEn());
//			roleInfo.setId(userInst.getRoleInfo_id());
			roleInfoList = roleInfoMapper.select(roleInfo);
			 //不为空，则有数据
			 if(roleInfoList!=null&&roleInfoList.size()>0){
				 roleInfo=roleInfoList.get(0);
				 userInst.setRoleInfo_id(roleInfo.getId());
			 }
			if (userInst.getUser_Id() == 0) {
				//result   返回的是 主键	
				if(userInst.getPswExpires()!=null && !userInst.getPswExpires().equals(""))
				{
					int days = Integer.parseInt(userInst.getPswExpires());
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DAY_OF_MONTH, days);
					String deadTime = String.valueOf(c.getTimeInMillis());
					userInst.setDeadTime(deadTime);
				}
				this.userInstMapper.insert(userInst);
				result = userInst.getUser_Id();
				//未选中所有域时
				if(0==userInst.getIsAll()){
					if(null!=userInst.getFieldList()){
						for(int i=0;i<userInst.getFieldList().size();i++){
							field=userInst.getFieldList().get(i);
							userField=new UserField();
							userField.setUser_id(result);
							userField.setField_id(field.getNetWorkId());
							//  循环内直接添加，即  批量添加
							userFieldMapper.insert(userField);
							
						}
					}
				}
			} else {
				
				if(userInst.getPswExpires()!=null && !userInst.getPswExpires().equals(""))
				{
					int days = Integer.parseInt(userInst.getPswExpires());
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DAY_OF_MONTH, days);
					String deadTime = String.valueOf(c.getTimeInMillis());
					userInst.setDeadTime(deadTime);
				}
				this.userInstMapper.update(userInst);
				/**
				 * 先删除  此用户的所有   域
				 */
				userFieldMapper.deleteByUserId(userInst.getUser_Id());
				/**
				 * 用户未选中选中所有域
				 * 添加   用户  选择的域
				 */
				if(0==userInst.getIsAll()){
					if(null!=userInst.getFieldList()&&userInst.getFieldList().size()>0){
						for(int j=0;j<userInst.getFieldList().size();j++){
							field=userInst.getFieldList().get(j);
							userField=new UserField();
							userField.setUser_id(userInst.getUser_Id());
							userField.setField_id(field.getNetWorkId());
							//  循环内直接添加，即  批量添加
							userFieldMapper.insert(userField);
						}
					}
				}
				
				result=userInst.getUser_Id();
			}
			if(!this.sqlSession.getConnection().getAutoCommit()){
				this.sqlSession.getConnection().commit();
			}
		} catch (Exception e) {
			this.sqlSession.getConnection().rollback();
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			this.sqlSession.getConnection().setAutoCommit(true);
		}
		return result;
	}
	

	
	/**
	 * 查询全部
	 * 
	 * @return List<UserInst> 集合
	 * @throws Exception
	 */
	public List<UserInst> select() throws Exception {
		List<UserInst> userinstList = null;
		List<NetWork> userFieldList=null;
		NetWorkMapper netWorkMapper = null;
		try {
			UserInst userinst = new UserInst();
			userinstList= new ArrayList<UserInst>();
			userinstList = userInstMapper.querByroleId(userinst);
			netWorkMapper = sqlSession.getMapper(NetWorkMapper.class);
			/**
			 * 遍历   用户集合
			 * 		像用户中添加  域集合
			 */
			if(userinstList!=null&&userinstList.size()>0){
				for(int i=0;i<userinstList.size();i++){
					userinst=userinstList.get(i);
					userFieldList = new ArrayList<NetWork>();
					userFieldList=netWorkMapper.queryByUserIdField(userinst);
					userinst.setFieldList(userFieldList);
					
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return userinstList;
	}
	
}
