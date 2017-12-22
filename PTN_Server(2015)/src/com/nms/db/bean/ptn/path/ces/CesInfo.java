package com.nms.db.bean.ptn.path.ces;

import java.io.Serializable;
import java.util.List;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.port.PortStmTimeslot;
import com.nms.db.bean.ptn.path.ServiceInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.enums.EActiveStatus;
import com.nms.db.enums.ECesType;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysObj;

public class CesInfo extends ServiceInfo implements Serializable {

	private static final long serialVersionUID = -7476236167718837754L;
	// 业务上的a端单站id
	private int axcId;
	// 业务上的z端单站id
	private int zxcId;
	private int cestype;
	private PortInst beforeAPort;
	private PortInst beforeZPort;
	private PortStmTimeslot beforeAPortStmTime;
	private PortStmTimeslot beforeZPortStmTime;

	public int getAxcId() {
		return axcId;
	}

	public void setAxcId(int axcId) {
		this.axcId = axcId;
	}

	public int getZxcId() {
		return zxcId;
	}

	public void setZxcId(int zxcId) {
		this.zxcId = zxcId;
	}

	@Override
	public int getCestype() {
		return cestype;
	}

	@Override
	public void setCestype(int cestype) {
		this.cestype = cestype;
	}

	public CesInfo() {

	}

	public CesInfo(int id) {
		super.setId(id);
	}

	@Override
	public void putObjectProperty() {
		try {
			this.putClientProperty("id", getId());
			this.putClientProperty("cesName", getName());
			this.putClientProperty("activeStates", getActiveStatus() == 1 ? EActiveStatus.ACTIVITY.toString() : EActiveStatus.UNACTIVITY.toString());
			this.putClientProperty("creater", getCreateUser());
			if(!getCreateTime().equals("null") && !getCreateTime().equals("")){ 
				this.putClientProperty("createTime", DateUtil.strDate(getCreateTime(), DateUtil.FULLTIME));
			}
			this.putClientProperty("type", ECesType.forms(this.getCestype()));
			
			if(!this.isNode()){
				this.putClientProperty("asiteName", getASiteName());
				this.putClientProperty("zsiteName", getZSiteName());
				this.putClientProperty("aportName", getAportId());
				this.putClientProperty("zportName", getZportId());
				this.putClientProperty("pwName", getPwNameById(super.getPwId()));
			}else{
				this.putClientProperty("issingle", this.getIsSingle()==0?ResourceUtil.srcStr(StringKeysObj.OBJ_NO):ResourceUtil.srcStr(StringKeysObj.OBJ_YES));
				this.putClientProperty("jobstatus", super.getJobStatus(this.getJobStatus()));
			}
			

			
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}

	private String getPwNameById(int pwId) {
		PwInfoService_MB pwService = null;
		PwInfo pwinfo = null;
		List<PwInfo> list = null;
		try {
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			pwinfo = new PwInfo();
			pwinfo.setPwId(pwId);
			list = pwService.select(pwinfo);
			if (list.size() > 0)
				return list.get(0).getPwName();

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(pwService);
			pwinfo = null;
			list = null;
		}
		return "";
	}

	public PortInst getBeforeAPort() {
		return beforeAPort;
	}

	public void setBeforeAPort(PortInst beforeAPort) {
		this.beforeAPort = beforeAPort;
	}

	public PortInst getBeforeZPort() {
		return beforeZPort;
	}

	public void setBeforeZPort(PortInst beforeZPort) {
		this.beforeZPort = beforeZPort;
	}

	public PortStmTimeslot getBeforeAPortStmTime() {
		return beforeAPortStmTime;
	}

	public void setBeforeAPortStmTime(PortStmTimeslot beforeAPortStmTime) {
		this.beforeAPortStmTime = beforeAPortStmTime;
	}

	public PortStmTimeslot getBeforeZPortStmTime() {
		return beforeZPortStmTime;
	}

	public void setBeforeZPortStmTime(PortStmTimeslot beforeZPortStmTime) {
		this.beforeZPortStmTime = beforeZPortStmTime;
	}

}
