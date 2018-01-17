package com.mo9.risk.util.sensitive.util;

import com.mo9.risk.util.sensitive.entity.KeyWord;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jxguo on 2018/1/8.
 */
public class EndTagUtil {

    /**
     *
     */
    private static final long serialVersionUID = 8278503553932163596L;

    /**
     * 尾节点key
     */
    public static final String TREE_END_TAG = "end";

    public static Map<String, Map> buind(KeyWord KeyWord) {
        Map<String, Map> endTag = new HashMap<String, Map>();
        endTag.put(TREE_END_TAG, new HashMap<String, String>());
        return endTag;
    }
}
