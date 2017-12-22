﻿package com.nms.ui.manager.keys;


/**
*    
* 项目名称：WuHanPTN2012   
* 类名称：StringKeysTip   
* 类描述：页面文本对应的key  弹出提示语
* 创建人：kk    
* 创建时间：2013-5-4 下午02:39:33   
* 修改人：kk   
* 修改时间：2013-5-4 下午02:39:33   
* 修改备注：   
* @version    
*
 */
public class StringKeysTip {
	//校时
	public static final String TIP_CHOOSE_CURRENT="TIP_CHOOSE_CURRENT";//校时对象
	//页面验证提示语
	public static final String MESSAGE_SPECIAL = "MESSAGE_SPECIAL"; //特殊字符
	public static final String MESSAGE_LENG = "MESSAGE_LENG"; // 长度
	public static final String MESSAGE_NUMBER = "MESSAGE_NUMBER"; // 只能是数字
	public static final String MESSAGE_NUMBERANDLETTER = "MESSAGE_NUMBERANDLETTER"; // 只能是数字或字母或者数字字母组合
	public static final String MESSAGE_IP = "MESSAGE_IP"; // 只是是ip
	public static final String MESSAGE_VALUE_SCOPE = "MESSAGE_VALUE_SCOPE"; // 取值范围应该在
	//MESSAGE_VALUE_SCOPE=取值范围应该在：
	public static final String MESSAGE_VALUE_SCOPE2 = "MESSAGE_VALUE_SCOPE2"; // 之间的数值！
	
	public static final String MESSAGE_VALUE_MAX = "MESSAGE_VALUE_MAX"; // 值不能大于:
	public static final String MESSAGE_VALUE_MIN = "MESSAGE_VALUE_MIN"; // 值不能小于:
	public static final String MESSAGE_PASSWORD = "MESSAGE_PASSWORD"; // 密码必须由字母或数字组成:
	public static final String MESSAGE_PASSWORD_LENG = "MESSAGE_PASSWORD_LENG"; // 密码长度必须在6-18之间
	//界面弹出提示
	public static final String TIP_NOT_USER = "TIP_NOT_USER"; //用户不存在
	public static final String TIP_NOT_FULL = "TIP_NOT_FULL"; //请填写完整
	public static final String TIP_SAVE_SUCCEED = "TIP_SAVE_SUCCEED"; //保存成功
	public static final String TIP_SELECT_DATA_ONE = "TIP_SELECT_DATA_ONE"; //请选择一条数据再进行操作
	public static final String TIP_SELECT_DATA_MORE = "TIP_SELECT_DATA_MORE"; //请选择数据再进行操作
	public static final String TIP_UNLOAD_OBJECT = "TIP_UNLOAD_OBJECT"; //请选择要激活的对象
	public static final String TIP_UNLOAD_FINESH = "TIP_UNLOAD_FINESH"; //转储完成！
	public static final String TIP_UNLOAD_START = "TIP_UNLOAD_START"; //转储开始 !......
	public static final String TIP_UNLOAD_FILE_FAIL = "TIP_UNLOAD_FILE_FAIL"; //转储失败： 路径不存在
	public static final String TIP_IS_DELETE = "TIP_IS_DELETE"; //确认删除吗？
	public static final String TIP_IS_SHOT = "TIP_IS_SHOT"; //确认注销吗？
	public static final String TIP_IS_LOCK = "TIP_IS_LOCK"; //确认解锁吗？
	public static final String TIP_UN_LOADING = "TIP_UN_LOADING"; //确认转储吗？
	public static final String TIP_IS_CLEAR = "TIP_IS_CLEAR"; //确认锁定吗？
	public static final String TIP_LABEL_NUMBER = "TIP_LABEL_NUMBER"; //标签只能为数字
	public static final String TIP_DELETE_NODE = "TIP_DELETE_NODE"; //只能删除单网元配置
	public static final String TIP_UPDATE_NODE = "TIP_UPDATE_NODE"; //只能修改单网元配置
	public static final String TIP_SELECT_PW_ERROR = "TIP_SELECT_PW_ERROR"; //选中PW条目数不正确
	public static final String TIP_CODE_IDENTITY_EXIST = "TIP_CODE_IDENTITY_EXIST"; //此标识已存在
	public static final String TIP_USERNAMEBESTOW = "TIP_USERNAMEBESTOW"; //删除用户中包含正在使用的用户,请重新选择
	public static final String TIP_PASSWORDERROR = "TIP_PASSWORDERROR"; //两次密码输入不一致
	public static final String TIP_USER_EXIST="TIP_USER_EXIST";//用户名称已存在
	public static final String TIP_USERINTERFACE_OUTOFLENGH="TIP_USERINTERFACE_OUTOFLENGH";//用户详细信息过长
	public static final String TIP_CONNECTION_ERROR = "TIP_CONNECTION_ERROR"; //连接服务器异常
	public static final String TIP_CONNECTION_MAXDATA = "TIP_CONNECTION_MAXDATA"; //超过最大连接数
	public static final String TIP_CONNECTION_LAST = "TIP_CONNECTION_LAST"; //服务器版本过期
	
	public static final String TIP_PORT_OCCUPY = "TIP_PORT_OCCUPY"; //此端口已被使用
	public static final String TIP_MUSTNETWORK_BEFORE = "TIP_MUSTNETWORK_BEFORE"; //请选择端口
	public static final String TIP_LABEL_OCCUPY = "TIP_LABEL_OCCUPY"; //标签不可用
	public static final String TIP_LABEL_REPEAT = "TIP_LABEL_REPEAT"; //标签重复
	public static final String TIP_SITENONENTILY = "TIP_SITENONENTILY"; //没有找到此网元
	public static final String TIP_NOSEGMENTBETWEENPORTS = "TIP_NOSEGMENTBETWEENPORTS"; //两端口之间没有段
	public static final String TIP_CONFIG_SUCCESS = "TIP_CONFIG_SUCCESS"; //配置成功
	public static final String TIP_CONFIG_FALSE = "TIP_CONFIG_FALSE"; //超时
	public static final String TIP_NAME_EXIST = "TIP_NAME_EXIST"; //名称已存在
	public static final String TIP_QOS_FILL = "TIP_QOS_FILL"; //请配置QoS
	public static final String TIP_SELECT_TUNNEL = "TIP_SELECT_TUNNEL"; //请选择TUNNEL
	public static final String TIP_SELECT_PW = "TIP_SELECT_PW"; //请选择PW
	public static final String TIP_SELECT_SITE = "TIP_SELECT_SITE"; //请选择对端网元
	public static final String TIP_SELECT_AC = "TIP_SELECT_AC"; //请选择AC
	public static final String TIP_SELECT_TIMESLOT = "TIP_SELECT_TIMESLOT"; //请选择时隙
	public static final String TIP_IPERROR = "TIP_IPERROR"; //请输入正确的IP格式 
	public static final String TIP_PASSWORDNULL = "TIP_PASSWORDNULL"; //请输入密码
	public static final String TIP_SITE_LOCK = "TIP_SITE_LOCK"; //网元被锁
	public static final String TIP_PORTNULL = "TIP_PORTNULL"; //A端口或Z端口不能为空
	public static final String TIP_SITELOCK = "TIP_SITELOCK"; //是否强制解锁
	public static final String TIP_QOS_TEMPLATENAME_NOTEMPTY = "TIP_QOS_TEMPLATENAME_NOTEMPTY"; //qos模板名不能为空
	public static final String TIP_QOS_TEMPLATENAME_ISEXSIT = "TIP_QOS_TEMPLATENAME_ISEXSIT"; //该模板名已存在
	public static final String TIP_PATHHASNOCHECK = "TIP_PATHHASNOCHECK"; //请先作路径检查
	public static final String TIP_ACISUSED = "TIP_ACISUSED"; //AC端口被使用
	public static final String TIP_MEPIDISNOTEMPTY = "TIP_MEPIDISNOTEMPTY"; //MEPID不能为空
	public static final String TIP_MEPIDISREPEAT = "TIP_MEPIDISREPEAT"; //MEPID重复
	public static final String TIP_MEGICCISNOTEMPTY = "TIP_MEGICCISNOTEMPTY"; //MEGICC不能为空
	public static final String TIP_MEGUMCISNOTEMPTY = "TIP_MEGUMCISNOTEMPTY"; //MEGUMC不能为空
	public static final String TIP_LPBOUTTIMEOUTOFLIMIT = "TIP_LPBOUTTIMEOUTOFLIMIT"; //取值范围应在0-100之间
	public static final String TIP_SITEPWEXIST = "TIP_SITEPWEXIST"; //网元之间已选择了pw
	public static final String TIP_PWNOTENOUGH = "TIP_PWNOTENOUGH"; //创建Elan业务时pw路径不足
	public static final String TIP_PWNOTENOUGHs = "TIP_PWNOTENOUGH"; //创建Elan业务时pw路径不足
	
	public static final String TIP_PWNNIBUFFERHASBEUSED = "TIP_PWNNIBUFFERHASBEUSED"; //此配置已被使用
	public static final String TIP_FIELDEXISTMNE = "TIP_FIELDEXISTMNE"; //此网元所在的域已存在M类型网元
	public static final String TIP_NEIDHASEXIST = "TIP_NEIDHASEXIST"; //网元ID已存在，请重新输入
	public static final String TIP_IPHASEXIST = "TIP_IPHASEXIST"; //网元IP已存在，请重新输入
	public static final String TIP_NEHASSEGMENT = "TIP_NEHASSEGMENT"; //网元上存在段，不能删除
	public static final String TIP_AC_NEHASSEGMENT = "TIP_AC_NEHASSEGMENT"; //网元上存在AC，不能删除
	public static final String TIP_TUNNEL_NEHASSEGMENT = "TIP_TUNNEL_NEHASSEGMENT"; //网元上存在TUNNEL，不能删除
	public static final String TIP_MACERROR = "TIP_MACERROR"; //请输入正确的MAC地址
	public static final String TIP_PORTISUSEDBYSEGMENT = "TIP_PORTISUSEDBYSEGMENT"; //此板卡下有端口被段使用，不能卸载
	public static final String TIP_PORTISUSEDBYACPORT = "TIP_PORTISUSEDBYACPORT"; //此板卡下有端口被AC使用，不能卸载
	public static final String TIP_PORTISUSEDBYPWNNI = "TIP_PORTISUSEDBYPWNNI"; //此板卡下有端口被PW端口使用，不能卸载
	public static final String TIP_BUFFER_MORETHAN_10 = "TIP_BUFFER_MORETHAN_10"; //最多只能创建10个流
	public static final String TIP_DOWNADDVLANID = "TIP_DOWNADDVLANID"; //下话增加VlanId范围必须在2至2047
	public static final String TIP_ETHLOOPVLANID = "TIP_ETHLOOPVLANID"; //ETHLOOPVlanId范围必须在1至4094
	public static final String TIP_VLANID = "TIP_VLANID"; //VlanId范围必须在2至4095
	public static final String TIP_MACEMPTY = "TIP_MACEMPTY"; //MAC地址为空
	public static final String TIP_IPEMPTY = "TIP_IPEMPTY"; //IP地址为空
	public static final String TIP_MELOUTOFLIMIT = "TIP_MELOUTOFLIMIT"; //取值范围应在1-7之间
	public static final String TIP_MEPIDANDMIPIDISREPEAT = "TIP_MEPIDANDMIPIDISREPEAT"; //MEPID和MIPID重复
	public static final String TIP_QOSISNOTENOUGH = "TIP_QOSISNOTENOUGH"; //QOS配置不足
	public static final String TIP_VLANISNOTENOUGH = "TIP_VLANISNOTENOUGH"; //多条流的vlanId不能一样
	public static final String TIP_MODELISNOTENOUGH = "TIP_MODELISNOTENOUGH"; //端口模式下，只能创建一个ac
	public static final String TIP_VLANIDREPEAT = "TIP_VLANIDREPEAT"; //多个ac的vlanid不能一样
	public static final String TIP_PORTFORAC = "TIP_PORTFORAC"; //承载的接口为空！
	public static final String TIP_HASBEANQUOTEASPROTECTION = "TIP_HASBEANQUOTEASPROTECTION"; //TUNNEL作为保护，不能删除
	public static final String TIP_PORTTYPEANDENABLE = "TIP_PORTTYPEANDENABLE"; //请选择NNI类型的使能端口！
	public static final String TIP_SELECTPWISNOTENOUGH = "TIP_SELECTPWISNOTENOUGH"; //在已选中的pw列表中选择的pw路径不匹配
	public static final String TIP_ATOZPROTNUMBERISNOTSAME = "TIP_ATOZPROTNUMBERISNOTSAME"; //A端与Z端端口数量不一致
	public static final String TIP_CREATECESSAMENEERROR = "TIP_CREATECESSAMENEERROR"; //创建Ces业务时，A端端口与Z端端口不能在同一网元
	public static final String TIP_APORTTABLEISZERO = "TIP_APORTTABLEISZERO"; //A端表中为空
	public static final String TIP_ZPORTTABLEISZERO = "TIP_ZPORTTABLEISZERO"; //Z端表中为空
	public static final String TIP_BEFORE_AFTER_SITE = "TIP_BEFORE_AFTER_SITE"; //前后向的对端网元、承载接口不能一致
	public static final String TIP_USEDBYLAG = "TIP_USEDBYLAG"; //LAG使用
	public static final String TIP_SELECT_FIELD_ERROR = "TIP_SELECT_FIELD_ERROR"; //查询field出错
	public static final String TIP_PATH_NOT_INSPECT = "TIP_PATH_NOT_INSPECT"; //路径未检查
	public static final String TIP_SELECT_SITEINST_ERROR = "TIP_SELECT_SITEINST_ERROR"; //查询siteinst出错
	public static final String TIP_LOADING_ERROR = "TIP_LOADING_ERROR"; //加载失败
	public static final String TIP_LIMIT_1_65535 = "TIP_LIMIT_1_65535"; //长度在1到65535之间
	public static final String TIP_LIMIT_0_255 = "TIP_LIMIT_0_255"; //长度在0到255之间
	public static final String TIP_LIMIT_24_65536 = "TIP_LIMIT_24_65536"; //长度在24到65536之间
	public static final String TIP_CHOOSE_TYPE = "TIP_CHOOSE_TYPE"; //请选择类型
	public static final String TIP_FIND_DETAIL_FAIL = "TIP_FIND_DETAIL_FAIL"; //查询详细信息失败！
	public static final String TIP_FULL_DATA = "TIP_FULL_DATA"; //请填写完整数据
	public static final String TIP_TEST_SUCCEED = "TIP_TEST_SUCCEED"; //测试成功
	public static final String TIP_TEST_FAIL = "TIP_TEST_FAIL"; //测试失败
	public static final String TIP_DELETE_AREA_INFORMATION = "TIP_DELETE_AREA_INFORMATION"; //请先删除AREA信息
	public static final String TIP_CHOOSE_ALARMOBJ = "TIP_CHOOSE_ALARMOBJ"; //请选择告警对象！
	public static final String TIP_CHOOSE_ALARMOBJTYPE = "TIP_CHOOSE_ALARMOBJTYPE"; //请选择告警类型！
	public static final String TIP_CHOOSE_ALARMOB = "TIP_CHOOSE_ALARMOB"; //请选择告警级别！
	public static final String TIP_SELECT_PORT = "TIP_SELECT_PORT"; //请选择一个端口成员
	public static final String TIP_SELECT_REMOVE = "TIP_SELECT_REMOVE"; //请选择一个移除对象
	public static final String TIP_GET_DATA_FAILED = "TIP_GET_DATA_FAILED"; //获取数据失败
	public static final String TIP_4_LAG = "TIP_4_LAG"; //已经建立了5条LAG了
	public static final String TIP_8_LAG = "TIP_8_LAG"; //已经建立了8条LAG了
	public static final String TIP_LAG_USE = "TIP_LAG_USE"; //LAG已经被引用
	public static final String TIP_PORT_MEMBERS_SAME = "TIP_PORT_MEMBERS_SAME"; //端口成员不能相同
	public static final String TIP_SELECT_PROTECT_TUNNEL = "TIP_SELECT_PROTECT_TUNNEL"; //请选择一条有保护的TUNNEL
	public static final String TIP_NOTLOOP = "TIP_NOTLOOP"; //所选路径不能组成环
	public static final String TIP_NOTLOOP_WAY = "TIP_NOTLOOP_WAY"; //两个网元之间只能选一条路径
	public static final String TIP_SITELOCK_ENABLED = "TIP_SITELOCK_ENABLED";//网元已解锁
	public static final String TIP_SELECT_LOCKED_SITE = "TIP_SELECT_LOCKED_SITE";//请选择被锁的网元
	public static final String TIP_CLEAR_SITELOCK = "TIP_CLEAR_SITELOCK";//强制解锁
	public static final String TIP_SITELOCK_INFO = "TIP_SITELOCK_INFO";//锁信息
	public static final String TIP_LABEL_UNOCCUPY = "TIP_LABEL_UNOCCUPY"; //标签可用
	public static final String TIP_LIMIT_16_1040383 = "TIP_LIMIT_16_1040383"; //标签的范围为: 16-1040383
	public static final String TIP_NO_PROTROUTER = "TIP_NO_PROTROUTER"; //当前没有保护路由
	public static final String TIP_LABEL_FILL = "TIP_LABEL_FILL"; //请配置标签
	public static final String TIP_CHOOSE_SITE = "TIP_CHOOSE_SITE";//请选择网元
	/**
	 * 账户已经登陆或者被锁定
	 */
	public static final String TIT_CREATE_ROLE="TIT_CREATE_ROLE";	//新建角色
	public static final String TIT_UPDATE_ROLE="TIT_UPDATE_ROLE";	//修改角色
	public static final String TIP_USER_LONGING = "TIP_USER_LONGING";//用户已经登陆,是否强制登陆
	
