package org.hw.sml.report.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置报表模块:进行查询ParamCriteria生成
 * @author wen
 *
 */
public class Queryer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4614154618748966036L;
	
	private String rcptId;
	
	private ParamCriteria paramCriteria;
	
	public Queryer(){
		paramCriteria=new ParamCriteria();
	}
	public Queryer(String rcptId){
		this.rcptId=rcptId;
		paramCriteria=new ParamCriteria();
		paramCriteria.setRcptId(rcptId);
	}
	
	public Queryer addQuery(String name,String operator,String value){
		Map<String,List<Operator>> conditions=paramCriteria.getConditionMap();
		if(conditions==null){
			conditions=new LinkedHashMap<String, List<Operator>>();
			paramCriteria.setConditionMap(conditions);
		}
		if(conditions.get(name)==null){
			conditions.put(name,new ArrayList<Operator>());
		}
		if(value!=null){
			conditions.get(name).add(new Operator(operator, value));
		}
		return this;
	}
	public Queryer addOrder(String orderName,String orderType){
		List<String> orderFields=paramCriteria.getOrderByFields();
		if(orderFields==null){
			orderFields=new ArrayList<String>();
			paramCriteria.setOrderByFields(orderFields);
		}
		if(!orderFields.contains(orderName)){
			orderFields.add(orderName);
			paramCriteria.setOrderByType(orderType);
		}
		return this;
	}
	public Queryer limit(int pageNo,int pageSize){
		paramCriteria.setRowPerPage(pageSize);
		paramCriteria.setStartIndex((pageNo-1)*pageSize+1);
		return this;
	}
	public String getRcptId() {
		return rcptId;
	}
	public Queryer setRcptId(String rcptId) {
		this.rcptId = rcptId;
		return this;
	}
	public ParamCriteria getParamCriteria() {
		return paramCriteria;
	}

}
