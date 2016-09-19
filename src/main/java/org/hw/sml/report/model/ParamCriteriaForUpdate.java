package org.hw.sml.report.model;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 对更新操作的参数
 * 一开始构思为接收一个map不行
 * 但考虑到其它地方的使用，及可读性分成两个map
 * @author hw
 *
 */
public class ParamCriteriaForUpdate extends Criteria{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8559926562531283326L;

	private String type=Constants.TYPE_UPDATE;//[update,insert,delete]
	
	private Map<String,Object> updateCondition=new LinkedHashMap<String,Object>();
	
	private Map<String,Object> updateField=new LinkedHashMap<String,Object>();
	
	  private boolean force = false;
	
	public ParamCriteriaForUpdate(){
		
	}
	public ParamCriteriaForUpdate(String type,Map<String,Object> updateCondition,Map<String,Object> updateField){
		this.type=type;
		this.updateCondition=updateCondition;
		this.updateField=updateField;
	}
	public ParamCriteriaForUpdate(String type,KeyValue[] ucdField,KeyValue[] uField){
		this.type=type;
		if(ucdField!=null&&ucdField.length>0){
			for(int i=0;i<ucdField.length;i++){
				updateCondition.put(ucdField[i].getKey(), ucdField[i].getValue());
			}
		}
		if(uField!=null&&uField.length>0){
			for(int i=0;i<uField.length;i++){
				updateField.put(uField[i].getKey(), uField[i].getValue());
			}
		}
	}
	private String userInfo;
	public Map<String, Object> getUpdateCondition() {
		return updateCondition;
	}
	public void setUpdateCondition(Map<String, Object> updateCondition) {
		this.updateCondition = updateCondition;
	}
	public Map<String, Object> getUpdateField() {
		return updateField;
	}
	public void setUpdateField(Map<String, Object> updateField) {
		this.updateField = updateField;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	public boolean isForce() {
		return force;
	}
	public void setForce(boolean force) {
		this.force = force;
	}
	
	
}