	public static final String TIP_USER_LOCKING= "TIP_USER_LOCKING";//用户已经被锁定,是否强制登陆
	public static final String TIP_USER_UNLOCK = "TIP_USER_UNLOCK"; //用户没有锁定
	public static final String TIP_USER_LOCK = "TIP_USER_LOCK"; //用户已经被锁定
	public static final String TIP_USER_ADMINLOCK = "TIP_USER_ADMINLOCK"; //管理员已经锁定此账号
	public static final String TIP_USER_SYSTEMLOCK = "TIP_USER_SYSTEMLOCK"; //此账号系统已自动锁定，30分钟后自动解锁
	public static final String TIP_USER_POWER = "TIP_USER_POWER"; //权限不足
	public static final String TIP_USER_LOGOUT = "TIP_USER_LOGOUT"; //用户注销失败，重新选择
	public static final String TIP_USER_DEFAULTUSER = "TIP_USER_DEFAULTUSER"; //缺省账户,不允许此次操作
	public static final String TIP_USER_LOG_LOCK = "TIP_USER_LOG_LOCK"; //用户已经登陆且被锁定
	public static final String TIP_USER_ROLE_USERING="TIP_USER_ROLE_USERING";//此角色以被使用，不可删除
	
	public static final String TIP_CHOOSE_FILEPATH="TIP_CHOOSE_FILEPATH";//请选择路径
	public static final String TIP_CREATE_TABLENAME="TIP_CREATE_TABLENAME";//创建表头失败
	public static final String TIP_CHOOSE_FILEPATHHASEXIT="TIP_CHOOSE_FILEPATHHASEXIT";//文件名已经存在
	/**
	 * 用户名或密码错误
	 */
	public static final String TIP_USER_PASSWORK_ERROR = "TIP_USER_PASSWORK_ERROR";
	public static final String TIP_USER_LOGINING_PASSWORK_ERROR = "TIP_USER_LOGINING_PASSWORK_ERROR";//用户已经登陆，并且密码错误
	public static final String TIP_PASSWORDERRORANAGE="TIP_PASSWORDERRORANAGE";
	public static final String WRITEXTUSERNAME="WRITEXTUSERNAME";//请输入用户名
	public static final String TIP_CHOOSE_FILTER = "TIP_CHOOSE_FILTER"; //请先设置过滤条件!
	public static final String TIP_CHOOSE_SITE_OR_BOARD = "TIP_CHOOSE_SITE_OR_BOARD"; //请选择网元或者板卡!
	public static final String TIP_CHOOSE_MONITORING_OBJ = "TIP_CHOOSE_MONITORING_OBJ"; //请选择监控对象!
	public static final String TIP_INPUT_TASK_NAME = "TIP_INPUT_TASK_NAME"; //请输入任务名称!
	
	public static final String TIP_INPUT_OVERTIME = "TIP_INPUT_OVERTIME"; //请选择结束时间
	public static final String TIP_TASK_NAME_EXIST_INPUT_AGAIN = "TIP_TASK_NAME_EXIST_INPUT_AGAIN"; //任务名称已存在，请重新输入！
	public static final String TIP_TASK_NAME_EXIST = "TIP_TASK_NAME_EXIST"; //任务已存在，请重新选择监控对象！
	public static final String TIP_CHOOSE_MONITORING_PERIOD = "TIP_CHOOSE_MONITORING_PERIOD"; //请选择监控周期!
	public static final String TIP_CHOOSE_PERFORMANCE_TYPE = "TIP_CHOOSE_PERFORMANCE_TYPE"; //请选择性能类型!
	public static final String TIP_PERFORMANCE_TYPE = "TIP_PERFORMANCE_TYPE"; //请选择性能类别!
	public static final String TIP_CONFIG_FAIL = "TIP_CONFIG_FAIL"; //配置失败
	
	public static final String TIP_ROLE_EXIT = "TIP_ROLE_EXIT"; //角色已经存在
	public static final String TIP_SELECT_ROLE = "TIP_SELECT_ROLE"; //请选择权限

	public static final String TIP_PERFORMANCE_MAX = "TIP_PERFORMANCE_MAX"; //最多只能选35个性能参数
	public static final String TIP_PERFORMANCEPORT_MAX = "TIP_PERFORMANCEPORT_MAX"; //请选择性能参数
	/**
	 * 配置失败,PW未激活
	 */
	public static final String TIP_CONFIG_FAIL_PW_ACTIVITY = "TIP_CONFIG_FAIL_PW_ACTIVITY";
	/**
	 * 配置失败,PW被使用
	 */
	public static final String TIP_CONFIG_FAIL_PW_USE = "TIP_CONFIG_FAIL_PW_USE";
	/**
	 * OSPF区域ID已存在
	 */
	public static final String TIP_OSPF_ID_EXIST = "TIP_OSPF_ID_EXIST";
	/**
	 * 重分发类型已存在
	 */
	public static final String TIP_REDISTRIBUTE_EXIST = "TIP_REDISTRIBUTE_EXIST";
	/**
	 * 配置失败,TUNNEL未激活
	 */
	public static final String TIP_CONFIG_FAIL_TUNNEL_ACTIVITY = "TIP_CONFIG_FAIL_TUNNEL_ACTIVITY";
	/**
	 * 配置失败,TUNNEL被使用
	 */
	public static final String TIP_CONFIG_FAIL_TUNNEL_USE = "TIP_CONFIG_FAIL_TUNNEL_USE";
	/**
	 * 此端口未连接
	 */
	public static final String TIP_PORT_UNUNITED = "TIP_PORT_UNUNITED";
	/**
	 * 此域中存在网元    Domain can not be deleted, please remove NEs first
	 */
	public static final String TIP_FIELD_SITE = "TIP_FIELD_SITE";
	/**
	 * 数据库中没有查到网元信息
	 */
	public static final String TIP_DATA_SITE = "TIP_DATA_SITE";

	/**
	 * 至少选择两个网元
	 */
	public static final String TIP_SELECT_SITE_TWO = "TIP_SELECT_SITE_TWO";
	/**
	 * 请先设置AZ端
	 */
	public static final String TIP_AZ_CONFIG_BEFORE = "TIP_AZ_CONFIG_BEFORE";
	/**
	 * 没有合适工作路径
	 */
	public static final String TIP_NO_JOB_PATH = "TIP_NO_JOB_PATH"; 
	/**
	 * 没有合适保护路径
	 */
	public static final String TIP_NO_PROTECT_PATH = "TIP_NO_PROTECT_PATH"; 
	/**
	 * 工作和保护端口不能相同
	 */
	public static final String TIP_JOB_PROJECT_PORT = "TIP_JOB_PROJECT_PORT"; 
	/**
	 * 请选择AZ端保护端口
	 */
	public static final String TIP_AZ_PROTECT_BEFORE = "TIP_AZ_PROTECT_BEFORE";
	/**
	 * 保护和工作端口不能相同
	 */
	public static final String TIP_PROTECT_PORT = "TIP_PROTECT_PORT"; 
	/**
	 * 修改MIP
	 */
	public static final String TIP_UPDATE_MIP = "TIP_UPDATE_MIP"; 
	
	/**
	 * 确认退出吗？
	 */
	public static final String TIP_AFFIRM_EXIT = "TIP_AFFIRM_EXIT"; 
	/**
	 * 请选择1:1类型的tunnel进行倒换
	 */
	public static final String TIP_SELECT_1TO1 = "TIP_SELECT_1TO1"; 
	
	/**
	 * 配置失败，数量超出设备限制
	 */
	public static final String TIP_BUSINESSID_NULL="TIP_BUSINESSID_NULL";
	/**
	 * 网元IP已存在
	 */
	public static final String TIP_SITE_IP_EXIST="TIP_SITE_IP_EXIST";
	/**
	 * 被TUNNEL使用
	 */
	public static final String TIP_DELETE_SEGMENT_TUNNEL="TIP_DELETE_SEGMENT_TUNNEL";
	/**
	 * 网元ID已存在
	 */
	public static final String TIP_SITE_ID_EXIST="TIP_SITE_ID_EXIST";
	/**
	 * 	有网元在子网中，不能删除
	 */
	public static final String TIP_SUBNET_DELETE_NODE = "TIP_SUBNET_DELETE_NODE";
	 /*沒有可配的端口 
	 */
	public static final String LBL_NO_PORT="LBL_NO_PORT";
	/**
	 *必须带一个流 
	 */
	public static final String LBL_BUFFER="LBL_BUFFER";
	/**
	 *确认初始化网元 
	 */
	public static final String LBL_INITIALIZTION="LBL_INITIALIZTION";
	
        public static final String TIP_UPGRADE="TIP_UPGRADE";
	/**
	 * 存在非运行状态任务，不能挂起
	 */
	public static final String TIP_NOSTOPTOHANG="TIP_NOSTOPTOHANG";
	/**
	 *终止的任务不能转导运行状态
	 */
	public static final String TIP_NOSTOPTORUN="TIP_NOSTOPTORUN";
	/**
	 * 域已经存在
	 */
	public static final String TiP_FiledISEXISTS="TiP_FiledISEXISTS";
	public static final String LBL_DIFSITE="LBL_DIFSITE"; //"真实网元和虚拟网元不可同时操作"
		
	public static final String LBL_IRTUALSITEACTIVITY="LBL_IRTUALSITEACTIVITY"; //虚拟网元不可激活
	
	public static final String LBL_IRTUALSITE="LBL_IRTUALSITE"; //虚拟网元不支持此功能
	/**
	 * 网元数据上载下载
	 */
	public static final String TiP_POSITION="TiP_POSITION";
	
	/**
	 * 网元数据上载下载文件目录不存在
	 */
	public static final String TiP_POSITION_FILEEXIT="TiP_POSITION_FILEEXIT";
	
	/**
	 * 请选择客户
	 */
	public static final String TIP_CLIENT_SELECT = "TIP_CLIENT_SELECT"; 
	
	/**
	 * 请选择A端
	 */
	public static final String TIP_PLEASE_SELECT_A="TIP_PLEASE_SELECT_A";
	
	/**
	 * 请选择Z端
	 */
	public static final String TIP_PLEASE_SELECT_Z="TIP_PLEASE_SELECT_Z"; 
	
	/**
	 * 请计算工作路由
	 */
	public static final String TIP_PLEASE_WORKROUTER="TIP_PLEASE_WORKROUTER"; 
	
