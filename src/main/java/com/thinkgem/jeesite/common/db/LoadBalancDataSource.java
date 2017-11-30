package com.thinkgem.jeesite.common.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Description 负载均衡数据源,
 * @author jxli
 * @version 2017/11/28
 */
public class LoadBalancDataSource extends AbstractRoutingDataSource {

	/**
	 *  加权值界限, 又小到大排列
	 */
	private final List<Integer> weightLimitList = new ArrayList<>();
	private Integer weightSum = 0;

	/**
	 * @Description 给weightDataSource赋值时, 进行初始化
	 * @param weightDataSource key为需要负载均衡数据源, value为加权值
	 * @return void
	 */
	public void setWeightDataSource(Map<DataSource, Integer> weightDataSource) {
		if (weightDataSource == null && weightDataSource.size() == 0){
			throw new IllegalArgumentException("Property 'weightDataSource ' is required");
		}

		Set<DataSource> dataSources = weightDataSource.keySet();
		Map<Object, Object> targetDataSources = new HashMap<>(dataSources.size());

		for (DataSource dataSource : dataSources) {
			int weight =  weightDataSource.get(dataSource);
			if (weight <= 0) {
				throw new IllegalArgumentException("error: weight=" + weight);
			}

			weightSum += weight;
			//使用加权值的上限作为数据库选择的KEY
			targetDataSources.put(weightSum, dataSource);
			weightLimitList.add(weightSum);
		}

		super.setTargetDataSources(targetDataSources);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		Integer n = ThreadLocalRandom.current().nextInt(weightSum);
		for (Integer limit : weightLimitList) {
			if (n < limit){
				//System.out.println(limit);
				return limit;
			}
		}
		throw new RuntimeException("加权计算发生问题, weightSum:"+weightSum+", n:"+n+", weightLimitList:"+ weightLimitList);
	}

}