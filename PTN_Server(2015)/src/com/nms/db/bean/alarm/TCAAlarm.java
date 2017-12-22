package com.nms.db.bean.alarm;

import com.nms.ui.frame.ViewDataObj;

/**
 * TCA告警bean 数据在current_alarm表。 关联capability得到数据
 * @author kk
 *
 */
public class TCAAlarm extends ViewDataObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;		//主键
	private int siteId;	//网元主键
	private String siteName;	//网元名称
	private String alarmSource;	//告警源
	private String performanceType;	//性能类型
	private String happenTime;	//发生时间
	private String EndTime;	//结束时间
	private String granularity;	//粒度
	private String threshold;	//门限
	private String remark_en;	//英文备注
	private String remark_zh;	//中文备注
	//查询条件
	private int capabilityCode;	//性能code
	private String capabilityIdentity;	//性能标识，标识是15分钟或24小时的 上限或下限 
	
	public String getRemark_en() {
		return remark_en;
	}

	public void setRemark_en(String remarkEn) {
		remark_en = remarkEn;
	}

	public String getRemark_zh() {
		return remark_zh;
	}

	public void setRemark_zh(String remarkZh) {
		remark_zh = remarkZh;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getAlarmSource() {
		return alarmSource;
	}

	public void setAlarmSource(String alarmSource) {
		this.alarmSource = alarmSource;
	}

	public String getPerformanceType() {
		return performanceType;
	}

	public void setPerformanceType(String performanceType) {
		this.performanceType = performanceType;
	}

	public String getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(String happenTime) {
		this.happenTime = happenTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
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

	@Override
	public void putObjectProperty() {
		this.putClientProperty("id", getId());
		this.putClientProperty("siteName", this.getSiteName());
		this.putClientProperty("alarmSource",  this.getAlarmSource());
		this.putClientProperty("performanceType", this.getPerformanceType());
		this.putClientProperty("happenTime", this.getHappenTime());
		this.putClientProperty("EndTime", this.getEndTime());
		this.putClientProperty("granularity",  this.getGranularity());
		this.putClientProperty("threshold",  this.getThreshold());
		this.putClientProperty("remark",  this.getRemark_en());
	}

}
