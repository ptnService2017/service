package com.nms.drivechenxiao.service.bean.qos.eelsp;

import com.nms.drivechenxiao.service.bean.qos.QosObject;

public class EELSPQosObject extends QosObject {
	
	public EELSPQosObject(String type){
		super.setQosType(type);
	}

	private String name = "";// 名称 **
	private QosCSObject cs6 = new QosCSObject();
	private QosCSObject cs7 = new QosCSObject();
	private QosBEObject be = new QosBEObject();
	private QosAFObject af1 = new QosAFObject();
	private QosAFObject af2 = new QosAFObject();
	private QosEFObject ef = new QosEFObject();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QosCSObject getCs6() {
		return cs6;
	}

	public void setCs6(QosCSObject cs6) {
		this.cs6 = cs6;
	}

	public QosCSObject getCs7() {
		return cs7;
	}

	public void setCs7(QosCSObject cs7) {
		this.cs7 = cs7;
	}

	public QosBEObject getBe() {
		return be;
	}

	public void setBe(QosBEObject be) {
		this.be = be;
	}

	public QosAFObject getAf1() {
		return af1;
	}

	public void setAf1(QosAFObject af1) {
		this.af1 = af1;
	}

	public QosAFObject getAf2() {
		return af2;
	}

	public void setAf2(QosAFObject af2) {
		this.af2 = af2;
	}

	public QosEFObject getEf() {
		return ef;
	}

	public void setEf(QosEFObject ef) {
		this.ef = ef;
	}

}
