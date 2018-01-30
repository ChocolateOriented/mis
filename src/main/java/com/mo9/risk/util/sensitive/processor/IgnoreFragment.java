package com.mo9.risk.util.sensitive.processor;

import com.mo9.risk.util.sensitive.entity.KeyWord;

/**
 * Created by jxguo on 2018/1/8.
 */
public class IgnoreFragment extends AbstractFragment {
    private String ignore = "";

    public IgnoreFragment() {
    }

    public IgnoreFragment(String ignore) {
        this.ignore = ignore;
    }

    @Override
    public String format(KeyWord word) {
        return ignore;
    }
}
