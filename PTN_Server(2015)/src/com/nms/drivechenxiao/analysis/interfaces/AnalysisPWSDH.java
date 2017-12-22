package com.nms.drivechenxiao.analysis.interfaces;

import java.util.ArrayList;
import java.util.List;

import com.nms.drivechenxiao.analysis.tool.CoreOper;
import com.nms.drivechenxiao.analysis.tool.CxtOpLump;
import com.nms.drivechenxiao.analysis.tool.bean.CxtATTable;
import com.nms.drivechenxiao.analysis.tool.bean.CxtAtomType;
import com.nms.drivechenxiao.analysis.tool.bean.CxtOpItem;
import com.nms.drivechenxiao.service.bean.CXNEObject;
import com.nms.drivechenxiao.service.bean.oam.OamObject;
import com.nms.drivechenxiao.service.bean.pwsdh.PwSdhObject;

public class AnalysisPWSDH extends CxtOpLump {
	/**
	 * 创建PW
	 * 
	 * @param qosObject
	 * @param pwEthObject
	 * @param session
	 * @param seqid
	 * @return
	 */
	public byte[] createPWSDH(PwSdhObject pwSdhObject, OamObject oamObject, int session, int seqid) {
		List<CxtOpItem> cxtOpItems = new ArrayList<CxtOpItem>();
		String cdPath = "ne/interfaces/pwsdh/" + pwSdhObject.getName();
		List<CxtOpItem> cxtOpItemListPw = createPw("create", pwSdhObject, session, seqid);

		cxtOpItems.add(begin(3));
		cxtOpItems.addAll(cxtOpItemListPw);
		if (oamObject != null) {
			List<CxtOpItem> cxtOpItemListOAM = super.createOAM("PW", cdPath, pwSdhObject.getOam(), session, seqid);
			cxtOpItems.addAll(cxtOpItemListOAM);
		}
		cxtOpItems.add(commit());

		byte[] command = getCommandBytes(cxtOpItems);
		command = getHeaderCommandBytes(CoreOper.ECXTMSG_REQ_OPS, command, session, seqid);
		return command;
	}

	/**
	 * 删除PW
	 * 
	 * @param qosObject
	 * @param pwEthObject
	 * @param session
	 * @param seqid
	 * @return
	 */
	public byte[] deletePWSDH(PwSdhObject pwSdhObject, int session, int seqid) {
		String path = "ne/interfaces/pwsdh";
		String name = pwSdhObject.getName();

		List<CxtOpItem> cxtOpItems = new ArrayList<CxtOpItem>();
		cxtOpItems.add(begin(3));
		cxtOpItems.add(cd(path));
		cxtOpItems.add(delete(name));
		cxtOpItems.add(commit());

		byte[] command = getCommandBytes(cxtOpItems);
		command = getHeaderCommandBytes(CoreOper.ECXTMSG_REQ_OPS, command, session, seqid);
		return command;
	}

	/**
	 * 修改PW
	 * 
	 * @param qosObject
	 * @param tunnelObject
	 * @param session
	 * @param seqid
	 * @return
	 */
	public byte[] updatePWSDH(PwSdhObject pwSdhObject, int session, int seqid) {
		List<CxtOpItem> cxtOpItems = new ArrayList<CxtOpItem>();
		List<CxtOpItem> cxtOpItemListPw = createPw("update", pwSdhObject, session, seqid);

		cxtOpItems.add(begin(3));
		cxtOpItems.addAll(cxtOpItemListPw);
		cxtOpItems.add(commit());

		byte[] command = getCommandBytes(cxtOpItems);
		command = getHeaderCommandBytes(CoreOper.ECXTMSG_REQ_OPS, command, session, seqid);
		return command;
	}

	public byte[] selectAllPWSDH(int session, int seqid) {
		List<CxtOpItem> cxtOpItems = new ArrayList<CxtOpItem>();
		String s = null;

		cxtOpItems.add(begin(3));
		cxtOpItems.add(cd("ne/interfaces/pwsdh"));
		cxtOpItems.add(get(s, 2));

		byte[] command = getCommandBytes(cxtOpItems);
		command = getHeaderCommandBytes(CoreOper.ECXTMSG_REQ_OPS, command, session, seqid);
		return command;
	}

