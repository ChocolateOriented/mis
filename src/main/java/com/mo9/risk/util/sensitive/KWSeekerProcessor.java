package com.mo9.risk.util.sensitive;

import com.mo9.risk.util.sensitive.config.Config;
import com.mo9.risk.util.sensitive.entity.KWSeeker;
import com.mo9.risk.util.sensitive.entity.KWSeekerManage;
import com.mo9.risk.util.sensitive.entity.KeyWord;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jxguo on 2018/1/8.
 */
public class KWSeekerProcessor extends KWSeekerManage{

    private static volatile KWSeekerProcessor instance;

    /**
     * 获取实例
     *
     * @return
     */
    public static KWSeekerProcessor newInstance() {
        if (null == instance) {
            synchronized (KWSeekerProcessor.class) {
                if (null == instance) {
                    instance = new KWSeekerProcessor();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造器
     */
    private KWSeekerProcessor() {
        initialize();
    }

    /**
     * 初始化敏感词
     */
    private void initialize() {
        Map<String, String> map = Config.newInstance().getAll();
        Set<Map.Entry<String, String>> entrySet = map.entrySet();

        Map<String, KWSeeker> seekers = new HashMap<String, KWSeeker>();
        Set<KeyWord> kws;

        for (Map.Entry<String, String> entry : entrySet) {
            String[] words = entry.getValue().split(",");
            kws = new HashSet<KeyWord>();
            for (String word : words) {
                kws.add(new KeyWord(word));
            }
            seekers.put(entry.getKey(), new KWSeeker(kws));
        }
        this.seekers.putAll(seekers);
    }
}