	/**
	 * 请计算保护路由
	 */
	public static final String TIP_PLEASE_PROTROUTER="TIP_PLEASE_PROTROUTER"; 
	
	public static final String TIP_IPERROR_SEARCH = "TIP_IPERROR_SEARCH"; //请输入正确的IP

	/**
	 * 您的用户被注销
	 */
	public static final String TIP_USERONLINE_LOGOUT="TIP_USERONLINE_LOGOUT";
	/**
	 * 您的用户被注销
	 */
	public static final String TIP_MUST_SEGMENTSUBNET="TIP_MUST_SEGMENTSUBNET";

	/**
	 * 保存失败
	 */
	public static final String TIP_SAVE_FAIL = "TIP_SAVE_FAIL";
	/**
	 * 初始化
	 */
	public static final String TIP_INITIALISE = "TIP_INITIALISE";
	/**
	 * 请选择速率相同的端口
	 */
	public static final String TIP_SAME_SPEED_PROT = "TIP_SAME_SPEED_PROT";
	/**
	 * tunnelId超出设备限制
	 */
	public static final String TIP_TUNNELID="TIP_TUNNELID";
	/**
	 * 1:1保护Id超出设备限制
	 */
	public static final String TIP_TUNNEL_PROTECT_ID="TIP_TUNNEL_PROTECT_ID";
	/**
	 * PWId超出设备限制
	 */
	public static final String TIP_PWID="TIP_PWID";
	/**
	 * elineId超出设备限制
	 */
	public static final String TIP_ELINEID="TIP_ELINEID";
	/**
	 * cesId超出设备限制
	 */
	public static final String TIP_CESID="TIP_CESID";
	/**
	 * 环网保护Id超出设备限制
	 */
	public static final String TIP_WRAPPINGID = "TIP_WRAPPINGID";
	/**
	 * etreeId超出设备限制
	 */
	public static final String TIP_ETREEID ="TIP_ETREEID";
	/**
	 * elanId超出设备限制
	 */
	public static final String TIP_ELANID ="TIP_ELANID";

	/**
	 * 历史性能中时间选择的标签信息
	 * 
	 */
	 public static final String START = "START";//从
	 public static final String END = "END";//到
	 public static final String CUSTOMDATESCOPE = "CUSTOMDATESCOPE";//输入筛选器的自定义日期范围
	 public static final String TIMEREGEX = "TIMEREGEX";//"时间格式为:1900-00-00 00:00:00"
	 public static final String CUSTOMSCOPE = "CUSTOMSCOPE";//"自定义范围"
	 
	 public static final String IMPORTDATE = "IMPORTDATE";//请输入时间
	 public static final String DATEREGEXFALSE = "DATEREGEXFALSE";//时间格式不正确
	 public static final String STARTTIMEANDENDTIME = "STARTTIMEANDENDTIME";//开始时间不能大于结束时间
	 public static final String STARTTIMEANOWTIME = "STARTTIMEANOWTIME";//开始时间不能大于当前时间
	 public static final String STARTTIMEERROR = "STARTTIMEERROR";//开始时间不能小于当前时间
	 

	/**
	 * 创建数量超出范围，可创建数量最大为：
	 */
	public static final String TIP_CREATE_MAX_NUM = "TIP_CREATE_MAX_NUM";
	/**
	 * 节点ID不能相同
	 */
	public static final String TIP_NODEID_DIFFERENT = "TIP_NODEID_DIFFERENT";
	
	/**
	 * 节点ID已存在
	 */
	public static final String TIP_NODEID_EXIT = "TIP_NODEID_EXIT";
	/**
	 * 请填入正确的时间
	 */
	public static final String TIP_CONFIRM_TIME = "TIP_CONFIRM_TIME";
	/**
	 * 被锁定,不能创建pw
	 */
	public static final String TIP_TUNNEL_LCK = "TIP_TUNNEL_LCK";
	/**
	 * 无路由时提示
	 */
	public static final String TIP_NO_ROUTE = "TIP_NO_ROUTE";
	/**
	 * 设置必经路径
	 */
	public static final String TIP_SETMUSTPATH = "TIP_SETMUSTPATH";
	/**
	 * 取消设置必经路径
	 */
	
	public static final String TIP_SETPROMUSTPATH = "TIP_SETPROMUSTPATH";
	/**
	 * 设置必经路径
	 */
	public static final String TIP_CANCELPROSETMUSTPATH = "TIP_CANCELPROSETMUSTPATH";
	/**
	 * 取消设置必经路径
	 */
	public static final String TIP_CANCELSETMUSTPATH ="TIP_CANCELSETMUSTPATH";
	/**
	 * 无SNCP保护路径，无法建立该TUNNEL
	 */
	public static final String TIP_SNCP_ROUTR="TIP_SNCP_ROUTR"; 
	/**
	 * 该配置存在按需OAM，请先清除
	 */
	public static final String TIP_CLEAN_OAM = "TIP_CLEAN_OAM"; 
	/**
	 * mepId的取值范围在0-8191之间
	 */
	public static final String TIP_MEPID = "TIP_MEPID"; 
	/**
	 * tstTLV长度的取值范围在29-65535之间
	 */
	public static final String TIP_TST_TLVLENGTH = "TIP_TST_TLVLENGTH";
	/**
	 * 环回TLV长度的取值范围在1-65535之间
	 */
	public static final String TIP_CYCYLE_TLVLENGTH = "TIP_CYCYLE_TLVLENGTH";
	/**
	 * TLV测试内容的取值范围在0-255之间
	 */
	public static final String TIP_CYCYLE_TLVCONTENT = "TIP_CYCYLE_TLVCONTENT";
	/**
	 * LT TTL的取值范围在1-255之间
	 */
	public static final String TIP_LT_TTL = "TIP_LT_TTL";
	/**
	 * 没有可用的pw
	 */
	public static final String TIP_NO_PW = "TIP_NO_PW";
		
	/**
	 * 成功{C}条，失败{S}条
	 */
	public static final String TIP_BATCH_CREATE_RESULT="TIP_BATCH_CREATE_RESULT";
	/**
	 * 域下存在网关网元
	 */
	public static final String TIP_GATEWAY_EXIT="TIP_GATEWAY_EXIT";
	/**
	 * 只能是电话格式
	 */
	public static final String MESSAGE_PHONENUMBER="MESSAGE_PHONENUMBER";
	/**
	 * OAM的数量不能超过10条
	 */
	public static final String TIP_OAM_LIMIT_10="TIP_OAM_LIMIT_10";
	/**
	 * pw和ac不匹配
	 */
	public static final String TIP_PWANDACNOMATE="TIP_PWANDACNOMATE";
	//
	public static final String TIP_PWANDACNOMATE_OTHER="TIP_PWANDACNOMATE_OTHER";
	/**
	 * 被换环保护使用
	 */
	public static final String TIP_DELETE_SEGMENT_LOOPPROTECT="TIP_DELETE_SEGMENT_LOOPPROTECT";
	/**
	 * 请选择根节点和端口
	 */
	public static final String TIP_SELECT_ROOT_PORT="TIP_SELECT_ROOT_PORT";
	
	/**
	 * 请选择根节点的端口
	 */
	public static final String TIP_SELECT_ROOT_PORTERROR="TIP_SELECT_ROOT_PORTERROR";
	/**
	 * 请选择叶子节点
	 */
	public static final String TIP_SELECT_BRANCH="TIP_SELECT_BRANCH";
	/**
	 * 请选择叶子端口
	 */
	public static final String TIP_SELECT_BRANCH_PORT="TIP_SELECT_BRANCH_PORT";
	/**
	 * lbTTL的取值范围在1-255之间
	 */
	public static final String TIP_LB_TTL = "TIP_LB_TTL";
	
	/**
	 * 没有可用SNCP保护选择
	 */
	public static final String TIP_NO_SNCP="TIP_NO_SNCP"; 
	/**
	 * 同一个端口的出标签不能一样
	 */
	public static final String TIP_SAMEPORT_SAMELABEL="TIP_SAMEPORT_SAMELABEL";
	/**
	 * 没有可用VPLS选择
	 */
	public static final String TIP_NO_VPLS="TIP_NO_VPLS"; 
	/**
	 * A、Z端口速率不同，不能创建段
	 */
	public static final String TIP_CREATE_SEGMENT_SPEED="TIP_CREATE_SEGMENT_SPEED";
	/**
	 * 选择一条数据下载项
	 */
	public static final String TIP_SELECT_DATA_DOWNLOAD="TIP_SELECT_DATA_DOWNLOAD";
	/**
	 * 路径选择不对
	 */
	public static final String TIP_RONGPATH = "TIP_RONGPATH";
	
	/**
	 * 网元数量超出网管管理最大网元数
	 */
	public static final String TIP_SITENUM_BEYOND="TIP_SITENUM_BEYOND";
	/**
	 * IP格式不对
	 */
	public static final String TIP_IP_ERROR="TIP_IP_ERROR";
	/**
	 * 附加流不可超过16个
	 */
	public static final String TIP_APPENDBUFFER_LIMIT="TIP_APPENDBUFFER_LIMIT";
	/**
	 * vlanId的值在 1-4094之间
	 */
	public static final String TIP_VLANID_1_4094="TIP_VLANID_1_4094";
	/**
	 * vlanId上限 >= vlanId下限
	 */
	public static final String TIP_MAXVLANID_MINVLANID="TIP_MAXVLANID_MINVLANID";
	/**
	 * 此标签已被占用
	 */
	public static final String TIP_LABEL_ISUSED="TIP_LABEL_ISUSED";
	/**
	 * 被qinq使用
	 */
	public static final String TIP_DELETE_SEGMENT_QINQ="TIP_DELETE_SEGMENT_QINQ";
	/**
	 * 请选择目的辅节点
	 */
	public static final String TIP_STAND_PROTECT="TIP_STAND_PROTECT";
	/**
	 * 请选择目的主节点
	 */
	public static final String TIP_STAND_MAIN="TIP_STAND_MAIN";
	
	/**
	 * 关联Tunnel不能是1:1
	 */
	public static final String TIP_RELEVANCE_TUNNEL="TIP_RELEVANCE_TUNNEL";
	/**
	 * Tunnel已经被双规保护使用
	 */
	public static final String TIP_TUNNEL_BEUSEDBYDUAL="TIP_TUNNEL_BEUSEDBYDUAL";
	/**
	 * Tunnel已被环保护使用
	 */
	public static final String TIP_TUNNEL_BEUSEDBYWRAP="TIP_TUNNEL_BEUSEDBYWRAP";
	/**
	 * 请选择一条穿通隧道
	 */
	public static final String TIP_TUNNELSELECT_FORDUAL="TIP_TUNNELSELECT_FORDUAL";
	/**
	 * qinqId超出设备限制
	 */
	public static final String TIP_QINQID="TIP_QINQID";
	
	public static final String TIP_MANULL="TIP_MANULL";//不能为空
	 
	public static final String TIP_TUNNEL_USED="TIP_TUNNEL_USED";
	public static final String TIP_PORT_USED="TIP_PORT_USED";
	/**
	 * 请选择LAG
	 */
	public static final String TIP_CHOOSELAG="TIP_CHOOSELAG";
	/**
	 * 无关联规则，只能建一条细分qos
	 */
	public static final String TIP_NORELEVANCE="TIP_NORELEVANCE";
	
	/**
	 * 清除 日志某一日期以前的数据
	 */
	public static final String TIP_REMOVER_DATALOG = "TIP_REMOVER_DATALOG";// 确定清除
	public static final String TIP_REMOVER_DATALOGLAST = "TIP_REMOVER_DATALOGLAST";// 以前的数据吗
	
	/**
	 * pwProtectId超出设备限制
	 */
	public static final String TIP_PWPROTECTID="TIP_PWPROTECTID";
	
	/**
	 * 网元类型不匹配
	 */
	public static final String TIP_SITETYPEDIF = "TIP_SITETYPEDIF";
	/**
	 * 查询成功
	 */
	public static final String TIP_QUERY_SUCCESS = "TIP_QUERY_SUCCESS"; 

	
	/**
	 * 请选择同步项
	 */
	public static final String TIP_SELECT_DATA_SYNCHRO = "TIP_SELECT_DATA_SYNCHRO";
	/**
	 * 文件正在使用中
	 */
	public static final String TIP_FILE_USERED="TIP_FILE_USERED";
	/**
	 * 修改线路时钟接口
	 */
	public static final String TIP_LINE_CLOCK="TIP_LINE_CLOCK";
	/**
	 * 业务带宽小于流带宽总和
	 */
	public static final String TIP_QOS_BUFFER="TIP_QOS_BUFFER";
	/**
	 * 超出范围
	 */
	public static final String TIP_OUT_LIMIT="TIP_OUT_LIMIT";
	/**
	 * 每个网元的黑名单MAC最多有50个
	 */
	public static final String TIP_MAX_MAC = "TIP_MAX_MAC";
	
	/**
	 * 每个网元的黑白MAC地址最多有50个
	 */
	public static final String TIP_BLACKWHITEMAX_MAC = "TIP_BLACKWHITEMAX_MAC";
	
	/**
	 * 数据为空
	 */
	public static final String TIP_NO_DATA = "TIP_NO_DATA";
	/**
	 * 基于端口
	 */
	public static final String TIP_BASE_PORT="TIP_BASE_PORT";
	/**
	 * 基于端口+VLAN
	 */
	public static final String TIP_BASE_PORT_VLAN="TIP_BASE_PORT_VLAN";
	/**
	 * 请先配置主动OAM
	 */
	public static final String TIP_OAM_CONFIG="TIP_OAM_CONFIG";
	/**
	 * vlan和mac重复
	 */
	public static final String TIP_VLAN_MAC_REPEAT = "TIP_VLAN_MAC_REPEAT";
	/**
	 * 700系列网元不支持此功能
	 */
	public static final String TIP_CX_NE_NONSUPPORT="TIP_CX_NE_NONSUPPORT";
	
	
	/**
	 * 700系列网元不支持SNCP类型Tunnel
	 */
	public static final String TIP_CX_NE_NONSUPPORT_SNCP="TIP_CX_NE_NONSUPPORT_SNCP";
	
