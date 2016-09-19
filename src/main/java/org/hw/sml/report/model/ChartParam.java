package org.hw.sml.report.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChartParam implements Serializable{

	private static final long serialVersionUID = 8827653897470861361L;
	private List<String> groupid;
	private Map<String,String> funcs=new LinkedHashMap<String,String>();
	public List<String> getGroupid() {
		return groupid;
	}
	public void setGroupid(List<String> groupid) {
		this.groupid = groupid;
	}
	public Map<String, String> getFuncs() {
		return funcs;
	}
	public void setFuncs(Map<String, String> funcs) {
		this.funcs = funcs;
	}
	
	
}
