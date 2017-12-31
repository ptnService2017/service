package com.nms.rmi.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.util.StringUtil;

import com.mchange.lang.StringUtils;
import com.nms.db.bean.system.SystemLog;
import com.nms.jms.common.OpviewMessage;
import com.nms.jms.jmsMeanager.JmsUtil;
import com.nms.model.system.SystemLogService_MB;
import com.nms.model.util.Services;
import com.nms.rmi.ui.util.ServerConstant;
import com.nms.rmi.ui.util.ThreadUtil;
import com.nms.snmp.ninteface.framework.AgentServer;
import com.nms.snmp.ninteface.impl.alarm.AlarmNorthconsumer;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DialogBoxUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysBtn;
import com.nms.ui.manager.keys.StringKeysLbl;
import com.nms.ui.manager.keys.StringKeysTip;
import com.nms.ui.ptn.alarm.controller.OperateCurrAlarmTask;
import com.nms.ui.ptn.alarm.controller.QueryCurrAlarmBySitesTask;


/**
 * 进程管理
 * @author guoqc
 * 
 */
public class RmUIDPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public RmUIDPanel() {
		this.initComponent();			
		this.init();		
		this.setLayout();
		this.addListener();
				
	}

	/**
	 * 初始化IP地址
	 */
	public void init() {
		map.put("OMC", "OMC");
		map.put("NEL", "网元");
		map.put("EQH", "容器");
		map.put("CRD", "板卡");
		map.put("PRT", "端口");
		map.put("PRB", "端口绑定");
		map.put("TNL", "隧道");
		map.put("LBS", "标签交换");
		map.put("TPI", "隧道保护组-基本信息");
		map.put("TPB", "隧道保护组-主备用隧道");
		map.put("PSW", "伪线");
		map.put("PWP", "伪线间承载关系");
		map.put("PWT", "伪线隧道承载关系");
		map.put("ETH", "以太网业务");
		map.put("ESP", "以太网业务接入点");
		map.put("ESI", "以太网业务承载伪线");
		map.put("TDM", "TDM业务");
		map.put("ETP", "端点");
		map.put("TPL", "拓扑连接");
		map.put("SBN", "子网（包含子网与子网关系");
		map.put("SNN", "子网与网元关系");
		map.put("EPG", "板卡保护组");
		map.put("EPU", "板卡保护组单元");
		map.put("PTG", "端口保护组");
		map.put("PGU", "端口保护组单元");
	}

	/**
	 * 添加监听事件
	 */
	private void addListener() {
		rimUidButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String rmuid = rimUidtext.getText();
				if(rmuid != null && rmuid.length()>8){
					if(rmuid.substring(0, 4).equals("3301")){
						provincesText.setText("3301__浙江省杭州市");
					}else{
						provincesText.setText(rmuid.substring(0, 4));
					}
					
					if(rmuid.substring(4, 6).equals("EB")){
						manufacturerText.setText("EB__亿邦");
					}else{
						manufacturerText.setText(rmuid.substring(4, 6));
					}
					
					if(rmuid.substring(6, 8).equals("CS")){
						majorsText.setText("CS__传输");
					}else{
						majorsText.setText(rmuid.substring(6, 8));
					}
					
					numberingtext.setText("1");
					if(rmuid.length()>12){
						objectTypetext.setText(rmuid.substring(9, 12)+"__"+map.get(rmuid.substring(9, 12)));
						objectNumtext.setText(rmuid.substring(12));
					}
					
				}else{
					DialogBoxUtil.errorDialog(null, ResourceUtil.srcStr(StringKeysLbl.LBL_NOR_RMUIDERROR));
					return;
				}
			}
		});
	}
	

	
	


	
		
	/**
	 * 初始化控件
	 */
	private void initComponent() {
//		AlarmNorthconsumer alarmNorthconsumer = new AlarmNorthconsumer();
//	    new Thread(alarmNorthconsumer).start();
		this.setBorder(BorderFactory.createTitledBorder("RMUID"));
		this.rimUid = new JLabel("RMUID");
		this.rimUidtext = new JTextField();
		this.rimUidButton = new JButton(ResourceUtil.srcStr(StringKeysBtn.BTN_SELECT));
		this.provinces = new JLabel(ResourceUtil.srcStr(StringKeysLbl.LBL_NOR_PROVINCES));
		this.provincesText = new JTextField();
		provincesText.setEditable(false);
		
		this.manufacturer = new JLabel(ResourceUtil.srcStr(StringKeysLbl.LBL_NOR_MANUFACTURER));
		this.manufacturerText = new JTextField();
		manufacturerText.setEditable(false);
		
		this.majors = new JLabel(ResourceUtil.srcStr(StringKeysLbl.LBL_NOR_MAJORS));
		this.majorsText = new JTextField();
		majorsText.setEditable(false);
		
		this.numbering = new JLabel(ResourceUtil.srcStr(StringKeysLbl.LBL_NOR_NUMBERING));
		this.numberingtext = new JTextField();
		numberingtext.setEditable(false);
		
		this.objectType = new JLabel(ResourceUtil.srcStr(StringKeysLbl.LBL_NOR_OBJECTTYPE));
		this.objectTypetext = new JTextField();
		objectTypetext.setEditable(false);
		
		this.objectNum = new JLabel(ResourceUtil.srcStr(StringKeysLbl.LBL_NOR_OBJECTNUM));
		this.objectNumtext = new JTextField();
		objectNumtext.setEditable(false);
		
		this.panel_select = new JPanel();
		this.panel_select.setBorder(null);
		
	}


	/**
	 * 系统IP设置panel布局
	 */
	private void setLayoutSelect() {

		GridBagLayout componentLayout = new GridBagLayout();
		componentLayout.columnWidths = new int[] { 30, 150, 30 };
		componentLayout.columnWeights = new double[] { 0.1, 0,0 };
		componentLayout.rowHeights = new int[] { 10, 10,10,10 };
		componentLayout.rowWeights = new double[] { 0.1, 0.1,0.1,0.1 };
				
		this.panel_select.setLayout(componentLayout);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		componentLayout.setConstraints(this.rimUid, c);
		this.panel_select.add(this.rimUid);

		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		componentLayout.setConstraints(this.rimUidtext, c);
		this.panel_select.add(this.rimUidtext);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = 0;
		componentLayout.setConstraints(this.rimUidButton, c);
		this.panel_select.add(this.rimUidButton);
		
		
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		componentLayout.setConstraints(this.provinces, c);
		this.panel_select.add(this.provinces);


		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		componentLayout.setConstraints(this.provincesText, c);
		this.panel_select.add(this.provincesText);


		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		componentLayout.setConstraints(this.manufacturer, c);
		this.panel_select.add(this.manufacturer);


		c.gridx = 1;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		componentLayout.setConstraints(this.manufacturerText, c);
		this.panel_select.add(this.manufacturerText);
		
		
		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		componentLayout.setConstraints(this.majors, c);
		this.panel_select.add(this.majors);


		c.gridx = 1;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		componentLayout.setConstraints(this.majorsText, c);
		this.panel_select.add(this.majorsText);
		
		c.gridx = 0;
		c.gridy = 4;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		componentLayout.setConstraints(this.numbering, c);
		this.panel_select.add(this.numbering);


		c.gridx = 1;
		c.gridy = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		componentLayout.setConstraints(this.numberingtext, c);
		this.panel_select.add(this.numberingtext);
		
		
		c.gridx = 0;
		c.gridy = 5;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		componentLayout.setConstraints(this.objectType, c);
		this.panel_select.add(this.objectType);


		c.gridx = 1;
		c.gridy = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		componentLayout.setConstraints(this.objectTypetext, c);
		this.panel_select.add(this.objectTypetext);
		
		c.gridx = 0;
		c.gridy = 6;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		componentLayout.setConstraints(this.objectNum, c);
		this.panel_select.add(this.objectNum);


		c.gridx = 1;
		c.gridy = 6;
		c.fill = GridBagConstraints.HORIZONTAL;
		componentLayout.setConstraints(this.objectNumtext, c);
		this.panel_select.add(this.objectNumtext);

	}


	/**
	 * 此页面总布局
	 */
	private void setLayout() {
		this.setLayoutSelect();

		GridBagLayout componentLayout = new GridBagLayout();
		componentLayout.columnWeights = new double[] { 0.1 };
		componentLayout.rowWeights = new double[] { 0.1, 0.1 };
		this.setLayout(componentLayout);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(30, 0, 15, 0);
		componentLayout.setConstraints(this.panel_select, c);
		this.add(this.panel_select);

	}

	
	private JLabel rimUid; // 告警轮询
	private JTextField rimUidtext;
	private JButton rimUidButton;
	
	private JLabel provinces;
	private JTextField provincesText;
	private JLabel manufacturer;
	private JTextField manufacturerText;
	private JLabel majors;
	private JTextField majorsText;
	private JLabel numbering;
	private JTextField numberingtext;
	private JLabel objectType;
	private JTextField objectTypetext;
	private JLabel objectNum;
	private JTextField objectNumtext;
	private JPanel panel_select;
	private Map<String,String> map = new HashMap<String,String>();
	public static void main(String[] args) {
		try {
			FileOutputStream  fs = new FileOutputStream( System.getProperty("user.dir") + "license.xml");
			try {
				fs.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
