package org.hw.sml.report.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * 查询参数集，包含分页，排序，条件操作集，导出集，数据库类型
 * 大部分都给了默认值所以使用上也不需要进行太多值的赋予
 * @author wen
 * 
 */
public class ParamCriteria extends  Criteria{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2142676092247175569L;
	private String type=Constants.TYPE_QUERY_QUERY;//[query,template]
	private long startIndex=1;
	private int rowPerPage=10;
	private Map<String,List<Operator>> conditionMap=new LinkedHashMap<String,List<Operator>>();
	private List<String> orderByFields;
	private String orderByType;
	private List<String> intendedFields=new ArrayList<String>();
	private ChartParam chart;
	private String exportType="xlsx";//[xlsx,csv]
	private Boolean flush=false;
	private String sqlType=Constants.TYPE_SQL_SYBASEIQ;
	private String sqlAppend;
	private Boolean isCompress=false;
	//
	private String id;
	
	public long getStartIndex() {
		if(startIndex<=0){
			startIndex=1;
		}
		return startIndex;
	}
	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}
	public int getRowPerPage() {
		return rowPerPage;
	}
	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}
	public Map<String, List<Operator>> getConditionMap() {
		return conditionMap;
	}
	public void setConditionMap(Map<String, List<Operator>> conditionMap) {
		this.conditionMap = conditionMap;
	}
	public List<String> getOrderByFields() {
		return orderByFields;
	}
	public void setOrderByFields(List<String> orderByFields) {
		this.orderByFields = orderByFields;
	}
	public String getOrderByType() {
		return orderByType;
	}
	public void setOrderByType(String orderByType) {
		this.orderByType = orderByType;
	}
	public List<String> getIntendedFields() {
		return intendedFields;
	}
	public void setIntendedFields(List<String> intendedFields) {
		this.intendedFields = intendedFields;
	}
	public ChartParam getChart() {
		return chart;
	}
	public void setChart(ChartParam chart) {
		this.chart = chart;
	}
	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	
	public Boolean getFlush() {
		return flush;
	}
	public void setFlush(Boolean flush) {
		this.flush = flush;
	}
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSqlAppend() {
		return sqlAppend;
	}
	public void setSqlAppend(String sqlAppend) {
		this.sqlAppend = sqlAppend;
	}
	
	public Boolean getIsCompress() {
		return isCompress;
	}
	public void setIsCompress(Boolean isCompress) {
		this.isCompress = isCompress;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
