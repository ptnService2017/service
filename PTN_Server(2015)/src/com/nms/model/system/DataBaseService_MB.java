package com.nms.model.system;

import java.sql.DatabaseMetaData;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.ptn.DbInfoTask;
import com.nms.db.bean.system.DataBaseInfo;
import com.nms.db.dao.system.PtndbInstMapper;
import com.nms.db.dao.system.PtndbInstPathMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ExceptionManage;

public class DataBaseService_MB extends ObjectService_Mybatis{
	
   private PtndbInstMapper ptndbInstMapper = null;
 
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	public PtndbInstMapper getMapper() {
		return ptndbInstMapper;
	}

	public void setMapper(PtndbInstMapper ptndbInstMapper) {
		this.ptndbInstMapper = ptndbInstMapper;
	}
	
	/**
	 * 查询数据库的基本信息
	 * @param tableName 数据库名称
	 * @param label 1:查询数据库  2:查询数据库表
	 * @return
	 * @throws Exception
	 */
	public DataBaseInfo selectTableInfo(String tableName,int label)throws Exception{
		DataBaseInfo dataBaseInfo = new DataBaseInfo();
		try {
			dataBaseInfo = this.ptndbInstMapper.slectTableInfo(tableName, label);
			if(label == 2 ){
				dataBaseInfo.setCountSize(this.ptndbInstMapper.countTableInfo(tableName));
			}
			dataBaseInfo.setName(tableName);
			dataBaseInfo.setMointorLevel(1);
			DecimalFormat df = new DecimalFormat("######0.00");
			if(dataBaseInfo.getCountSize() == 0 && label == 1){
				double countSize = dataBaseInfo.getDataSize()+dataBaseInfo.getFreeSize()+dataBaseInfo.getIndexSize();
				dataBaseInfo.setCountSize(Double.parseDouble(df.format(countSize)));
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return dataBaseInfo;
	}
	
	public DataBaseInfo slectDataInfo() throws Exception{
		DataBaseInfo dataBaseInfo = new DataBaseInfo();
		try {
			DatabaseMetaData dbMetaData = this.sqlSession.getConnection().getMetaData();
			dataBaseInfo.setProductName(dbMetaData.getDatabaseProductName());
			dataBaseInfo.setProductVersion(dbMetaData.getDatabaseProductVersion());
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return dataBaseInfo;
	}

	/**
	 * 查询数据库的监控的基本信息
	 * @param tableName 数据库名称
	 * @param mointorType 1:监控内存
	 * @return
	 * @throws Exception
	 */
	public DbInfoTask selectMoinTableInfo(String tableName, String mointorType)throws Exception{
		DataBaseInfo dataBaseInfo = new DataBaseInfo();
		DbInfoTask dbInfoTask = null;
		try {
			if(mointorType.equals("1")){
				dataBaseInfo = this.ptndbInstMapper.slectTableInfo(tableName, 1);
			}
			PtndbInstPathMapper ptndbInstPathMapper = this.sqlSession.getMapper(PtndbInstPathMapper.class);
			List<DbInfoTask> taskList = ptndbInstPathMapper.selectDbTask(mointorType);
			if(taskList != null && taskList.size() > 0){
				dbInfoTask = ptndbInstPathMapper.selectDbTask(mointorType).get(0);
			}else{
				dbInfoTask = new DbInfoTask();
			}
			dbInfoTask.setMointorType(mointorType);
			if("true".equalsIgnoreCase(dbInfoTask.getMointorTypeDb_bool())){
				dbInfoTask.setMointorTypeDb(true);
			}else{
				dbInfoTask.setMointorTypeDb(false);
			}
			if("true".equalsIgnoreCase(dbInfoTask.getIsMointorTotal_bool())){
				dbInfoTask.setMointorTotal(true);
			}else{
				dbInfoTask.setMointorTotal(false);
			}
			List<DataBaseInfo> dbInfoList = this.ptndbInstMapper.selectDbInfo(dbInfoTask.getId());
			if(dbInfoList == null || dbInfoList.size() == 0){
				dbInfoList = new ArrayList<DataBaseInfo>();
				dbInfoList.add(dataBaseInfo);
			}
			dbInfoTask.setDaTableList(dbInfoList);
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return dbInfoTask;
	}
	
	/**
	 * 插入或更新数据库的监控的基本信息
	 * @param tableName 数据库名称
	 * @param label 1:查询数据库  2:查询数据库表
	 * @return
	 * @throws Exception
	 */
	public void saveOrUpdate(DbInfoTask dbInfoTask)throws Exception{
		try {
			PtndbInstPathMapper ptndbInstPathMapper = this.sqlSession.getMapper(PtndbInstPathMapper.class);
			if(dbInfoTask.isMointorTypeDb()){
				dbInfoTask.setMointorTypeDb_bool("true");
			}else{
				dbInfoTask.setMointorTypeDb_bool("false");
			}
			if(dbInfoTask.isMointorTotal()){
				dbInfoTask.setMointorTypeDb_bool("true");
			}else{
				dbInfoTask.setMointorTypeDb_bool("false");
			}
			if(dbInfoTask.getId() >0)
			{
				ptndbInstPathMapper.update(dbInfoTask);
				List<DataBaseInfo> dataInfoList = dbInfoTask.getDaTableList();
				if(dataInfoList != null && dataInfoList.size()>0){
					for (DataBaseInfo dataBaseInfo : dataInfoList) {
						this.ptndbInstMapper.update(dataBaseInfo);
					}
				}
			}else
			{
				ptndbInstPathMapper.insert(dbInfoTask);
				List<DataBaseInfo> dataInfoList = dbInfoTask.getDaTableList();
				if(dataInfoList != null && dataInfoList.size()>0){
					for (DataBaseInfo dataBaseInfo : dataInfoList) {
						dataBaseInfo.setPathId(dbInfoTask.getId());
						this.ptndbInstMapper.insert(dataBaseInfo);
					}
				}
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
	}
}
