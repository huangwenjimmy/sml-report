# sml-report
sql markup report[curd]

##demo

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
