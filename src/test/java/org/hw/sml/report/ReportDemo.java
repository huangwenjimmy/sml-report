package org.hw.sml.report;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hw.sml.model.Result;
import org.hw.sml.report.model.Queryer;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;



public class ReportDemo {
	public static void main(String[] args) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@10.221.247.46:1521/ipms");
		dataSource.setUsername("ipmsdm");
		dataSource.setPassword("SHipmsdm!23$");
		//库集
		Map<String,DataSource> dss=new HashMap<String,DataSource>();
		dss.put("defJt", dataSource);
		//对象
		ReportCommonService rcptCommonService=new ReportCommonService();
		try{
			rcptCommonService.setDss(dss);//
			rcptCommonService.init();
			//使用核心方法
			Result data=rcptCommonService.getResult(
				new Queryer("10301_m").
				 addQuery("TIME_ID", ">=","201601010000")
				.addQuery("TIME_ID", "<","201609010000")
				.addQuery("LTE_CELL_NAME", "ilike","h")
				.addQuery("BTS_NAME", "like", "路")
				.limit(1, 10)
				.addOrder("BTS_NAME", "DESC")
				);
			LoggerFactory.getLogger(ReportDemo.class).info(data.toString());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//对象销毁
			rcptCommonService.destroy();
		}
		//
	}
}
