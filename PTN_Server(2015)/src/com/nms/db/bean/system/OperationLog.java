package com.nms.db.bean.system;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.enums.EOperationLogType;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysBtn;

/**
 * 操作日志  
 * @author Administrator
 *
 */
public class OperationLog extends ViewDataObj {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int user_id;//用户ID   与  user_inst表关联
	private String startTime;
	private String overTime;//结束时间
	private int operationType;//操作类型
	private int operationResult;//操作结果
	private String IP;
	/**
	 * 注： 数据库无此数据，仅做过滤查询时  判断之用
	 *  true  : 模糊查询
	 *    默认 false ： 精确查询
	 */
	private boolean isSelect;
	/**
	 * userName 
	 * operation_log 表中没有此数据
	 * 仅作为界面显示
	 */
	private String userName;
	private String operationObjName;
	private int siteId;
	private List<OperationDataLog> dataLogList = new ArrayList<OperationDataLog>();
	
	public String getOperationObjName() {
		return operationObjName;
	}
	public void setOperationObjName(String operationObjName) {
		this.operationObjName = operationObjName;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public List<OperationDataLog> getDataLogList() {
		return dataLogList;
	}
	public void setDataLogList(List<OperationDataLog> dataLogList) {
		this.dataLogList = dataLogList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String ip) {
		IP = ip;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	public int getOperationType() {
		return operationType;
	}
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}
	public int getOperationResult() {
		return operationResult;
	}
	public void setOperationResult(int operationResult) {
		this.operationResult = operationResult;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void putObjectProperty() {
			getClientProperties().put("id", getId());
			getClientProperties().put("userName", this.getUserName());
			getClientProperties().put("startTime", this.getStartTime());
			getClientProperties().put("overTime", this.getOverTime());
			getClientProperties().put("operationType", EOperationLogType.forms(this.getOperationType()));
			getClientProperties().put("IP", getIP());
			String resultName="";
			if(1==this.getOperationResult()){
				resultName=ResourceUtil.srcStr(StringKeysBtn.BTN_EXPORT_ISUCCESS);
				
			}else if(2==this.getOperationResult()){
				resultName=ResourceUtil.srcStr(StringKeysBtn.BTN_EXPORT_FALSE);
			}
			getClientProperties().put("operationResult",resultName);
	}
	
}
