package org.hw.sml.report.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 */

public class ImportSql implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6255476935661214L;
	private String type;//update,import,adu(混合)
	private PiTable piTable;
	private Map<String,PiTableDetail> piMap=new HashMap<String,PiTableDetail>();
	private List<PiTableDetail> piTableDetails;
	private List<UpdateSql> updateSqls=new ArrayList<UpdateSql>();
	private List<Map<String,Object>> datas=new ArrayList<Map<String,Object>>();
	public ImportSql() {
		super();
	}

	public ImportSql(String type,PiTable piTable,List<PiTableDetail> piTableDetails,List<Map<String,Object>> datas) {
		super();
		this.piTable=piTable;
		this.piTableDetails = piTableDetails;
		this.type=type;
		this.datas=datas;
		if(type.equals(Constants.TYPE_UPDATE)){
			update();
		}else if(type.equals(Constants.TYPE_INSERT)||type.equals(Constants.TYPE_ADU)){
			insert();
		}
	}

	
	
	private void insert() {
		List<String> updateFields=new ArrayList<String>();
		for(PiTableDetail pi:piTableDetails){
			piMap.put(pi.getField(), pi);
			if(pi.getForImport()==1){
				updateFields.add(pi.getField());
			}
		}
		for(Map<String,Object> data:datas){
			ParamCriteriaForUpdate pcu=new ParamCriteriaForUpdate();
			Map<String,Object> ucf=new LinkedHashMap<String,Object>();
			for(String uf:updateFields){
				ucf.put(uf,data.get(piMap.get(uf).getFieldZn()));
			}
			pcu.setType(type);
			pcu.setUpdateField(ucf);
			UpdateSql updateSql=new UpdateSql(pcu, piTable, piTableDetails);
			updateSqls.add(updateSql);
		}
	}

	private void update() {
		List<String> updateCondition=new ArrayList<String>();
		List<String> updateFields=new ArrayList<String>();
		for(PiTableDetail pi:piTableDetails){
			piMap.put(pi.getField(), pi);
			if(pi.getForImportUpdate()==1){
				updateCondition.add(pi.getField());
			}else if(pi.getForImportUpdate()==2){
				updateFields.add(pi.getField());
			}
		}
		for(Map<String,Object> data:datas){
			ParamCriteriaForUpdate pcu=new ParamCriteriaForUpdate();
			Map<String,Object> ucd=new LinkedHashMap<String,Object>();
			Map<String,Object> ucf=new LinkedHashMap<String,Object>();
			for(String uc:updateCondition){
				ucd.put(uc,data.get(piMap.get(uc).getFieldZn()));
			}
			for(String uf:updateFields){
				ucf.put(uf,data.get(piMap.get(uf).getFieldZn()));
			}
			if(isNotNullContent(ucf)){
				pcu.setUpdateCondition(ucd);
				pcu.setUpdateField(ucf);
				UpdateSql updateSql=new UpdateSql(pcu, piTable, piTableDetails);
				updateSqls.add(updateSql);
			}
		}
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PiTable getPiTable() {
		return piTable;
	}

	public void setPiTable(PiTable piTable) {
		this.piTable = piTable;
	}

	public Map<String, PiTableDetail> getPiMap() {
		return piMap;
	}

	public void setPiMap(Map<String, PiTableDetail> piMap) {
		this.piMap = piMap;
	}

	public List<UpdateSql> getUpdateSqls() {
		return updateSqls;
	}

	public void setUpdateSqls(List<UpdateSql> updateSqls) {
		this.updateSqls = updateSqls;
	}

	public List<PiTableDetail> getPiTableDetails() {
		return piTableDetails;
	}

	public void setPiTableDetails(List<PiTableDetail> piTableDetails) {
		this.piTableDetails = piTableDetails;
	}
	private  boolean isNotNullContent(Map<String, Object> obj) {
		boolean flag=false;
		for(Map.Entry<String,Object> entry:obj.entrySet()){
			flag=entry.getValue()!=null&&String.valueOf(entry.getValue()).trim().length()>0;
			if(flag){
				return true;
			}
		}
		return flag;
	}
}
