package com.nms.db.bean.alarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nms.ui.ptn.alarm.model.AlarmLineInfo;

public class AlarmShieldInfo_t implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3331050307639196047L;
	private List<Integer> lineCountList = new ArrayList<Integer>();//单线路号集合
	private Map<Integer,Integer> areaCountList = new HashMap<Integer,Integer>();//区域线路号(开始区域 和结束区域)
	private int shieldAlarmCode = 0;//表示是否屏蔽列出的线路的所有告警代码，当值0x01,表示屏蔽，后面的配置数据部不处理，但保留。当值为0x00表示不屏蔽
                                    //屏蔽配置由后面的配置数据决定。
	//告警代码数
	private List<Integer> shieldAlarmCodeCountList = new ArrayList<Integer>();
	
	//日志
	private List<AlarmLineInfo> lineList = new ArrayList<AlarmLineInfo>();
	private List<AlarmLineInfo> alarmCodeList = new ArrayList<AlarmLineInfo>();
	
	public List<Integer> getLineCountList() {
		return lineCountList;
	}
	public void setLineCountList(List<Integer> lineCountList) {
		this.lineCountList = lineCountList;
	}
	public Map<Integer, Integer> getAreaCountList() {
		return areaCountList;
	}
	public void setAreaCountList(Map<Integer, Integer> areaCountList) {
		this.areaCountList = areaCountList;
	}
	public int getShieldAlarmCode() {
		return shieldAlarmCode;
	}
	public void setShieldAlarmCode(int shieldAlarmCode) {
		this.shieldAlarmCode = shieldAlarmCode;
	}
	public List<Integer> getShieldAlarmCodeCountList() {
		return shieldAlarmCodeCountList;
	}
	public void setShieldAlarmCodeCountList(List<Integer> shieldAlarmCodeCountList) {
		this.shieldAlarmCodeCountList = shieldAlarmCodeCountList;
	}
	public List<AlarmLineInfo> getLineList() {
		return lineList;
	}
	public void setLineList(List<AlarmLineInfo> lineList) {
		this.lineList = lineList;
	}
	public List<AlarmLineInfo> getAlarmCodeList() {
		return alarmCodeList;
	}
	public void setAlarmCodeList(List<AlarmLineInfo> alarmCodeList) {
		this.alarmCodeList = alarmCodeList;
	}
	
}
	
