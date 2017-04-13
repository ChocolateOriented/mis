package com.thinkgem.jeesite.common.db;
 
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
 
/**
 * Mysql 多数据源
 * @author xs
 * @date  
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
       
  
    public static String getCurrentLookupKey() {  
        return (String) contextHolder.get();  
    }  
   
    public static void setCurrentLookupKey(String currentLookupKey) {  
        contextHolder.set(currentLookupKey);  
    }  
   
    @Override  
    protected Object determineCurrentLookupKey() {  
        return getCurrentLookupKey();  
    }  
    
}