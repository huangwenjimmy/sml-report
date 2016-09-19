package org.hw.sml.report.model;

import java.io.Serializable;

/**
 * 字段类，描述字段的含义，包含中英文，表头长度，字段类型
 * @author hw
 *
 */
public class PiTableDetail  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2456370895312460192L;
	private String tableId;
	private String field;
	private String fieldZn;
	/*
	 * 字段类型
	 */
	private String fieldType;
	/*
	 * 组合字段，函数的选择
	 */
	private String format;
	/**
	 * 可根据实际调整表头列宽
	 */
	private String length;
	private Integer orderIndex;
	private Integer forUpdate;
	private Integer forImport;
	private Integer forInsert;
	private Integer forImportUpdate;
	private Integer forQuery;
	private Integer isQuery;
	
	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFieldZn() {
		return fieldZn==null?field:fieldZn;
	}

	public void setFieldZn(String fieldZn) {
		this.fieldZn = fieldZn;
	}

	

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFormat() {
		return format==null?field:format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
	public String getFiledReturnType(String type){
		if(this.fieldType==null||!type.equals(Constants.TYPE_QUERY_QUERY))
			return "";
		if(this.fieldType.split("@").length==1)
			return "";
		return "@"+this.fieldType.split("@")[1];
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public Integer getForUpdate() {
		return forUpdate==null?0:forUpdate;
	}

	public void setForUpdate(Integer forUpdate) {
		this.forUpdate = forUpdate;
	}

	public Integer getForImport() {
		return forImport==null?0:forImport;
	}

	public void setForImport(Integer forImport) {
		this.forImport = forImport;
	}

	public Integer getForInsert() {
		return forInsert==null?0:forInsert;
	}

	public void setForInsert(Integer forInsert) {
		this.forInsert = forInsert;
	}

	public Integer getForImportUpdate() {
		return forImportUpdate==null?0:forImportUpdate;
	}

	public void setForImportUpdate(Integer forImportUpdate) {
		this.forImportUpdate = forImportUpdate;
	}

	public Integer getForQuery() {
		return forQuery==null?0:forQuery;
	}

	public void setForQuery(Integer forQuery) {
		this.forQuery = forQuery;
	}
	public boolean contain(String type){
		if(type.equals(Constants.TYPE_QUERY_QUERY)){
			return getForQuery()==1;
		}else if(type.equals(Constants.TYPE_QUERY_TEMPLATE)){
			return getForImportUpdate()==1||getForImportUpdate()==2;
		}
		return false;
	}

	public Integer getIsQuery() {
		return isQuery==null?0:isQuery;
	}

	public void setIsQuery(Integer isQuery) {
		this.isQuery = isQuery;
	}
	
}
