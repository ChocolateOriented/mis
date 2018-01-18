package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisCallingAutomaticSpeechRecognitionDao;
import com.mo9.risk.modules.dunning.entity.TMisCallingQualityTest;
import com.mo9.risk.util.BaiduAutomaticSpeechRecognitionUtil;
import com.mo9.risk.util.WavCutUtil;
import com.mo9.risk.util.sensitive.KWSeekerProcessor;
import com.mo9.risk.util.sensitive.entity.KWSeeker;
import com.mo9.risk.util.sensitive.entity.SensitiveWordResult;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jxguo on 2018/1/2.
 */
@Service
@Transactional(readOnly = true)
@Lazy(false)
public class TMisCallingAutomaticSpeechRecognitionService extends CrudService<TMisCallingAutomaticSpeechRecognitionDao, TMisCallingQualityTest>{

    public Page<TMisCallingQualityTest> findPage(Page<TMisCallingQualityTest> page, TMisCallingQualityTest entity){
        page.setUsePaginationInterceptor(false);
        page.setOrderBy("StartTime desc");
        entity.setPage(page);
        page.setCount(dao.listCount(entity));
        page.setList(dao.findList(entity));
        return page;
    }

    public List<TMisCallingQualityTest> findList(TMisCallingQualityTest entity){
        return dao.findList(entity);
    }

    public String getCallingContent(TMisCallingQualityTest entity){
        String content = dao.findCallingContentById(entity);
        //敏感词标红
        String scontent = KWSeekerProcessor.newInstance().getKWSeeker("sensitive-word").htmlHighlightWords(content);
        return scontent == null ? "": scontent;
    }

    @Scheduled(cron = "01 00 00 * * ?")
    @Transactional(readOnly = false)
    public void ASRcallcontentofYesterday(){
        logger.info("定时识别通话语音开始");
        TMisCallingQualityTest entity = new TMisCallingQualityTest();
        initQueryDate(entity,-1);
        List<TMisCallingQualityTest> list = dao.findCallingRecordList(entity);
        for (TMisCallingQualityTest callingQualityTest :list){
            entity.setId(callingQualityTest.getId());
            entity.setUuid(callingQualityTest.getUuid());
            entity.setCallType(callingQualityTest.getCallType());
            entity.setPeopleId(StringUtils.isEmpty(callingQualityTest.getPeopleId())? "" : callingQualityTest.getPeopleId());
            entity.setDurationTime(callingQualityTest.getDurationTime());
            entity.setDealcode(StringUtils.isEmpty(callingQualityTest.getDealcode())? "" : callingQualityTest.getDealcode());
            entity.setTargetNumber(StringUtils.isEmpty(callingQualityTest.getTargetNumber())? "" : callingQualityTest.getTargetNumber());
            entity.setStartTime(callingQualityTest.getStartTime());
            String url = DictUtils.getDictValue("ctiUrl", "callcenter", "") + "audio/";
            List<byte[]> dataList = WavCutUtil.cutWavforPeriod(url+callingQualityTest.getAudioUrl() ,59);
            entity.setCallContent(BaiduAutomaticSpeechRecognitionUtil.asr(dataList));
            entity.setSensitiveWordNumber(String.valueOf(KWSeekerProcessor.newInstance().getKWSeeker("sensitive-word").findWords(entity.getCallContent()).size()));
            dao.insert(entity);
        }
        logger.info("定时识别通话语音结束");
    }

    private TMisCallingQualityTest initQueryDate(TMisCallingQualityTest entity, int day) {
        Date startdate = null;
        Calendar calendarstart = Calendar.getInstance();
        calendarstart.add(Calendar.DAY_OF_MONTH, day);//正数可以得到当前时间+n天，负数可以得到当前时间-n天
        calendarstart.set(Calendar.HOUR_OF_DAY, 00);
        calendarstart.set(Calendar.MINUTE, 00);
        calendarstart.set(Calendar.SECOND, 00);
        startdate = calendarstart.getTime();
        entity.setStartTime(startdate);
        Date enddate = null;
        Calendar calendarend = Calendar.getInstance();
        calendarend.add(Calendar.DAY_OF_MONTH, day);//正数可以得到当前时间+n天，负数可以得到当前时间-n天
        calendarend.set(Calendar.HOUR_OF_DAY, 23);
        calendarend.set(Calendar.MINUTE, 59);
        calendarend.set(Calendar.SECOND, 59);
        enddate = calendarend.getTime();
        entity.setEndTime(enddate);
        return entity;
    }

}
