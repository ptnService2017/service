﻿package com.nms.db.dao.equipment.port;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.equipment.port.PortInst;

public interface PortInstMapper {
	int deleteByPrimaryKey(Integer portid);

	int insertSelective(PortInst record);

	PortInst selectByPrimaryKey(Integer portid);

	int updateByPrimaryKeySelective(PortInst record);

	int updateByPrimaryKey(PortInst record);

	public List<PortInst> queryByCondition(PortInst portinst);

	List<PortInst> quertyChildPort(int portId);

	public int update(PortInst portinst);

	/**
	 * 通过端口ID查询
	 * 
	 * @param portid
	 * @return
	 */
	public PortInst selectPortybyid(@Param("portid") Integer portid);

	public int insert(@Param("portInst") PortInst portInst);

	/**
	 * 通过ID和siteid来查询所有的端口
	 * 
	 * @param ids
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public List<PortInst> quertyAllPortByIdsAndSiteId(@Param("ids") List<Integer> ids, @Param("siteId") int siteId);

	List<PortInst> quertyNNIPortbySiteId(int siteId);
	
	public List<PortInst> quertyUniOrE1PortbySiteId(@Param("siteId")int siteId,@Param("type")String type);

	/**
	 * 通过ID来查询所有的端口
	 * @param ids
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public List<String> quertyAllPortNameByNumber(@Param("ids")List<Integer> ids,@Param("siteId")int siteId);

	
	public List<PortInst> queryByIsOccupy(PortInst portinstCondition);


	public List<PortInst> quertyPortbySiteandName(int siteId, String portName);
	
	public int updateStatus(@Param("siteId")int siteId,@Param("status")int status,@Param("portType")String portType);

	public int update_synchro(PortInst portinst);

	
	public int selectBySiteIdAndPortId(@Param("siteId")int siteId,@Param("portId")int portId);
	
	public int selectBySiteIdAndNumber(@Param("siteId")int siteId,@Param("number")int number);


	void updatestatus(PortInst portInst);

	void updateOccupyByIdList(@Param("pdhportList")List<Integer> pdhportList, @Param("isused")int isused);
	
	public int deleteBySiteId(@Param("siteId")int siteId);
	
	public int delete(PortInst portInst);
	
	List<PortInst> selectAlarmReversal();

}