	/**
	 * 最大长度为7
	 */
	public static final String TIP_OAM_MEGID_LENGHT="TIP_OAM_MEGID_LENGHT";
	/**
	 * MEPID和对端MEPID不能相同
	 */
	public static final String TIP_MEPID_DIFFERENT="TIP_MEPID_DIFFERENT";
	
	/**
	 * MEPID和对端MEPID不能相同
	 */
	public static final String TIP_LOOPBACKOVERTIME_SCOPE="TIP_LOOPBACKOVERTIME_SCOPE";
	/**
	 * 段层按需OAM,tst帧TLV长度在29到65535
	 */
	public static final String TIP_LIMIT_28_65536 = "TIP_LIMIT_28_65536"; //长度在28到65536之间
	/**
	 * pw层按需OAM,tst帧TLV长度在21到65535
	 */
	public static final String TIP_LIMIT_20_65536 = "TIP_LIMIT_20_65536"; //长度在20到65536之间
	/**
	 * 丢弃流已创建
	 * 
	 */
	public static final String TIP_DISCRAD_CREATE = "TIP_DISCRAD_CREATE"; //丢弃流已创建
	/**
	 * 丢弃流已删除
	 * 
	 */
	public static final String TIP_DISCRAD_DELETE = "TIP_DISCRAD_DELETE"; //丢弃流已删除
	/**
	 * 没有端口成员
	 */
	public static final String TIP_MUST_PORT_MEMBERS = "TIP_MUST_PORT_MEMBERS";
	/**
	 * 范围
	 */
	public static final String TIP_SCOPE= "TIP_SCOPE";
	/**
	 * 查询数据库出错
	 */
	public static final String TIP_QUERY_DATA_ERROR = "TIP_QUERY_DATA_ERROR";
	/**
	 * 升级失败
	 */
	public static final String TIP_UPGRADE_FAIL = "TIP_UPGRADE_FAIL";
	/**
	 * 下发软件摘要信息失败
	 */
	public static final String TIP_SEND_SUMMARY_FAIL = "TIP_SEND_SUMMARY_FAIL";
	/**
	 * 设备温度过高门限:60~100C
	 */
	public static final String TIP_HITEMP = "TIP_HITEMP";
	/**
	 * 设备温度过低门限:-20~40C
	 */
	public static final String TIP_LOWTEMP = "TIP_LOWTEMP";
	/**
	 * 丢包个数门限:0~65535
	 */
	public static final String TIP_LOSS_NUMBER = "TIP_LOSS_NUMBER";
	/**
	 * 请先增加端口和隧道的QoS配额
	 */
	public static final String TIP_QOSISNOTENOUGH_E1 = "TIP_QOSISNOTENOUGH_E1";
	/**
	 * 没有M网元提示
	 */
	public static final String TIP_NOMSITE = "TIP_NOMSITE";
	/**
	 * 同步出现异常
	 */
	public static final String QUERY_FAILED = "QUERY_FAILED";
	/**
	 * 不支持同步的提示
	 */
	public static final String SYNC_FAILED = "SYNC_FAILED";
	/**
	 * 网络连接异常
	 */
	public static final String NETWORK_CONNECTIONXCEPTION = "NETWORK_CONNECTIONXCEPTION";
	/**
	 * 请选择所属组
	 */
	public static final String TIP_GROUP_BELONG = "TIP_GROUP_BELONG";
	/**
	 * 组id已存在
	 */
	public static final String TIP_GROUP_EXIST = "TIP_GROUP_EXIST";
	/**
	 * 组已用完
	 */
	public static final String TIP_NO_GROUP = "TIP_NO_GROUP";
	/**
	 * 告警锁定提示
	 */
	public static final String TIP_ALARMLOCK = "TIP_ALARMLOCK";
	
	public static final String TIP_CLEARED = "TIP_CLEARED";
	
	public static final String TIP_UNCLEARED = "TIP_UNCLEARED";
	
	/**
	 * 组已存在
	 */
	public static final String TIP_EXIST_GROUP = "TIP_EXIST_GROUP";
	
	/**
	 * 请先设置IP
	 */
	public static final String TIP_CONFIG_IP = "TIP_CONFIG_IP";
	
	//超过创建最大值
	public static final String TIP_EXCEED_MAXVALUE = "TIP_EXCEED_MAXVALUE";
	
	//用户提示信息
	public static final String TIP_USER_ERROR = "TIP_USER_ERROR";
	
	//用户提示信息VLANID已被使用
	public static final String TIP_ISUSER_VLANID = "TIP_ISUSER_VLANID";
	//"设置失败:该网元的告警反转设置的为不反转!"
	public static final String TIP_ALARMRESVAL_ERROR="TIP_ALARMRESVAL_ERROR";
	
	public static final String TIP_SUSPER_USER="TIP_SUSPER_USER";//超级系统管理员
	public static final String TIP_SYSTEM_MANAGE="TIP_SYSTEM_MANAGE";//系统管理员
	public static final String TIP_SYSTEM_MAINTAIN="TIP_SYSTEM_MAINTAIN";//系统维护员
	public static final String TIP_SYSTEM_OPERATE="TIP_SYSTEM_OPERATE";//系统操作员  
	public static final String TIP_SYSTEM_MONITOR="TIP_SYSTEM_MONITOR";//系统监控员 
	public static final String LBL_TIP_DIFF="LBL_TIP_DIFF";//系统监控员 
	public static final String TIP_CHNNEL_ADD="TIP_CHNNEL_ADD";//没有可用的"端口/通道/速率等级/成帧格式/复帧格式"，无法新增新纪录
	public static final String TIP_CREATE_IP="TIP_CREATE_IP";//新建IP段
	public static final String TIT_UPDATE_IP="TIT_UPDATE_IP";//修改IP段
	public static final String TIP_USER_TYPE="TIP_USER_TYPE";//请选择用户
	public static final String TIT_CREATE_NAMERULE="TIT_CREATE_NAMERULE";//新建命名规则
	public static final String TIT_UPDATE_NAMERULE="TIT_UPDATE_NAMERULE";//修改命名规则
	public static final String TIP_UP_DOWN="TIP_UP_DOWN";//非在线网元不能上下载
	public static final String TIT_IP_IN="TIT_IP_IN";//登陆IP是否在范围之内的提示
	public static final String L2CPTRAPNUMBER="L2CPTRAPNUMBER";//协议号格式错误;正确格式"00-00"
	//
	
	/**
	 * 流的cir不能大于pir
	 */
	public static final String CIR_PIR = "CIR_PIR";//
	
	public static final String DATABASE = "DATABASE";//数据库资源查看
	public static final String DATABASEINFO = "DATABASEINFO";//数据库信息
	public static final String DATABASETABLE = "DATABASETABLE";//数据库表信息
	public static final String COURSEINFO = "COURSEINFO";//数据库进程信息
	public static final String EXPORTDATA = "EXPORTDATA";//导出数据
	public static final String CLOSE = "CLOSE";//关闭
	public static final String EMSCLIENT = "EMSCLIENT";//客户端进程信息
	public static final String EMSCLIENTINFO = "EMSCLIENTINFO";//客户端
	public static final String SERVICE = "SERVICE";//服务端
	public static final String SERVICESEINFO = "SERVICESEINFO";//服务端进程信息
	public static final String COURSEINFOTABLENUMBER = "COURSEINFOTABLENUMBER";//行号
	public static final String COURSEINFOTABLEID = "COURSEINFOTABLEID";//进程ID
	public static final String COURSEINFOTABLEUSER = "COURSEINFOTABLEUSER";//用户
	public static final String COURSEINFOTABLTABLE = "COURSEINFOTABLTABLE";//数据库名称
	public static final String COURSEINFOTABLRUNNING = "COURSEINFOTABLRUNNING";//状态
	
	public static final String DATAFIND = "DATAFIND";//数据库资源查询
	public static final String DATASERVICE = "DATASERVICE";//数据库服务器基本信息
	public static final String DESCSTRING = "DESCSTRING";//对表空间,数据表信息，进程信息进行查看,同时可以对信息进行导出操作
	public static final String FINDBUTTON = "FINDBUTTON";//查看
	
	public static final String TABLENAME = "TABLENAME";//表名
	public static final String COUNTSIZE = "COUNTSIZE";//记录数
	public static final String KEEPSPACE = "KEEPSPACE";//保留空间(KB)
	public static final String DATASPACE = "DATASPACE";//数据空间(KB)
	public static final String INDEXSPACE = "INDEXSPACE";//索引空间(KB)
	
	public static final String DATABASESIZE = "DATABASESIZE";//数据库大小
	public static final String FREEDPACE = "FREEDPACE";//未分配空间
	public static final String DATASPACE1 = "DATASPACE1";//数据空间
	public static final String INDEXSPACE1 = "INDEXSPACE1";//索引空间
	
	public static final String EQNAME = "EQNAME";//设备树
	public static final String DATABSENAME = "DATABSENAME";//数据库
	
	public static final String EQNAMES = "EQNAMES";//设备名称:    SQLService@
	public static final String EQTYPE = "EQTYPE";//设备类型:    数据库服务器
	public static final String DBTYPE = "DBTYPE";//数据库类型:  
	public static final String DBPORT = "DBPORT";//数据库端口:  
	public static final String DBVER = "DBVER";//数据库版本:  
	public static final String HOSTIP = "HOSTIP";//主机IP:      
	
	
	/**
	 * qos带宽不足
	 */
	public static final String TIP_QOS_ALARM = "TIP_QOS_ALARM";
	
	/**
	 * 只有在线网元才能进行一致性检测
	 */
	public static final String TIP_ONLY_ONLINE = "TIP_ONLY_ONLINE";
	//性能监控对象达到上限
	public static final String TIP_PERFORMANCETASKUP = "TIP_PERFORMANCETASKUP"; //监控对象达到上限
	/**
	 * elan列表
	 */
	public static final String TIP_ELAN = "TIP_ELAN";
	/**
	 * 数据为空,无法比较
	 */
	public static final String TIP_DATAISNULL = "TIP_DATAISNULL";
	/**
	 * eline列表
	 */
	public static final String TIP_ELINE = "TIP_ELINE";
	/**
	 * etree列表
	 */
	public static final String TIP_ETREE = "TIP_ETREE";
	/**
	 * ces列表
	 */
	public static final String TIP_CES = "TIP_CES";
	/**
	 * 简单流信息列表
	 */
	public static final String TIP_SIMPLEQOS = "TIP_SIMPLEQOS";
	/**
	 * 细分流信息列表
	 */
	public static final String TIP_BUFFQOS = "TIP_BUFFQOS";
	/**
	 * 细分流
	 */
	public static final String TIP_BUFF = "TIP_BUFF";
	/**
	 * NNI流信息列表
	 */
	public static final String TIP_NNIBUFFLIST = "TIP_NNIBUFFLIST";
	/**
	 * NNI流
	 */
	public static final String TIP_NNIBUFF = "TIP_NNIBUFF";
	/**
	 * 源TCP/UDP端口号
	 */
	public static final String TIP_SOURCE_TCP = "TIP_SOURCE_TCP";
	/**
	 * 宿TCP/UDP端口号
	 */
	public static final String TIP_END_TCP = "TIP_END_TCP";
	/**
	 * 基于用户优先级到PHB映射
	 */
	public static final String TIP_BASE_PHB = "TIP_BASE_PHB";
	/**
	 * TUNNEL列表
	 */
	public static final String TIP_TUNNEL_TABLE = "TIP_TUNNEL_TABLE";
	/**
	 * 网管数据为空,请保存设备数据
	 */
	public static final String TIP_EMSISNULL = "TIP_EMSISNULL";
	/**
	 * 设备数据为空,请保存网管数据
	 */
	public static final String TIP_NEISNULL = "TIP_NEISNULL";
	/**
	 * 去激活tunnel被pw使用
	 */
	public static final String TIP_ISUSEDBYPW_UNACTIVE = "TIP_ISUSEDBYPW_UNACTIVE";
	/**
	 * 名字段类型
	 */
	public static final String TIP_NAME_TYPE = "TIP_NAME_TYPE";
	/**
	 * 取值
	 */
	public static final String TIP_NAME_VALUE = "TIP_NAME_VALUE";
	
