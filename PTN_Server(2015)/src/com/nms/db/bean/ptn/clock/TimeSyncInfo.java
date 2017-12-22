package com.nms.db.bean.ptn.clock;

import java.util.ArrayList;
import java.util.List;

import com.nms.ui.frame.ViewDataObj;

public class TimeSyncInfo extends ViewDataObj{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id =0;
	private int ptpModel=0;//PTP模式
	private int clockModel=0;//时钟模型
	private int srcclockModel=0;//源时钟模式
	private String srcclockId="0";//源时钟ID
	private int srcclocktype=0;//源时钟类型
	private String srcclockId1="0";//源时钟ID
	private String srcclockId2="0";//源时钟ID
	private String srcclockId3="0";//源时钟ID
	private String srcclockId4="0";//源时钟ID
	private int srcclockpri1=0;
	private int srcclockpri2=0;
	private int srcclockLevel=0;//源时钟等级
	private int srcclockaccuray=0;//源时钟精度
	private int SlaveOnlyModel=0;//SlaveOnly 模式
	private int inCompensation=0;//输入时延补偿属性
	private int inDelay=0; //输入时延补偿值
	private int outCompensation=0;  //输出时延补偿属性
	private int outDelay=0;    //输出时延补偿值
	private int SynCicle=0;  //同步周期
	private int NoteCicle=0;  //通告周期
	private int TimeInfoIt=0;  //时间信息接口
	private int ptpNum=0;    //PTP端口条目数
	private int siteId=0;
	private List<PtpPortinfo> ptpPortlist = new ArrayList<PtpPortinfo>();
	private int syncFreq=0;  //Sync报文发包频率
	private int delayFreq=0; //Delay_Req报文发包频率
	private int announceFreq=0;  //Announce报文发包频率
	private int delayOver=0;    //Delay_Req 报文超时系数
	private int announceOver=0;  //Announce报文超时系数
	private int domainNumbe=0;   //DomainNumber
	
	
	public int getId() {
		return id;
	}
	/**
	 * @return the srcclockId4
	 */
	public String getSrcclockId4() {
		return srcclockId4;
	}
	/**
	 * @param srcclockId4 the srcclockId4 to set
	 */
	public void setSrcclockId4(String srcclockId4) {
		this.srcclockId4 = srcclockId4;
	}
	/**
	 * @return the srcclockId3
	 */
	public String getSrcclockId3() {
		return srcclockId3;
	}
	/**
	 * @param srcclockId3 the srcclockId3 to set
	 */
	public void setSrcclockId3(String srcclockId3) {
		this.srcclockId3 = srcclockId3;
	}
	/**
	 * @return the srcclockId2
	 */
	public String getSrcclockId2() {
		return srcclockId2;
	}
	/**
	 * @param srcclockId2 the srcclockId2 to set
	 */
	public void setSrcclockId2(String srcclockId2) {
		this.srcclockId2 = srcclockId2;
	}
	/**
	 * @return the srcclockId1
	 */
	public String getSrcclockId1() {
		return srcclockId1;
	}
	/**
	 * @param srcclockId1 the srcclockId1 to set
	 */
	public void setSrcclockId1(String srcclockId1) {
		this.srcclockId1 = srcclockId1;
	}
	/**
	 * @return the srcclockId1
	 */

	public void setId(int id) {
		this.id = id;
	}

	public List<PtpPortinfo> getPtpPortlist() {
		return ptpPortlist;
	}
	public void setPtpPortlist(List<PtpPortinfo> ptpPortlist) {
		this.ptpPortlist = ptpPortlist;
	}
	public int getSyncFreq() {
		return syncFreq;
	}
	public void setSyncFreq(int syncFreq) {
		this.syncFreq = syncFreq;
	}
	public int getDelayFreq() {
		return delayFreq;
	}
	public void setDelayFreq(int delayFreq) {
		this.delayFreq = delayFreq;
	}
	public int getAnnounceFreq() {
		return announceFreq;
	}
	public void setAnnounceFreq(int announceFreq) {
		this.announceFreq = announceFreq;
	}
	public int getDelayOver() {
		return delayOver;
	}
	public void setDelayOver(int delayOver) {
		this.delayOver = delayOver;
	}
	public int getAnnounceOver() {
		return announceOver;
	}
	public void setAnnounceOver(int announceOver) {
		this.announceOver = announceOver;
	}
	public int getDomainNumbe() {
		return domainNumbe;
	}
	public void setDomainNumbe(int domainNumbe) {
		this.domainNumbe = domainNumbe;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public int getPtpModel() {
		return ptpModel;
	}
	public void setPtpModel(int ptpModel) {
		this.ptpModel = ptpModel;
	}
	public int getClockModel() {
		return clockModel;
	}
	public void setClockModel(int clockModel) {
		this.clockModel = clockModel;
	}
	public int getSrcclockModel() {
		return srcclockModel;
	}
	public void setSrcclockModel(int srcclockModel) {
		this.srcclockModel = srcclockModel;
	}
	public String getSrcclockId() {
		return srcclockId;
	}
	public void setSrcclockId(String srcclockId) {
		this.srcclockId = srcclockId;
	}
	public int getSrcclocktype() {
		return srcclocktype;
	}
	public void setSrcclocktype(int srcclocktype) {
		this.srcclocktype = srcclocktype;
	}
	public int getSrcclockpri1() {
		return srcclockpri1;
	}
	public void setSrcclockpri1(int srcclockpri1) {
		this.srcclockpri1 = srcclockpri1;
	}
	public int getSrcclockpri2() {
		return srcclockpri2;
	}
	public void setSrcclockpri2(int srcclockpri2) {
		this.srcclockpri2 = srcclockpri2;
	}
	public int getSrcclockLevel() {
		return srcclockLevel;
	}
	public void setSrcclockLevel(int srcclockLevel) {
		this.srcclockLevel = srcclockLevel;
	}
	public int getSrcclockaccuray() {
		return srcclockaccuray;
	}
	public void setSrcclockaccuray(int srcclockaccuray) {
		this.srcclockaccuray = srcclockaccuray;
	}
	public int getSlaveOnlyModel() {
		return SlaveOnlyModel;
	}
	public void setSlaveOnlyModel(int slaveOnlyModel) {
		SlaveOnlyModel = slaveOnlyModel;
	}
	public int getInCompensation() {
		return inCompensation;
	}
	public void setInCompensation(int inCompensation) {
		this.inCompensation = inCompensation;
	}
	public int getInDelay() {
		return inDelay;
	}
	public void setInDelay(int inDelay) {
		this.inDelay = inDelay;
	}
	public int getOutCompensation() {
		return outCompensation;
	}
	public void setOutCompensation(int outCompensation) {
		this.outCompensation = outCompensation;
	}
	public int getOutDelay() {
		return outDelay;
	}
	public void setOutDelay(int outDelay) {
		this.outDelay = outDelay;
	}
	public int getSynCicle() {
		return SynCicle;
	}
	public void setSynCicle(int synCicle) {
		SynCicle = synCicle;
	}
	public int getNoteCicle() {
		return NoteCicle;
	}
	public void setNoteCicle(int noteCicle) {
		NoteCicle = noteCicle;
	}
	public int getTimeInfoIt() {
		return TimeInfoIt;
	}
	public void setTimeInfoIt(int timeInfoIt) {
		TimeInfoIt = timeInfoIt;
	}
	public int getPtpNum() {
		return ptpNum;
	}
	public void setPtpNum(int ptpNum) {
		this.ptpNum = ptpNum;
	}

	@Override
	public void putObjectProperty() {
		// TODO Auto-generated method stub
		
	}
}
