package com.nms.model.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.system.Field;
import com.nms.db.bean.system.user.UserField;
import com.nms.db.dao.equipment.shelf.SiteInstMapper;
import com.nms.db.dao.system.FieldMapper;
import com.nms.db.dao.system.user.UserFieldMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;

/**
 * 子网管理相关功能
 * @author dzy
 *
 */
public class SubnetService_MB extends ObjectService_Mybatis{
	
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
	 *  初始化和刷新数据
	 * @param field 域
	 * @return
	 */
	public List<Field> searchAndrefreshdata(Field field){
		List<Field> list = null;
		List<UserField> userFieldList=null;
		List<Integer> fiedIdList=new ArrayList<Integer>();
		List<Field> fieldsList=null;
		UserFieldMapper userFieldMapper = null;
		try {
			fieldsList=new ArrayList<Field>();
			list = new ArrayList<Field>();
			list = this.mapper.queryByCondition(field);
			if(list!=null&&list.size()>0){
				if(0==ConstantUtil.user.getIsAll()){
					//不可查看所有域
					//查询   用户权限 内 可以 查看的域（用户-域的关联表）
					userFieldList = new ArrayList<UserField>();
					userFieldMapper = this.sqlSession.getMapper(UserFieldMapper.class);
					userFieldList=userFieldMapper.queryByUserInst(ConstantUtil.user);
					for(UserField userField:userFieldList){
						//遍历，将域的 ID  加入 List
						fiedIdList.add(userField.getField_id());
					}
					for(Field fields:list){
						// 是否包含
						if(fiedIdList.contains(fields.getId())){
							
							fieldsList.add(fields);
						}
					}
				}else{
					// 用户已经选中查看所有域
					fieldsList.addAll(list);
				}
			
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return list;
		
	}
	/**
	 *  下拉列表
	 * @return
	 */
	public List<Field> comboboxList() {
		List<Field> list = null;
		List<UserField> userFieldList=null;
		UserFieldMapper userFieldMapper = null;
		List<Integer> fiedIdList=new ArrayList<Integer>();
		List<Field> fieldsList=null;
		try {
			fieldsList=new ArrayList<Field>();
			list = new ArrayList<Field>();
			list = this.mapper.comboboxList();
			if(list!=null&&list.size()>0){
				/**
				 * 判断 用户 是否有 查看所有域权限
				 */
				if(0==ConstantUtil.user.getIsAll()){
					//查询   用户权限 内 可以 查看的域（用户-域的关联表）
					userFieldMapper = this.sqlSession.getMapper(UserFieldMapper.class);
					userFieldList=userFieldMapper.queryByUserInst(ConstantUtil.user);
					for(UserField userField:userFieldList){
						//遍历，将域的 ID  加入 List
						fiedIdList.add(userField.getField_id());
					}
					for(Field fields:list){
						// 是否包含
						if(fiedIdList.contains(fields.getId())){
							
							fieldsList.add(fields);
						}
					}
				}else{
					fieldsList.addAll(list);
				}
				
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return fieldsList;
	}
	/**
	 * 新建 、修改域
	 * @param field 域
	 * @throws Exception
	 */
	public void saveOrUpdate(Field field) throws Exception {
		if (field == null) {
			throw new Exception("field is null");
		}
		if(field.getId()>0){
			this.mapper.update(field);
			this.sqlSession.commit();
		}else{
			this.mapper.insert(field,"subnet");
			this.sqlSession.commit();
		}
	}
	/**
	 * 
	 * @param afterName 修改前名称
	 * @param beforeName 修改后名称
	 * @param siteId  网元ID
	 * @return
	 */
	public boolean nameRepetition(String afterName, String beforeName, int siteId) {
		int result = 0;
		try {
			result = this.mapper.query_name(afterName, beforeName, siteId);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		if(0== result){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 判断是否有子网元
	 * @param field  域
	 * @return
	 */
	public boolean isSingle(Field field) {
		int result = 0;
		if (null == field) {
			try {
				throw new Exception("field is null");
			} catch (Exception e) {
				ExceptionManage.dispose(e,this.getClass());
			}
		}
		SiteInstMapper siteInstMapper =null;
		try {	
			siteInstMapper =this.sqlSession.getMapper(SiteInstMapper.class);
			result = siteInstMapper.selectFieldIsSingle(field);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		if(0== result){
			return false;			
		}else{
			return true;
		}
	}
	/**
	 * 删除域
	 * @param field 域
	 * @return
	 */
	public boolean delete(Field field) {
		int result = 0;
		try {
			result=this.mapper.delete(field);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		if(0== result){
			return false;
		}else{
			return true;
		}
		
	}
	/**
	 * 域下的子网
	 * @param id 域ID
	 * @return
	 */
	public List<Field> subnetCombo(String id) {
		List<Field> list = new ArrayList<Field>();
		list = this.mapper.subnetCombo(id);
		return list;
	}
	/**
	 *  查询子网 和 所属网元
	 * @param searchField  查询的子网
	 * @param connection
	 * @return
	 */
	public List<Field> querySiteByCondition(Field searchField){
		List<SiteInst> SiteInstList = null;  //网元List
		List<Field> subnetList = new ArrayList<Field>(); 
		SiteInstMapper siteInstMapper = this.sqlSession.getMapper(SiteInstMapper.class);//子网List

		SiteInst siteInst = new SiteInst();		
		try {
			subnetList = this.mapper.querySiteByCondition(searchField);
			for(Field field :subnetList){
				siteInst.setFieldID(field.getId());
				SiteInstList = new ArrayList<SiteInst>();
				SiteInstList = siteInstMapper.queryByCondition(siteInst);
				field.setSiteInstList(SiteInstList);
			}
			
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			SiteInstList = null;
			siteInstMapper  = null;
			siteInst = null;		

		}
		return subnetList;
	}
	
	public Field siteParentType(SiteInst siteInst) {
		Field field = new Field();
		field = this.mapper.getSiteParent(siteInst);
		return field;
	}
	 public List<Field> select_SBN_north()
	  {
	    return this.mapper.select_SBN_north();
	  }
}