	/**
	 * 常量
	 */
	public static final String TIP_STATIC_ATTRIBUTE = "TIP_STATIC_ATTRIBUTE";
	/**
	 * 变量
	 */
	public static final String TIP_CHANGE_ATTRIBUTE = "TIP_CHANGE_ATTRIBUTE";
	/**
	 * 连接符
	 */
	public static final String TIP_CONNECT_ATTRIBUTE = "TIP_CONNECT_ATTRIBUTE";
	/**
	 * 层速率
	 */
	public static final String TIP_LAYERRATE = "TIP_LAYERRATE";
	/**
	 * 显示规则
	 */
	public static final String TIP_SHOW_TYPE = "TIP_SHOW_TYPE";
	/**
	 * 升级完成，设备在2分钟后将自动复位
	 */
	public static final String TIP_UPDATE_SUCCESSED_REBOOT = "TIP_UPDATE_SUCCESSED_REBOOT";
	/**
	 * 删除用户中包含正在使用的用户,请重新选择
	 */
	public static final String TIP_USER_IS_ONLINE = "TIP_USER_IS_ONLINE"; 
	/**
	 * 服务器连接异常
	 */
	public static final String TIP_SERVER_CONNECTION_EXCEPTION = "TIP_SERVER_CONNECTION_EXCEPTION";
	/**
	 * 非在线网元操作成功
	 */
	public static final String TIP_NOT_ONLINE_SUCCESS = "TIP_NOT_ONLINE_SUCCESS";
	public static final String TIP_OFFNLINE_SUCCESS = "TIP_OFFNLINE_SUCCESS";
	/**
	 * 请先配置按需OAM
	 */
	public static final String TIP_CONFIG_TEST_OAM = "TIP_CONFIG_TEST_OAM";
	/**
	 * 确认复位吗
	 */
	public static final String TIP_IS_RESET = "TIP_IS_RESET";
	public static final String TIP_DELETE_SEGMENT_RING = "TIP_DELETE_SEGMENT_RING";
	/**
	 * 该配置是未激活状态
	 */
	public static final String TIP_CONFIG_UNACTIVATE = "TIP_CONFIG_UNACTIVATE";
	/**
	 * MAC地址学习条目数的范围:1-30000
	 */
	public static final String TIP_MAC_COUNT_1_30000 = "TIP_MAC_COUNT_1_30000";
	/**
	 * 数据库监控和CPU内存监控
	 */
	public static final String MOINTOR_LABEL_NAME = "MOINTOR_LABEL_NAME";//名称
	public static final String MOINTOR_LABEL_CPURATE = "MOINTOR_LABEL_CPURATE";//CPU使用率
	public static final String MOINTOR_LABEL_CPUFREE = "MOINTOR_LABEL_CPUFREE";//CPU当前空闲率
	public static final String MOINTOR_LABEL_CPUTIME = "MOINTOR_LABEL_CPUTIME";//时间点
	public static final String MOINTOR_LABEL_TOTALMEMORY = "MOINTOR_LABEL_TOTALMEMORY";//总内存(MB)
	public static final String MOINTOR_LABEL_USERMEMORY = "MOINTOR_LABEL_USERMEMORY";//已使用(MB)
	public static final String MOINTOR_LABEL_USERRATE = "MOINTOR_LABEL_USERRATE";//使用率
	public static final String MOINTOR_LABEL_FREEMEMORY = "MOINTOR_LABEL_FREEMEMORY";//剩余内存(M)
	public static final String MOINTOR_LABEL_MEMORY = "MOINTOR_LABEL_MEMORY";//内存
	public static final String MOINTOR_LABEL_VALUE = "MOINTOR_LABEL_VALUE";//监控
	public static final String MOINTOR_LABEL_VALUE1 = "MOINTOR_LABEL_VALUE1";//不监控
	public static final String MOINTOR_LABEL_TYPE = "MOINTOR_LABEL_TYPE";//控制类型
	public static final String MOINTOR_LABEL_OBJECT = "MOINTOR_LABEL_OBJECT";//监控对象
	public static final String MOINTOR_LABEL_CIRCALE = "MOINTOR_LABEL_CIRCALE";//监控阀值(严重)%
	public static final String MOINTOR_LABEL_MAJOR = "MOINTOR_LABEL_MAJOR";//监控阀值(主要)%
	public static final String MOINTOR_LABEL_MINOR = "MOINTOR_LABEL_MINOR";//监控阀值(次要)%
	public static final String MOINTOR_LABEL_WARNING = "MOINTOR_LABEL_WARNING";//监控阀值(警告)%
	public static final String MOINTOR_LABEL_DB = "MOINTOR_LABEL_DB";//数据资源监控
	public static final String MOINTOR_LABEL_SERVICEPER = "MOINTOR_LABEL_SERVICEPER";//服务器性能监控
	public static final String MOINTOR_LABEL_SERVICEFIND = "MOINTOR_LABEL_SERVICEFIND";//服务器性能查看
	public static final String MOINTOR_LABEL_SERVICEBASEINFO = "MOINTOR_LABEL_SERVICEBASEINFO";//服务器基本信息
	public static final String MOINTOR_LABEL_START= "MOINTOR_LABEL_START";//开始
	public static final String MOINTOR_LABEL_STOP= "MOINTOR_LABEL_STOP";//停止
	public static final String MOINTOR_LABEL_SERVICETEXT1= "MOINTOR_LABEL_SERVICETEXT1";//对服务器性能进行实时监控,其中包括对CPU,内存，硬盘的周期的设
	public static final String MOINTOR_LABEL_SERVICETEXT2= "MOINTOR_LABEL_SERVICETEXT2";//置,同时也可以对其使用状况进行查看.
	public static final String MOINTOR_LABEL_DBTEXT1= "MOINTOR_LABEL_DBTEXT1";//对数据库硬盘空间进行监控,同时可以对监控周期进行设置也可以
	public static final String MOINTOR_LABEL_DBTEXT2= "MOINTOR_LABEL_DBTEXT2";//对数据库表空间进行选择性的监控，超过设置的阀值则发送告警信息。
	public static final String MOINTOR_LABEL_CPUTEXT= "MOINTOR_LABEL_CPUTEXT";//对CPU,内存,硬盘监控阀值进行设置,当超过阀值时则发送告警信息"
	public static final String MOINTOR_LABEL_SET= "MOINTOR_LABEL_SET";//设置
	public static final String MOINTOR_LABEL_EQUIETNAME= "MOINTOR_LABEL_EQUIETNAME";//设备名称:     应用服务器
	public static final String MOINTOR_LABEL_EQUIETTYPE= "MOINTOR_LABEL_EQUIETTYPE";//设备类型:     服务器
	public static final String MOINTOR_LABEL_NEVERSIONS= "MOINTOR_LABEL_NEVERSIONS";//网管版本号:   
	public static final String MOINTOR_LABEL_HOSTIP= "MOINTOR_LABEL_HOSTIP";//主机IP:       
	public static final String MOINTOR_LABEL_HOSTNAME= "MOINTOR_LABEL_HOSTNAME";//主机名:       
	public static final String MOINTOR_LABEL_OS= "MOINTOR_LABEL_OS";//操作系统:     
	public static final String MOINTOR_LABEL_SERVICEINFO= "MOINTOR_LABEL_SERVICEINFO";//应用服务器性能查看
	public static final String MOINTOR_LABEL_CPUINFO= "MOINTOR_LABEL_CPUINFO";//CPU信息
	public static final String MOINTOR_LABEL_MEMORYINFO= "MOINTOR_LABEL_MEMORYINFO";//内存信息
	public static final String MOINTOR_LABEL_DISCINFO= "MOINTOR_LABEL_DISCINFO";//硬盘信息
	public static final String MOINTOR_LABEL_CPUMEMORY= "MOINTOR_LABEL_CPUMEMORY";//CPU,内存监控
	public static final String MOINTOR_LABEL_DISC= "MOINTOR_LABEL_DISC";//硬盘监控
	public static final String MOINTOR_LABEL_CYCLE= "MOINTOR_LABEL_CYCLE";//监控周期(s)
	public static final String MOINTOR_LABEL_CPUMEMORYSET= "MOINTOR_LABEL_CPUMEMORYSET";//CPU,内存监控阀值控制设置
	public static final String MOINTOR_LABEL_MEMORYSET= "MOINTOR_LABEL_MEMORYSET";//硬盘监控阀值设置
	public static final String MOINTOR_LABEL_SELECTOBJECT= "MOINTOR_LABEL_SELECTOBJECT";//请选择一个监控对象
	public static final String MOINTOR_LABEL_SURE= "MOINTOR_LABEL_SURE";//确定
	public static final String MOINTOR_LABEL_CANCEL= "MOINTOR_LABEL_CANCEL";//取消
	public static final String MOINTOR_LABEL_MOINTORSELECT= "MOINTOR_LABEL_MOINTORSELECT";//监控选择
	public static final String MOINTOR_LABEL_DBTOTAL= "MOINTOR_LABEL_DBTOTAL";//数据库总空间使用监控
	public static final String MOINTOR_LABEL_DBUSER= "MOINTOR_LABEL_DBUSER";//"数据库使用空间监控"
	public static final String MOINTOR_LABEL_DBCYCLE= "MOINTOR_LABEL_DBCYCLE";//周期信息
	public static final String MOINTOR_LABEL_DBCYCLEMIN= "MOINTOR_LABEL_DBCYCLEMIN";//监控周期(分钟):
	public static final String MOINTOR_LABEL_DBTOTALMIN= "MOINTOR_LABEL_DBTOTALMIN";//数据库总空间使用监控设置
	public static final String MOINTOR_LABEL_DBTOTALTHRESHOLD= "MOINTOR_LABEL_DBTOTALTHRESHOLD";//数据库总空间使用告警门限(GB):
	public static final String MOINTOR_LABEL_DBUSERMIN= "MOINTOR_LABEL_DBUSERMIN";//数据库使用空间监控
	public static final String MOINTOR_LABEL_DBMINSET= "MOINTOR_LABEL_DBMINSET";//数据库监控配置
	public static final String MOINTOR_LABEL_USERSERVICE= "MOINTOR_LABEL_USERSERVICE";//应用服务器
	public static final String MOINTOR_LABEL_USERSERVICE1= "MOINTOR_LABEL_USERSERVICE1";//服务器
	public static final String TIP_LABEL_DBTOTAL= "MOINTOR_LABEL_DBTOTAL";//数据库总空间
	public static final String TIP_LABEL_EMSSERVICE= "TIP_LABEL_EMSSERVICE";//EMS服务器_
	
	/**************单站多段配置**********************/
	public static final String SING1_MULTE_MSPWLABELError= "SING1_MULTE_MSPWLABELError";//请配置多段PW的配置属性
	public static final String SING1_MULTE_MANAGER= "SING1_MULTE_MANAGER";//多段配置管理
	public static final String SING1_MULTE_ATTRIBUTE= "SING1_MULTE_ATTRIBUTE";//多段PW的属性列表
	public static final String SING1_MULTE_LSPERROR= "SING1_MULTE_LSPERROR";//前向LSP或者后项LSP为空
	public static final String SING1_MULTE_LSPPORTERROR= "SING1_MULTE_LSPPORTERROR";//前向LSP和后项LSP端口能相同
	public static final String SING1_MULTE_LSP= "SING1_MULTE_LSP";//前向LSP和后项LSP不能相同
	public static final String SING1_MULTE_FRONTLSP= "SING1_MULTE_FRONTLSP";//前向LSP
	public static final String SING1_MULTE_BALCKLSP= "SING1_MULTE_BALCKLSP";//后向LSP
	public static final String SING1_MULTE_FRONTINLSP= "SING1_MULTE_FRONTINLSP";//前向入标签
	public static final String SING1_MULTE_FRONTBLACKLSP= "SING1_MULTE_FRONTBLACKLSP";//前向出标签
	public static final String SING1_MULTE_BACKINLSP= "SING1_MULTE_BACKINLSP";//后向入标签
	public static final String SING1_MULTE_BACLOUTLSP= "SING1_MULTE_BACLOUTLSP";//后向出标签
	public static final String SING1_MULTE_FRONTPORT= "SING1_MULTE_FRONTPORT";//可选前端口
	public static final String SING1_MULTE_BLACKPORT= "SING1_MULTE_BLACKPORT";//可选后端口
	
	//双规PW
	public static final String SY_DUAL_PW_FAILED ="SY_DUAL_PW_FAILED";//双规PW保护同步PW没找到
	public static final String SY_DUAL_TUNNEL_FAILED ="SY_DUAL_TUNNEL_FAILED";//双规PW保护同步tunnnel没找到
	
	public static final String TIP_MAC_NUM = "TIP_MAC_NUM";
	
	public static final String TIP_MAC_ADDRESS = "TIP_MAC_ADDRESS";//MAC地址
	
	public static final String TIT_CREATE_STATIC_MAC ="TIT_CREATE_STATIC_MAC";//新建MAC地址
	public static final String TIT_UPDATE_STATIC_MAC ="TIT_UPDATE_STATIC_MAC";//修改mac地址
	public static final String TIP_VLANERROR = "TIP_VLANERROR";//同一个端口VLAN值不能一样
	
