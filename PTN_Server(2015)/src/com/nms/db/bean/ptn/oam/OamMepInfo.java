package com.nms.db.bean.ptn.oam;

/*
 * ETH OAM--CCM LPB TST 三种测试帧
 * TMP TMC TMS OAM--LPB TST 
 * */
@SuppressWarnings("serial")
public class OamMepInfo extends OamCommonInfo {

	private int localMepId; // 源MEP ID:0-1-8191 界面10进制显示(字节26是数据高位)
	private int remoteMepId; // 对等MEP ID:0-1-8191 界面10进制显示(字节28是数据高位)
	private int mel; // 等级
	private int reserve1;// 备用
	private int reserve2;// 备用
	/*
	 * ETH TMP(lsp) TMC(pw)
	 */
	private boolean lm; // 丢包率 LM帧发送使能: 0/1=不使能/使能
	private int lmCycle; // 发送周期 0/1=1s/100ms
	private int lmReserve1;// 备用
	private boolean dm; // 延时帧发送使能 0/1=不使能/使能
	private int dmCycle; // 发送周期 0/1=1s/100ms
	private int dmReserve1; // 备用
	private boolean csfEnable;//csf使能
	private boolean lck; // 锁定帧使能 0/1=不使能/使能
	// -----------------------------------------------------
	private boolean ringEnable;// 环回发送使能 0/1=不使能/使能
	private int ringCycle;// 环回发送周期
	private int ringTestWay;// 环回测试方式
	private int offLineTestTLV;// 离线测试TLV
	private int ringTLVLength;// 环回tlv长度
	private int ringTLVInfo;// 环回TLV测试内容
	private boolean tstEnable;// tst发送使能 0/1=不使能/使能
	private int tstCycle;// tst发送周期
	private int tstTLVType;// tstTLV类型
	private int tstTLVLength;// tstTLV长度
	
	/* ETH */
	private int lpbOutTime;// 环回超时时间 1-100 (单位:50ms)
	private String targetMacAdd; // 目的Mac地址 0X00 00 00 00 00 01

	/* TMP */
	private boolean fdi;
	private int reserve3; // 备用
	private int reserve4; // 备用
	private int reserve5; // 备用

	private int lspTc;
	private int pwTc;

	/* TMS */
	private boolean cv; // 连通性检测帧使能 0/1=不使能/使能
	private int cvCycle;// 检测周期 001/010/011/100=3.33ms/10ms/100ms/1s
	private int cvReserve1;// 备用
	private boolean aps;// aps帧使能 0/1=不使能/使能
	private boolean ssm;// ssm帧使能 0/1=不使能/使能
	private boolean sccTest;// scc帧使能0/1=不使能/使能

	private int inPwLabel;
	private int outPwLabel;

	private int ltEnable;//lt 使能
	private int ltEXP;//lt EXP
	private int ltTTL;//lt TTL
	private int lbTTL;//lb TTL
	
	//段层oam特有属性
	private int vlanEnable;
	private int outVlanValue;
	private int tpId;
	private String sourceMac;
	private String endMac;
	private boolean oamEnable;
	private int dmlength;
	
	public int getOutVlanValue() {
		return outVlanValue;
	}

	public void setOutVlanValue(int outVlanValue) {
		this.outVlanValue = outVlanValue;
	}

	public int getLocalMepId() {
		return localMepId;
	}

	public void setLocalMepId(int localMepId) {
		this.localMepId = localMepId;
	}

	public int getRemoteMepId() {
		return remoteMepId;
	}

	public void setRemoteMepId(int remoteMepId) {
		this.remoteMepId = remoteMepId;
	}

	public int getMel() {
		return mel;
	}

	public void setMel(int mel) {
		this.mel = mel;
	}

	public boolean isLm() {
		return lm;
	}

	public void setLm(boolean lm) {
		this.lm = lm;
	}

	public int getLmCycle() {
		return lmCycle;
	}

	public void setLmCycle(int lmCycle) {
		this.lmCycle = lmCycle;
	}

	public boolean isDm() {
		return dm;
	}

	public void setDm(boolean dm) {
		this.dm = dm;
	}

	public int getDmCycle() {
		return dmCycle;
	}

	public void setDmCycle(int dmCycle) {
		this.dmCycle = dmCycle;
	}

	public boolean isLck() {
		return lck;
	}

	public void setLck(boolean lck) {
		this.lck = lck;
	}

	public int getLpbOutTime() {
		return lpbOutTime;
	}

	public void setLpbOutTime(int lpbOutTime) {
		this.lpbOutTime = lpbOutTime;
	}

	public String getTargetMacAdd() {
		return targetMacAdd;
	}

	public void setTargetMacAdd(String targetMacAdd) {
		this.targetMacAdd = targetMacAdd;
	}

	public boolean isCv() {
		return cv;
	}

	public void setCv(boolean cv) {
		this.cv = cv;
	}

	public int getCvCycle() {
		return cvCycle;
	}

	public void setCvCycle(int cvCycle) {
		this.cvCycle = cvCycle;
	}

	public boolean isAps() {
		return aps;
	}

	public void setAps(boolean aps) {
		this.aps = aps;
	}

	public boolean isSsm() {
		return ssm;
	}

	public void setSsm(boolean ssm) {
		this.ssm = ssm;
	}

	public boolean isSccTest() {
		return sccTest;
	}

	public void setSccTest(boolean scc) {
		this.sccTest = scc;
	}

