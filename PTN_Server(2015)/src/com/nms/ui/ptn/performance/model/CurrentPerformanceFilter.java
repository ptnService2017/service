package com.nms.ui.ptn.performance.model;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.enums.EMonitorCycle;
import com.nms.db.enums.EObjectType;

public class CurrentPerformanceFilter implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 328945626576264677L;
	public CurrentPerformanceFilter() {
		siteInsts = new ArrayList<Integer>();
		slotInsts = new ArrayList<Integer>();
		site_slotInsts = new ArrayList<String>();
		capabilityIdList = new ArrayList<Integer>();
		capabilityNameList = new ArrayList<String>();
	}
	//监控板卡:需要 网元ID+盘地址号
	private List<String> site_slotInsts;
	//监控类型
	private EObjectType objectType;
	//监控网元
	private List<Integer> siteInsts;
	//监控板卡
	private List<Integer> slotInsts;
	//监控端口
	private List<Integer> portInsts;
	//性能类型表capability  数据库id集合
	private List<Integer> capabilityIdList;
	//性能类型表capability  数据库描述
	private List<String> capabilityNameList;
	private String typeStr;
	//监控周期
	private EMonitorCycle monitorCycle;
	//用于是否零值过滤
	private int filterZero;
	private String performanceOverTime;
	private String performanceMonitorTime;//  性能监控  时间
	private int cardAddress;
	private int performanceCount;//性能个数
	private int performanceBeginCount;//性能起始个数
	private long performanceBeginDataTime;//性能查询时间
	private int performanceType;//性能数据类型
	
	private String objectName;////添加历史性能过滤条件 ： 端口名称标识  
	
	


	
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public int getPerformanceCount() {
		return performanceCount;
	}
	
	public String getPerformanceOverTime() {
		return performanceOverTime;
	}

	public void setPerformanceOverTime(String performanceStartTime) {
		this.performanceOverTime = performanceStartTime;
	}

	public void setPerformanceCount(int performanceCount) {
		this.performanceCount = performanceCount;
	}
	public int getPerformanceBeginCount() {
		return performanceBeginCount;
	}
	public void setPerformanceBeginCount(int performanceBeginCount) {
		this.performanceBeginCount = performanceBeginCount;
	}
	public List<Integer> getSiteInsts() {
		return siteInsts;
	}
	
	public String getPerformanceMonitorTime() {
		return performanceMonitorTime;
	}
	public void setPerformanceMonitorTime(String performanceMonitorTime) {
		this.performanceMonitorTime = performanceMonitorTime;
	}
	public void setSiteInsts(List<Integer> siteInsts) {
		this.siteInsts = siteInsts;
	}
	public List<Integer> getSlotInsts() {
		return slotInsts;
	}
	public void setSlotInsts(List<Integer> cardInsts) {
		this.slotInsts = cardInsts;
	}
	public void setPortInsts(List<Integer> portInsts) {
		this.portInsts = portInsts;
	}

	public List<Integer> getPortInsts() {
		return portInsts;
	}

	public List<Integer> getCapabilityIdList() {
		return capabilityIdList;
	}
	public void setCapabilityIdList(List<Integer> capabilityIdList) {
		this.capabilityIdList = capabilityIdList;
	}
	public EMonitorCycle getMonitorCycle() {
		return monitorCycle;
	}
	public void setMonitorCycle(EMonitorCycle monitorCycle) {
		this.monitorCycle = monitorCycle;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public EObjectType getObjectType() {
		return objectType;
	}
	public void setObjectType(EObjectType objectType) {
		this.objectType = objectType;
	}
	public int getCardAddress() {
		return cardAddress;
	}
	public void setCardAddress(int cardAddress) {
		this.cardAddress = cardAddress;
	}
	public List<String> getSite_slotInsts() {
		return site_slotInsts;
	}
	public void setSite_slotInsts(List<String> siteSlotInsts) {
		site_slotInsts = siteSlotInsts;
	}
	public int getFilterZero() {
		return filterZero;
	}
	public void setFilterZero(int filterZero) {
		this.filterZero = filterZero;
	}
	public long getPerformanceBeginDataTime() {
		return performanceBeginDataTime;
	}
	public void setPerformanceBeginDataTime(long performanceBeginDataTime) {
		this.performanceBeginDataTime = performanceBeginDataTime;
	}
	public int getPerformanceType() {
		return performanceType;
	}
	public void setPerformanceType(int performanceType) {
		this.performanceType = performanceType;
	}

	public List<String> getCapabilityNameList() {
		return capabilityNameList;
	}

	public void setCapabilityNameList(List<String> capabilityNameList) {
		this.capabilityNameList = capabilityNameList;
	}
	
}
