package com.nms.model.system.loginlog;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.alarm.CurrentAlarmInfo;
import com.nms.db.bean.alarm.HisAlarmInfo;
import com.nms.db.bean.system.loginlog.LoginLog;
import com.nms.db.bean.system.loginlog.UserLock;
import com.nms.db.bean.system.user.UserInst;
import com.nms.db.dao.system.loginlog.LoginLogMapper;
import com.nms.db.dao.system.loginlog.UserLockMapper;
import com.nms.model.alarm.CurAlarmService_MB;
import com.nms.model.alarm.HisAlarmService_MB;
import com.nms.model.system.user.UserInstServiece_Mb;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysTip;

/**
 * 表user_lock 账户锁定信息
 * 
 * @author Administrator
 * 
 */
public class UserLockServiece_MB extends ObjectService_Mybatis {
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private UserLockMapper mapper = null;
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	public UserLockMapper getMapper() {
		return mapper;
	}

	public void setMapper(UserLockMapper mapper) {
		this.mapper = mapper;
	}

	private UserLock userLock = null;
	private List<UserLock> userlockList = null;
	private List<LoginLog> loginlogList = null;
	private List<LoginLog> loginlock = null;
	private final int MAX_LOCKTYPE = 1;
	private final int SYSTEM_LOCKTYPE = 4;
	private LoginLog loginlog = new LoginLog();
	private static int count = 0;
	private static int count1 = 0;
	private int LOCKMINETES = 30;

	/**系统
	 * 查看用户是否锁定
	 * 
	 * @param userlock
	 * @return
	 * @throws Exception
	 */
	public boolean findUserLock(UserLock userlock) throws Exception {
		if (null == userlock) {
			throw new Exception("userlock is null");
		}
		boolean flag = false;
		try {
			this.sqlSession.getConnection().setAutoCommit(false);
			if (userlock != null) {
				loginlog.setUser_id(userlock.getUser_id());
				userlockList = new ArrayList<UserLock>();
				userlockList = this.mapper.selectLockType(userlock, this.MAX_LOCKTYPE);
				int userlockcount = userlockList.size();
				if (userlockcount > 0) {
					userLock = userlockList.get(0);// 账户已经锁定，是否自动解锁
					if (null == userLock.getClearTime() || "".equals(userLock.getClearTime())) {
						if (userLock.getLockType() == 0) {
							String lockTime = userLock.getLockTime();
							Date lockdate1 = new Date();
							Date lockdate = (Date) df.parse(lockTime.trim());
							long time = (lockdate.getTime() / 1000) + 60 * this.LOCKMINETES;
							lockdate.setTime(time * 1000);

							long n = lockdate.getTime() - lockdate1.getTime();
							if (n <= 0) {// 已经超过30分钟了自动解锁
								userLock.setClearTime(df.format(lockdate));
								userLock.setClearType(0);
								userLock.setClearUsername("System");
								this.mapper.updateUserLock(userLock);
							} else {
								flag = true;
							}
							if (userLock.getLockType() == 1) {
								flag = true;
							}
						} else {
							flag = true;
						}
					}
				}
			}
			if(!this.sqlSession.getConnection().getAutoCommit()){
				this.sqlSession.getConnection().commit();
			}
		} catch (Exception e) {
			this.sqlSession.getConnection().rollback();
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			this.sqlSession.getConnection().setAutoCommit(true);
		}

		return flag;
	}