	/**
	 * 
	 * 操作日志中英文
	 */
	public static final String TIP_OPERATION = "TIP_OPERATION";//创建tunnel时QoS未配
	public static final String TIP_OPERATION1 = "TIP_OPERATION1";//导出时文件正在使用中
	public static final String TIP_OPERATION2 = "TIP_OPERATION2";//页面数据转为 Excel 表中创建表头失败
	public static final String TIP_OPERATION3 = "TIP_OPERATION3";//删除时请选择一条数据再进行操作
	public static final String TIP_OPERATION4 = "TIP_OPERATION4";//修改时请选择一条数据再进行操作
	public static final String TIP_OPERATION5 = "TIP_OPERATION5";//操作拓扑图是配置失败
	public static final String TIP_OPERATION6 = "TIP_OPERATION6";//数据转储时请选择要激活的对象
	public static final String TIP_OPERATION7 = "TIP_OPERATION7";//时请选择数据再进行操作
	public static final String TIP_OPERATION8 = "TIP_OPERATION8";//上载/下载配置文件时
	public static final String TIP_OPERATION9 = "TIP_OPERATION9";//创建和修改UDA组时
	public static final String TIP_OPERATION10 = "TIP_OPERATION10";//设置网元IP时
	public static final String TIP_OPERATION11 = "TIP_OPERATION11";//初始化网元时
	public static final String TIP_OPERATION12 = "TIP_OPERATION12";//下发网元填写工作站IP时
	public static final String TIP_OPERATION13 = "TIP_OPERATION13";//下发网元工作站 IP1时
	public static final String TIP_OPERATION14 = "TIP_OPERATION14";//下发网元工作站 IP2时
	public static final String TIP_OPERATION15 = "TIP_OPERATION15";//下发网元工作站 IP3时
	public static final String TIP_OPERATION16 = "TIP_OPERATION16";//下发网元工作站 IP4时
	public static final String TIP_OPERATION17 = "TIP_OPERATION17";//下发网元工作站 IP5时
	public static final String TIP_OPERATION18 = "TIP_OPERATION18";//下发网元工作站 IP6时
	public static final String TIP_OPERATION19 = "TIP_OPERATION19";//网元手工校时时
	public static final String TIP_OPERATION20 = "TIP_OPERATION20";//修改网元时
	public static final String TIP_OPERATION21 = "TIP_OPERATION21";//同步网元时
	public static final String TIP_OPERATION22 = "TIP_OPERATION22";//网元数据下载时
	public static final String TIP_OPERATION23 = "TIP_OPERATION23";//标签统计时
	public static final String TIP_OPERATION24 = "TIP_OPERATION24";//用户在线查看注销时
	public static final String TIP_OPERATION25 = "TIP_OPERATION25";//用户管理解锁时
	public static final String TIP_OPERATION26 = "TIP_OPERATION26";//强制解锁时
	public static final String TIP_OPERATION27 = "TIP_OPERATION27";//新建或修改角色时
	public static final String TIP_OPERATION28 = "TIP_OPERATION28";//在角色管理中删除角色时
	public static final String TIP_OPERATION29 = "TIP_OPERATION29";//在新建或修改时
	public static final String TIP_OPERATION30 = "TIP_OPERATION30";//在删除段时
	public static final String TIP_OPERATION31 = "TIP_OPERATION31";//在创建段配置OAM时
	public static final String TIP_OPERATION32 = "TIP_OPERATION32";//在创建段或修改段时
	public static final String TIP_OPERATION33 = "TIP_OPERATION33";//在创建段时
	public static final String TIP_OPERATION34 = "TIP_OPERATION34";//在创建段或修改段自动命名时
	public static final String TIP_OPERATION35 = "TIP_OPERATION35";//在修改网络测CES业务时
	public static final String TIP_OPERATION36 = "TIP_OPERATION36";//在创建或修改网络测CES业务时
	public static final String TIP_OPERATION37 = "TIP_OPERATION37";//在创建或修改网络测双规保护时
	public static final String TIP_OPERATION38 = "TIP_OPERATION38";//在创建或修改网络测ELAN业务时
	public static final String TIP_OPERATION39 = "TIP_OPERATION39";//在修改网络测ELINE业务时
	public static final String TIP_OPERATION40 = "TIP_OPERATION40";//在设置环网倒换时
	public static final String TIP_OPERATION41 = "TIP_OPERATION41";//在删除网络测PW时
	public static final String TIP_OPERATION42 = "TIP_OPERATION42";//在修改网络测PW时
	public static final String TIP_OPERATION43 = "TIP_OPERATION43";//在网络测配置PWVLAN配置时
	public static final String TIP_OPERATION44 = "TIP_OPERATION44";//在修改QiQ配置时
	public static final String TIP_OPERATION45 = "TIP_OPERATION45";//在修改或创建PWOAM时
	public static final String TIP_OPERATION46 = "TIP_OPERATION46";//在修改或创建段OAM时
	public static final String TIP_OPERATION47 = "TIP_OPERATION47";//在修改或创建TUNNELOAM时
	public static final String TIP_OPERATION48 = "TIP_OPERATION48";//在修改或创建按需段OAM时
	public static final String TIP_OPERATION49 = "TIP_OPERATION49";//在修改或创建按需TUNNELOAM时
	public static final String TIP_OPERATION50 = "TIP_OPERATION50";//在修改或创建按需PWOAM时
	public static final String TIP_OPERATION51 = "TIP_OPERATION51";//在修改或创建TNP时
	public static final String TIP_OPERATION52 = "TIP_OPERATION52";//在删除网络测TUNNEL时
	public static final String TIP_OPERATION53 = "TIP_OPERATION53";//在网络测TUNNEL创建倒换时
	public static final String TIP_OPERATION54 = "TIP_OPERATION54";//在单站测删除CES业务时
	public static final String TIP_OPERATION55 = "TIP_OPERATION55";//在单站测检查CES一致性时 
	public static final String TIP_OPERATION56 = "TIP_OPERATION56";//在单站侧创建或修改CES业务时 
	public static final String TIP_OPERATION57 = "TIP_OPERATION57";//在单站侧删除ELAN业务时 
	public static final String TIP_OPERATION58 = "TIP_OPERATION58";//在单站侧检查ELAN业务一致性时
	public static final String TIP_OPERATION59 = "TIP_OPERATION59";//在单站侧创建或修改ELAN业务
	public static final String TIP_OPERATION60 = "TIP_OPERATION60";//在单站侧删除ELINE业务时 
	public static final String TIP_OPERATION61 = "TIP_OPERATION61";//在单站侧检查ELINE业务一致性时
	public static final String TIP_OPERATION62 = "TIP_OPERATION62";//在单站侧创建或修改ELINE业务
	public static final String TIP_OPERATION63 = "TIP_OPERATION63";//在修改端口配置时
	public static final String TIP_OPERATION64 = "TIP_OPERATION64";//在检查端口一致性时
	public static final String TIP_OPERATION65 = "TIP_OPERATION65";//在修改端口配置QoS时
	public static final String TIP_OPERATION66 = "TIP_OPERATION66";//在单站侧检查ETREE业务一致性时
	public static final String TIP_OPERATION67 = "TIP_OPERATION67";//在单站侧创建或修改ETREE业务
	public static final String TIP_OPERATION68 = "TIP_OPERATION68";//在单站侧删除PW业务
	public static final String TIP_OPERATION69 = "TIP_OPERATION69";//在单站侧检查PW业务一致性时
	public static final String TIP_OPERATION70 = "TIP_OPERATION70";//在单站侧创建和修改PW业务时
	public static final String TIP_OPERATION71 = "TIP_OPERATION71";//在单站侧配置PW端口时
	public static final String TIP_OPERATION72 = "TIP_OPERATION72";//在单站侧删除TUNNEL业务时
	public static final String TIP_OPERATION73 = "TIP_OPERATION73";//在单站侧TUNNEL一致性时
	public static final String TIP_OPERATION74 = "TIP_OPERATION74";//在单站侧 创建或修改TUNNEL时
	public static final String TIP_OPERATION75 = "TIP_OPERATION75";//在单站侧TUNNEL1:1倒换时
	public static final String TIP_OPERATION76 = "TIP_OPERATION76";//在单站侧 创建或修改TUNNEL时
	public static final String TIP_OPERATION77 = "TIP_OPERATION77";//在单站侧 创建或修改LAG时
	public static final String TIP_OPERATION78 = "TIP_OPERATION78";//在单站侧 创建LAG时
	public static final String TIP_OPERATION79 = "TIP_OPERATION79";//在单站侧 创建或修改LAG,选择端口时
	public static final String TIP_OPERATION80 = "TIP_OPERATION80";//在创建或修改OAM时
	public static final String TIP_OPERATION81 = "TIP_OPERATION81";//在删除OAM时
	public static final String TIP_OPERATION82 = "TIP_OPERATION82";//在修改OAM时
	public static final String TIP_OPERATION83 = "TIP_OPERATION83";//在新建OAM时
	public static final String TIP_OPERATION84 = "TIP_OPERATION84";//在同步OAM时
	public static final String TIP_OPERATION85 = "TIP_OPERATION85";//在新建或修改PWOAM时
	public static final String TIP_OPERATION86 = "TIP_OPERATION86";//在新建或修改段OAM时
	public static final String TIP_OPERATION87 = "TIP_OPERATION87";//在新建或修改TUNNELOAM时
	public static final String TIP_OPERATION88 = "TIP_OPERATION88";//在单站侧修改LAG时
	public static final String TIP_OPERATION89 = "TIP_OPERATION89";//在单站侧删除ETREE业务时 
	public static final String TIP_OPERATION90 = "TIP_OPERATION90";//在单站侧删除ETREE业务时 
	public static final String TIP_TIMEERROR="TIP_TIMEERROR";//700系列网元不支持此功能
	/******添加成功后的操作日志************/
	public static final String TIP_SYSALARM="TIP_SYSALARM";//同步当前告警
	//网元批量升级
	public static final String TIP_NOT_TYPE = "TIP_NOT_TYPE";
	public static final String TIP_UNLOAD_CARD = "TIP_UNLOAD_CARD";//未加载板卡，请重新选择网元
	public static final String TIP_QUERY_FAIL = "TIP_QUERY_FAIL";//查询摘要失败
	public static final String TIP_UNCHOUSEN = "TIP_UNCHOUSEN";//查询摘要失败
	public static final String TIP_CANCLE_UPDATE = "TIP_CANCLE_UPDATE";//是否取消批量升级
	/**********添加操作日志***************/
	public static final String TIP_ALARSHIELD= "TIP_ALARSHIELD";//告警反转
	public static final String TIP_ALARSHIELDS= "TIP_ALARSHIELDS";//告警屏蔽设置
	public static final String TIP_NETWORKCESACTIVE= "TIP_NETWORKCESACTIVE";//网络侧CES激活
	public static final String TIP_NETWORKCESDOACTIVE= "TIP_NETWORKCESDOACTIVE";//网络侧CES去激活
	public static final String TIP_LOOPPROTECT= "TIP_LOOPPROTECT";//环网保护设置
	public static final String TIP_INSERTQIQ= "TIP_INSERTQIQ";//QIQ新建
	public static final String TIP_UPDATEQIQ= "TIP_UPDATEQIQ";//QIQ修改
	public static final String TIP_NETELAEACTIVE= "TIP_NETELAEACTIVE";//网络侧ELAN业务激活
	public static final String TIP_NETELAEDOACTICE= "TIP_NETELAEDOACTICE";//网络侧ELAN业务去激活
	public static final String TIP_NETELAEDOELINEACTICE= "TIP_NETELAEDOELINEACTICE";//网络侧Eline业务激活
	public static final String TIP_NETELAEELINEDOACTICE= "TIP_NETELAEELINEDOACTICE";//网络侧Eline业务去激活
	public static final String TIP_NETELAEDOETREEEACTICE= "TIP_NETELAEDOETREEEACTICE";//网络侧ETree业务激活
	public static final String TIP_NETELAEETREEDOACTICE= "TIP_NETELAEETREEDOACTICE";//网络侧ETree业务去激活
	public static final String TIP_DELETEQIQ= "TIP_DELETEQIQ";//删除QIQ业务
	public static final String TIP_INSERTTNP= "TIP_INSERTTNP";//新建TNP通道
	public static final String TIP_UPDATETTNP= "TIP_UPDATETTNP";//修改TNP通道
	public static final String TIP_DELETEACL= "TIP_DELETEACL";//删除ACL配置成功
	public static final String TIP_UPDATEALLCONFIG= "TIP_UPDATEALLCONFIG";//修改全局配置块成功
	public static final String TIP_DELETEBLACK= "TIP_DELETEBLACK";//删除黑白名单配置成功
	public static final String TIP_INSERTBLACK= "TIP_INSERTBLACK";//新建黑白名单配置成功
	public static final String TIP_UPDATEBLACK= "TIP_UPDATEBLACK";//修改黑白名单配置成功
	public static final String TIP_SINGCESACTIVE= "TIP_SINGCESACTIVE";//单站侧CES激活配置成功
	public static final String TIP_SINGCESDOACTIVE= "TIP_SINGCESDOACTIVE";//单站侧CES去激活配置成功
	public static final String TIP_DELETEDISC= "TIP_DELETEDISC";//删除丢弃流配置成功
	public static final String TIP_INSERTDISC= "TIP_INSERTDISC";//新建丢弃流配置成功
	public static final String TIP_UPDATEDUAL= "TIP_UPDATEDUAL";//修改双规保护配置块成功
	public static final String TIP_INSERTDUAL= "TIP_INSERTDUAL";//新建双规保护配置块成功
	public static final String TIP_SINGELANACTIVE= "TIP_SINGELANACTIVE";//单站侧Elan业务激活成功
	public static final String TIP_SINGELANDOACTIVE= "TIP_SINGELANDOACTIVE";//单站侧Elan业务去激活成功
	public static final String TIP_SINGELINEACTIVE= "TIP_SINGELINEACTIVE";//单站侧Eline业务激活成功
	public static final String TIP_SINGELINEDOACTIVE= "TIP_SINGELINEDOACTIVE";//单站侧Eline业务去激活成功
	public static final String TIP_PORTQOS= "TIP_PORTQOS";//端口配置QoS业务配置成功
	public static final String TIP_ETHLOOPINSERT= "TIP_ETHLOOPINSERT";//以太网环回新建成功
	public static final String TIP_ETHLOOPUPDATE= "TIP_ETHLOOPUPDATE";//以太网环回修改成功
	public static final String TIP_DELETEETHSERVICE= "TIP_DELETEETHSERVICE";//以太网二层业务配置块删除成功
	public static final String TIP_UPDATEETHSERVICE= "TIP_UPDATEETHSERVICE";//以太网环回同步
	public static final String TIP_SINGETREEACTIVE= "TIP_SINGETREEACTIVE";//单站侧Etree激活配置成功
	public static final String TIP_SINGETREEDOACTIVE= "TIP_SINGETREEDOACTIVE";//单站侧Etree去激活配置成功
	public static final String TIP_INSERTGORUP= "TIP_INSERTGORUP";//新建静态组播管理配置成功
	public static final String TIP_UPDATEGORUP= "TIP_UPDATEGORUP";//修改静态组播管理配置成功
	public static final String TIP_DELETEGORUP= "TIP_DELETEGORUP";//删除静态组播管理配置成功
	public static final String TIP_CREATEL2CP= "TIP_CREATEL2CP";//新建L2CP配置成功
	public static final String TIP_UPDATEL2CP= "TIP_UPDATEL2CP";//修改L2CP配置成功
	public static final String TIP_DELETEMAC= "TIP_DELETEMAC";//删除MAC学习数目限制管理
	public static final String TIP_DELETEBACLMANAGER= "TIP_DELETEBACLMANAGER";//删除黑名单MAC管理
	public static final String TIP_CREATEBACLMANAGER= "TIP_CREATEBACLMANAGER";//新建黑名单MAC管理
	public static final String TIP_UPDATEBACLMANAGER= "TIP_UPDATEBACLMANAGER";//修改黑名单MAC管理
	public static final String TIP_CREATEMAC= "TIP_CREATEMAC";//新建MAC学习数目限制管理
	public static final String TIP_UPDATEMAC= "TIP_UPDATEMAC";//修改MAC学习数目限制管理
	public static final String TIP_UPDATEE1= "TIP_UPDATEE1";//修改E1端口
	public static final String TIP_E1SYC= "TIP_E1SYC";//E1端口同步
	public static final String TIP_V35SYC= "TIP_V35SYC";//V35端口管理同步
	public static final String TIP_V35UPDATE= "TIP_V35UPDATE";//V35端口管理修改
	public static final String TIP_V35UPCREATE= "TIP_V35UPCREATE";//V35端口管理新建
	public static final String TIP_PERFORMILTUPDATE= "TIP_PERFORMILTUPDATE";//性能门限修改
	public static final String TIP_PERFORMILTSYC= "TIP_PERFORMILTSYC";//性能门限同步
	public static final String TIP_PHBTOPRI= "TIP_PHBTOPRI";//PHB To PRI映射表管理
	public static final String TIP_RESTMANAGER= "TIP_RESTMANAGER";//复位管理
	public static final String TIP_fanMANAGER= "TIP_fanMANAGER";//智能风扇管理
	public static final String TIP_E1STATE= "TIP_E1STATE";//E1仿真状态查询
	public static final String TIP_LINKOAM= "TIP_LINKOAM";//接入链路oam查询
	public static final String TIP_OAMMEP= "TIP_OAMMEP";//接入链路以太网OAM对端MEP信息
	public static final String TIP_ETHOAM= "TIP_ETHOAM";//接入链路以太网oam状态查询 
	public static final String TIP_OAMPING= "TIP_OAMPING";//以太网OAM ping状态信息查询 
	public static final String TIP_OAMTRACE= "TIP_OAMTRACE";//以太网OAM TRACE状态信息
	public static final String TIP_PORTSTATE= "TIP_PORTSTATE";//端口状态信息查询
	public static final String TIP_PWSTATE= "TIP_PWSTATE";//PW状态信息查询
	public static final String TIP_TUNNELSTATE= "TIP_TUNNELSTATE";//TUNNEL状态信息查询
	public static final String TIP_SERVICESTATE= "TIP_SERVICESTATE";//业务状态信息查询
	public static final String TIP_LOOPPROTECTSTATE= "TIP_LOOPPROTECTSTATE";//环保护状态信息查询
	public static final String TIP_STATICSTATE= "TIP_STATICSTATE";//静态单播管理
	public static final String TIP_TDMLOOP= "TIP_TDMLOOP";//TDM环回
	public static final String TIP_TUNNELPROTECT= "TIP_TUNNELPROTECT";//TUNNEL保护倒换
	public static final String TIP_PWOAM= "TIP_PWOAM";//单站PW-OAM
	public static final String TIP_DEGMENTOAM= "TIP_DEGMENTOAM";//单站段层-OAM
	public static final String TIP_TUNNELOAM= "TIP_TUNNELOAM";//单站TUNNEL-OAM
	public static final String TIP_NE= "TIP_NE";//网元解锁
	public static final String TIP_MIOINTORSERVICE= "TIP_MIOINTORSERVICE";//配置监控服务器数据
	public static final String TIP_DB= "TIP_DB";//配置监控数据库数据
	public static final String TIP_UPGRADENE= "TIP_UPGRADENE";//设置升级
	public static final String TIP_SEGMENTOAM= "TIP_SEGMENTOAM";//单站段层主动OAM
	public static final String TIP_ETHOAMSING= "TIP_ETHOAMSING";//单站以太网OAM
	public static final String TIP_ETHLINKOAM= "TIP_ETHLINKOAM";//单站以太网链路OAM
	public static final String TIP_PWOAMSING= "TIP_PWOAMSING";//单站pw主动OAM
	public static final String TIP_TUNNELOAMSING= "TIP_TUNNELOAMSING";//单站tunnel主动OAM
	public static final String TIP_SEGMENTTESTOAM= "TIP_SEGMENTTESTOAM";//单站段层按需OAM
	public static final String TIP_PWTESTOAM= "TIP_PWTESTOAM";//单站PW按需OAM
	public static final String TIP_TUNNELTESTOAM= "TIP_TUNNELTESTOAM";//单站TUNNEL按需OAM
	public static final String TIP_EXCEEDPWNUMBER="TIP_EXCEEDPWNUMBER";//创建单站Etree业务通道PW超过最大值64
	public static final String TIP_EXCEEDACNUMBER="TIP_EXCEEDACNUMBER";//创建单站Etree业务AC超过最大值10
	public static final String TIP_EXCEEDPWELANNUMBER="TIP_EXCEEDPWELANNUMBER";//Elan业务通道PW超过最大值64
	public static final String TIP_EXCEEDACELANNUMBER="TIP_EXCEEDACELANNUMBER";//Elan业务AC超过最大值10

