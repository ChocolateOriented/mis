package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.bean.TaskIssuePage;
import com.mo9.risk.modules.dunning.entity.TaskIssue;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 客服问题Dao接口
 * Created by qtzhou on 2017/10/26.
 */
@MyBatisDao
public interface TaskIssueDao extends CrudDao<TaskIssue> {

	List<TaskIssuePage> notifyList(TaskIssuePage taskIssue);

	/**
	 * @Description 添加浏览记录
	 * @param issueId
	 * @param userId
	 * @return void
	 */
	void addReadRecord(@Param("issueId") String issueId,@Param("userId") String userId);

	/**
	 * @Description 查询催收专员的未读提醒数
	 * @param userid
	 * @return java.lang.Integer
	 */
	Integer dunningPeopleRemindCount(String userid);
}
