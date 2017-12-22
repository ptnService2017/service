package com.nms.model.equipment.port;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.equipment.port.PortAttr;
import com.nms.db.dao.equipment.port.PortAttrMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ExceptionManage;

public class PortAttrService_MB extends ObjectService_Mybatis {
	private PortAttrMapper mapper = null;
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	public PortAttrMapper getMapper() {
		return mapper;
	}

	public void setMapper(PortAttrMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 根据端口ID查询对象
	 */
	public PortAttr select_portId(int portId) throws Exception {
		PortAttr portAttr = null;
		try {
			portAttr = new PortAttr();
			portAttr.setPortId(portId);
			List<PortAttr> portAttrList = this.mapper.queryByCondition(portAttr);
			if (null != portAttrList && portAttrList.size() == 1) {
				portAttr = portAttrList.get(0);
			} else {
				portAttr = new PortAttr();
			}
		} catch (Exception e) {
			throw e;
		}
		return portAttr;
	}
	
	/**
	 * 根据条件查询portattr
	 * 
	 * @param portAttrCondition
	 *            查询条件
	 * @return portattr集合
	 * @throws Exception
	 */
	public Map<Integer, PortAttr> selectForMap(PortAttr portattr) throws Exception {
		Map<Integer, PortAttr> map = null;
		List<PortAttr> portAttrList = null;
		try {
			map = new HashMap<Integer, PortAttr>();
			portAttrList = mapper.queryByCondition(portattr);

			for (PortAttr pa : portAttrList) {
				// map.put(pa.getAttrId(), pa);
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			portAttrList = null;
		}
		return map;
	}
}