	/**
	 * 没有可操作的网元
	 */
	public static final String TIP_NO_SELECTED_SITE = "TIP_NO_SELECTED_SITE";
	/**
	 * 原端口不能为空
	 */
	public static final String TIP_SOURCE_PORT_IS_NULL = "TIP_SOURCE_PORT_IS_NULL";
	/**
	 * 已选端口不能为空
	 */
	public static final String TIP_END_PORT_IS_NULL = "TIP_END_PORT_IS_NULL";
	/**
	 * 原端口和已选端口数量不匹配
	 */
	public static final String TIP_SOURCE_END_PORT_NOT_EQUAL = "TIP_SOURCE_END_PORT_NOT_EQUAL";
	/**
	 * 一次只能操作一个端口
	 */
	public static final String TIP_OPERATION_PORT_ONCE = "TIP_OPERATION_PORT_ONCE";
	/**
	 * 单站侧多段PW不需要配置VLAN
	 */
	public static final String TIP_DOULEPW = "TIP_DOULEPW";
	/**
	 * 多条流的关联属性值不能一样
	 */
	public static final String TIP_MORE_BUFFER_VALUE_UNIQUENESS = "TIP_MORE_BUFFER_VALUE_UNIQUENESS";
	/**
	 * 多条AC的流关联属性值不能一样
	 */
	public static final String TIP_MORE_AC_VALUE_UNIQUENESS = "TIP_MORE_AC_VALUE_UNIQUENESS";
	public static final String TIP_NOVIDEO = "TIP_NOVIDEO";
	/**
	 * 有E1端口被CES业务使用
	 */
	public static final String TIP_IS_USEDBY_CES = "TIP_IS_USEDBY_CES";
	/**
	 * 范围1-4095
	 */
	public static final String TIP_PVID_1_4095 = "TIP_PVID_1_4095";
	/**
	 * VLAN上限应大于等于下限
	 */
	public static final String TIP_OUTVALN_INVLAN = "TIP_OUTVALN_INVLAN";
	public static final String TIP_NAMERULE = "TIP_NAMERULE";
	public static final String TIP_SNCPPRO = "TIP_SNCPPRO";
	public static final String TIP_SELNAMETYPE = "TIP_SELNAMETYPE";
	public static final String TIP_LAGMODE0 = "TIP_LAGMODE0";
	public static final String TIP_LAGMODE1 = "TIP_LAGMODE1";
	public static final String TIP_LAGMODE2 = "TIP_LAGMODE2";
	public static final String TIP_LAGMODE3 = "TIP_LAGMODE3";
	public static final String TIP_LAGMODE4 = "TIP_LAGMODE4";
	public static final String TIP_LAGMODE5 = "TIP_LAGMODE5";
	public static final String TIP_LAGMODE6 = "TIP_LAGMODE6";
	public static final String TIP_LAGMODE7 = "TIP_LAGMODE7";
	public static final String TIP_RETURN0 = "TIP_RETURN0";
	public static final String TIP_RETURN1 = "TIP_RETURN1";
	public static final String TIP_CLOSE = "TIP_CLOSE";
	public static final String TIP_OPEN = "TIP_OPEN";
	public static final String TIP_PORTSTATE0 = "TIP_PORTSTATE0";
	public static final String TIP_PORTSTATE1 = "TIP_PORTSTATE1";
	public static final String TIP_PORTSTATE2 = "TIP_PORTSTATE2";
	public static final String TIP_WORKMODE1 = "TIP_WORKMODE1";
	public static final String TIP_WORKMODE2 = "TIP_WORKMODE2";
	public static final String TIP_WORKMODE3 = "TIP_WORKMODE3";
	public static final String TIP_WORKMODE4 = "TIP_WORKMODE4";
	public static final String TIP_WORKMODE5 = "TIP_WORKMODE5";
	/**
	 * 请选择A/Z端网元
	 */
	public static final String TIP_SELECT_AZ_SITE = "TIP_SELECT_AZ_SITE";
	/**
	 * 端口数量不匹配
	 */
	public static final String TIP_PORTCOUNT_NOT_EQUAL = "TIP_PORTCOUNT_NOT_EQUAL";
	//时间同步
	public static final String TIP_INDEXERROR = "TIP_INDEXERROR";
	public static final String TIP_INDEXERROR1 = "TIP_INDEXERROR1";
	public static final String TIP_INDEXERROR2 = "TIP_INDEXERROR2";
	public static final String TIP_MAXNUM = "TIP_MAXNUM";
	public static final String TIP_PORTIDERROR1="TIP_PORTIDERROR1";
	public static final String TIP_PORTIDERROR2="TIP_PORTIDERROR2";
	public static final String TIP_PORTIDERROR="TIP_PORTIDERROR";
	public static final String TIP_DOMAINERROR="TIP_DOMAINERROR";
	public static final String TIP_ANNOUNCEERROR="TIP_ANNOUNCEERROR";
	
	public static final String TIP_DELAYOVERERROR="TIP_DELAYOVERERROR";
	public static final String TIP_SYNCICLEERROR="TIP_SYNCICLEERROR";
	public static final String TIP_NOTECICLEERROR="TIP_NOTECICLEERROR";
	public static final String TIP_OUTDELAYERROR="TIP_OUTDELAYERROR";
	public static final String TIP_INDELAYERROR="TIP_INTDELAYERROR";
	
	public static final String TIP_SRCCLOCKPRI1ERROR="TIP_SRCCLOCKPRI1ERROR";
	public static final String TIP_SRCCLOCKPRI2ERROR="TIP_SRCCLOCKPRI2ERROR";
	public static final String TIP_SRCCLOCKLEVELERROR="TIP_SRCCLOCKLEVELERROR";
	
	public static final String TIP_DOMAINERROR1="TIP_DOMAINERROR1";
	public static final String TIP_ANNOUNCEERROR1="TIP_ANNOUNCEERROR1";
	
	public static final String TIP_DELAYOVERERROR1="TIP_DELAYOVERERROR1";
	public static final String TIP_SYNCICLEERROR1="TIP_SYNCICLEERROR1";
	public static final String TIP_NOTECICLEERROR1="TIP_NOTECICLEERROR1";
	public static final String TIP_OUTDELAYERROR1="TIP_OUTDELAYERROR1";
	public static final String TIP_INDELAYERROR1="TIP_INTDELAYERROR1";
	
	public static final String TIP_SRCCLOCKPRI1ERROR1="TIP_SRCCLOCKPRI1ERROR1";
	public static final String TIP_SRCCLOCKPRI2ERROR1="TIP_SRCCLOCKPRI2ERROR1";
	public static final String TIP_SRCCLOCKLEVELERROR1="TIP_SRCCLOCKLEVELERROR1";
	public static final String TIP_SRCCLOCKIDERROR="TIP_SRCCLOCKIDERROR";
	public static final String TIP_SRCCLOCKIDERROR1="TIP_SRCCLOCKIDERROR1";
	//在线网元托管时不能进行删除，修改为离线网元才能删除
	public static final String TIP_NOT_DELETEONLINE = "TIP_NOT_DELETEONLINE";
	public static final String TIP_SEGMENTNOT_DELETEONLINE = "TIP_SEGMENTNOT_DELETEONLINE";
	public static final String TIP_TUNNELNOT_DELETEONLINE = "TIP_TUNNELNOT_DELETEONLINE";
	public static final String TIP_PWNOT_DELETEONLINE ="TIP_PWNOT_DELETEONLINE";
	public static final String TIP_ELINENOT_DELETEONLINE = "TIP_ELINENOT_DELETEONLINE";
	public static final String TIP_ONLINENOT_DELETEONLINE = "TIP_ONLINENOT_DELETEONLINE";
	public static final String TIP_ETREENOT_DELETEONLINE = "TIP_ETREENOT_DELETEONLINE";
	public static final String TIP_ELANNOT_DELETEONLINE = "TIP_ELANNOT_DELETEONLINE";
	public static final String TIP_DUALNOT_DELETEONLINE ="TIP_DUALNOT_DELETEONLINE";
	public static final String TIP_CESNOT_DELETEONLINE ="TIP_CESNOT_DELETEONLINE";
	public static final String TIP_QINQNOT_DELETEONLINE ="TIP_QINQNOT_DELETEONLINE";
	public static final String TIP_LOOPNOT_DELETEONLINE ="TIP_LOOPNOT_DELETEONLINE";
	public static final String TIP_DCDUALNOT_DELETEONLINE ="TIP_DCDUALNOT_DELETEONLINE";
	public static final String TIP_ETHSERVICENOT_DELETEONLINE ="TIP_ETHSERVICENOT_DELETEONLINE";
	public static final String TIP_SECONDMACNOT_DELETEONLINE ="TIP_SECONDMACNOT_DELETEONLINE";
	public static final String TIP_ACLNOT_DELETEONLINE ="TIP_ACLNOT_DELETEONLINE";
	public static final String TIP_STATICUNINOT_DELETEONLINE ="TIP_STATICUNINOT_DELETEONLINE";
	public static final String TIP_DCELANNOT_DELETEONLINE ="TIP_DCELANNOT_DELETEONLINE";
	public static final String TIP_DCETREENOT_DELETEONLINE ="TIP_DCETREENOT_DELETEONLINE";
	public static final String TIP_DCELINENOT_DELETEONLINE ="TIP_DCELINENOT_DELETEONLINE";
	public static final String TIP_DCCESENOT_DELETEONLINE ="TIP_DCCESENOT_DELETEONLINE";
	public static final String TIP_DCPWENOT_DELETEONLINE ="TIP_DCPWENOT_DELETEONLINE";
	public static final String TIP_DCTUNNELENOT_DELETEONLINE ="TIP_DCTUNNELENOT_DELETEONLINE";
	public static final String TIP_DCLAGNOT_DELETEONLINE = "TIP_DCLAGNOT_DELETEONLINE";
	public static final String TIP_DCPTPPORTNOT_DELETEONLINE ="TIP_DCPTPPORTNOT_DELETEONLINE";
	public static final String TIP_DCDISCARDFLOWNOT_DELETEONLINE ="TIP_DCDISCARDFLOWNOT_DELETEONLINE";
	public static final String TIP_DCETHOAMNOT_DELETEONLINE ="TIP_DCETHOAMNOT_DELETEONLINE";
	public static final String TIP_DCETHLINKOAMNOT_DELETEONLINE="TIP_DCETHLINKOAMNOT_DELETEONLINE";
	public static final String TIP_DCSEGMENTOAMNOT_DELETEONLINE="TIP_DCSEGMENTOAMNOT_DELETEONLINE";
	public static final String TIP_DCPWOAMNOT_DELETEONLINE="TIP_DCPWOAMNOT_DELETEONLINE";
	public static final String TIP_DCTUNNELOAMNOT_DELETEONLINE="TIP_DCTUNNELOAMNOT_DELETEONLINE";
	public static final String TIP_DCAXSEGMENTOAMNOT_DELETEONLINE="TIP_DCAXSEGMENTOAMNOT_DELETEONLINE";
	public static final String TIP_DCAXTUNNELOAMNOT_DELETEONLINE="TIP_DCAXTUNNELOAMNOT_DELETEONLINE";
	public static final String TIP_DCAXPWOAMNOT_DELETEONLINE="TIP_DCAXPWOAMNOT_DELETEONLINE";
	public static final String TIP_WLAXTUNNELOAMNOT_DELETEONLINE="TIP_WLAXTUNNELOAMNOT_DELETEONLINE";
	public static final String TIP_WLAXPWOAMNOT_DELETEONLINE="TIP_WLAXPWOAMNOT_DELETEONLINE";
	public static final String TIP_WLAXSEGMENTOAMNOT_DELETEONLINE="TIP_WLAXSEGMENTOAMNOT_DELETEONLINE";
	public static final String TIP_TNPDELETE_DELETEONLINE="TIP_TNPDELETE_DELETEONLINE";

	

