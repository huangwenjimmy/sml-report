package org.hw.sml.report.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hw.sml.tools.Assert;
import org.hw.sml.tools.DateTools;
/**
 * 不依赖于配置的更新操作
 * @author wen
 *修复debug   如果修改字段写错时,进行校验
 */
public class Update extends Criteria {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8961222448940694098L;

	private String dbId;
	
	private String tableName;
	
	private String type=Constants.TYPE_INSERT;
	
	private List<Map<String,Object>> datas=new ArrayList<Map<String,Object>>();
	
	private Map<String,Object> data=new LinkedHashMap<String, Object>();
	
	private List<String> conditions=new ArrayList<String>();
	
	public String getUpdateSqlForInsert(){
		StringBuffer sb=new StringBuffer("insert into "+tableName+"(");
		for(Map.Entry<String,Object> entry:data.entrySet()){
			//参数绑定
			String field=entry.getKey();
			if(field.startsWith("old.")){
				continue;
			}
			String[] fts=field.split("@");
			String ft=fts[0];
			sb.append(ft+",");
		}
		sb.deleteCharAt(sb.length()-1).append(") values(");
		for(int i=0;i<data.size();i++){
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length()-1).append(")");
		return sb.toString();
	}
	public String getUpdateSqlForUpdate(){
		Assert.isTrue(conditions.size()>0,"不允许更新全表["+tableName+"]操作");
		StringBuffer sb=new StringBuffer("update "+tableName+" set ");
		for(Map.Entry<String,Object> entry:data.entrySet()){
			//参数绑定
			String field=entry.getKey();
			String[] fts=field.split("@");
			String ft=fts[0];
			if(conditions.contains(ft)){
				continue;
			}
			sb.append(ft+"=?,");
		}
		sb.deleteCharAt(sb.length()-1).append(" where 1=1");
		for(Map.Entry<String,Object> entry:data.entrySet()){
			String field=entry.getKey();
			String[] fts=field.split("@");
			String ft=fts[0];
			String[] no=ft.split("\\.");
			String cn=no.length==2?no[1]:no[0];
			if(conditions.contains(ft)){
				sb.append(" and "+cn+"=?");
			}
		}
		return sb.toString();
	}
	public String getUpdateSqlForDelete(){
		StringBuffer sb=new StringBuffer("delete from "+tableName+" where 1=1 ");
		Assert.isTrue(data.size()>0,"不允许删除表["+tableName+"]操作");
		for(Map.Entry<String,Object> entry:data.entrySet()){
			//参数绑定
			String field=entry.getKey();
			String[] fts=field.split("@");
			String ft=fts[0];
			sb.append(" and "+ft+"=?");
		}
		return sb.toString();
	}
	public String getUpdateSqlForAdu(boolean exists){
		if(exists){
			return getUpdateSqlForUpdate();
		}else{
			return getUpdateSqlForInsert();
		}
	}
	public void init(){
		if(datas.size()>0){
			data=datas.get(0);
		}else{
			if(data.size()>0)
				datas.add(data);
		}
		assertCondigion();
	}
	public String getUpateSql(){
		init();
		if(type.equalsIgnoreCase(Constants.TYPE_INSERT)){
			return getUpdateSqlForInsert();
		}else if(type.equalsIgnoreCase(Constants.TYPE_UPDATE)){
			return getUpdateSqlForUpdate();
		}else if(type.equalsIgnoreCase(Constants.TYPE_DELETE)){
			return getUpdateSqlForDelete();
		}
		return null;
	}
	public String isExistSql(){
		StringBuffer sb=new StringBuffer();
		sb.append("select count(1) from "+tableName+" where 1=1 ");
		for(Map.Entry<String,Object> entry:data.entrySet()){
			//参数绑定
			String field=entry.getKey();
			String[] fts=field.split("@");
			String ft=fts[0];
			if(conditions.contains(ft)){
				sb.append(" and "+ft+"=?");
			}
		}
		return sb.toString();
	}
	public Object[] getExistParams(){
		List<Object> object=new ArrayList<Object>();
		for(Map.Entry<String,Object> entry:data.entrySet()){
			//参数绑定
			String field=entry.getKey();
			String[] fts=field.split("@");
			String ft=fts[0];
			if(conditions.contains(ft)){
				if(fts.length==1)
					object.add(entry.getValue());
				else
					object.add(DateTools.parse(String.valueOf(entry.getValue())));
			}
		}
		return object.toArray(new Object[]{});
	}
	
