package com.nms.model.system;



import org.apache.ibatis.session.SqlSession;

import com.nms.db.dao.system.PerformanceRamInfoMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.ptn.performance.model.PerformanceRAMInfo;

public class PerformanceRamService_MB extends ObjectService_Mybatis{
	
   private PerformanceRamInfoMapper mapper = null;
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	public PerformanceRamInfoMapper getMapper() {
		return mapper;
	}

	public void setMapper(PerformanceRamInfoMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 批量插入
	 * @param performanceRAMInfo
	 * @throws Exception 
	 */
	public void save(PerformanceRAMInfo performanceRAMInfo) throws Exception{
		try {
			if(performanceRAMInfo.getId()>0){
				update(performanceRAMInfo);
			}else{
				mapper.insert(performanceRAMInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
		}
	}
	/**
	 * 修改
	 * @param performanceRAMInfo
	 * @throws Exception 
	 */
	public void update(PerformanceRAMInfo performanceRAMInfo) throws Exception{
		try {
			mapper.updateByPrimaryKey(performanceRAMInfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
		}
	}
	
	/**
	 * 查詢
	 * @param performanceRAMInfo
	 * @throws Exception 
	 */
	public PerformanceRAMInfo  select(String userName) throws Exception{
		PerformanceRAMInfo performanceRAMInfo = null;
		try {
			performanceRAMInfo = new PerformanceRAMInfo();
			performanceRAMInfo = mapper.queryByCondition(userName);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
		}
		return performanceRAMInfo;
	}
}
