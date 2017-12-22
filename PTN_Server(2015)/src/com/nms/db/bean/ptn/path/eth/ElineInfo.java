package com.nms.db.bean.ptn.path.eth;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.ptn.oam.OamInfo;
import com.nms.db.bean.ptn.path.ServiceInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.enums.EActiveStatus;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysObj;

public class ElineInfo extends ServiceInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9103902018940983262L;
	private int aXcId;
	private int zXcId;
	private List<OamInfo> oamList = new ArrayList<OamInfo>();

	public int getaXcId() {
		return aXcId;
	}

	public void setaXcId(int aXcId) {
		this.aXcId = aXcId;
	}

	public int getzXcId() {
		return zXcId;
	}

	public void setzXcId(int zXcId) {
		this.zXcId = zXcId;
	}

	public List<OamInfo> getOamList() {
		return oamList;
	}

	public void setOamList(List<OamInfo> oamList) {
		this.oamList = oamList;
	}

	@Override
	public void putObjectProperty() {
		
		try {
			this.putClientProperty("id", getId());
			this.putClientProperty("elineName", getName());
			this.putClientProperty("activeStates", getActiveStatus() == 1 ? EActiveStatus.ACTIVITY.toString() : EActiveStatus.UNACTIVITY.toString());
			this.putClientProperty("creater", getCreateUser());
			if (getCreateTime() != null) {
				this.putClientProperty("createTime", DateUtil.strDate(getCreateTime(), DateUtil.FULLTIME));
			}
			
			if(0==this.getIsSingle()){
				this.putClientProperty("pwName", getPwname(getPwId()));
				this.putClientProperty("asiteName", getASiteName());
				this.putClientProperty("zsiteName", getZSiteName());
				this.putClientProperty("aportName", getASiteName());
				this.putClientProperty("zportName", getZSiteName());
			} else {
				this.putClientProperty("jobstatus", super.getJobStatus(this.getJobStatus()));
			}
			this.putClientProperty("issingle", this.getIsSingle() == 0 ? ResourceUtil.srcStr(StringKeysObj.OBJ_NO) : ResourceUtil.srcStr(StringKeysObj.OBJ_YES));
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}

	private String getPwname(int pwId) throws Exception {
		PwInfoService_MB pwInfoService = null;
		PwInfo pwinfo = null;
		List<PwInfo> pwInfos = null;
		pwinfo = new PwInfo();
		String pwName = "";
		try {
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			pwinfo.setPwId(pwId);
			pwInfos = pwInfoService.select(pwinfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(pwInfoService);
		}
		if(pwInfos.size()>0)
		{
			if(pwInfos.get(0).getPwName() != null)
			{
				pwName = pwInfos.get(0).getPwName();
			}
		}
		return pwName;
	}
}
