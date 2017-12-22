package com.nms.db.bean.equipment.port;


import com.nms.db.enums.EManagerStatus;
import com.nms.db.enums.EManufacturer;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.util.Services;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class E1Info extends ViewDataObj {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2056149586549162578L;
	private int id;
	private int siteId; // --(端口)CIR
	/* 武汉E1属性 */
	private int e1Id;// e1id
	private int e1LegOutPutClockSource = 0;// E1支路输出时钟源选择 00/01/02/03/04/05/06/07/08/09/10/11/12/13/14/15=支路1/支路2/支路3/支路4/支路5/支路6/支路7/支路8/支路9/支路10/支路11/支路12/支路13/支路14/支路15/支路16)
	private int tdmClockSource = 0;// TDM时钟源 (01)/02/03=(自适应时钟恢复)/差分时钟恢复/网络侧增强型自适应恢复时钟
	private int rtpEnable = 0;// RTP使能(00)/01=(不使能)/使能
	private int legShield = 0;// 2M支路屏蔽(00)/01=(不屏蔽)/屏蔽
	private int legEnable = 0;// 2M支路使能00/(01)= 不使能/(使能)
	private int prestoreTimeEnable = 0;// 缓存时间控制使能(00)/01=(不使能)/使能
	private int prestoreTime = 0;// 缓存时间 4(取值范围3~7) 单位：毫秒
	private int pinCount = 0;// 封装帧个数4(取值范围1/2/4/8)
	private int pwLabel;// pw标签
	private int legId;// 支路id
	private int isAlarmRevesal = 1;//1:未使能   2:使能
	
    private int fzType=0;//封装类型
	private int frameformat=0;//成帧格式
    private int complexFrame=0;//复帧格式
    
	public int getComplexFrame() {
	return complexFrame;
}

public void setComplexFrame(int complexFrame) {
	this.complexFrame = complexFrame;
}

	public int getFrameformat() {
		return frameformat;
	}

	public void setFrameformat(int frameformat) {
		this.frameformat = frameformat;
	}

	/* 晨晓E1属性 */
	private int portId;// 端口id
	private String portName;// 端口名称
	private String model = "E1";// 模式
	private String linecoding ;// 线路编码  HDB3=0 ; 2=ami
	private int impedance = 75;// 阻抗
	private PortInst portInst;
	private int cardId;
	public E1Info(){
		try {
			this.setLinecoding(UiUtil.getCodeByValue("LINECODE", "0").getId()+"");
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSiteId() {
		return siteId;
	}
    
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getE1LegOutPutClockSource() {
		return e1LegOutPutClockSource;
	}

	public void setE1LegOutPutClockSource(int e1LegOutPutClockSource) {
		this.e1LegOutPutClockSource = e1LegOutPutClockSource;
	}

	public int getTdmClockSource() {
		return tdmClockSource;
	}

	public void setTdmClockSource(int tdmClockSource) {
		this.tdmClockSource = tdmClockSource;
	}

	public int getRtpEnable() {
		return rtpEnable;
	}

	public void setRtpEnable(int rtpEnable) {
		this.rtpEnable = rtpEnable;
	}
	public int getFzType() {
		return fzType;
	}

	public void setFzType(int fzType) {
		this.fzType = fzType;
	}
	
	public int getLegShield() {
		return legShield;
	}

	public void setLegShield(int legShield) {
		this.legShield = legShield;
	}

	public int getLegEnable() {
		return legEnable;
	}

	public void setLegEnable(int legEnable) {
		this.legEnable = legEnable;
	}

	public int getPrestoreTimeEnable() {
		return prestoreTimeEnable;
	}

	public void setPrestoreTimeEnable(int prestoreTimeEnable) {
		this.prestoreTimeEnable = prestoreTimeEnable;
	}

	public int getPrestoreTime() {
		return prestoreTime;
	}

	public void setPrestoreTime(int prestoreTime) {
		this.prestoreTime = prestoreTime;
	}

	public int getPinCount() {
		return pinCount;
	}

	public void setPinCount(int pinCount) {
		this.pinCount = pinCount;
	}

	public int getPwLabel() {
		return pwLabel;
	}

	public void setPwLabel(int pwLabel) {
		this.pwLabel = pwLabel;
	}

	public int getLegId() {
		return legId;
	}

	public void setLegId(int legId) {
		this.legId = legId;
	}

	public int getPortId() {
		return portId;
	}

	public void setPortId(int portId) {
		this.portId = portId;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLinecoding() {
		return linecoding;
	}

	public void setLinecoding(String linecoding) {
		this.linecoding = linecoding;
	}

	public int getImpedance() {
		return impedance;
	}

	public void setImpedance(int impedance) {
		this.impedance = impedance;
	}

	public int getE1Id() {
		return e1Id;
	}

	public void setE1Id(int e1Id) {
		this.e1Id = e1Id;
	}

	public PortInst getPortInst() {
		return portInst;
	}

	public void setPortInst(PortInst portInst) {
		this.portInst = portInst;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putObjectProperty() {
		String manageStatus = null;
		SiteService_MB siteService=null;
		
		try {	
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
//			if (this.getPortInst().getIsEnabled_code()== 0) {
				manageStatus = EManagerStatus.forms(this.getPortInst().getIsEnabled_code()).toString();
//			} else {
//				manageStatus = EManagerStatus.forms(value)
//			}

			if (siteService.getManufacturer(this.getSiteId())== EManufacturer.WUHAN.getValue()) {
				getClientProperties().put("id", getId());
				getClientProperties().put("legId", this.legId);
				getClientProperties().put("legShield", this.legShield);
				getClientProperties().put("legEnable", this.legEnable);
				getClientProperties().put("prestoreTimeEnable", this.getPrestoreTimeEnable());
				getClientProperties().put("prestoreTime", this.getPrestoreTime());
				getClientProperties().put("pinCount", this.getPinCount());
				getClientProperties().put("pwLabel", this.getPwLabel());
			} else {
				getClientProperties().put("id", getId());
				getClientProperties().put("portName", this.portName);
				getClientProperties().put("model", this.getModel());
				getClientProperties().put("managerStatus", manageStatus);
				getClientProperties().put("jotStatus", super.getJobStatus(this.getPortInst().getJobStatus()));
				getClientProperties().put("linecoding", UiUtil.getCodeById(Integer.parseInt(this.getLinecoding())).getCodeName());
				getClientProperties().put("impedance", this.getImpedance());
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(siteService);
		}

	}

	public int getIsAlarmRevesal() {
		return isAlarmRevesal;
	}

	public void setIsAlarmRevesal(int isAlarmRevesal) {
		this.isAlarmRevesal = isAlarmRevesal;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

}