	/**
	 * 端口qos带宽不足
	 */
	public static final String TIP_PORT_QOS_ALARM = "TIP_PORT_QOS_ALARM";
	/**
	 * 隧道qos带宽不足
	 */
	public static final String TIP_TUNNEL_QOS_ALARM = "TIP_TUNNEL_QOS_ALARM";
	/**
	 * PW和AC的QoS不匹配
	 */
	public static final String TIP_PW_AC_QOS_NOT_MATCHING = "TIP_PW_AC_QOS_NOT_MATCHING";
	/**
	 * A端AC数量不匹配
	 */
	public static final String TIP_A_AC_IS_NOT_MATCHING = "TIP_A_AC_IS_NOT_MATCHING";
	/**
	 * Z端AC数量不匹配
	 */
	public static final String TIP_Z_AC_IS_NOT_MATCHING = "TIP_Z_AC_IS_NOT_MATCHING";
	/**
	 * PW数量不匹配
	 */
	public static final String TIP_PW_IS_NOT_MATCHING = "TIP_PW_IS_NOT_MATCHING";
	/**
	 * 请先配置ELAN业务
	 */
	public static final String TIP_CREATE_ELAN = "TIP_CREATE_ELAN";

	
	//同步二层静态MAC
		public static final String TIP_SYS_SECONDMAC="TIP_SYS_SECONDMAC";
		//同步环网保护
		public static final String TIP_SYS_LOOPPROTECT="TIP_SYS_LOOPPROTECT";
		//时间同步
		public static final String TIP_SYS_TIMESYN="TIP_SYS_TIMESYN";
	/**
	 * 在线托管网元删除黑白MAC
	 *
	 */
	public static final String TIP_BLACKANDWHITEDELETE_DELETEONLINE = "TIP_BLACKANDWHITEDELETE_DELETEONLINE";	
	//在线托管网元删除黑名单MAC
	public static final String TIP_MACMANAGEDELETE_DELETEONLINE ="TIP_MACMANAGEDELETE_DELETEONLINE";
	//L2CP同步	
	public static final String TIP_SYS_L2CP="TIP_SYS_L2CP";
	//MAC黑名单同步
	public static final String TIP_SYS_MACMANAGE="TIP_SYS_MACMANAGE";
	//MAC黑白名单同步
	public static final String TIP_SYS_BLACKWHITEMAC="TIP_SYS_BLACKWHITEMAC";
	//开启告警声音
	public static final String TIP_ALARMSOUND_OPEN="MENU_ALARMSOUND_OPEN";
	//关闭告警声音
	public static final String TIP_ALARMSOUND_CLOSE="MENU_ALARMSOUND_CLOSE";
	//个性化设置操作日志
	public static final String TIP_SELF_MANAGE="TIP_SELF_MANAGE";
	//屏幕锁定操作日志
	public static final String TIP_HAND_LOCK="TIP_HAND_LOCK";
	//修改命名规则
	public static final String TIP_UPDATE_SETNAMERULE="TIP_UPDATE_SETNAMERULE";
	//下发
	public static final String TIP_SITE_WORKIP="TIP_SITE_WORKIP";
	//取消批量升级
	public static final String TIP_CANCLE_BATCHUPGRADE="TIP_CANCLE_BATCHUPGRADE";
	//性能门限同步
	public static final String TIP_SYNC_PM="TIP_SYNC_PM";
	//修改以太网链路OAM
	public static final String TIP_UPDATE_ETHLINKOAM="TIP_UPDATE_ETHLINKOAM";
	//新建以太网链路OAM
	public static final String TIP_INSERT_ETHLINKOAM="TIP_INSERT_ETHLINKOAM";
	//端口聚合查询
	public static final String TIP_LAG_PORTSTATUS="TIP_LAG_PORTSTATUS";
	//pw保护状态查询
	public static final String TIP_PWPROTECT_STATUS="TIP_PWPROTECT_STATUS";
	//lsp保护状态查询
	public static final String TIP_LSPPROTECT_STATUS="TIP_LSPPROTECT_STATUS";
	//时钟状态变成信息查询
	public static final String TIP_CLOCK_STATUS="TIP_CLOCK_STATUS";
	//时钟状态基本信息查询
	public static final String TIP_CLOCKBASIC_STATUS="TIP_CLOCKBASIC_STATUS";
	//单站升级查询摘要
	public static final String TIP_DOWN_SERVISON="TIP_DOWN_SERVISON";
	//批量升级查询摘要
	public static final String TIP_BATCHDOWN_SERVISON="TIP_BATCHDOWN_SERVISON";
	//取消单站升级	
	public static final String TIP_CANCLE_UPGRADE="TIP_CANCLE_UPGRADE";
	//时钟频率同步
	public static final String TIP_SYNC_CLOCK="TIP_SYNC_CLOCK";
	//时钟频率倒换
	public static final String TIP_ROATE_CLOCK="TIP_ROATE_CLOCK";
	//设置时钟频率
	public static final String TIP_CLOCK_COMFIRM="TIP_CLOCK_COMFIRM";
	//一致性检测保存网管数据
	public static final String TIP_EMS_PORT="TIP_EMS_PORT";
	public static final String TIP_EMS_TUNNEL="TIP_EMS_TUNNEL";
	public static final String TIP_EMS_PW="TIP_EMS_PW";
	public static final String TIP_EMS_ELINE="TIP_EMS_ELINE";
	public static final String TIP_EMS_ETREE="TIP_EMS_ETREE";
	public static final String TIP_EMS_ELAN="TIP_EMS_ELAN";
	public static final String TIP_EMS_CES="TIP_EMS_CES";
	//一致性检测保存设备数据
	public static final String TIP_PORT_EMS="TIP_PORT_EMS";
	public static final String TIP_TUNNEL_EMS="TIP_TUNNEL_EMS";
	public static final String TIP_PW_EMS="TIP_PW_EMS";
	public static final String TIP_ELINE_EMS="TIP_ELINE_EMS";
	public static final String TIP_ETREE_EMS="TIP_ETREE_EMS";
	public static final String TIP_ELAN_EMS="TIP_ELAN_EMS";
	public static final String TIP_CES_EMS="TIP_CES_EMS";
	//网元上下载
	public static final String TIP_NE_UPLOAD="TIP_NE_UPLOAD";
	public static final String TIP_NE_DOWNLOAD="TIP_NE_DOWNLOAD";
	//段层按需oam
	public static final String TIP_TESTOAM_SEGMENTUPDATE="TIP_TESTOAM_SEGMENTUPDATE";
	public static final String TIP_TESTOAM_SEGMENTDELETE="TIP_TESTOAM_SEGMENTDELETE";
	//TUNNEL层按需oam
	public static final String TIP_TESTOAM_TUNNELUPDATE="TIP_TESTOAM_TUNNELUPDATE";
	public static final String TIP_TESTOAM_TUNNELDELETE="TIP_TESTOAM_TUNNELDELETE";
	//PW层按需oam
	public static final String TIP_TESTOAM_PWUPDATE="TIP_TESTOAM_PWUPDATE";
	public static final String TIP_TESTOAM_PWDELETE="TIP_TESTOAM_PWDELETE";
	//QOS
	public static final String TIP_TUNNEL_QOS="TIP_TUNNEL_QOS";
	public static final String TIP_PW_QOS="TIP_PW_QOS";
	public static final String TIP_TNP_DELETE="TIP_TNP_DELETE";
	//tunnel激活
	public static final String TIP_TUNNEL_ACTIVE="TIP_TUNNEL_ACTIVE";
	public static final String TIP_TUNNEL_UNACTIVE="TIP_TUNNEL_UNACTIVE";
	//PW激活
	public static final String TIP_PW_ACTIVE="TIP_PW_ACTIVE";
	public static final String TIP_PW_UNACTIVE="TIP_PW_UNACTIVE";
	/**
	 * 该端口无关联规则,不允许批量创建
	 */
	public static final String TIP_PORT_ISNOT_RELEVANCE_CANT_BATCH = "TIP_PORT_ISNOT_RELEVANCE_CANT_BATCH";
	/**
	 * vlanId超出范围
	 */
	public static final String TIP_VLANID_OUT_OF_SCOPE = "TIP_VLANID_OUT_OF_SCOPE";
	/**
	 * 源MAC超出范围
	 */
	public static final String TIP_SOURCE_MAC_OUT_OF_SCOPE = "TIP_SOURCE_MAC_OUT_OF_SCOPE";
	/**
	 * 目的MAC超出范围
	 */
	public static final String TIP_END_MAC_OUT_OF_SCOPE = "TIP_END_MAC_OUT_OF_SCOPE";
	/**
	 * 源IP超出范围
	 */
	public static final String TIP_SOURCE_IP_OUT_OF_SCOPE = "TIP_SOURCE_IP_OUT_OF_SCOPE";
	/**
	 * 目的IP超出范围
	 */
	public static final String TIP_END_IP_OUT_OF_SCOPE = "TIP_END_IP_OUT_OF_SCOPE";
	/**
	 * 请选择主用或备用
	 */
	public static final String TIP_SELECT_MAIN_OR_RESERVE = "TIP_SELECT_MAIN_OR_RESERVE";
	/**
	 * A/Z两端端口不能相同
	 */
	public static final String TIP_APORT_NOT_LIKE_ZPORT = "TIP_APORT_NOT_LIKE_ZPORT";
	/**
	 * 数据已回滚
	 */
	public static final String TIP_DATA_IS_ROLLBABK = "TIP_DATA_IS_ROLLBABK";
	/**
	 * 该条tunnel不符合扩容要求
	 */
	public static final String TIP_TUNNEL_CAN_NOT_ADD_NODE = "TIP_TUNNEL_CAN_NOT_ADD_NODE";
	/**
	 * 网元不能为空
	 */
	public static final String TIP_SITE_IS_NOT_NULL = "TIP_SITE_IS_NOT_NULL";
	/**
	 * 端口不能为空
	 */
	public static final String TIP_PORT_IS_NOT_NULL = "TIP_PORT_IS_NOT_NULL";
	/**
	 * 该条tunnel不符合缩容要求
	 */
	public static final String TIP_TUNNEL_CAN_NOT_DELETE_NODE = "TIP_TUNNEL_CAN_NOT_DELETE_NODE";
	//查询网元属性
	public static final String TIP_SELECT_NE="TIP_SELECT_NE";
	//单站qinq同步
	public static final String TIP_SYNC_QINQSINGLE="TIP_SYNC_QINQSINGLE";
	//cccid超出范围
	public static final String TIP_CCCID="TIP_CCCID";
	//CCC配置块
	public static final String TIP_INSERT_CCC="TIP_INSERT_CCC";
	public static final String TIP_UPDATE_CCC="TIP_UPDATE_CCC";
	public static final String TIP_DELETE_CCC="TIP_DELETE_CCC";
	public static final String TIP_SYNC_CCC="TIP_SYNC_CCC";
	public static final String TIP_ACTIVE_CCC="TIP_ACTIVE_CCC";
	public static final String TIP_UNACTIVE_CCC="TIP_UNACTIVE_CCC";
	public static final String TIP_DELETE_CCC1="TIP_DELETE_CCC1";
	public static final String TIP_CCC_EMS="TIP_CCC_EMS";
	public static final String TIP_EMS_CCC="TIP_EMS_CCC";
	public static final String TIP_OPERATIONC1="TIP_OPERATIONC1";
	public static final String TIP_OPERATIONC2="TIP_OPERATIONC2";
	/**
	 * CCC列表
	 */
	public static final String TIP_CCC = "TIP_CCC";
	
	public static final String TIP_SHIELD_ALARM = "TIP_SHIELD_ALARM";
	//telnet
	public static final String TIP_COMPUTERNAME = "TIP_COMPUTERNAME";
	public static final String TIP_COMPUTER_PASSWORD = "TIP_COMPUTER_PASSWORD";
	public static final String TIP_SERVER_IP = "TIP_SERVER_IP";
	public static final String TIP_NE_PASSWORD = "TIP_NE_PASSWORD";
	public static final String TIP_TELNET_MANAGE = "TIP_TELNET_MANAGE";
	public static final String SERVER_IP_MATCH="SERVER_IP_MATCH";
	//业务被静态单播所用，不能修改
	public static final String TIP_UPDATE_NOT="TIP_UPDATE_NOT";
	public static final String TIP_DELETE_NOT="TIP_DELETE_NOT";
}

