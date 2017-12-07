package com.mo9.risk.modules.dunning.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mo9.risk.modules.dunning.dao.TMisDunningLetterDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningLetter;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 东港信函Controller
 * @author chijw
 * @version 2017-12-07
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningLetterService extends CrudService<TMisDunningLetterDao, TMisDunningLetter>  {
	
	
	
	@Scheduled(cron = "0 20 3 * * ?")
	@Transactional(readOnly = false)
	public void synDealcode(){
		
		
	}
}
