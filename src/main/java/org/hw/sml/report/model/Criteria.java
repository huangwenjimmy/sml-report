package org.hw.sml.report.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Criteria implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id标识
	 */
	private String rcptId;
	/**
	 * 是否忽略记录日志
	 */
	private Boolean inLog=false;
	/**
	 * 信息相关权限信息
	 */
	private String userInfo;
	/**
	 * 模块相关信息
	 */
	private String moduleCode;
	
	private Map<String,Object> extInfo=new LinkedHashMap<String,Object>();
	
	public Boolean getInLog() {
		return inLog;
	}
	public void setInLog(Boolean inLog) {
		this.inLog = inLog;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public String getRcptId() {
		return rcptId;
	}
	public void setRcptId(String rcptId) {
		this.rcptId = rcptId;
	}
	public Map<String, Object> getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(Map<String, Object> extInfo) {
		this.extInfo = extInfo;
	}
	
	
	
	
}
