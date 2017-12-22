package com.nms.db.enums;

import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysObj;

public enum ERunStates implements IntEnum{
	RUN(1), HANG(2), STOP(3), END(4);
	private int value;

	private ERunStates(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ERunStates forms(int value) {
		for (ERunStates serviceType : ERunStates.values()) {
			if (value == serviceType.value)
				return serviceType;
		}
		return null;
	}

	@Override
	public String toString() {
		if (value == 1)
			return ResourceUtil.srcStr(StringKeysObj.OBJ_RUN);
		else if (value == 2)
			return ResourceUtil.srcStr(StringKeysObj.OBJ_HANG_UP);
		else if (value == 3)
			return ResourceUtil.srcStr(StringKeysObj.OBJ_STOP);
		else if (value == 4)
			return ResourceUtil.srcStr(StringKeysObj.OBJ_TERMINATION_OF);
		else
			return null;
	}
}
