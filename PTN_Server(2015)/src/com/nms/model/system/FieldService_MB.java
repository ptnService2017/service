package com.nms.model.system;


import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.system.Field;
import com.nms.db.bean.system.WorkIps;
import com.nms.db.bean.system.user.UserField;
import com.nms.db.bean.system.user.UserInst;
import com.nms.db.dao.equipment.shelf.SiteInstMapper;
import com.nms.db.dao.system.FieldMapper;
import com.nms.db.dao.system.WorkIpsMapper;
import com.nms.db.dao.system.user.UserFieldMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;


public class FieldService_MB extends ObjectService_Mybatis {

   private FieldMapper mapper = null;
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	public FieldMapper getMapper() {
		return mapper;
	}

	public void setMapper(FieldMapper mapper) {
		this.mapper = mapper;
	}


	/**
	 * 新增或修改field对象，通过field.getId()来判断是修改还是新增
	 * 
	 * @param field
	 *            实体
	 * @return 执行成功的记录数
	 * @throws Exception
	 */
	public int saveOrUpdate(Field field) throws Exception {

		if (field == null) {
			throw new Exception("field is null");
		}
		UserFieldMapper userFieldMapper=null;
		UserField userField=null;
		UserInst userInst=null;
		int result = 0;
		try {
			userFieldMapper=this.sqlSession.getMapper(UserFieldMapper.class);
			if (field.getId() == 0) {
				result = this.mapper.insertField(field);
				/**
				 * 若此次新建域的操作用户不是系统默认账户（admin）
				 * 	 查看 isAll属性
				 * 		0，添加到用户，域的关联表中，1 不做处理
				 */
				userInst=ConstantUtil.user;
				if(!"admin".equals(userInst.getUser_Name())){
					//已选择  显示 所有域
					if(0==userInst.getIsAll()){
						//向  用户—域关联表中添加数据
						userField=new UserField();
						userField.setUser_id(userInst.getUser_Id());
						userField.setField_id(result);
						userFieldMapper.insert(userField);
					}
				}
				
					
			} else {
				result = this.mapper.updateField(field);
			}
            this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{			
			userFieldMapper=null;
			 userField=null;
			 userInst=null;
		}
		return result;
	}

	/**
	 * 通过主键删除file对象
	 * 
	 * @param id
	 *            主键
	 * @return 删除的记录数
	 * @throws Exception
	 */
	public int delete(int id) throws Exception {

		int result = 0;
		UserFieldMapper userFieldMapper = null;
		try {
			this.sqlSession.getConnection().setAutoCommit(false);
			userFieldMapper=this.sqlSession.getMapper(UserFieldMapper.class);			
			userFieldMapper.deleteByField(id);
			//删除域时，，还需删除   用户 —域关联表中  此域的信息
			result = this.mapper.deleteById(id);
			
			//删除域时，，还需删除该域下所有子网
			this.mapper.deleteSubNet(id);
			if(!this.sqlSession.getConnection().getAutoCommit()){
				this.sqlSession.getConnection().commit();
			}
		} catch (Exception e) {
			this.sqlSession.getConnection().rollback();
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			this.sqlSession.getConnection().setAutoCommit(true);
		}
		return result;

	}

	/**
	 * 查询全部
	 * 
	 * @return List<Field>集合
	 * @throws Exception
	 */
	public List<Field> select() throws Exception {
		List<Field> fieldList = null;
		List<Field> resultList=null;
		Field field = null;
		SiteInstMapper siteInstMapper = null;
		SiteInst siteInst = null;
		List<SiteInst> siteInstList = null;
		WorkIpsMapper workIpsMapper = null;
		WorkIps workIps = null;
		UserFieldMapper userFieldMapper = null;
		List<UserField> userFieldList=null;
		try {
			/**
			 * 登陆用户是否有查看所有域的权限
			 *   1   有
			 */
			field = new Field();
			resultList=new ArrayList<Field>();
			if(1==ConstantUtil.user.getIsAll()){
				fieldList = this.mapper.queryByConditions(field);
			}else{
				//查找可以查看的域
				userFieldMapper=this.sqlSession.getMapper(UserFieldMapper.class);
				userFieldList = new ArrayList<UserField>();
				userFieldList=	userFieldMapper.queryByUserInst(ConstantUtil.user);
				fieldList=new ArrayList<Field>();
					for(int i=0;i<userFieldList.size();i++){
						field.setId(userFieldList.get(i).getField_id());
						fieldList.addAll( this.mapper.queryByConditions(field));
						
					}
			}
			if (null != fieldList && fieldList.size() != 0) {
				for (int i = 0; i < fieldList.size(); i++) {
					//加载此域下的所有网元，包括子网下的网元
					siteInst = new SiteInst();
					siteInst.setFieldID(fieldList.get(i).getId());
					siteInstMapper = this.sqlSession.getMapper(SiteInstMapper.class);
					siteInstList = siteInstMapper.queryByCondition(siteInst);
					fieldList.get(i).setSiteInstList(siteInstList);
					
					//
					workIpsMapper = this.sqlSession.getMapper(WorkIpsMapper.class);
					workIps = new WorkIps();
					workIps.setField(fieldList.get(i).getId());
					List<WorkIps> work=workIpsMapper.queryByCondition(workIps);
					workIps = new WorkIps();
					if(work !=null && work.size()>0){					
						workIps=work.get(work.size()-1);
					}
					fieldList.get(i).setWorkIps(workIps);
						resultList.add(fieldList.get(i));
					}
					
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			field = null;
			siteInstMapper = null;
			workIpsMapper=null;
			siteInst = null;
			siteInstList = null;
		}
		return resultList;
	}

	/**
	 * 根据条件查询
	 * 
	 * @param field
	 *            查询条件
	 * @return List<Field> 集合
	 * @throws Exception
	 */
	public List<Field> select(Field field) throws Exception {
		List<Field> fieldList = null;
		SiteInstMapper siteInstMapper = null;
		SiteInst siteInst = null;
		List<SiteInst> siteInstList = null;
		try {
			fieldList = new ArrayList<Field>();
			fieldList = this.mapper.queryByConditions(field);
			if (null != fieldList && fieldList.size() != 0) {
				for (int i = 0; i < fieldList.size(); i++) {
					siteInst = new SiteInst();
					siteInst.setFieldID(fieldList.get(i).getId());
					siteInstMapper = this.sqlSession.getMapper(SiteInstMapper.class);
					siteInstList = new ArrayList<SiteInst>();
					siteInstList = siteInstMapper.queryByCondition(siteInst);
					fieldList.get(i).setSiteInstList(siteInstList);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			siteInstMapper = null;
			siteInst = null;
			siteInstList = null;
		}
		return fieldList;
	}

	/**
	 * 根据fieldID集合查询field
	 * 
	 * @param fieldids
	 *            field集合
	 * @return count
	 * @throws Exception
	 */
	public List<Field> selectfieldidByid(List<Integer> fieldids) throws Exception {

		List<Field> fields = null;
		SiteInstMapper siteInstMapper = null;
		WorkIpsMapper workIpsMapper = null;
		SiteInst site = null;
		WorkIps workIps = null;
		try {
			fields = new ArrayList<Field>();
			fields = this.mapper.queryByidCondition(fieldids);
			workIpsMapper = this.sqlSession.getMapper(WorkIpsMapper.class);
			siteInstMapper = this.sqlSession.getMapper(SiteInstMapper.class);
			
			for (int i = 0; i < fields.size(); i++) {
				site = new SiteInst();
				site.setFieldID(fields.get(i).getId());
				List<SiteInst> siteInst= new ArrayList<SiteInst>();
				siteInst =siteInstMapper.queryByCondition(site);
				fields.get(i).setSiteInstList(siteInst);				
				workIps = new WorkIps();
				workIps.setField(fields.get(i).getId());
				WorkIps workIp = new WorkIps();					
				List<WorkIps> work=workIpsMapper.queryByCondition(workIps);
				if(work !=null && work.size()>0){					
					workIp=work.get(work.size()-1);
				}
				fields.get(i).setWorkIps(workIp);
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			siteInstMapper = null;
			site = null;
			workIpsMapper = null;
			workIps = null;
		}
		return fields;
	}

	/**
	 * 批量更新
	 * 
	 * @param fieList
	 *            域的ID集合
	 * @throws Exception
	 */
	public void updateBatch(List<Field> fieldList) throws Exception {
		if (fieldList == null) {
			throw new Exception("fieldList is null");
		}
		try {
			this.sqlSession.getConnection().setAutoCommit(false);
			for(int i = 0; i < fieldList.size(); i++){
			  this.mapper.updateBatch(fieldList.get(i));
			}
			if(!this.sqlSession.getConnection().getAutoCommit()){
				this.sqlSession.getConnection().commit();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	
	/**
	 * 通过用户id（权限域）查找域
	 * @param name
	 * @return
	 */
	public List<Field> selectRootField(UserInst userInst){
		List<Field> fields = new ArrayList<Field>();
		try {			
			fields = this.mapper.queryByUserIdField(userInst);				
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return fields;
		
	}
	/**
	 * 查询所有域
	 * @return
	 * @throws Exception
	 */
	public List<Field> selectField() throws Exception {
		List<Field> fieldList = null;
		Field field = null;
		WorkIpsMapper workIpsMapper = this.sqlSession.getMapper(WorkIpsMapper.class);
		try {
			field = new Field();
			fieldList = this.mapper.queryByConditions(field);
			for(Field f : fieldList){
				List<String > ipList = new ArrayList<String>();
				WorkIps workIps = new WorkIps();
				workIps.setField(f.getId());
				List<WorkIps> work=workIpsMapper.queryByCondition(workIps);
				workIps = new WorkIps();
				if(work !=null && work.size()>0){					
					workIps=work.get(work.size()-1);
				}				
				if(workIps != null){
					if(workIps.getWorkIp1() != null && !"".equals(workIps.getWorkIp1())){
						ipList.add(workIps.getWorkIp1());
					}
					if(workIps.getWorkIp2() != null && !"".equals(workIps.getWorkIp2())){
						ipList.add(workIps.getWorkIp2());
					}
					if(workIps.getWorkIp3() != null && !"".equals(workIps.getWorkIp3())){
						ipList.add(workIps.getWorkIp3());
					}
					if(workIps.getWorkIp4() != null && !"".equals(workIps.getWorkIp4())){
						ipList.add(workIps.getWorkIp4());
					}
					if(workIps.getWorkIp5() != null && !"".equals(workIps.getWorkIp5())){
						ipList.add(workIps.getWorkIp5());
					}
					if(workIps.getWorkIp6() != null && !"".equals(workIps.getWorkIp6())){
						ipList.add(workIps.getWorkIp6());
					}
				}
				
				if(ipList.size()>0){
					f.setWorkIP(ipList);
				}
				f.setWorkIps(workIps);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			field = null;
			workIpsMapper = null;
		}
		return fieldList;
	}
		
	

	/**
	 * 通过域名字查找域
	 * @param name
	 * @return
	 */
	public List<Field> queryByUserId(UserInst userInst){
		List<Field> fields = null;
		try {
			fields = new ArrayList<Field>();
			fields = this.mapper.queryByUserId(userInst);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return fields;
		
	}
	
	/**
	 * 查询没有M网元的域
	 * @return
	 */
	public List<Field> queryNoMsite(){
		List<Field> fields = null;
		try {
			fields = new ArrayList<Field>();
			fields = this.mapper.queryNoMsite();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return fields;
	}
	
	/**
	 * 通过netWorkId查询所有网元组
	 * @param netWorkId
	 * @return
	 */
	public List<Field> queryByNetWorkid(int netWorkId){
		List<Field> fields = null;
		WorkIpsMapper workIpsMapper = this.sqlSession.getMapper(WorkIpsMapper.class);
		try {
			fields = new ArrayList<Field>();
			fields = this.mapper.queryByNetWorkId(netWorkId);
			for(Field f : fields){
				List<String > ipList = new ArrayList<String>();
				WorkIps workIps = new WorkIps();
				workIps.setField(f.getId());
				List<WorkIps> work=workIpsMapper.queryByCondition(workIps);
				workIps = new WorkIps();
				if(work !=null && work.size()>0){					
					workIps=work.get(work.size()-1);
				}
				
				if(workIps != null){
					if(workIps.getWorkIp1() != null && !"".equals(workIps.getWorkIp1())){
						ipList.add(workIps.getWorkIp1());
					}
					if(workIps.getWorkIp2() != null && !"".equals(workIps.getWorkIp2())){
						ipList.add(workIps.getWorkIp2());
					}
					if(workIps.getWorkIp3() != null && !"".equals(workIps.getWorkIp3())){
						ipList.add(workIps.getWorkIp3());
					}
					if(workIps.getWorkIp4() != null && !"".equals(workIps.getWorkIp4())){
						ipList.add(workIps.getWorkIp4());
					}
					if(workIps.getWorkIp5() != null && !"".equals(workIps.getWorkIp5())){
						ipList.add(workIps.getWorkIp5());
					}
					if(workIps.getWorkIp6() != null && !"".equals(workIps.getWorkIp6())){
						ipList.add(workIps.getWorkIp6());
					}
				}
				
				if(ipList.size()>0){
					f.setWorkIP(ipList);
				}
				f.setWorkIps(workIps);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return fields;
	}
	
	/**
	 * 验证名称是否可用
	 * @param name
	 * @return
	 */
	public boolean checkNameExist(String name){
		Boolean isExist = false;
		List<Field> field =new ArrayList<Field>();
		field = this.mapper.checkNameExist(name);
		if(field!=null && field.size()!=0){
			isExist = true;
		}
		return isExist;
	}
	
	/**
	 * 根据域i对应的域对象
	 * @param fieldId
	 * @return
	 */
	public List<Field> selectByFieldId(int fieldId){
		List<Field> fieldList = null;
		List<Integer> fieldIntegers = null;
		try {
			fieldIntegers = new ArrayList<Integer>();
			fieldIntegers.add(fieldId);
			fieldList = new ArrayList<Field>();
			fieldList = this.mapper.queryByidCondition(fieldIntegers);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			fieldIntegers = null;
		}
		return fieldList;
	}

	/**
	 * 根据域id查询域和包含的子网
	 * @param fieldId
	 * @return
	 */
	public List<Integer> selectByFieldIds(int fieldId){
		
		List<Integer> ids = null;
		List<Field> fieldList = null;
		List<Field> fieldGroupList = null;
		try {
			ids = new ArrayList<Integer>();
			fieldList = new ArrayList<Field>();
			fieldList = this.mapper.selectByGroupId(fieldId);
			if(null != fieldList && !fieldList.isEmpty())
			{
				for(Field field : fieldList)
				{
					ids.add(field.getId());
					fieldGroupList = new ArrayList<Field>();
					fieldGroupList = this.mapper.selectByParentId(field.getId());
					if(null != fieldGroupList && !fieldGroupList.isEmpty())
					{
					for(Field fieldGroup : fieldGroupList)
					 {
						ids.add(fieldGroup.getId());
					 }
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			fieldList = null;
			fieldGroupList = null;
		}
		return ids;
	}
	
	/**
	 * 通过域名字查找域
	 * @param name
	 * @return
	 */
	public List<Field> selectfieldidByName(String name){
		List<Field> fields = null;
		try {
			fields = this.mapper.queryByNameCondition(name);
			if(fields!=null){
				for(int i=0;i<fields.size();i++){
					fields.get(i).setName(name);
				}
			}else{
				fields=new ArrayList<Field>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return fields;
		
	}
}