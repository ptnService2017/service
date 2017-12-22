package com.nms.db.bean.ptn;

import java.util.List;

import com.nms.db.bean.ptn.clock.FrequencySource;
import com.nms.db.bean.ptn.clock.FrequencyStatusInfo;

public class SiteStatusInfo implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 605046322513176560L;
	//状态信息
	private FrequencyStatusInfo frequencyStatusInfo ;
	private List<FrequencySource> frequencySources ;
	private List<PortStatus> portStatusList ;
	private List<ProtectStatus> protectStatusList ;
	private List<PwStatus> pwStatus ;
	private List<TunnelStatus> tunnelStatusList;
	private List<VpwsStatus> vpwsStatusList;
	private List<WrappingStatus> wrappingStatusList;
	private List<E1LegStatus> e1LegStatusList;
	private List<PwProtectStatus> pwProtectStatusList;
	private List<lagStatusInfo> lagStatusList;
	private List<PtpPortStatus> ptpPortStatusList;
	private List<PtpBasicStatus> ptpBasicStatusList;
	private List<BfdStatus> bfdStatusList;
	private List<ARPInfo> arpStatusList;
	public List<ARPInfo> getArpStatusList() {
		return arpStatusList;
	}
	public void setArpStatusList(List<ARPInfo> arpStatusList) {
		this.arpStatusList = arpStatusList;
	}
	public List<BfdStatus> getBfdStatusList() {
		return bfdStatusList;
	}
	public void setBfdStatusList(List<BfdStatus> bfdStatusList) {
		this.bfdStatusList = bfdStatusList;
	}
	public List<PtpBasicStatus> getPtpBasicStatusList() {
		return ptpBasicStatusList;
	}
	public void setPtpBaiscStatusList(List<PtpBasicStatus> ptpBasicStatusList) {
		this.ptpBasicStatusList = ptpBasicStatusList;
	}
	public List<PtpPortStatus> getPtpPortStatusList() {
		return ptpPortStatusList;
	}
	public void setPtpPortStatusList(List<PtpPortStatus> ptpPortStatusList) {
		this.ptpPortStatusList = ptpPortStatusList;
	}
	
	public List<lagStatusInfo> getLagStatusList() {
		return lagStatusList;
	}
	public void setLagStatusList(List<lagStatusInfo> lagStatusList) {
		this.lagStatusList = lagStatusList;
	}
	public List<PwProtectStatus> getPwProtectStatusList() {
		return pwProtectStatusList;
	}
	public void setPwProtectStatusList(List<PwProtectStatus> pwProtectStatusList) {
		this.pwProtectStatusList = pwProtectStatusList;
	}
	public List<WrappingStatus> getWrappingStatusList() {
		return wrappingStatusList;
	}
	public void setWrappingStatusList(List<WrappingStatus> wrappingStatusList) {
		this.wrappingStatusList = wrappingStatusList;
	}
	public FrequencyStatusInfo getFrequencyStatusInfo() {
		return frequencyStatusInfo;
	}
	public void setFrequencyStatusInfo(FrequencyStatusInfo frequencyStatusInfo) {
		this.frequencyStatusInfo = frequencyStatusInfo;
	}
	public List<FrequencySource> getFrequencySources() {
		return frequencySources;
	}
	public void setFrequencySources(List<FrequencySource> frequencySources) {
		this.frequencySources = frequencySources;
	}
	public List<PortStatus> getPortStatusList() {
		return portStatusList;
	}
	public void setPortStatusList(List<PortStatus> portStatusList) {
		this.portStatusList = portStatusList;
	}
	public List<ProtectStatus> getProtectStatusList() {
		return protectStatusList;
	}
	public void setProtectStatusList(List<ProtectStatus> protectStatusList) {
		this.protectStatusList = protectStatusList;
	}
	public List<PwStatus> getPwStatus() {
		return pwStatus;
	}
	public void setPwStatus(List<PwStatus> pwStatus) {
		this.pwStatus = pwStatus;
	}
	public List<TunnelStatus> getTunnelStatusList() {
		return tunnelStatusList;
	}
	public void setTunnelStatusList(List<TunnelStatus> tunnelStatusList) {
		this.tunnelStatusList = tunnelStatusList;
	}
	public List<VpwsStatus> getVpwsStatusList() {
		return vpwsStatusList;
	}
	public void setVpwsStatusList(List<VpwsStatus> vpwsStatusList) {
		this.vpwsStatusList = vpwsStatusList;
	}
	public List<E1LegStatus> getE1LegStatusList() {
		return e1LegStatusList;
	}
	public void setE1LegStatusList(List<E1LegStatus> e1LegStatusList) {
		this.e1LegStatusList = e1LegStatusList;
	}
	
}
