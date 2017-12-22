package com.nms.ui.ptn.alarm.model;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.db.enums.EObjectType;
/**
 * 当前告警过滤条件类
 * @author lp
 *
 */
public class CurrentAlarmFilter {
	// 监控网元id列表
	private List<SiteInst> siteInsts;
	// 监控板卡
	private List<SlotInst> slotInsts;
	//查询类型：网元、网元+槽位
	private EObjectType objectType;
	//告警级别
	private List<Integer> alarmLevel;
	//告警名称
	private List<String> alarmTypeList;
	//告警状态
	private String alarmState;
    //告警发生时间
	private String happenTime;
	private String happenEndTime;
	//告警的清除时间
	private String ClearTime;
	private String ClearEndTime;
	//告警类型
	private int warningtype;
	//告警确认时间
	private String ensureTime;
	private String ensureEndTime;
	// 确认用户
    private String ensureUser;	
	//下列属性在查询历史告警时需要用到
    private List<Integer> alarmCodeList = new ArrayList<Integer>();
    
    private int objectType_db;//从数据库里获取值，在service层将type_db转换成枚举类型的type
    public List<Integer> getAlarmCodeList() {
		return alarmCodeList;
	}

	public void setAlarmCodeList(List<Integer> alarmCodeList) {
		this.alarmCodeList = alarmCodeList;
	}
	
	public List<Integer> getAlarmLevel()
	{
		return alarmLevel;
	}

	public void setAlarmLevel(List<Integer> alarmLevel)
	{
		this.alarmLevel = alarmLevel;
	}

	public CurrentAlarmFilter() {
		siteInsts = new ArrayList<SiteInst>();
		slotInsts = new ArrayList<SlotInst>();
	}

	public EObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(EObjectType objectType) {
		this.objectType = objectType;
	}

	public List<SiteInst> getSiteInsts() {
		return siteInsts;
	}

	public void setSiteInsts(List<SiteInst> siteInsts) {
		this.siteInsts = siteInsts;
	}

	public List<SlotInst> getSlotInsts() {
		return slotInsts;
	}

	public void setSlotInsts(List<SlotInst> slotInsts) {
		this.slotInsts = slotInsts;
	}

	public List<String> getAlarmTypeList() {
		return alarmTypeList;
	}

	public void setAlarmTypeList(List<String> alarmTypeList) {
		this.alarmTypeList = alarmTypeList;
	}

	public String getAlarmState() {
		return alarmState;
	}

	public void setAlarmState(String alarmState) {
		this.alarmState = alarmState;
	}

	public String getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(String happenTime) {
		this.happenTime = happenTime;
	}

	public String getClearTime() {
		return ClearTime;
	}

	public void setClearTime(String clearTime) {
		ClearTime = clearTime;
	}

	public int getWarningtype() {
		return warningtype;
	}

	public void setWarningtype(int warningtype) {
		this.warningtype = warningtype;
	}

	public String getEnsureTime() {
		return ensureTime;
	}

	public void setEnsureTime(String ensureTime) {
		this.ensureTime = ensureTime;
	}

	public String getEnsureUser() {
		return ensureUser;
	}

	public void setEnsureUser(String ensureUser) {
		this.ensureUser = ensureUser;
	}

	public String getHappenEndTime() {
		return happenEndTime;
	}

	public void setHappenEndTime(String happenEndTime) {
		this.happenEndTime = happenEndTime;
	}

	public String getClearEndTime() {
		return ClearEndTime;
	}

	public void setClearEndTime(String clearEndTime) {
		ClearEndTime = clearEndTime;
	}

	public String getEnsureEndTime() {
		return ensureEndTime;
	}

	public void setEnsureEndTime(String ensureEndTime) {
		this.ensureEndTime = ensureEndTime;
	}

	public int getObjectType_db() {
		return objectType_db;
	}

	public void setObjectType_db(int objectTypeDb) {
		objectType_db = objectTypeDb;
	}

}
