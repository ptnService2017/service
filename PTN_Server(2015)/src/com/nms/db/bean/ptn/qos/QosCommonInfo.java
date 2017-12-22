package com.nms.db.bean.ptn.qos;

import com.nms.ui.frame.ViewDataObj;


public class QosCommonInfo extends ViewDataObj{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2270749275065165699L;
	private int id;
	private String qosType;
	private int seq;
	private int cos;
	private String direction;
	private int cir;
	private int cbs;
	private int eir;
	private int ebs;
	private int colorSence;
	private int pir;
	private int pbs;
	private int strategy;
	private String qosname;
	
	public String toString(){
		StringBuffer sb=new StringBuffer().append(" id=").append(id)
		.append(" ;qosType=").append(qosType).append(" ;seq=").append(seq)
		.append(" ;cos=").append(cos).append(" ;direction=").append(direction)
		.append(" ;cir=").append(cir).append(" ;cbs=").append(cbs)
		.append(" ;eir=").append(eir).append(" ;ebs=").append(ebs)
		.append(" ;colorSence=").append(colorSence).append(" ;pir=").append(pir)
		.append(" ;pbs=").append(pbs).append(" ;strategy=").append(strategy)
		.append(" ;qosname=").append(qosname);
		return  sb.toString();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQosType() {
		return qosType;
	}

	public void setQosType(String qosType) {
		this.qosType = qosType;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getCos() {
		return cos;
	}

	public void setCos(int cos) {
		this.cos = cos;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getCir() {
		return cir;
	}

	public void setCir(int cir) {
		this.cir = cir;
	}

	public int getCbs() {
		return cbs;
	}

	public void setCbs(int cbs) {
		this.cbs = cbs;
	}

	public int getEir() {
		return eir;
	}

	public void setEir(int eir) {
		this.eir = eir;
	}

	public int getEbs() {
		return ebs;
	}

	public void setEbs(int ebs) {
		this.ebs = ebs;
	}

	public int getColorSence() {
		return colorSence;
	}

	public void setColorSence(int colorSence) {
		this.colorSence = colorSence;
	}

	public int getPir() {
		return pir;
	}

	public void setPir(int pir) {
		this.pir = pir;
	}

	public int getPbs() {
		return pbs;
	}

	public void setPbs(int pbs) {
		this.pbs = pbs;
	}

	public int getStrategy() {
		return strategy;
	}

	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}

	public String getQosname() {
		return qosname;
	}
	public void setQosname(String qosname) {
		this.qosname = qosname;
	}
	@Override
	public void putObjectProperty() {
		 
		
	}
}
