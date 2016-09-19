package org.hw.sml.report.model;

import java.io.Serializable;

/**
 * 表类，dbId为数据库标识
 * @author hw
 *
 */
public class PiTable implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8351819428300248926L;

	private String id;
	
	private String tableName;
	
	private String description;
	
	private String dbId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	
	
}
