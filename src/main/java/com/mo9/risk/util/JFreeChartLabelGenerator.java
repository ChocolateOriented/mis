package com.mo9.risk.util;

import org.jfree.chart.labels.AbstractXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;

import java.util.List;

/**
 * Created by cyuan on 2017/12/6.
 */
public class JFreeChartLabelGenerator extends AbstractXYItemLabelGenerator implements XYItemLabelGenerator {

    public JFreeChartLabelGenerator(){}

    private String title = null;

    private List list = null;
    public JFreeChartLabelGenerator(String title,List list){
        this.title=title;
        this.list = list;
    }


    @Override
    public String generateLabel(XYDataset dataset, int series, int item) {
        Number x = dataset.getX(series, item);
        if ("C-P1".equals(title) && series == 0 && x.intValue() == (int) list.get(0) ) {
            Number value = dataset.getY(series, item);

            return value.toString();
        }
        return null;
    }
}
