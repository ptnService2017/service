package com.nms.model.system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.system.UnLoading;
import com.nms.db.dao.system.TranferInfoMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.ptn.systemManage.bean.TranferInfo;

/**
 * 数据转储
 * 提取sql集合和ID集合
 * @author sy
 *
 */
public class TranferService_Mb extends ObjectService_Mybatis{
	
	private TranferInfoMapper  tranferMapper=null;

	public TranferInfoMapper getMapper() {
		return tranferMapper;
	}

	public void setMapper(TranferInfoMapper tranferMapper) {
		this.tranferMapper = tranferMapper;
	}

	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	/**
	 * 根据table名称。 获取table表的个属性的 类型
	 * 
	 * @param tableName
	 *            表名
	 * @return typeList    类型集合
	 * @throws SQLException
	 */

	private  List<String> getTableBeans(String tableName) throws Exception {		
		List<String> typeList = null;
		try {		
			typeList=new ArrayList<String>();
			typeList=this.tranferMapper.tableColumnType(tableName);
		} catch (Exception e) {
			throw e;
		} 
		return typeList;
	}
	/**
	 * 根据unloadType,判断为转储哪个表
	 * @param unload
	 * @param count
	 * 			数据转储的 数目  
	 * @return  sqlList
	 * 			 sql语句的集合
	 * @throws Exception
	 */
	public  List<TranferInfo> getDataStr(UnLoading unload,int count) throws Exception {
		
		ResultSet resultSet = null;
		StringBuffer stringBuffer =null;
		PreparedStatement preparedStatement = null;
		String sql = null;
		String label = "";
		String tableName=null;
		String byTime=null;
		List<String> typeList=null;
		List<TranferInfo> tranferInfoList=null;
		TranferInfo tranferInfo=null;
		String tableDesc = "";
		
		try {
			tranferInfoList=new ArrayList<TranferInfo>();
			stringBuffer = new StringBuffer();
			if(1==unload.getUnloadType()){
				//告警
				tableName="history_alarm";
				byTime="happenedtime";
			}else if(2==unload.getUnloadType()){
				// 性能
				tableName="history_performance";
				byTime="performancetime";
			}else if(3 == unload.getUnloadType()){
				// 日志
				tableName="operation_log";
				byTime="startTime";
			}else if(4 == unload.getUnloadType()){
				// 登录日志
				tableName="login_log";
				byTime="startTime";
			}
			tableDesc = getTableDesc(tableName).substring(1);
			sql = "select * from " + tableName +" limit 0,?";
			typeList=getTableBeans(tableName);
			preparedStatement = this.sqlSession.getConnection().prepareStatement(sql);
			preparedStatement.setInt(1, count);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				tranferInfo=new TranferInfo();
				stringBuffer=new StringBuffer();
				stringBuffer.append("insert into " + tableName +"("+tableDesc+") values (");
				// 遍历列集合，取索引
				for (int i = 2; i <= typeList.size(); i++) {
					// 如果是第一次循环。前方不加","
					if (i == 2) {
						label = "";
					} else {
						label = ",";
					}
					// 如果值为null 直接存入NULL 而不是'null'
					if (null != resultSet.getObject(i)) {
						stringBuffer.append(label + "'" + resultSet.getObject(i) + "'");
						
					} else {
						stringBuffer.append(label + "null");
					}
				}
				stringBuffer.append(");\n");
				tranferInfo.setId(resultSet.getInt("id"));
				tranferInfo.setSql(stringBuffer.toString());
				tranferInfoList.add(tranferInfo);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {
					ExceptionManage.dispose(e,this.getClass());
				}finally{
					resultSet = null;
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (Exception e) {
					ExceptionManage.dispose(e,this.getClass());
				}finally{
					preparedStatement = null;
				}
			}
			sql = null;
			tableName=null;
			byTime=null;
			stringBuffer =null;
			tranferInfo=null;
			tableDesc = null;
		}
		return tranferInfoList;
		
	}
	/**
	 * 获取表的结构
	 * @param tableName 表名称
	 * @return
	 */
	private String getTableDesc(String tableName) {
		List<String> typeList = null;
		String table = "";
		try {
			typeList=new ArrayList<String>();
			typeList=this.tranferMapper.tableColumnName(tableName);
			if(typeList!=null && typeList.size()>0){
				for(int i=0;i<typeList.size();i++){
					if(!"".equals(typeList.get(i)) && !"id".equals(typeList.get(i))){
						table +=","+typeList.get(i);
					}
				}
			}
		
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return table;
	}
}
