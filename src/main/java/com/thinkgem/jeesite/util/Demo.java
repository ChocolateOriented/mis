package com.thinkgem.jeesite.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TMisDunningTaskLog;
import com.sun.tools.javac.util.List;
import com.thinkgem.jeesite.modules.sys.entity.User;

public class Demo {

	
	
//	for(int i= 0 ; i < tasks.size() ; i++ ){  
//		TMisDunningTask dunningTask = (TMisDunningTask)tasks.get(i);
//		int j = i % dunningPeoples.size();  
//		/**  任务催收人员添加    */
//		dunningTask.setDunningpeopleid(dunningPeoples.get(j).getId());
//		dunningTask.setDunningpeoplename(dunningPeoples.get(j).getName());
//		/**  任务log 催收人员添加    */
//		if(inDunningTaskLogsMap.containsKey(dunningTask.getId())){
//			inDunningTaskLogsMap.get(dunningTask.getId()).setTaskid(dunningTask.getId());
//			inDunningTaskLogsMap.get(dunningTask.getId()).setBehaviorstatus("in");
//			inDunningTaskLogsMap.get(dunningTask.getId()).setDunningpeopleid(dunningTask.getDunningpeopleid());
//			inDunningTaskLogsMap.get(dunningTask.getId()).setDunningpeoplename(dunningTask.getDunningpeoplename());
//			inDunningTaskLogsMap.get(dunningTask.getId()).setDunningcycle(dunningTask.getDunningcycle());
//			inDunningTaskLogsMap.get(dunningTask.getId()).setCreateDate(new Date());
//			inDunningTaskLogsMap.get(dunningTask.getId()).setCreateBy(new User("auto_admin"));
//		}else{
//			inDunningTaskLogsMap.put(dunningTask.getId(), 
//					new TMisDunningTaskLog(dunningTask.getDealcode(), 
//							dunningTask.getDunningpeopleid(),
//							dunningTask.getDunningpeoplename(),
//							dunningTask.getDunningcycle(),
//							"in_warn"));
//			logger.warn("行为状态in_warn：任务taskID:" +dunningTask.getId() + "移入" + dunningTask.getDunningcycle() + "队列" +dunningTask.getDunningpeoplename() +"数据缺失" );
//		}
//		inDunningTaskLogs.add(inDunningTaskLogsMap.get(dunningTask.getId()));
//	}
	
	
	public static void main(String[] args) {
		ArrayList<Integer> tasks = new ArrayList<Integer>();
		for(int i= 0 ; i < 1870 ; i++ ){
			Integer mon = 2000;
			tasks.add(mon);
		}
		for(int i= 0 ; i < 1590 ; i++ ){
			Integer mon = 1500;
			tasks.add(mon);
		}
		for(int i= 0 ; i < 962 ; i++ ){
			Integer mon = 1000;
			tasks.add(mon);
		}
		for(int i= 0 ; i < 568 ; i++ ){
			Integer mon = 500;
			tasks.add(mon);
		}
		
		int dunningPeoples = 30;
		int j = 0;
	    for (int i = 0; i < tasks.size(); i++) {
	      if (i / dunningPeoples % 2 == 0) {
	        j = i % dunningPeoples;
	      } else {
	        j = dunningPeoples - 1 - i % dunningPeoples;
	      }
	      System.out.println(j);
	    }
	
//	 public static void main(String[] args){
//		  Scanner sc=new Scanner(System.in);
//		  System.out.print("请输入一个数");
//		  int i=sc.nextInt();
//		  System.out.print("倒序后的数字为：");
//		  for (; i!=0; ){
//			  int a=i%10;
//			  i=i/20;
//			  System.out.print(a);
//		  }
//		    
		 }
	
	
	
}
