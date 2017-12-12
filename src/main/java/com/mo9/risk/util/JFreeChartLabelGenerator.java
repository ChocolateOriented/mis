package com.mo9.risk.util;

import org.jfree.chart.labels.AbstractXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Created by cyuan on 2017/12/6.
 */
public class JFreeChartLabelGenerator extends AbstractXYItemLabelGenerator implements XYItemLabelGenerator {

    public JFreeChartLabelGenerator(){}

    private XYSeriesCollection collection;
    private String title = null;

    public JFreeChartLabelGenerator(String title,XYSeriesCollection collection){
        this.title=title;
        this.collection= collection;
    }


    @Override
    public String generateLabel(XYDataset dataset, int series, int item) {
        int maxX = (int)collection.getSeries(0).getMaxX();
        Number x = dataset.getX(series, item);
        if ("C-P1".equals(title) && series == 0 && x.intValue() == maxX ) {
            Number value = dataset.getY(series, item);

            return value.toString();
        }
        return null;
    }
}
