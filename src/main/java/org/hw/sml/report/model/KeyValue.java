package org.hw.sml.report.model;

import java.io.Serializable;

public class KeyValue implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7875187088759809373L;
	private String key;
	private Object value;
	
	public KeyValue() {
		super();
	}
	
	public KeyValue(String key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}