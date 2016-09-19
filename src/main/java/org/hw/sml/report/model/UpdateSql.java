package org.hw.sml.report.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hw.sml.tools.Assert;
import org.hw.sml.tools.DateTools;
/**
 * 更新生成sql
 * @author hw
 *
 */
public class UpdateSql {
	private String type;//[update,insert,delete]
	private ParamCriteriaForUpdate pcu;
	private PiTable piTable;
	private String updateSqlPre="";
	private String updateFieldSql="";
	private String conditionFieldSql="";
	private Map<String,PiTableDetail> piMap=new HashMap<String,PiTableDetail>();
	private List<Object> updateParams=new ArrayList<Object>();
	public UpdateSql() {
		super();
	}
	public UpdateSql(ParamCriteriaForUpdate pcu,PiTable piTable,List<PiTableDetail> piTableDetails) {
		this.pcu=pcu;
		this.piTable=piTable;
		this.type=pcu.getType();
		for(PiTableDetail pi:piTableDetails){
			piMap.put(pi.getField(), pi);
		}
		if(type.equals("update")){
			builedUpdateSqlPre();
			builedUpdateFieldSql();
			builedConditionFieldSql();
		}else if(type.equals("insert")){
			buildedInsertSqlPre();
			builderInsertFieldSql();
		}else if(type.equals("delete")){
			buildedDeleteSqlPre();
			builedConditionFieldSql();
		}
	}
	private void buildedDeleteSqlPre() {
		updateSqlPre="delete from "+piTable.getTableName()+" ";
		
	}
	private void builderInsertFieldSql() {
		StringBuffer sb=new StringBuffer("(");
		Map<String,Object> fv=pcu.getUpdateField();
		for(Map.Entry<String,Object> entry:fv.entrySet()){
			sb.append(entry.getKey()+",");
			//参数绑定
			PiTableDetail pit=piMap.get(entry.getKey());
			Assert.notNull(pit, entry.getKey() +" is not config for insert field!");
			String fieldType=pit.getFieldType();
			String[] fts=fieldType.split("@");
			String ft=fts[0].toLowerCase();
			updateParams.add(handleValue(ft,entry.getValue()));
		}
		this.updateFieldSql=sb.deleteCharAt(sb.length()-1).append(")").toString();
		this.conditionFieldSql=getPreparam();
		
	}
	private String getPreparam() {
		StringBuffer sb=new StringBuffer(" values(");
		for(int i=0;i<updateParams.size();i++){
			sb.append("?,");
		}
		return sb.deleteCharAt(sb.length()-1).append(")").toString();
	}
	private void buildedInsertSqlPre() {
		updateSqlPre="insert into "+piTable.getTableName();
	}
	private void builedConditionFieldSql() {
		Map<String,Object> fv=pcu.getUpdateCondition();
		StringBuffer sb=new StringBuffer(" where 1=1 ");
		for(Map.Entry<String,Object> entry:fv.entrySet()){
			sb.append("and "+entry.getKey()+"=? ");
			//参数绑定
			PiTableDetail pit=piMap.get(entry.getKey());
			Assert.notNull(pit, entry.getKey() +" is not config for update or insert condition!");
			String fieldType=pit.getFieldType();
			String[] fts=fieldType.split("@");
			String ft=fts[0].toLowerCase();
			updateParams.add(handleValue(ft,entry.getValue()));
		}
		this.conditionFieldSql=sb.deleteCharAt(sb.length()-1).toString();
		
	}
	private void builedUpdateFieldSql() {
		Map<String,Object> fv=pcu.getUpdateField();
		StringBuffer sb=new StringBuffer();
		for(Map.Entry<String,Object> entry:fv.entrySet()){
			sb.append(entry.getKey()+"=?,");
			//参数绑定
			PiTableDetail pit=piMap.get(entry.getKey());
			Assert.notNull(pit, entry.getKey() +" is not config for update field!");
			String fieldType=pit.getFieldType();
			String[] fts=fieldType.split("@");
			String ft=fts[0].toLowerCase();
			updateParams.add(handleValue(ft,entry.getValue()));
		}
		this.updateFieldSql=sb.deleteCharAt(sb.length()-1).toString();
	}
	private Object handleValue(String ft, Object val) {
		Object value=val;
		if(ft.contains("date")||ft.contains("time")){//对于时间格式
			if(val!=null&&(val.getClass().getSimpleName().toLowerCase().contains("time")||val.getClass().getSimpleName().toLowerCase().contains("date"))){
				value=val;
			}else{
				value=DateTools.parse(String.valueOf(val));
			}
		}else if(ft.contains("char")){
			value=val==null?null:String.valueOf(val);
		}
		return value;
	}
	private void builedUpdateSqlPre() {
		updateSqlPre="update "+piTable.getTableName()+" set ";
	}
	public String toString(){
		return this.updateSqlPre+this.updateFieldSql+this.conditionFieldSql;
	}
	public List<Object> getUpdateParams() {
		return updateParams;
	}
	
	
	
}
