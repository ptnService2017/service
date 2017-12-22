package com.nms.rmi.ui.util.backups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;

import com.ibatis.common.jdbc.ScriptRunner;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysLbl;
		 
public class DataBaseUtil {
	/**  
     * 备份
     */    
    public boolean backUp(File backUpPath, List<String> tableNames) { 
    	InputStream in = null;
    	InputStreamReader isr = null;
    	BufferedReader br = null;
    	FileOutputStream fout = null;
    	OutputStreamWriter writer = null;
        try { 
        	StringBuffer sbNames = new StringBuffer("");
        	for (String tName : tableNames) {
        		sbNames.append(" "+tName);
			}
            Runtime rt = Runtime.getRuntime();     
            // 调用 mysql 的 cmd:   
            String cmd = System.getProperty("user.dir")+
    		"\\mysql\\bin\\mysqldump -uroot -p1234 -f ptn"+sbNames.toString();
//            String cmd = "D:\\Program Files\\Server"+
//    		"\\mysql\\bin\\mysqldump -uroot -p1234 -f ptn"+sbNames.toString();
            Process child = rt.exec(cmd);
            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行     
            in = child.getInputStream();// 控制台的输出信息作为输入流     
            // 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码     
            isr = new InputStreamReader(in, "utf8");
            String inStr;     
            // 组合控制台输出信息字符串     
            br = new BufferedReader(isr);
         // 要用来做导入用的sql目标文件：
            fout = new FileOutputStream(backUpPath);     
            writer = new OutputStreamWriter(fout, "utf8");
            String version = ResourceUtil.srcStr(StringKeysLbl.LBL_JLABTL3_PTN);
            writer.write("-- "+version.substring(version.length()-6, version.length())+"\r\n");//写入版本号
            writer.flush();
            while ((inStr = br.readLine()) != null) { 
            	writer.write(inStr + "\r\n");     
                // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免     
                writer.flush(); 
            }     
        } catch (Exception e) {     
            ExceptionManage.dispose(e, this.getClass());
            return false;
        } finally {
        	try {
				in.close();
			} catch (IOException e) {
				ExceptionManage.dispose(e, this.getClass());
			}     
            try {
				isr.close();
			} catch (IOException e) {
				ExceptionManage.dispose(e, this.getClass());
			}     
            try {
				br.close();
			} catch (IOException e) {
				ExceptionManage.dispose(e, this.getClass());
			}     
            try {
				writer.close();
			} catch (IOException e) {
				ExceptionManage.dispose(e, this.getClass());
			}     
            try {
				fout.close();
			} catch (IOException e) {
				ExceptionManage.dispose(e, this.getClass());
			}
			in = null;
			isr = null;
			br = null;
			writer = null;
			fout = null;
        }
        return true;
    }     
  
   /**   
    * 导入恢复 
    * @param conn 
    */    
   public boolean recover(String recoverPath, Connection conn) { 
		ScriptRunner runner = null;
		InputStreamReader inputStreamReader = null;
		try {
			runner = new ScriptRunner(conn, false, false);
			runner.setErrorLogWriter(null);
			runner.setLogWriter(null);
			inputStreamReader = new InputStreamReader(new FileInputStream(recoverPath), "UTF-8");
			runner.runScript(inputStreamReader);
		} catch (UnsupportedEncodingException e) {
			ExceptionManage.dispose(e, this.getClass());
			return false;
		} catch (FileNotFoundException e) {
			ExceptionManage.dispose(e, this.getClass());
			return false;
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
			return false;
		} finally {
			if (null != inputStreamReader) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					ExceptionManage.dispose(e, this.getClass());
				}
				inputStreamReader = null;
			}
		}
		return true;
   }
}