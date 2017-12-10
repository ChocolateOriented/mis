package com.mo9.risk.util;

import org.jfree.chart.labels.AbstractXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;

import java.util.Map;

/**
 * Created by cyuan on 2017/12/6.
 */
public class JFreeChartLabelGenerator extends AbstractXYItemLabelGenerator implements XYItemLabelGenerator {

    public JFreeChartLabelGenerator(){}

    private String title = null;
    private Map<Integer,Integer> map = null;

    public JFreeChartLabelGenerator(String title, Map<Integer,Integer> map){
        this.title=title;
        this.map = map;
    }


    @Override
    public String generateLabel(XYDataset dataset, int series, int item) {

        if ("C-P1".equals(title)){
            Number x = dataset.getX(series, item);

            if(x != null){
                if(x.intValue() == map.get(series) || (x.intValue() == 8 && series == 0)){

                    Number value = dataset.getYValue(series,item);
                    return value.toString();
                }

            }

            return null;
        }else{
            Number x = dataset.getX(series, item);
            if(x != null){
                if(x.intValue() == map.get(series) || x.intValue() == 1){

                    Number value = dataset.getYValue(series,item);
                    return value.toString();
                }

            }

            return null;
        }

    }
}
