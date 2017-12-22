package com.nms.model.ptn.oam;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.ptn.oam.OamEthernetInfo;
import com.nms.db.dao.ptn.oam.OamEthernetInfoMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ExceptionManage;

public class OamEthreNetService_MB extends ObjectService_Mybatis {
	private OamEthernetInfoMapper mapper = null;
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	public void setMapper(OamEthernetInfoMapper mapper1) {
		this.mapper = sqlSession.getMapper(OamEthernetInfoMapper.class);
	}
	
	/**
	 * 查询
	 * @param oamEthernetInfo 单查询
	 * @return 
	 * @throws Exception
	 */
	public List<OamEthernetInfo> queryByNeIDSide(OamEthernetInfo oamEthernetInfo) throws Exception {
		if (oamEthernetInfo == null) {
			throw new Exception("oamEthernetInfo is null");
		}
		List<OamEthernetInfo> ccnList = null;
		try {
			ccnList = this.mapper.queryOamLinkInfoByConditionSide(oamEthernetInfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return ccnList;
	}

	public List<OamEthernetInfo> queryByNeID(OamEthernetInfo oamEthernetInfo) throws Exception {
		if (oamEthernetInfo == null) {
			throw new Exception("oamEthernetInfo is null");
		}
		List<OamEthernetInfo> ccnList = new ArrayList<OamEthernetInfo>();
		try {
			ccnList = this.mapper.queryOamLinkInfoByCondition(oamEthernetInfo);
			if(ccnList == null){
				return new ArrayList<OamEthernetInfo>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return ccnList;
	}

	public int delete(OamEthernetInfo oamEthernetInfo) throws Exception {
		if (oamEthernetInfo == null) {
			throw new Exception("oamEthernetInfo is null");
		}
		int ccnId = 1;
		try {
			ccnId = this.mapper.delete(oamEthernetInfo);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return ccnId;
	}

	public int insert(OamEthernetInfo oamEthernetInfo) throws Exception {
		if (oamEthernetInfo == null) {
			throw new Exception("oamEthernetInfo is null");
		}
		int ccnId = 1;
		List<Integer> allItemNumber=null;
		List<Integer> itemNumberFromDB=null; 
		try {
			itemNumberFromDB=new ArrayList<Integer>();
			allItemNumber=new ArrayList<Integer>();
			for(int i=0;i<6;i++){
				allItemNumber.add(i+1);
			}
			itemNumberFromDB=this.count(oamEthernetInfo);
			allItemNumber.removeAll(itemNumberFromDB);
			if(allItemNumber!=null&&allItemNumber.size()>0){
				oamEthernetInfo.setItemNumber(allItemNumber.get(0));
			}
			this.mapper.insert(oamEthernetInfo);
			ccnId = oamEthernetInfo.getId();
			oamEthernetInfo.setId(ccnId);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return ccnId;
	}
	
	 private List<Integer> count(OamEthernetInfo oamInfo) throws Exception{
			if (oamInfo == null) {
				throw new Exception("oamEthernetInfo is null");
			}
			List<Integer> countresult = new ArrayList<Integer>();
			try {
				countresult = this.mapper.count(oamInfo);
				if(countresult == null){
					return new ArrayList<Integer>();
				}
			} catch (Exception e) {
				ExceptionManage.dispose(e,this.getClass());
			}
			return countresult;
		}

	public int deleteBySiteId(int siteId) {
		int ccnId = 1;
		try {
			ccnId = this.mapper.deleteBySiteId(siteId);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return ccnId;
	}

	public int update(OamEthernetInfo oamEthernetInfo) throws Exception {
		if (oamEthernetInfo == null) {
			throw new Exception("oamEthernetInfo is null");
		}
		int id = 0;
		try {
			id = this.mapper.update(oamEthernetInfo);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return id;
	}  
	
	public void saveOrUpdate(OamEthernetInfo oamInfo) throws Exception{
		
		if (oamInfo == null) {
			throw new Exception("oamEthernetInfo is null");
		}
		try {
			
			if(!checkOamMepIsExist(oamInfo)){
				 this.mapper.insert(oamInfo);
			}else{
				this.mapper.update(oamInfo);
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
		}
	}

	/*
	 * 判断该ethOam是否存在
	 */
	public boolean checkOamMepIsExist(OamEthernetInfo oamInfo) {
		List<OamEthernetInfo> list=null;
		try {
			list = this.queryByNeID(oamInfo);
			if (list!=null&&list.size()>0) {
				return true;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
		}
		return false;
	}
}
