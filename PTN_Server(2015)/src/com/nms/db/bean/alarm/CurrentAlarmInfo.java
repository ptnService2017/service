package com.nms.db.bean.alarm;

import twaver.AlarmSeverity;

import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysObj;
import com.nms.ui.manager.keys.StringKeysTip;
import com.nms.ui.ptn.alarm.AlarmTools;

/**
 * 当前告警数据类
 * @author 
 *
 */
public class CurrentAlarmInfo extends AlarmInfo {

	private static final long serialVersionUID = 1L;
	//网元名称
	private String siteName;
	//槽位的数据库id
	private int slotId;
	private String ackUser;   //确认人
	private String alarmDesc;  //告警描述
	private String warningNote; //产生告警的可能原因
	private int warningType;   //告警类型
	private WarningLevel warningLevel;
	private int warningLevel_temp;
	private String commentS;//区别父类的comments
	private int capabilityCode;	//性能code
	private String capabilityIdentity;	//性能标识，标识是15分钟或24小时的 上限或下限 
	private int labelAlarmStatus ; //用来标记 设备上报的告警是 主动告警 还是报的告警消失 1:代表主动告警  0:道标被动告警
	private String isCleared;    //标记是否已经清除
	
	public String getIsCleared()
	{
		return isCleared;
	}

	public void setIsCleared(String isCleared)
	{
		this.isCleared = isCleared;
		if(this.getIsCleared()!=null && this.getIsCleared().equals(StringKeysTip.TIP_CLEARED))
		{
			this.putClientProperty("isCleared", ResourceUtil.srcStr(StringKeysTip.TIP_CLEARED));
		}
		else
		{
			this.putClientProperty("isCleared", ResourceUtil.srcStr(StringKeysTip.TIP_UNCLEARED));
		}
		
	}

	public String getAlarmComments()
	{
		return commentS;
	}

	public String getCommentS() {
		return commentS;
	}

	public void setCommentS(String comments)
	{
		this.commentS = comments;
		this.putClientProperty("remarks", this.getAlarmComments());
	}

	public WarningLevel getWarningLevel() {
		return warningLevel;
	}

	public void setWarningLevel(WarningLevel warningLevel) {
		this.warningLevel = warningLevel;
	}
	public String getSiteName() {
		if(null == siteName || siteName.equals("null"))
		{
			return this.getObjectName();
		}else
		{
			return siteName;
		}
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public int getSlotId() {
		return slotId;
	}

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	public String getAckUser() {
		return ackUser;
	}

	public void setAckUser(String ackUser) {
		this.ackUser = ackUser;
		this.putClientProperty("ackUser", this.getAckUser());
	}

	public String getAlarmDesc() {
		return alarmDesc;
	}

	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}

	public String getWarningNote() {
		return warningNote;
	}

	public void setWarningNote(String warningNote) {
		this.warningNote = warningNote;
	}

	public int getWarningType() {
		return warningType;
	}

	public void setWarningType(int warningType) {
		this.warningType = warningType;
	}
	
	public int getCapabilityCode() {
		return capabilityCode;
	}

	public void setCapabilityCode(int capabilityCode) {
		this.capabilityCode = capabilityCode;
	}

	public String getCapabilityIdentity() {
		return capabilityIdentity;
	}

	public void setCapabilityIdentity(String capabilityIdentity) {
		this.capabilityIdentity = capabilityIdentity;
	}

	public int getWarningLevel_temp() {
		return warningLevel_temp;
	}
	
	public void setWarningLevel_temp(int warningLevelTemp) {
		warningLevel_temp = warningLevelTemp;
//		if(this.getClearedTime()!=null)
//		{
//			this.putClientProperty("alarmSeverity", getAlarmSeverity(0));
//		}
	}

	public int getLabelAlarmStatus() {
		return labelAlarmStatus;
	}

	public void setLabelAlarmStatus(int labelAlarmStatus) {
		this.labelAlarmStatus = labelAlarmStatus;
	}

	public void putClientProperty() {
		this.putClientProperty("obj", this);
		if(this.getClearedTime()!=null)
		{
			this.putClientProperty("alarmSeverity", getAlarmSeverity(0));
		}else{
			this.putClientProperty("alarmSeverity", getAlarmSeverity(this.getWarningLevel_temp()));
		}
		this.putClientProperty("siteName", getSiteName());
		this.putClientProperty("alarmSource", getObjectType().toString()+"/"+this.getObjectName());
		this.putClientProperty("alarmDesc", this.getAlarmDesc());		
		if(this.getWarningLevel() != null){
			AlarmTools alarmTools =new AlarmTools();
			this.putClientProperty("warningTypes",alarmTools.getAlarmType(getWarningLevel().getWarningtype()));
			this.putClientProperty("warningNotes", this.getWarningLevel().getWarningname());
		}
	}
	
	public AlarmSeverity getAlarmSeverity(int value) {
		AlarmSeverity.WARNING.setDisplayName(ResourceUtil.srcStr(StringKeysObj.ALARMSEVERITY_WARNING));
		AlarmSeverity type = null;
		switch (value) {
		case 0:
			type = AlarmSeverity.CLEARED;
			break;
		case 1:
			type = AlarmSeverity.INDETERMINATE;
			break;
		case 2:
			type = AlarmSeverity.WARNING;
			break;
		case 3:
			type = AlarmSeverity.MINOR;
			break;
		case 4:
			type = AlarmSeverity.MAJOR;
			break;
		case 5:
			type = AlarmSeverity.CRITICAL;
			break;
		default:
			type = null;
			break;
		}
		return type;
	}
}
