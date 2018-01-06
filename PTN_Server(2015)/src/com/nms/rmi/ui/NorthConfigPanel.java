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
import com.nms.snmp.ninteface.impl.config.CRDXml;
import com.nms.snmp.ninteface.impl.config.EPGXml;
import com.nms.snmp.ninteface.impl.config.EPUXml;
import com.nms.snmp.ninteface.impl.config.EQHXml;
import com.nms.snmp.ninteface.impl.config.ESIXml;
import com.nms.snmp.ninteface.impl.config.ESPXml;
import com.nms.snmp.ninteface.impl.config.ETHXml;
import com.nms.snmp.ninteface.impl.config.LBSXml;
import com.nms.snmp.ninteface.impl.config.NELXml;
import com.nms.snmp.ninteface.impl.config.OMCXml;
import com.nms.snmp.ninteface.impl.config.PGUXml;
import com.nms.snmp.ninteface.impl.config.PRBXml;
import com.nms.snmp.ninteface.impl.config.PRTXml;
import com.nms.snmp.ninteface.impl.config.PSWXml;
import com.nms.snmp.ninteface.impl.config.PTGXml;
import com.nms.snmp.ninteface.impl.config.PWPXml;
import com.nms.snmp.ninteface.impl.config.PWTXml;
import com.nms.snmp.ninteface.impl.config.SBNXml;
import com.nms.snmp.ninteface.impl.config.SNNXml;
import com.nms.snmp.ninteface.impl.config.TDMXml;
import com.nms.snmp.ninteface.impl.config.TNLXml;
import com.nms.snmp.ninteface.impl.config.TPBXml;
import com.nms.snmp.ninteface.impl.config.TPIXml;
import com.nms.snmp.ninteface.impl.config.TPLXml;
import com.nms.snmp.ninteface.util.NorthConfig;
import com.nms.ui.manager.CheckingUtil;
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
public class NorthConfigPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public NorthConfigPanel() {
		this.initComponent();			
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
				try {
					String rmuid = rimUidtext.getText();
					if(!rmuid.matches(CheckingUtil.IP_REGULAR)){
						DialogBoxUtil.errorDialog(null, "IP格式不对");
						return;
					}
					NorthConfig.northServiceIp = rmuid;
					Integer alarmPort = Integer.parseInt(provincesText.getText());
					if(alarmPort<31232 || alarmPort>31241){
						DialogBoxUtil.errorDialog(null, "告警端口范围不对");
						return;
					}
					NorthConfig.northAlarmPort = alarmPort;
					Integer telnetPort = Integer.parseInt(manufacturerText.getText());
					if(telnetPort<31232 || telnetPort>31241){
						DialogBoxUtil.errorDialog(null, "操作指令端口范围不对");
						return;
					}
					NorthConfig.northTelnetPort = telnetPort;
					DialogBoxUtil.succeedDialog(null, "配置成功");
				} catch (Exception e2) {
					e2.printStackTrace();
					DialogBoxUtil.errorDialog(null, "端口号不是数字");
					return;
				}
				 
			}
		});
		createFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new OMCXml().getOMCXml();
				new NELXml().getNELXml();
				new EQHXml().getEQHXml();
				new CRDXml().getCRDXml();
				new PRTXml().getPRTXml();
				new PRBXml().getPRBXml();
				new TNLXml().getTNLXml();
				new LBSXml().getLSBXml();
				new TPIXml().getTPIXml();
				new TPBXml().getTPBXml();
				
				new PSWXml().getPSWXml();
				new PWPXml().getPWPXml();
				new PWTXml().getPWTXml();
				new ETHXml().getETHXml();
				new ESPXml().getESPXml();
				new ESIXml().getESIXml();
				
				new TDMXml().getTDMXml();
//				new ETPXml().getETPXml();
				new TPLXml().getTPLXml();
				new SBNXml().getSBNXml();
				new SNNXml().getSNNXml();
				new EPGXml().getEPGXml();
				new EPUXml().getEPUXml();
				
				new PTGXml().getPTGXml();
				new PGUXml().getPGUXml();
				DialogBoxUtil.succeedDialog(null, "生成成功");
			}
		});
	}
	

	
	


	
		
	/**
	 * 初始化控件
	 */
	private void initComponent() {
//		AlarmNorthconsumer alarmNorthconsumer = new AlarmNorthconsumer();
//	    new Thread(alarmNorthconsumer).start();
		this.setBorder(BorderFactory.createTitledBorder("NORTHCONFIG"));
		this.rimUid = new JLabel("serviceIP");
		this.rimUidtext = new JTextField(NorthConfig.northServiceIp);
		this.rimUidButton = new JButton(ResourceUtil.srcStr(StringKeysBtn.BTN_CONFIG));
		this.provinces = new JLabel("alarmPort");
		this.provincesText = new JTextField(NorthConfig.northAlarmPort+"");
		
		this.manufacturer = new JLabel("telnetPort");
		this.manufacturerText = new JTextField(NorthConfig.northTelnetPort+"");
		
		createFile = new JButton("生成文件");
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
		
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = 2;
		componentLayout.setConstraints(this.rimUidButton, c);
		this.panel_select.add(this.rimUidButton);
		
		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		componentLayout.setConstraints(this.createFile, c);
		this.panel_select.add(this.createFile);
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
	private JPanel panel_select;
	private Map<String,String> map = new HashMap<String,String>();
	private JButton createFile;//生成文件
	
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