	public boolean isFdi() {
		return fdi;
	}

	public void setFdi(boolean fdi) {
		this.fdi = fdi;
	}

	public int getReserve1() {
		return reserve1;
	}

	public void setReserve1(int reserve1) {
		this.reserve1 = reserve1;
	}

	public int getReserve2() {
		return reserve2;
	}

	public void setReserve2(int reserve2) {
		this.reserve2 = reserve2;
	}

	public int getLmReserve1() {
		return lmReserve1;
	}

	public void setLmReserve1(int lmReserve1) {
		this.lmReserve1 = lmReserve1;
	}

	public int getDmReserve1() {
		return dmReserve1;
	}

	public void setDmReserve1(int dmReserve1) {
		this.dmReserve1 = dmReserve1;
	}

	public int getReserve3() {
		return reserve3;
	}

	public void setReserve3(int reserve3) {
		this.reserve3 = reserve3;
	}

	public int getReserve4() {
		return reserve4;
	}

	public void setReserve4(int reserve4) {
		this.reserve4 = reserve4;
	}

	public int getReserve5() {
		return reserve5;
	}

	public void setReserve5(int reserve5) {
		this.reserve5 = reserve5;
	}

	public int getCvReserve1() {
		return cvReserve1;
	}

	public void setCvReserve1(int cvReserve1) {
		this.cvReserve1 = cvReserve1;
	}

	public boolean isRingEnable() {
		return ringEnable;
	}

	public void setRingEnable(boolean ringEnable) {
		this.ringEnable = ringEnable;
	}

	public int getRingCycle() {
		return ringCycle;
	}

	public void setRingCycle(int ringCycle) {
		this.ringCycle = ringCycle;
	}

	public int getRingTestWay() {
		return ringTestWay;
	}

	public void setRingTestWay(int ringTestWay) {
		this.ringTestWay = ringTestWay;
	}

	public int getOffLineTestTLV() {
		return offLineTestTLV;
	}

	public void setOffLineTestTLV(int offLineTestTLV) {
		this.offLineTestTLV = offLineTestTLV;
	}

	public int getRingTLVLength() {
		return ringTLVLength;
	}

	public void setRingTLVLength(int ringTLVLength) {
		this.ringTLVLength = ringTLVLength;
	}

	public int getRingTLVInfo() {
		return ringTLVInfo;
	}

	public void setRingTLVInfo(int ringTLVInfo) {
		this.ringTLVInfo = ringTLVInfo;
	}

	public boolean isTstEnable() {
		return tstEnable;
	}

	public void setTstEnable(boolean tstEnable) {
		this.tstEnable = tstEnable;
	}

	public int getTstCycle() {
		return tstCycle;
	}

	public void setTstCycle(int tstCycle) {
		this.tstCycle = tstCycle;
	}

	public int getTstTLVType() {
		return tstTLVType;
	}

	public void setTstTLVType(int tstTLVType) {
		this.tstTLVType = tstTLVType;
	}

	public int getTstTLVLength() {
		return tstTLVLength;
	}

	public void setTstTLVLength(int tstTLVLength) {
		this.tstTLVLength = tstTLVLength;
	}


	

	public int getLspTc() {
		return lspTc;
	}

	public void setLspTc(int lspTc) {
		this.lspTc = lspTc;
	}

	public int getPwTc() {
		return pwTc;
	}

	public void setPwTc(int pwTc) {
		this.pwTc = pwTc;
	}


	public int getInPwLabel() {
		return inPwLabel;
	}

	public void setInPwLabel(int inPwLabel) {
		this.inPwLabel = inPwLabel;
	}

	public int getOutPwLabel() {
		return outPwLabel;
	}

	public void setOutPwLabel(int outPwLabel) {
		this.outPwLabel = outPwLabel;
	}

	public int getLtEnable() {
		return ltEnable;
	}

	public void setLtEnable(int ltEnable) {
		this.ltEnable = ltEnable;
	}

	public int getLtEXP() {
		return ltEXP;
	}

	public void setLtEXP(int ltEXP) {
		this.ltEXP = ltEXP;
	}

	public int getLtTTL() {
		return ltTTL;
	}

	public void setLtTTL(int ltTTL) {
		this.ltTTL = ltTTL;
	}

	public int getLbTTL() {
		return lbTTL;
	}

	public void setLbTTL(int lbTTL) {
		this.lbTTL = lbTTL;
	}

	public boolean isCsfEnable() {
		return csfEnable;
	}

	public void setCsfEnable(boolean csfEnable) {
		this.csfEnable = csfEnable;
	}

	public int getVlanEnable() {
		return vlanEnable;
	}

	public void setVlanEnable(int vlanEnable) {
		this.vlanEnable = vlanEnable;
	}

	public int getTpId() {
		return tpId;
	}

	public void setTpId(int tpId) {
		this.tpId = tpId;
	}

	public String getSourceMac() {
		return sourceMac;
	}

	public void setSourceMac(String sourceMac) {
		this.sourceMac = sourceMac;
	}

	public String getEndMac() {
		return endMac;
	}

	public void setEndMac(String endMac) {
		this.endMac = endMac;
	}

	public boolean isOamEnable() {
		return oamEnable;
	}

	public void setOamEnable(boolean oamEnable) {
		this.oamEnable = oamEnable;
	}

	public int getDmlength() {
		return dmlength;
	}

	public void setDmlength(int dmlength) {
		this.dmlength = dmlength;
	}

	
}
