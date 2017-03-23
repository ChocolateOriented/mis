/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunnedConclusionDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningTaskDao;
import com.mo9.risk.modules.dunning.entity.TMisContantRecord;
import com.mo9.risk.modules.dunning.entity.TMisDunnedConclusion;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.dao.DictDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

/**
 * 电催结论Service
 * @author shijlu
 * @version 2017-03-06
 */
@Service
@Transactional(readOnly = true)
public class TMisDunnedConclusionService extends CrudService<TMisDunnedConclusionDao, TMisDunnedConclusion> {
	
	private static Logger logger = Logger.getLogger(TMisDunnedConclusionService.class);
	
	@Autowired
	private TMisDunnedConclusionDao tMisDunnedConclusionDao;
	
	@Autowired
	private TMisDunningTaskDao tMisDunningTaskDao;

	@Autowired
	private DictDao dictDao;

	@Override
	public TMisDunnedConclusion get(String id) {
		return super.get(id);
	}

	@Override
	public List<TMisDunnedConclusion> findList(TMisDunnedConclusion TMisDunnedConclusion) {
		return super.findList(TMisDunnedConclusion);
	}

	@Override
	public Page<TMisDunnedConclusion> findPage(Page<TMisDunnedConclusion> page, TMisDunnedConclusion tMisDunnedConclusion) {
		page.setOrderBy("dbid desc");
		return super.findPage(page, tMisDunnedConclusion);
	}

	@Transactional(readOnly = false)
	public boolean saveRecord(TMisDunnedConclusion tMisDunnedConclusion, String dealcode, String dunningtaskdbid) {
		TMisDunningOrder order = tMisDunningTaskDao.findOrderByDealcode(dealcode);
		
		if (order == null) {
			logger.warn("订单不存在，订单号：" + dealcode);
			return false;
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("STATUS_DUNNING", "dunning");
		params.put("DEALCODE", dealcode);
		TMisDunningTask task = tMisDunningTaskDao.findDunningTaskByDealcode(params);
		
		if (task == null) {
			logger.warn("任务不存在，订单号：" + dealcode);
			return false;
		}
		
		tMisDunnedConclusion.setTaskid(task.getId());
		tMisDunnedConclusion.setDunningtime(new Date());
		tMisDunnedConclusion.setOrderstatus("payoff".equals(order.status));
		tMisDunnedConclusion.setRepaymenttime(order.repaymentDate);
		tMisDunnedConclusion.setDunningpeopleid(task.getDunningpeopleid());
		tMisDunnedConclusion.setDunningcycle(task.getDunningcycle());
		save(tMisDunnedConclusion);
		tMisDunnedConclusionDao.updateTelAction(tMisDunnedConclusion);
		return true;
	}

	/**
	 * 根据电话号码去重，生成初始的备注信息
	 * @param tMisDunnedConclusion
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getDefalutRemark(TMisDunnedConclusion tMisDunnedConclusion) {
		List<TMisContantRecord> list = tMisDunnedConclusionDao.findTelActionContacts(tMisDunnedConclusion);
		if (list == null || list.size() == 0) {
			return "";
		}
		
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < list.size() && i < 20; i++) {
			TMisContantRecord record = list.get(i);
			buffer.append(record.getContactstype().getDesc());
			if (record.getContactsname() != null && !"".equals(record.getContactsname())) {
				buffer.append("-").append(record.getContactsname());
			}
			buffer.append(" ").append(record.getContanttarget());
			buffer.append(" ").append(record.getTelstatus() == null ? "" : record.getTelstatus().getDesc());
			buffer.append("/");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		if (list.size() > 20) {
			buffer.append("...");
		}
		return buffer.toString();
	}
	
	/**
	 * 查询结果代码对应的下次跟进日期
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String, String> getFollowDateConfig() {
		Dict dict = new Dict();
		dict.setType("dunning_result_code");
		List<Dict> rs = dictDao.findList(dict);
		
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (Dict nextDay : rs) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, Integer.parseInt(nextDay.getValue()));
			map.put(nextDay.getLabel(), format.format(c.getTime()));
		}
		return map;
	}
	
	@Transactional(readOnly = false)
	@Override
	public void save(TMisDunnedConclusion tMisDunnedConclusion) {
		super.save(tMisDunnedConclusion);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void delete(TMisDunnedConclusion TMisDunnedConclusion) {
		super.delete(TMisDunnedConclusion);
	}
	
}