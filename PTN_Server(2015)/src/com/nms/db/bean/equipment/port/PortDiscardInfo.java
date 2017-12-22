package com.nms.db.bean.equipment.port;

import java.io.Serializable;

public class PortDiscardInfo implements Serializable{
	private static final long serialVersionUID = -8387263385828829442L;
	private int id;
	private int siteId;
	private int portLine1;//所选端口bit0/bit1/…/bit7=port1/port2/…port8
	private int portLine2;//所选端口bit0/bit1/…/bit7=port9/port10/…port16
	private int portLine3;//所选端口bit0/bit1/…/bit7=port17/port10/…port24
	private int portLine4;//所选端口bit0/bit1/…/bit7=port25/port26/…
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
	public int getPortLine1() {
		return portLine1;
	}
	public void setPortLine1(int portLine1) {
		this.portLine1 = portLine1;
	}
	public int getPortLine2() {
		return portLine2;
	}
	public void setPortLine2(int portLine2) {
		this.portLine2 = portLine2;
	}
	public int getPortLine3() {
		return portLine3;
	}
	public void setPortLine3(int portLine3) {
		this.portLine3 = portLine3;
	}
	public int getPortLine4() {
		return portLine4;
	}
	public void setPortLine4(int portLine4) {
		this.portLine4 = portLine4;
	}
	
}
