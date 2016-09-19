package org.hw.sml.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.hw.sml.FrameworkConstant;
import org.hw.sml.FrameworkConstant.Type;
import org.hw.sml.model.Result;
import org.hw.sml.report.model.Constants;
import org.hw.sml.report.model.ImportSql;
import org.hw.sml.report.model.ParamCriteria;
import org.hw.sml.report.model.ParamCriteriaForUpdate;
import org.hw.sml.report.model.PiTable;
import org.hw.sml.report.model.PiTableDetail;
import org.hw.sml.report.model.QuerySql;
import org.hw.sml.report.model.Queryer;
import org.hw.sml.report.model.UpdateSql;
import org.hw.sml.report.support.Source;
import org.hw.sml.support.CallableHelper;
import org.springframework.jdbc.core.RowMapper;
/**
 * 
 */
public class ReportCommonService extends Source{
	
	public static final  String CACHE_PRE="report";
	
	public void init(){
		super.init();
	}
	
	public PiTable get(String id) {
		String key=CACHE_PRE+":"+id+":getPiTable";
		if(getCacheManager().get(key)!=null){
			return (PiTable)getCacheManager().get(key);
		}
		PiTable result=getJdbc("defJt").queryForObject(FrameworkConstant.getSupportKey(frameworkMark, Type.FRAMEWORK_CFG_REPORT_SQL),new RowMapper<PiTable>(){
			public PiTable mapRow(ResultSet rs, int arg1) throws SQLException {
				PiTable pi=new PiTable();
				pi.setId(rs.getString("id"));
				pi.setTableName(rs.getString("tablename"));
				pi.setDescription(rs.getString("description"));
				pi.setDbId(rs.getString("db_id"));
				return pi;
			}
		},id);
		if(result!=null)
			getCacheManager().set(key, result,-1);
		return result;
	}
	public List<PiTableDetail> findAllByTableId(String id){
		String key=CACHE_PRE+":"+id+":findAllByTableId";
		if(getCacheManager().get(key)!=null){
			return (List<PiTableDetail>)getCacheManager().get(key);
		}
		List<PiTableDetail> result= getJdbc("defJt").query(FrameworkConstant.getSupportKey(frameworkMark,Type.FRAMEWORK_CFG_REPORT_DETAIL_SQL),new RowMapper<PiTableDetail>(){
			public PiTableDetail mapRow(ResultSet rs, int arg1) throws SQLException {
				PiTableDetail pi=new PiTableDetail();
				pi.setTableId(rs.getString("table_id"));
				pi.setField(rs.getString("field_name"));
				pi.setFieldType(rs.getString("field_Type"));
				pi.setFieldZn(rs.getString("field_name_zn"));
				pi.setFormat(rs.getString("format"));
				pi.setLength(rs.getString("length"));
				pi.setOrderIndex(rs.getInt("order_index"));
				pi.setForImport(rs.getInt("for_import"));
				pi.setForUpdate(rs.getInt("for_update"));
				pi.setForInsert(rs.getInt("for_insert"));
				pi.setForImportUpdate(rs.getInt("for_import_update"));
				pi.setForQuery(rs.getInt("for_query"));
				pi.setIsQuery(rs.getInt("is_query"));
				return pi;
			}
		},id);
		if(result!=null&&!result.isEmpty())
		getCacheManager().set(key,result,-1);
		return result;
	}
	//----原始操作---开始
	public List<Map<String, Object>> query(String dbid,String sql, Object[] array,boolean inLog) {
		if(!inLog)
		logger.debug("query for sql[{}],params[{}]",sql,(array==null||array.length==0)?"":Arrays.asList(array).toString());
		return getJdbc(dbid).queryForList(sql,array);
	}
	
	public List<Map<String, Object>> query(String dbid,String sql, Object[] array) {
		return query(dbid, sql, array, false);
	}
	public Long count(String dbid,String tableId,String sql, Object[] params) {
		String key=new StringBuffer(CACHE_PRE+":"+tableId+":"+sql).append(params==null||params.length==0?"":Arrays.asList(params)).toString();
		if(getCacheManager().get(key)!=null){
			return (Long)getCacheManager().get(key);
		}
		Long count=getJdbc(dbid).queryForObject("select count(1) from("+sql+") t1",params,Long.class);
		if(count!=null&&count!=0)
		getCacheManager().set(key,count,120);//缓存一小时
		return count;
	}
	public int update(String dbid,String sql, Object[] array,boolean inLog) {
		if(!inLog)
		logger.info("update for sql[{}],params[{}]",sql,(array==null||array.length==0)?"":Arrays.asList(array).toString());
		return getJdbc(dbid).update(sql,array);
	}
	public int update(String dbid,String sql, Object[] array) {
		return update(dbid, sql, array,false);
	}
	