	private void assertCondigion() {
		boolean flag=true;
		for(String condition:conditions){
			boolean flag2=false;
			for(String key:data.keySet()){
				if(key.split("@")[0].equals(condition)){
					flag2=true;
					break;
				}
			}
			flag=flag2;
			Assert.isTrue(flag,"字段："+condition+"不存在");
		}
		if(!type.equals(Constants.TYPE_DELETE))
			Assert.isTrue(data.keySet().size()>conditions.size(),"无更新字段！");
		if(type.equals(Constants.TYPE_ADU))
			Assert.isTrue(conditions.size()>0,"无法确认数据是否存在!");
	}
	public List<Object[]> getObjectsForInsert(){
		List<Object[]> objects=new ArrayList<Object[]>();
		for(Map<String,Object> dt:datas){
			Object[] object=new Object[dt.size()];
			int i=0;
			for(Map.Entry<String,Object> entry:dt.entrySet()){
				if(entry.getKey().startsWith("old.")){
					continue;
				}
				String[] keyInfo=entry.getKey().split("@");
				object[i]=entry.getValue();
				if(keyInfo.length==2){
					//if(keyInfo[1].equals("date")){
						object[i]=DateTools.parse(String.valueOf(entry.getValue()));
					/*}else if(keyInfo[1].equals("seq")){
						object[i]=String.valueOf(System.currentTimeMillis());
					}else if(keyInfo[1].equals("uuid")){
						object[i]=String.valueOf(UUID.randomUUID().toString().replace("-",""));
					}*/
				}
				i++;
			}
			objects.add(object);
		}
		return objects;
	}
	public Object[] getObjectForAdu(boolean exists){
		if(exists){
			return getObjectsForUpdate().get(0);
		}else{
			return getObjectsForInsert().get(0);
		}
	}
	public List<Object[]> getObjectsForDelete(){
		List<Object[]> objects=new ArrayList<Object[]>();
		for(Map<String,Object> dt:datas){
			List<Object> object=new ArrayList<Object>();
			for(Map.Entry<String,Object> entry:dt.entrySet()){
				//参数绑定
				String field=entry.getKey();
				String[] fts=field.split("@");
				if(fts.length==1)
					object.add(entry.getValue());
				else
					object.add(DateTools.parse(String.valueOf(entry.getValue())));
			}
			objects.add(object.toArray(new Object[]{}));
		}
		return objects;
	}
	public List<Object[]> getObjectsForUpdate(){
		List<Object[]> objects=new ArrayList<Object[]>();
		for(Map<String,Object> dt:datas){
			List<Object> object=new ArrayList<Object>();
			for(Map.Entry<String,Object> entry:dt.entrySet()){
				String field=entry.getKey();
				String[] fts=field.split("@");
				String ft=fts[0];
				if(conditions.contains(ft)){
					continue;
				}
				if(fts.length==1)
					object.add(entry.getValue());
				else
					object.add(DateTools.parse(String.valueOf(entry.getValue())));
			}
			for(Map.Entry<String,Object> entry:dt.entrySet()){
				String field=entry.getKey();
				String[] fts=field.split("@");
				String ft=fts[0];
				if(conditions.contains(ft)){
					if(fts.length==1)
						object.add(entry.getValue());
					else
						object.add(DateTools.parse(String.valueOf(entry.getValue())));
				}
				
			}
			objects.add(object.toArray(new Object[]{}));
		}
		return objects;
	}
	public List<Object[]> getObjects(){
		if(type.equalsIgnoreCase(Constants.TYPE_INSERT)){
			return getObjectsForInsert();
		}else if(type.equalsIgnoreCase(Constants.TYPE_UPDATE)){
			return getObjectsForUpdate();
		}else if(type.equalsIgnoreCase(Constants.TYPE_DELETE)){
			return getObjectsForDelete();
		}
		return null;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Map<String, Object>> getDatas() {
		return datas;
	}
	public void setDatas(List<Map<String, Object>> datas) {
		this.datas = datas;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public String getDbId() {
		return dbId;
	}
	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	
	public List<String> getConditions() {
		return conditions;
	}
	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}
	
}
