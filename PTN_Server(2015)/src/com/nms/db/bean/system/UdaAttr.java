package com.nms.db.bean.system;

import java.io.Serializable;


@SuppressWarnings("serial")
public class UdaAttr implements Serializable {
	private int id;
	private int groupId;
	private String attrName;
	private String attrENName;
	private String attrType;
	private String isNeedText;
	private String codeGroupId;
	private String defaultValue;
	private int width;
	private int height;
	private int distanceLeft;
	private int distanceTop;
	private String isTableShow;
	private String isMustFill;


	public String getAttrENName() {
		return attrENName;
	}

	public void setAttrENName(String attrENName) {
		this.attrENName = attrENName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrType() {
		return attrType;
	}

	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}

	public String getIsNeedText() {
		return isNeedText;
	}

	public void setIsNeedText(String isNeedText) {
		this.isNeedText = isNeedText;
	}

	public String getCodeGroupId() {
		return codeGroupId;
	}

	public void setCodeGroupId(String codeGroupId) {
		this.codeGroupId = codeGroupId;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDistanceLeft() {
		return distanceLeft;
	}

	public void setDistanceLeft(int distanceLeft) {
		this.distanceLeft = distanceLeft;
	}

	public int getDistanceTop() {
		return distanceTop;
	}

	public void setDistanceTop(int distanceTop) {
		this.distanceTop = distanceTop;
	}

	public String getIsTableShow() {
		return isTableShow;
	}

	public void setIsTableShow(String isTableShow) {
		this.isTableShow = isTableShow;
	}
    
	
	public String getIsMustFill() {
		return isMustFill;
	}

	public void setIsMustFill(String isMustFill) {
		this.isMustFill = isMustFill;
	}
}