	/**
	 * 解锁
	 * 
	 * @param userlock
	 * @throws Exception
	 */
	public boolean clearUser(UserLock userlock, UserInst userinst) throws Exception {
		if (null == userlock) {
			throw new Exception("userlock is null");
		}
		boolean flag = this.findUserLock(userlock);
		List<UserLock> userlocklist = null;
		boolean result = false;
		if (flag) {
			// 用户已经被锁定
			userlocklist = this.mapper.selectLockType(userlock, 1);
			UserLock userLock = userlocklist.get(0);

			userLock.setClearTime(df.format(new Date()));
			userLock.setClearType(1);
			userLock.setClearUsername(userinst.getUser_Name());

			this.mapper.updateUserLock(userLock);
			userinst.setIsLock(0);
			userLock.setLock(0);
			
			//并将登陆锁定用户的登录log清除
	        LoginLogServiece_Mb loginLogServiece = (LoginLogServiece_Mb)ConstantUtil.serviceFactory.newService_MB(Services.LOGINLOGSERVIECE,this.sqlSession);
	        LoginLog loginlogClear = new LoginLog();
	        loginlogClear.setUser_id(userlock.getUser_id());
	        loginlogClear.setState(0);
	        loginLogServiece.deleteBySite(loginlogClear);
	        
	        UserInstServiece_Mb  userInstServiece = (UserInstServiece_Mb)ConstantUtil.serviceFactory.newService_MB(Services.UserInst,this.sqlSession);
	        UserInst userInstLcok = new UserInst();
	        userInstLcok.setUser_Id(userLock.getUser_id());
	        List<UserInst> userInstList = userInstServiece.select(userInstLcok);
	        if(userInstList != null && userInstList.size()>0){
	        	userInstLcok = userInstList.get(0);
	        }
	        //并且查看在当前告警中是否存在锁定用户的告警，如果存在就将当前告警清除,
	        //首先要判断当前告警中是否已经存在了,存在了就不需要在提供告警。
	        CurAlarmService_MB  alarmService = (CurAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CurrentAlarm,this.sqlSession);
	        HisAlarmService_MB	hisservice = (HisAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.HisAlarm,this.sqlSession);
		    List<CurrentAlarmInfo>	alarmList = alarmService.alarmByAlarmLevel();
			if (null != alarmList && alarmList.size() > 0) {
			 for (CurrentAlarmInfo currentAlarmInfo : alarmList) {
				 if(currentAlarmInfo.getObjectName().contains(userInstLcok.getUser_Name()) && currentAlarmInfo.getAlarmCode() == 1000){
					 alarmService.delete(currentAlarmInfo.getId());
					 currentAlarmInfo.setIsCleared(ResourceUtil.srcStr(StringKeysTip.TIP_CLEARED));
					 currentAlarmInfo.setClearedTime(new Date());
					 hisservice.saveOrUpdate(getHisAlarm(currentAlarmInfo));
				 }
			 }
		  }
			
		  UiUtil.closeService_MB(loginLogServiece);
		  UiUtil.closeService_MB(userInstServiece);
		  UiUtil.closeService_MB(alarmService);
		  UiUtil.closeService_MB(hisservice);
			
		} else {
			// 用户没有被锁定
			result = true;
		}
		return result;
	}
	
	
	
	
	/**
	 * 更新表user_lock 解锁
	 * 
	 * @param userlock
	 * @throws Exception
	 */
	/*
	 * public void updatUserLockClearTime(UserLock userlock,UserInst
	 * userinst)throws Exception{ if(null==userlock||"".equals(userlock)){ throw
	 * new Exception("userlock is null"); } boolean
	 * flag=this.findUserLock(userlock); try{ if(flag){ //用户已经被锁定
	 * if(userlock!=null&&userinst.getUser_Name()!=null){ //管理员解锁
	 * userlockList=lockdao.selectLockType(userlock, connection,
	 * this.MAX_LOCKTYPE); userlock=userlockList.get(0);
	 * userlock.setClearTime(df.format(new Date())); userlock.setClearType(1);
	 * userlock.setClearUsername(userinst.getUser_Name()); int result
	 * =lockdao.updateUserLock(userlock, connection); } else if(userlock!=null){
	 * //系统自动解锁 System.out.println("系统试试去解锁");
	 * userlockList=lockdao.selectLockType(userlock, connection,
	 * this.SYSTEM_LOCKTYPE);
	 * 
	 * userLock=userlockList.get(0);
	 * 
	 * int lockminetes=30; String lockTime=userlock.getLockTime(); Date
	 * lockdate=(Date)df.parseObject(lockTime);
	 * 
	 * long time=(lockdate.getTime()/1000)+60*lockminetes;
	 * lockdate.setTime(time*1000); int n=lockdate.compareTo(new Date());
	 * System.out.println("试试解锁"); if(n>0){//已经超过30分钟了自动解锁
	 * System.out.println("解锁成功"); userlock.setClearTime(df.format(new Date()));
	 * userlock.setClearType(0); userlock.setClearUsername("System"); int result
	 * =lockdao.updateUserLock(userlock, connection); }
	 * 
	 * } } } catch (Exception e) { ExceptionManage.dispose(e,this.getClass()); } }
	 */
	/**
	 * 更新表user_lock 锁定账户
	 * 
	 * @param userlock
	 * @param userinst
	 * @throws Exception
	 */
	public int updateLockTime(UserLock userlock, UserInst userinst) throws Exception {
		if (null == userlock) {
			throw new Exception("userlock is null");
		}
		boolean flag = this.findUserLock(userlock);
		int label = 0;
		LoginLogMapper loginLogMapper = null;
		try {
			if(!flag){				
			 if (userlock != null) {
					// 系统自动锁定
					// 账户没有锁定，是否自动锁定
				loginLogMapper =this.sqlSession.getMapper(LoginLogMapper.class);
				loginlog.setUser_id(userlock.getUser_id());
				userlockList = new ArrayList<UserLock>();
				userlockList = this.mapper.selectLockType(userlock, this.MAX_LOCKTYPE);
				int userlockcount = userlockList.size();
				loginlock = new ArrayList<LoginLog>();
				loginlock = loginLogMapper.findState(loginlog, this.SYSTEM_LOCKTYPE);
				if(loginlock!=null && loginlock.size()>0){
					Iterator iterator = loginlock.iterator();
					LoginLog log = new LoginLog();
					while (iterator.hasNext()) {
						log = (LoginLog) iterator.next();
						if (log.getState() == 0) {
							count++;
						}
					}
					LoginLog log1 = new LoginLog();
					LoginLog log2 = new LoginLog();
					Date logdate1 = new Date();
					if (count == 4) {// 最后登录3次都是失败
						for (int i = loginlock.size() - 1; i > 0; i--) {
							log1 = loginlock.get(i);
							log2 = loginlock.get(i - 1);
							logdate1 = (Date) df.parse(log1.getLoginTime().trim());
							Date logdate2 = (Date) df.parse(log2.getLoginTime().trim());
							long time = (logdate1.getTime() / 1000) + 60 * this.LOCKMINETES;
							logdate1.setTime(time * 1000);
							long n = logdate1.getTime() - logdate2.getTime();
							if (n >= 0) {
								// System.out.println("count1 is 是否间隔30分钟"+count1);
								count1++;
							}
						}
						if (count1 >= 3) {// 2次失败间隔都不到30分钟；
							// System.out.println("5次失败间隔都不到30分钟；");
							loginlogList = new ArrayList<LoginLog>();
							loginlogList = loginLogMapper.findState(loginlog, this.MAX_LOCKTYPE);
							loginlog = (LoginLog) loginlogList.get(0);
							if (userlockcount == 0) {
								userlock.setLockTime(loginlog.getLoginTime());
								userlock.setLockType(0);
								userlock.setLockUsername("System");
								this.mapper.insertUserLock(userlock);
							}
							if (userlockcount != 0) {
								// 判断
								UserLock userlock1 = userlockList.get(0);
								if (userlock1.getClearTime() != null) {
									userlock.setLockTime(loginlog.getLoginTime());
									userlock.setLockType(0);
									userlock.setLockUsername("System");
									this.mapper.insertUserLock(userlock);
								}
							}
							label = 4;
						}
					}
					count = 0;
					count1 = 0;
					}
				 }
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return label;
	}

	/**
	 * 锁定用户
	 */
	public boolean lockUser(UserLock userlock, UserInst userinst) throws Exception {
		boolean result = false;
		try {
			if (null == userlock) {
				throw new Exception("userlock is null");
			}
			boolean flag = this.findUserLock(userlock);
			if (!flag) {// 用户没有被锁定
				userlock.setLockTime(df.format(new Date()));
				userlock.setLockUsername(userinst.getUser_Name());
				userlock.setLockType(1);				
				this.mapper.insertUserLock(userlock);
				this.sqlSession.commit();
			} else {
				result = true;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return result;
	}

	
		/**
		 * 获得历史告警对象
		 * 
		 * @param curAlarmInfo
		 * @return
		 */
		public HisAlarmInfo getHisAlarm(CurrentAlarmInfo curAlarmInfo) {
			HisAlarmInfo hisAlarmInfo = new HisAlarmInfo();
			hisAlarmInfo.setSiteId(curAlarmInfo.getSiteId());
			hisAlarmInfo.setSlotId(curAlarmInfo.getSlotId());
			hisAlarmInfo.setObjectId(curAlarmInfo.getObjectId());
			hisAlarmInfo.setObjectType(curAlarmInfo.getObjectType());
			hisAlarmInfo.setAlarmCode(curAlarmInfo.getAlarmCode());
			hisAlarmInfo.setAlarmLevel(curAlarmInfo.getAlarmLevel());
			hisAlarmInfo.setObjectName(curAlarmInfo.getObjectName());
			hisAlarmInfo.setAckUser(curAlarmInfo.getAckUser());
			hisAlarmInfo.setAckTime(curAlarmInfo.getAckTime());
			hisAlarmInfo.setRaisedTime(curAlarmInfo.getRaisedTime());
			hisAlarmInfo.setIsCleared(curAlarmInfo.getIsCleared());
			hisAlarmInfo.setClearedTime(new Date());
			hisAlarmInfo.setCommonts(curAlarmInfo.getAlarmComments());

			return hisAlarmInfo;
		}
		
	/**
	 * 管理员
	 * 查看是否锁定
	 * @param userlock
	 * @return
	 * @throws Exception
	 */
	public UserLock selectLockType(UserLock userlock)throws Exception {
		if (null == userlock) {
			throw new Exception("userlock is null");
		}
		UserLock userLock=new UserLock();
		userlockList= new ArrayList<UserLock>();
		userlockList=this.mapper.selectLockType(userlock, 1);
		if(userlockList!=null && userlockList.size()>0){
			userLock=userlockList.get(0);
			
		}
		return userLock;
	}
	/**
	 * 查看（某个用户）
	 * userlock
	 * 的锁定信息
	 * @param userlock
	 * @return
	 * @throws Exception
	 */
	public List<UserLock> selectUserLock(UserLock userlock)throws Exception{
		if(null==userlock){
			throw new Exception("userlock is null");
		}
		
		List<UserLock> userlocklist=new ArrayList<UserLock>();
		userlocklist=this.mapper.selectUserLock(userlock);
		return userlocklist;
	}
}
