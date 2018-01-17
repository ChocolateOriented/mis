package com.mo9.risk.modules.dunning.web;

import com.mo9.risk.modules.dunning.entity.TMisCallingQualityTest;
import com.mo9.risk.modules.dunning.entity.TMisCallingRecord;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.service.TMisCallingAutomaticSpeechRecognitionService;
import com.mo9.risk.modules.dunning.service.TMisDunningGroupService;
import com.mo9.risk.modules.dunning.service.TMisDunningPeopleService;
import com.mo9.risk.modules.dunning.service.TMisDunningTaskService;
import com.mo9.risk.util.BaiduAutomaticSpeechRecognitionUtil;
import com.mo9.risk.util.WavCutUtil;
import com.mo9.risk.util.sensitive.KWSeekerProcessor;
import com.mo9.risk.util.sensitive.entity.KWSeeker;
import com.mo9.risk.util.sensitive.entity.KWSeekerManage;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jxguo on 2018/1/2.
 */
@Controller
@RequestMapping(value="${adminPath}/dunning/tMisCallingASR")
public class TMisCallingAutomaticSpeechRecognitionController extends BaseController {

    @Autowired
    private TMisCallingAutomaticSpeechRecognitionService tMisCallingAutomaticSpeechRecognitionService;

    @Autowired
    private TMisDunningGroupService tMisDunningGroupService;

    @RequestMapping(value = {"list", ""})
    public String list(TMisCallingQualityTest entity, HttpServletRequest request, HttpServletResponse response, Model model){

        Map<String, Object> map = initGetPhoneCallingReport(entity, new TMisDunningPeople(), -1);
        Page<TMisCallingQualityTest> page = tMisCallingAutomaticSpeechRecognitionService.findPage(new Page<TMisCallingQualityTest>(request, response), entity);
        model.addAttribute("groupTypes", TMisDunningGroup.groupTypes);
        model.addAttribute("page", page);
        model.addAttribute("groupList",map.get("groupList"));
        String url = DictUtils.getDictValue("ctiUrl", "callcenter", "") + "audio/";
        model.addAttribute("ctiUrl", url);
        model.addAttribute("tMisCallingQualityTest", map.get("tMisCallingQualityTest"));
        return "modules/dunning/tMisCallingQualityTestRecordList";
    }

    @RequestMapping(value = "getCallingContent")
    public String getCallingContent(TMisCallingQualityTest tMisCallingQualityTest, Model model){
        String callingcontent = tMisCallingAutomaticSpeechRecognitionService.getCallingContent(tMisCallingQualityTest);
        model.addAttribute("callingCotent", callingcontent);
        return "modules/dunning/tMisCallingContentDisplay";
    }

    private Map<String, Object> initGetPhoneCallingReport(TMisCallingQualityTest tMisCallingQualityTest, TMisDunningPeople dunningPeople, int day){
        Map<String, Object> map = new HashMap<String, Object>();
        List<TMisDunningPeople> dunningPeoples = null;
        List<TMisDunningGroup> groups = new ArrayList<TMisDunningGroup>();
        if (tMisCallingQualityTest.getStartTime() == null || "".equals(tMisCallingQualityTest.getStartTime())){
            Date date = null;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, day);//正数可以得到当前时间+n天，负数可以得到当前时间-n天
/*            calendar.set(Calendar.HOUR_OF_DAY, 00);
            calendar.set(Calendar.MINUTE, 00);*/
            date = calendar.getTime();
            calendar.setTime(date);
            tMisCallingQualityTest.setStartTime(date);
        }
        if (tMisCallingQualityTest.getEndTime() == null || "".equals(tMisCallingQualityTest.getEndTime())){
            Date date = null;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, day+1);//正数可以得到当前时间+n天，负数可以得到当前时间-n天
/*            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);*/
            date = calendar.getTime();
            calendar.setTime(date);
            tMisCallingQualityTest.setEndTime(date);
        }
        TMisDunningGroup tMisDunningGroup = new TMisDunningGroup();
        int permissions = TMisDunningTaskService.getPermissions();
        //催收监管
        if (permissions == TMisDunningTaskService.DUNNING_SUPERVISOR) {
            tMisDunningGroup.setSupervisor(UserUtils.getUser());
        }
        groups = tMisDunningGroupService.findList(tMisDunningGroup);

        map.put("groupList", groups);
        map.put("tMisCallingQualityTest", tMisCallingQualityTest);
        return map;
    }

}
