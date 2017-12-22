package com.nms.model.ptn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.ptn.Businessid;
import com.nms.db.dao.ptn.BusinessidMapper;
import com.nms.model.ptn.oam.OamInfoService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.ServiceFactory;
import com.nms.model.util.Services;
import com.nms.ui.manager.BusinessIdUtil;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.xmlbean.BusinessidXml;
import com.nms.util.Mybatis_DBManager;

public class BusinessidService_MB extends ObjectService_Mybatis {
	private BusinessidMapper mapper = null;

	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}

	public BusinessidMapper getMapper() {
		return mapper;
	}

	public void setMapper(BusinessidMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 通过网元ID,类型查询可用ID
	 * 
	 * @param siteId
	 * @param type
	 * @return
	 */
	public Businessid select(int siteId, String type, int businessId) {
		Businessid businessid = null;
		try {
			businessid = new Businessid();
			businessid.setSiteId(siteId);
			businessid.setType(type);
			businessid.setIdValue(businessId);
			businessid.setIdStatus(0);
			businessid = this.mapper.query(businessid).get(0);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return businessid;
	}

	/**
	 * 修改状态
	 * 
	 * @throws Exception
	 */
	public void update(int id, int idStatus) throws Exception {
		Businessid businessid = null;
		try {
			businessid = new Businessid();
			businessid.setId(id);
			businessid.setIdStatus(idStatus);
			this.mapper.update(businessid);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	/**
	 * 批量插入id值
	 * 
	 * @param siteId
	 *            网元id
	 * @param type
	 *            类型（pw,lsp,tunnel,eline）
	 * @param begin
	 *            id起始值
	 * @param end
	 *            id最大值
	 * @throws Exception
	 */
	public void save(SiteInst siteInst) throws Exception {

		List<BusinessidXml> businessidXmlList = null;
		String fileName = null;
		try {
			if (siteInst.getCellType().equals("700D")) {
				fileName = "businessid_700d.xml";
			} else if (siteInst.getCellType().equals("700B")) {
				fileName = "businessid_700b.xml";
			} else if (siteInst.getCellType().equals("710A") || siteInst.getCellType().equals("710") 
					|| siteInst.getCellType().equals("CSG T5000")|| siteInst.getCellType().equals("ETN-5000")) {
				fileName = "businessid_710.xml";
			} else if (siteInst.getCellType().equals("700E")) {
				fileName = "businessid_20a.xml";
			} else if (siteInst.getCellType().equals("703A")) {
				fileName = "businessid_703.xml";
			} else if (siteInst.getCellType().equals("703B")) {
				fileName = "businessid_703b.xml";
			} else if (siteInst.getCellType().equals("710B")) {
				fileName = "businessid_710b.xml";
			} else if (siteInst.getCellType().equals("703B2")) {
				fileName = "businessid_703b.xml";
			} else if (siteInst.getCellType().equals("703C-1A") || siteInst.getCellType().equals("703C-2A")) {
				fileName = "businessid_703c.xml";
			} else if (siteInst.getCellType().equals("700A")) {
				fileName = "businessid_700a.xml";
			} else if (siteInst.getCellType().equals("700C")) {
				fileName = "businessid_700c.xml";
			} else if (siteInst.getCellType().equals("610A")) {
				fileName = "businessid_610a.xml";
			} else if (siteInst.getCellType().equals("ZXWT CTN280") || siteInst.getCellType().equals("PAS100/200")) {
				fileName = "businessid_710.xml";
			} else if (siteInst.getCellType().equals("ZXWT CTN180") || siteInst.getCellType().equals("IPA500") || siteInst.getCellType().contains("ZXCTN")) {
				fileName = "businessid_703b.xml";
			} else if (siteInst.getCellType().equals("703-2C") ||siteInst.getCellType().equals("703-1A") 
					|| siteInst.getCellType().equals("703-2A") || siteInst.getCellType().equals("703-4A") 
					|| siteInst.getCellType().equals("703-1") || siteInst.getCellType().equals("703-2") 
					|| siteInst.getCellType().equals("703-3") || siteInst.getCellType().equals("703-4") 
					|| siteInst.getCellType().equals("703-5") || siteInst.getCellType().equals("703-6") 
					|| siteInst.getCellType().equals("703-7") || siteInst.getCellType().equals("ETN-200-204E")
					|| siteInst.getCellType().equals("ETN-200-204") || siteInst.getCellType().equals("703-6A")) {
				fileName = "businessid_703c-4a.xml";
			}
			BusinessIdUtil businessIdUtil = new BusinessIdUtil();
			businessidXmlList = businessIdUtil.getList(fileName);
			for (BusinessidXml businessidXml : businessidXmlList) {
				for (int i = businessidXml.getBegin(); i <= businessidXml.getEnd(); i++) {
					this.mapper.insert(siteInst.getSite_Inst_Id(), businessidXml.getType(), i, 0);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			businessidXmlList = null;
			fileName = null;
		}
	}
	
	/**
	 * 根据网元ID删除
	 * @param siteId 网元id
	 * @throws Exception
	 */
	public void delete(int siteId) throws Exception{
		try {
			this.mapper.delete(siteId);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	/**
	 * 查询可用业务id
	 * @param siteId 网元id
	 * @param type 类型
	 * @return 业务id对象
	 * @throws Exception 
	 */
	public Businessid select(int siteId,String type) throws Exception{
		Businessid businessid=null;
		List<Businessid> businessids = null;
		try {
			businessids=this.mapper.queryUseBySiteIDType(siteId, type,0);
			if(businessids != null && businessids.size()>0){
				businessid = businessids.get(0);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return businessid;
	}
	
	/**
	 * 通过网元和值查询
	 * @param idvalue  值
	 * @param siteid 网元id
	 * @return 业务id对象
	 * @throws Exception 
	 */
	public Businessid select(int idvalue,int siteid,String type) throws Exception{
		Businessid businessid=null;
		try {
			businessid=this.mapper.queryByIdValueSiteIdtype(idvalue,siteid,type);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return businessid;
	}
	
	/**
	 * 根据网元ID集合查询出此网元下所有相同的环ID
	
	* @author kk
	
	* @param   
	
	* @return 
	
	* @Exception 异常对象
	 */
	public int select_site(List<Integer> siteIds,String type) throws Exception{
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("siteIds", siteIds);
		hashMap.put("type", type);
		List<Integer> integers = mapper.queryBySite(hashMap);
		return integers.get(0);
	}
	
	/**
	 * 根据网元id初始化该网元所有businessId
	 * @param siteId
	 * @throws SQLException
	 */
	public void initializtionSite(int siteId) throws SQLException{
		try {
			this.mapper.initializtionSite(siteId);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	/**
	 * 根据网元和类型获取可用的businessId个数
	 * @param siteId 网元ID
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	public int getBusinessCount(int siteId,String type) throws Exception{
		List<Businessid> businessIdList=null;
		int result=0;
		try {
			businessIdList=this.mapper.queryList(siteId, type);
			if(null != businessIdList){
				result=businessIdList.size();
			}
		} catch (Exception e) {
			throw e;
		} finally{
			businessIdList=null;
		}
		return result;
	}
	
	/**
	 * 通过条件查询
	 * @param siteId 网元id
	 * @param type 类型
	 * @return 业务id对象
	 * @throws Exception 
	 */
	public List<Businessid> selectAllByTypeForCondition(int siteId,String type,List<Integer> condition) throws Exception{
		List<Businessid> businessid=null;
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("siteId", siteId);
		hashMap.put("type", type);
		hashMap.put("condition", condition);
		try {
			businessid=this.mapper.queryListForCondition(hashMap);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return businessid;
	}
	
	/**
	 * 修改状态
	 * @throws Exception 
	 */
	public void updateBusinessid(Businessid businessId) throws Exception{
		try {
			this.mapper.updateBusinessid(businessId);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	/**
	 * 根据条件去查询
	 */
	public List<Businessid> queryByCondition(Businessid bId){
		try {
			return this.mapper.queryByCondition(bId);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return new ArrayList<Businessid>();
	}

	

	/**
	 * 修改状态
	 * @throws Exception 
	 */
	public void updateBusinessidForSite(Businessid businessId) throws Exception{
		try {
			this.mapper.updateBusinessidForSite(businessId);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	

	/**
	 * 查询该网元下已用的业务id
	 * @throws Exception 
	 */
	public int select(int siteId) throws Exception {
		try {
			return this.mapper.queryUsedIdCount(siteId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	public static void main(String[] args) {
		try {
			Mybatis_DBManager.init("10.18.1.10");
			ConstantUtil.serviceFactory = new ServiceFactory();
			BusinessidService_MB businessidServiceMB = (BusinessidService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BUSINESSID);
			List<Integer> siteIds = new ArrayList<Integer>();
			siteIds.add(9);
			siteIds.add(10);
			siteIds.add(11);
			System.out.println(businessidServiceMB.select_site(siteIds, "ring"));
		} catch (Exception e) {
			ExceptionManage.dispose(e, OamInfoService_MB.class);
		}
		
	}
}
