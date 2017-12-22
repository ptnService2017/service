package com.nms.drive.analysis;

import com.nms.drive.analysis.datablock.AnalysisSinglePanAutoTable;
import com.nms.drive.service.bean.SinglePan;

public class TestPan {
	public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 96; i++) {
//			System.out.print("0x01,");
//		}
		byte[] command = new byte[]{0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01};
		SinglePan singlePan = new SinglePan();
		String configXml = "src\\com\\ptn\\drive\\analysis\\xmltool\\file\\单盘自动发现信息.xml";
		AnalysisSinglePanAutoTable aspat = new AnalysisSinglePanAutoTable();
		singlePan = aspat.analysisCommandToObject(command, configXml);
		System.out.println(singlePan.getNEAddr());
		System.out.println(singlePan.getPanType());
		System.out.println(singlePan.getPanNum());
		System.out.println(singlePan.getVersionNum());
		System.out.println(singlePan.getPanTime());
		System.out.println(singlePan.getBMUversion());
		System.out.println(singlePan.getBMUtime());
		System.out.println(singlePan.getDataInfoList().get(0).getTagNum());
		System.out.println(singlePan.getDataInfoList().get(0).getCRCcheck());
		System.out.println(singlePan.getDataInfoList().get(0).getProductionTime());
	}
}