	public int updates(String dbid,final String sql,final List<Object[]> arrays){
		logger.info("updates for sql[{}] sizes={}",sql,arrays.size());
		final int total=arrays.size();
		int count=0;
		int perOneSize=10000;
		int eachs=total/perOneSize+1;
		for(int j=0;j<eachs;j++){
			int start=j*perOneSize;
			int end=(j+1)*perOneSize>total?total:(j+1)*perOneSize;
			final List<Object[]> newArrays=arrays.subList(start,end);
			int[] is= getJdbc(dbid).batchUpdate(sql,newArrays);
			for(int i:is){
				count+=i;
			}
			logger.info("updates for sql["+sql+"] start[{}]-----end[{}]",start,end);
		}
	
		return count;
	}
	//----原始操作结束
	
	public QuerySql getQuerySql(String id) {
		PiTable piTable=get(id);
		List<PiTableDetail> piTableDetails=findAllByTableId(id);
		return new QuerySql(piTable,piTableDetails);
	}
	
	public QuerySql getQuerySql(String id, ParamCriteria pc) {
		PiTable piTable=get(id);
		List<PiTableDetail> piTableDetails=findAllByTableId(id);
		return new QuerySql(piTable,piTableDetails,pc);
	}
	//更新操作
	public int update(String id, ParamCriteriaForUpdate pcu) {
		PiTable piTable=get(id);
		List<PiTableDetail> piTableDetails=findAllByTableId(id);
		UpdateSql updateSql=new UpdateSql(pcu,piTable,piTableDetails);
		return update(piTable.getDbId(),updateSql.toString(),updateSql.getUpdateParams().toArray(new Object[]{}),pcu.getInLog());
	}
	//查询
	public List<Map<String,Object>>  query(String id, ParamCriteria pc) {
		QuerySql querySql=getQuerySql(id,pc);
		return query(querySql.getPiTable().getDbId(),querySql.toString(),querySql.getQueryParam().toArray(new Object[]{}));
	}
	public List<Map<String,Object>> query(Queryer queryer){
		return query(queryer.getRcptId(),queryer.getParamCriteria());
	}
	//查询
	public Result getResult(Queryer queryer){
		return getResult(queryer.getRcptId(),queryer.getParamCriteria());
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Result getResult(final String id, final ParamCriteria pc) {
		final QuerySql querySql=getQuerySql(id,pc);
		Result result=new Result();
		List rs=CallableHelper.callresults(new Callable() {
			public Object call() throws Exception {
				return count(querySql.getPiTable().getDbId(),id,querySql.getQuerySql(),querySql.getQueryParamWithOutPage().toArray(new Object[]{}));
			}
		},new Callable(){
			public Object call() throws Exception {
				//缓存第一页
				if(pc.getStartIndex()==1&&pc.getRowPerPage()<=50){
					String key=CACHE_PRE+":"+id+":query-"+querySql.toString()+querySql.getQueryParam();
					List value=(List)getCacheManager().get(key);
					if(value!=null){
						return value;
					}else{
						value=query(querySql.getPiTable().getDbId(),querySql.toString(),querySql.getQueryParam().toArray(new Object[]{}));
						if(value.size()>0)
						getCacheManager().set(key, value,120);
						return value;
					}
				}else
				return query(querySql.getPiTable().getDbId(),querySql.toString(),querySql.getQueryParam().toArray(new Object[]{}));
			}
		});
		result.setCount(Long.parseLong(String.valueOf(rs.get(0)==null?0:rs.get(0))));
		result.setDatas((List)(rs.get(1)==null?new ArrayList():rs.get(1)));
		return result;
	}
	//导入根据id导入默认模板2007xlsx
	public int importReport(String id, String type,
			List<Map<String,Object>> datas) {
		int i=0;
		PiTable piTable=get(id);
		List<PiTableDetail> piTableDetails=findAllByTableId(id);
		ImportSql importSql=new ImportSql(type, piTable, piTableDetails, datas);
		List<UpdateSql> updateSqls=importSql.getUpdateSqls();
		if(type.equals(Constants.TYPE_ADU)){
			
		}
		//---
		if(updateSqls.size()==0){
			return 0;
		}
		String dbid=piTable.getDbId();
		String sql=updateSqls.get(0).toString();
		List<Object[]> arrays=new ArrayList<Object[]>();
		for(UpdateSql updateSql:updateSqls){
			arrays.add(updateSql.getUpdateParams().toArray());
		}
		i=updates(dbid, sql, arrays);
		return i;
	}

	
}
