package com.mo9.risk.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.thinkgem.jeesite.modules.buyer.service.MRiskBuyerReportService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-context*.xml"})
public class BaseSpringTest {
	
	
	@Autowired
	private MRiskBuyerReportService mRiskBuyerReportService;
	
	@Test
	public void getTest(){
		mRiskBuyerReportService.get("1");
	}
	
	

}