	public List<PwSdhObject> analysisPWSDH(byte[] command, CXNEObject CXNEObject) {
		List<PwSdhObject> pwSdhObject = new ArrayList<PwSdhObject>();
		int start = 49;
		byte[] tt = command;
		//		byte[] tt = new byte[] { 114, 109, 116, 1, 0, 0, 1, -54, 34, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 1, 32, 0, 0, 0, 1, 49, 48, 0, 0, 0, 24, 32, 0, 0, 0, 5, 97, 100, 109, 105, 110, 19, 0, 0, 0, 1, 32, 0, 0, 0, 9, 99, 97, 114, 114, 105, 101, 114, 105, 102, 19, 0, 2, 0, 1, 32, 0, 0, 0, 17, 99, 108, 111, 99, 107, 114, 101, 99, 111, 118, 101, 114, 121, 109, 111, 100, 101, 19, 0, 0, 0, 2, 32, 0, 0, 0, 3, 99, 111, 115, 19, 0, 0, 0, 5, 32, 0, 0, 0, 4, 100, 101, 115, 99, 32, 0, 0, 0, 3, 112, 116, 110, 32, 0, 0, 0, 7, 105, 102, 105, 110, 100, 101, 120, 19, 2, 3, 0, 1, 32, 0, 0, 0, 6, 105, 102, 110, 97, 109, 101, 19, 2, 3, 0, 1, 32, 0, 0, 0, 7, 105, 110, 108, 97, 98, 101, 108, 19, 0, 0, 0, 21, 32,
		//				0, 0, 0, 6, 106, 105, 116, 116, 101, 114, 19, 0, 0, 0, 8, 32, 0, 0, 0, 8, 108, 98, 108, 104, 119, 114, 101, 115, 19, 0, 0, 0, 2, 32, 0, 0, 0, 4, 111, 112, 101, 114, 19, 0, 0, 0, 1, 32, 0, 0, 0, 8, 111, 117, 116, 108, 97, 98, 101, 108, 19, 0, 0, 0, 22, 32, 0, 0, 0, 7, 111, 117, 116, 115, 121, 110, 99, 19, 0, 0, 0, 10, 32, 0, 0, 0, 5, 111, 119, 110, 101, 114, 19, 0, 0, 0, 0, 32, 0, 0, 0, 7, 112, 97, 121, 108, 111, 97, 100, 19, 0, 0, 0, 2, 32, 0, 0, 0, 4, 112, 101, 101, 114, 19, 20, 0, 0, 35, 32, 0, 0, 0, 10, 112, 101, 114, 112, 114, 111, 102, 105, 108, 101, 32, 0, 0, 0, 7, 112, 119, 116, 100, 109, 46, 48, 32, 0, 0, 0, 3, 114, 101, 102, 19, 0, 0, 0, 0, 32, 0, 0, 0, 7, 114, 101, 111, 114, 100, 101, 114, 1, 1, 32, 0, 0, 0, 6, 114, 116, 112, 104, 100, 114, 1, 1, 32, 0, 0, 0, 11,
		//				115, 117, 112, 112, 114, 101, 115, 115, 105, 111, 110, 1, 0, 32, 0, 0, 0, 4, 115, 121, 110, 99, 19, 0, 0, 0, 2, 32, 0, 0, 0, 7, 116, 100, 109, 116, 121, 112, 101, 19, 0, 0, 0, 0, 32, 0, 0, 0, 4, 116, 121, 112, 101, 19, 0, 0, 0, 1, 0, };
		byte[] t = new byte[tt.length - start];
		System.arraycopy(tt, start, t, 0, tt.length - start);
		pwSdhObject = super.analysisTabble("sdhpw", t);
		return pwSdhObject;
	}

	/*
	 * 创建PW
	 */
	private List<CxtOpItem> createPw(String type, PwSdhObject pwSdhObject, int session, int seqid) {
		String path = "ne/interfaces/pwsdh";
		List<CxtOpItem> cxtOpItems = new ArrayList<CxtOpItem>();
		List<CxtAtomType> cxtAtomTypes = new ArrayList<CxtAtomType>();
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, "peer"));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, pwSdhObject.getPeer()));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, "inlabel"));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, pwSdhObject.getInlabel()));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, "outlabel"));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, pwSdhObject.getOutlabel()));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, "carrierif"));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, pwSdhObject.getCarrierif()));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, "admin"));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, pwSdhObject.getAdmin()));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, "cos"));
		cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, pwSdhObject.getCos()));
		if(!"".equals(pwSdhObject.getPayload())){
			cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, "payload"));
			cxtAtomTypes.add(getCxtAtomType(CxtAtomType.AT_STRING, getPlayload(pwSdhObject.getPayload()) ));
		}
		CxtATTable cxtATTable = getCxtATTable(cxtAtomTypes.size() / 2, cxtAtomTypes);

		cxtOpItems.add(cd(path));
		if ("create".equals(type)) {
			cxtOpItems.add(create(pwSdhObject.getName(), cxtATTable));
		} else if ("update".equals(type)) {
			cxtOpItems.add(mset(pwSdhObject.getName(), cxtATTable));
		}

		return cxtOpItems;
	}
	private String getPlayload(String s){
		if("0".equals(s))return "64";
		if("1".equals(s))return "128";
		if("2".equals(s))return "256";
		if("3".equals(s))return "512";
		else return "256";
	}
}
