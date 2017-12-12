package com.mo9.risk.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.*;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Jfreechart工具类
 * <p>
 * 解决中午乱码问题<br>
 * 用来创建类别图表数据集、创建饼图数据集、时间序列图数据集<br>
 * 用来对柱状图、折线图、饼图、堆积柱状图、时间序列图的样式进行渲染<br>
 * 设置X-Y坐标轴样式
 * <p>
 * 
 */
public class ChartUtils {
    private static String NO_DATA_MSG = "数据加载失败";
    private static Font FONT = new Font("宋体", Font.PLAIN, 12);
    public static Color[] CHART_COLORS = {
            new Color(31,129,188), new Color(92,92,97), new Color(144,237,125), new Color(255,188,117),
            new Color(153,158,255), new Color(255,117,153), new Color(253,236,109), new Color(128,133,232),
            new Color(158,90,102),new Color(255, 204, 102) };//颜色
    public static Color[] CHART_XYCOLORS={new Color(81,107,255),new Color(255,234,137), new Color(255,130,130),
            new Color(8,255,26),new Color(94,94,94),
            new Color(255,156,173),new Color(255, 204, 102)};

    static {
        setChartTheme();
    }

    public ChartUtils() {
    }

    /**
     * 中文主题样式 解决乱码
     */
    public static void setChartTheme() {
        // 设置中文主题样式 解决乱码
        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        // 设置标题字体
        chartTheme.setExtraLargeFont(FONT);
        // 设置图例的字体
        chartTheme.setRegularFont(FONT);
        // 设置轴向的字体
        chartTheme.setLargeFont(FONT);
        chartTheme.setSmallFont(FONT);
        chartTheme.setTitlePaint(new Color(51, 51, 51));
        chartTheme.setSubtitlePaint(new Color(85, 85, 85));

        chartTheme.setLegendBackgroundPaint(Color.WHITE);// 设置标注
        chartTheme.setLegendItemPaint(Color.BLACK);//
        chartTheme.setChartBackgroundPaint(Color.WHITE);
        // 绘制颜色绘制颜色.轮廓供应商
        // paintSequence,outlinePaintSequence,strokeSequence,outlineStrokeSequence,shapeSequence

        Paint[] OUTLINE_PAINT_SEQUENCE = new Paint[] { Color.WHITE };
        // 绘制器颜色源
        DefaultDrawingSupplier drawingSupplier = new DefaultDrawingSupplier(CHART_COLORS, CHART_COLORS, OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
        chartTheme.setDrawingSupplier(drawingSupplier);

        chartTheme.setPlotBackgroundPaint(Color.WHITE);// 绘制区域
        chartTheme.setPlotOutlinePaint(Color.WHITE);// 绘制区域外边框
        chartTheme.setLabelLinkPaint(new Color(8, 55, 114));// 链接标签颜色
        chartTheme.setLabelLinkStyle(PieLabelLinkStyle.CUBIC_CURVE);

        chartTheme.setAxisOffset(new RectangleInsets(5, 12, 5, 12));
        chartTheme.setDomainGridlinePaint(new Color(192, 208, 224));// X坐标轴垂直网格颜色
        chartTheme.setRangeGridlinePaint(new Color(192, 192, 192));// Y坐标轴水平网格颜色

        chartTheme.setBaselinePaint(Color.WHITE);
        chartTheme.setCrosshairPaint(Color.BLUE);// 不确定含义
        chartTheme.setAxisLabelPaint(new Color(51, 51, 51));// 坐标轴标题文字颜色
        chartTheme.setTickLabelPaint(new Color(67, 67, 72));// 刻度数字
        chartTheme.setBarPainter(new StandardBarPainter());// 设置柱状图渲染
        chartTheme.setXYBarPainter(new StandardXYBarPainter());// XYBar 渲染

        chartTheme.setItemLabelPaint(Color.black);
        chartTheme.setThermometerPaint(Color.white);// 温度计

        ChartFactory.setChartTheme(chartTheme);
    }

    /**
     * 必须设置文本抗锯齿
     */
    public static void setAntiAlias(JFreeChart chart) {
        chart.setTextAntiAlias(false);

    }

    /**
     * 设置图例无边框，默认黑色边框
     */
    public static void setLegendEmptyBorder(JFreeChart chart) {
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));

    }


    /**
     * 创建饼图数据集合
     */
    public static DefaultPieDataset createDefaultPieDataset(String[] categories, Object[] datas) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < categories.length && categories != null; i++) {
            String value = datas[i].toString();
            if (isPercent(value)) {
                value = value.substring(0, value.length() - 1);
            }
            if (isNumber(value)) {
                dataset.setValue(categories[i], Double.valueOf(value));
            }
        }
        return dataset;

    }

    /**
     * 创建时间序列数据
     * 
     * @param category
     *            类别
     * @param dateValues
     *            日期-值 数组
     * @param xAxisTitle
     *            X坐标轴标题
     * @return
     */
    public static TimeSeries createTimeseries(String category, Vector<Object[]> dateValues) {
        TimeSeries timeseries = new TimeSeries(category);

        if (dateValues != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (Object[] objects : dateValues) {
                Date date = null;
                try {
                    date = dateFormat.parse(objects[0].toString());
                } catch (ParseException e) {
                }
                String sValue = objects[1].toString();
                double dValue = 0;
                if (date != null && isNumber(sValue)) {
                    dValue = Double.parseDouble(sValue);
                    timeseries.add(new Day(date), dValue);
                }
            }
        }

        return timeseries;
    }
    /**
     * 设置 折线图样式
     * 
     * @param plot
     * @param isShowDataLabels
     *            是否显示数据标签 默认不显示节点形状
     */
    public static void setLineRender(CategoryPlot plot, boolean isShowDataLabels) {
        setLineRender(plot, isShowDataLabels, false);
    }

    /**
     * 设置折线图样式
     * 
     * @param plot
     * @param isShowDataLabels
     *            是否显示数据标签
     */
    public static void setLineRender(CategoryPlot plot, boolean isShowDataLabels, boolean isShapesVisible) {
        plot.setNoDataMessage(NO_DATA_MSG);
        plot.setInsets(new RectangleInsets(10, 10, 0, 10), false);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        renderer.setStroke(new BasicStroke(1.5F));
        if (isShowDataLabels) {
            renderer.setBaseItemLabelsVisible(true);
            renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(StandardCategoryItemLabelGenerator.DEFAULT_LABEL_FORMAT_STRING,
                    NumberFormat.getInstance()));
            renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BOTTOM_CENTER));// weizhi
        }
        renderer.setBaseShapesVisible(isShapesVisible);// 数据点绘制形状
        setXAixs(plot);
        setYAixs(plot);

    }

    /**
     * 设置时间序列图样式
     * 
     * @param plot
     * @param isShowData
     *            是否显示数据
     * @param isShapesVisible
     *            是否显示数据节点形状
     */
    public static void setTimeSeriesRender(Plot plot, boolean isShowData, boolean isShapesVisible) {

        XYPlot xyplot = (XYPlot) plot;
        xyplot.setNoDataMessage(NO_DATA_MSG);
        xyplot.setInsets(new RectangleInsets(10, 10, 5, 10));

        XYLineAndShapeRenderer xyRenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();

        xyRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        xyRenderer.setBaseShapesVisible(false);
        if (isShowData) {
            xyRenderer.setBaseItemLabelsVisible(true);
            xyRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
            xyRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BOTTOM_CENTER));// weizhi
        }
        xyRenderer.setBaseShapesVisible(isShapesVisible);// 数据点绘制形状

        DateAxis domainAxis = (DateAxis) xyplot.getDomainAxis();
        domainAxis.setAutoTickUnitSelection(false);
        DateTickUnit dateTickUnit = new DateTickUnit(DateTickUnitType.YEAR, 1, new SimpleDateFormat("yyyy-MM")); // 第二个参数是时间轴间距
        domainAxis.setTickUnit(dateTickUnit);

        StandardXYToolTipGenerator xyTooltipGenerator = new StandardXYToolTipGenerator("{1}:{2}", new SimpleDateFormat("yyyy-MM-dd"), new DecimalFormat("0"));
        xyRenderer.setBaseToolTipGenerator(xyTooltipGenerator);

        setXY_XAixs(xyplot);
        setXY_YAixs(xyplot);

    }

    /**
     * 设置时间序列图样式 -默认不显示数据节点形状
     * 
     * @param plot
     * @param isShowData
     *            是否显示数据
     */

    public static void setTimeSeriesRender(Plot plot, boolean isShowData) {
        setTimeSeriesRender(plot, isShowData, false);
    }

    /**
     * 设置时间序列图渲染：但是存在一个问题：如果timeseries里面的日期是按照天组织， 那么柱子的宽度会非常小，和直线一样粗细
     * 
     * @param plot
     * @param isShowDataLabels
     */

    public static void setTimeSeriesBarRender(Plot plot, boolean isShowDataLabels) {

        XYPlot xyplot = (XYPlot) plot;
        xyplot.setNoDataMessage(NO_DATA_MSG);

        XYBarRenderer xyRenderer = new XYBarRenderer(0.1D);
        xyRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());

        if (isShowDataLabels) {
            xyRenderer.setBaseItemLabelsVisible(true);
            xyRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        }

        StandardXYToolTipGenerator xyTooltipGenerator = new StandardXYToolTipGenerator("{1}:{2}", new SimpleDateFormat("yyyy-MM-dd"), new DecimalFormat("0"));
        xyRenderer.setBaseToolTipGenerator(xyTooltipGenerator);
        setXY_XAixs(xyplot);
        setXY_YAixs(xyplot);

    }

    /**
     * 设置柱状图渲染
     * 
     * @param plot
     * @param isShowDataLabels
     */
    public static void setBarRenderer(CategoryPlot plot, boolean isShowDataLabels) {

        plot.setNoDataMessage(NO_DATA_MSG);
        plot.setInsets(new RectangleInsets(10, 10, 5, 10));
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setMaximumBarWidth(0.075);// 设置柱子最大宽度

        if (isShowDataLabels) {
            renderer.setBaseItemLabelsVisible(true);
        }

        setXAixs(plot);
        setYAixs(plot);
    }

    /**
     * 设置堆积柱状图渲染
     * 
     * @param plot
     */

    public static void setStackBarRender(CategoryPlot plot) {
        plot.setNoDataMessage(NO_DATA_MSG);
        plot.setInsets(new RectangleInsets(10, 10, 5, 10));
        StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        plot.setRenderer(renderer);
        setXAixs(plot);
        setYAixs(plot);
    }

    /**
     * 设置类别图表(CategoryPlot) X坐标轴线条颜色和样式
     * 
     * @param axis
     */
    public static void setXAixs(CategoryPlot plot) {
        Color lineColor = new Color(31, 121, 170);
        plot.getDomainAxis().setAxisLinePaint(lineColor);// X坐标轴颜色
        plot.getDomainAxis().setTickMarkPaint(lineColor);// X坐标轴标记|竖线颜色

    }

    /**
     * 设置类别图表(CategoryPlot) Y坐标轴线条颜色和样式 同时防止数据无法显示
     * 
     * @param axis
     */
    public static void setYAixs(CategoryPlot plot) {
        Color lineColor = new Color(192, 208, 224);
        ValueAxis axis = plot.getRangeAxis();
        axis.setAxisLinePaint(lineColor);// Y坐标轴颜色
        axis.setTickMarkPaint(lineColor);// Y坐标轴标记|竖线颜色
        // 隐藏Y刻度
        axis.setAxisLineVisible(false);
        axis.setTickMarksVisible(false);
        // Y轴网格线条
        plot.setRangeGridlinePaint(new Color(192, 192, 192));
        plot.setRangeGridlineStroke(new BasicStroke(1));

        plot.getRangeAxis().setUpperMargin(0.1);// 设置顶部Y坐标轴间距,防止数据无法显示
        plot.getRangeAxis().setLowerMargin(0.1);// 设置底部Y坐标轴间距

    }

    /**
     * 设置XY图表(XYPlot) X坐标轴线条颜色和样式
     * 
     * @param axis
     */
    public static void setXY_XAixs(XYPlot plot) {
        Color lineColor = new Color(31, 121, 170);
        plot.getDomainAxis().setAxisLinePaint(lineColor);// X坐标轴颜色
        plot.getDomainAxis().setTickMarkPaint(lineColor);// X坐标轴标记|竖线颜色

    }

    /**
     * 设置XY图表(XYPlot) Y坐标轴线条颜色和样式 同时防止数据无法显示
     * 
     * @param axis
     */
    public static void setXY_YAixs(XYPlot plot) {
        Color lineColor = new Color(192, 208, 224);
        ValueAxis axis = plot.getRangeAxis();
        axis.setAxisLinePaint(lineColor);// X坐标轴颜色
        axis.setTickMarkPaint(lineColor);// X坐标轴标记|竖线颜色
        // 隐藏Y刻度
        axis.setAxisLineVisible(false);
        axis.setTickMarksVisible(false);
        // Y轴网格线条
        plot.setRangeGridlinePaint(new Color(192, 192, 192));
        plot.setRangeGridlineStroke(new BasicStroke(1));
        plot.setDomainGridlinesVisible(false);

        plot.getRangeAxis().setUpperMargin(0.12);// 设置顶部Y坐标轴间距,防止数据无法显示
        plot.getRangeAxis().setLowerMargin(0.12);// 设置底部Y坐标轴间距

    }

    /**
     * 设置饼状图渲染
     */
    public static void setPieRender(Plot plot) {

        plot.setNoDataMessage(NO_DATA_MSG);
        plot.setInsets(new RectangleInsets(10, 10, 5, 10));
        PiePlot piePlot = (PiePlot) plot;
        piePlot.setInsets(new RectangleInsets(0, 0, 0, 0));
        piePlot.setCircular(true);// 圆形

        // piePlot.setSimpleLabels(true);// 简单标签
        piePlot.setLabelGap(0.01);
        piePlot.setInteriorGap(0.05D);
        piePlot.setLegendItemShape(new Rectangle(10, 10));// 图例形状
        piePlot.setIgnoreNullValues(true);
        piePlot.setLabelBackgroundPaint(null);// 去掉背景色
        piePlot.setLabelShadowPaint(null);// 去掉阴影
        piePlot.setLabelOutlinePaint(null);// 去掉边框
        piePlot.setShadowPaint(null);
        // 0:category 1:value:2 :percentage
        piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{2}"));// 显示标签数据
    }

    /**
     * 是不是一个%形式的百分比
     * 
     * @param str
     * @return
     */
    public static boolean isPercent(String str) {
        return str != null ? str.endsWith("%") && isNumber(str.substring(0, str.length() - 1)) : false;
    }

    /**
     * 是不是一个数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        return str != null ? str.matches("^[-+]?(([0-9]+)((([.]{0})([0-9]*))|(([.]{1})([0-9]+))))$") : false;
    }

    public static JFreeChart createLineChart(String title, String categoryAxisLabel, String valueAxisLabel, CategoryDataset dataset){
       return ChartFactory.createLineChart(title, categoryAxisLabel, valueAxisLabel, dataset);
    }

    /**
     * 创建图表对象XYChart
     * @param title
     * @param s
     * @param s1
     * @param xySeriesCollection
     * @param vertical
     * @param b
     * @param b1
     * @param b2
     * @return
     */
    public static JFreeChart createXYChart(String title, String s, String s1, XYSeriesCollection xySeriesCollection, PlotOrientation vertical, boolean b, boolean b1, boolean b2) {
        JFreeChart jfreechart = ChartFactory.createXYLineChart(
                title, // 标题
                s, // categoryAxisLabel （category轴，横轴，X轴标签）
                s1, // valueAxisLabel（value轴，纵轴，Y轴的标签）
                xySeriesCollection, // dataset
                vertical,
                b, // legend
                b1, // tooltips
                b2); // URLs

        return jfreechart;

    }

    /**
     * 设置图示
     * @param jfreechart
     */
    public static void setJfreeChart(JFreeChart jfreechart){
        //图示外框去掉
        jfreechart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        //修改字体的齿轮
        jfreechart.setTextAntiAlias(false);
        //修改背景颜色
        jfreechart.setBackgroundPaint(Color.white);
        //修改图示框里面的字体颜色
        jfreechart.getLegend().setItemPaint(Color.gray);
    }

    /**
     * 设置字体以及背景网格
     * @param jfreechart
     * @param plot
     */

    public static void setFont(JFreeChart jfreechart,XYPlot plot){
        // 设置配置字体（解决中文乱码的通用方法）
        Font xfont = new Font("宋体", Font.PLAIN, 16); // X轴
        Font yfont = new Font("宋体", Font.PLAIN, 16); // Y轴
        Font kfont = new Font("宋体", Font.PLAIN, 14); // 底部
        Font titleFont = new Font("宋体", Font.PLAIN, 20); // 图片标题
        //设置背景颜色

        plot.setOutlinePaint(Color.white);//设置外框颜色
        plot.setOutlineVisible(false);//设置外框是否可见
        plot.getDomainAxis().setLabelFont(xfont);
        plot.getRangeAxis().setLabelFont(yfont);
        jfreechart.getLegend().setItemFont(kfont);
        jfreechart.getTitle().setFont(titleFont);



        // 设置网格背景颜色
        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot.setDomainGridlinesVisible(false);
        // 设置网格横线颜色
        plot.setRangeGridlinePaint(new Color(162,162,162));
        //设置横线是是实体线
        plot.setRangeGridlineStroke(new BasicStroke(1));
        // 设置曲线图与xy轴的距离
        plot.setAxisOffset(new RectangleInsets(30D, 0D, 0D, 70D));
    }

    /**
     * 设置XY轴
     * @param plot
     */
    public static void setAxis(XYPlot plot){
        //设置x数据距离图片左端的距离
        ValueAxis domainAxis = plot.getDomainAxis();
        //设置x轴到图片顶端的距离
        domainAxis.setUpperMargin(0.2);
        //设置x轴刻度的颜色
        domainAxis.setTickLabelPaint(new Color(162,162,162));
        domainAxis.setUpperBound(16.9);


        //设置y
        NumberAxis numberaxis  = (NumberAxis)plot.getRangeAxis();
        //去除y轴那条线
        numberaxis.setAxisLineVisible(false);
        //设置坐标轴刻度的颜色
        numberaxis.setTickLabelPaint(new Color(162,162,162));
        //设置y轴起始段不需要从0开始
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setUpperMargin(0.3);//设置y轴距离图片顶端的距离
        numberaxis.setLowerMargin(0.15);//设置y轴距离图片底端的距离


    }

    /**
     * 设置线条
     * @param plot
     * @param title
     * @param map
     */
    public static void setRenderer(XYPlot plot,String title,XYSeriesCollection x,List list){
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer();
        // 设置曲线是否显示数据点(控制折点)
        xylineandshaperenderer.setBaseShapesVisible(true);
        xylineandshaperenderer.setBaseItemLabelsVisible(true);
        //设置线条的颜色
        List series = x.getSeries();
        for (int i = 0;i<series.size();i++){
            //设置线条颜色
            xylineandshaperenderer.setSeriesPaint(i,ChartUtils.CHART_XYCOLORS[i]);
            //设置标签颜色
            xylineandshaperenderer.setSeriesItemLabelPaint(i,ChartUtils.CHART_XYCOLORS[i]);
        }
        //设置线条的粗细
        xylineandshaperenderer.setStroke(new BasicStroke(1.5f));
        xylineandshaperenderer.setBaseItemLabelFont(new Font("宋体",Font.PLAIN,10));
        //第一个参数,控制距离拐点的距离,第二个参数,控制相对拐点的位置,第三个参数,第四个参数是控制旋转的
        xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_LEFT));
        xylineandshaperenderer.setBaseItemLabelGenerator(new JFreeChartLabelGenerator(title,list));
        plot.setRenderer(xylineandshaperenderer);
    }

    /**
     * 获取图片里面的小标签
     * @return
     */
    public static BufferedImage getSmall(Color color,String  content){
        //public static BufferedImage getSmall(){
        //设置画布
        BufferedImage bufferedImage = new BufferedImage(43, 15, BufferedImage.TYPE_INT_RGB);
        //设置画笔
        Graphics2D g = bufferedImage.createGraphics();
        //设置背景颜色
        g.setBackground(color);
        //矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
        g.clearRect(0,   0,   100,   100);
        //画笔的颜色
        g.setColor(Color.black);
        //画笔的字体
        g.setFont(new Font("宋体",Font.TRUETYPE_FONT,12));
        //写的内容
        g.drawString(content,8,11);
        g.dispose();

        return bufferedImage;
    }

    /**
     * 获取融合后的图片
     * @param jfreechart
     * @return
     */
    public static BufferedImage getJfreechartimage(JFreeChart jfreechart,String title,XYSeriesCollection c){

        java.util.List series = c.getSeries();
        BufferedImage big = jfreechart.createBufferedImage(530, 350);

        if("C-P1".equals(title)){
            for(int i = 0;i<series.size();i++){
                BufferedImage small = ChartUtils.getSmall(CHART_XYCOLORS[i],c.getSeries(i).getMaxY()+"");
                //BufferedImage small = ChartUtils.getSmall();
                Graphics2D g = big.createGraphics();
                int x = big.getWidth() - small.getWidth()-32;
                int y = big.getHeight() - (18-i)*small.getHeight();
                g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);


                g.dispose();
            }

        }else {
            for(int i = 0;i<series.size();i++){

                //前面的标签框
                BufferedImage small = ChartUtils.getSmall(CHART_XYCOLORS[i],c.getSeries(i).getMinY()+"");
                //BufferedImage small = ChartUtils.getSmall();
                Graphics2D g = big.createGraphics();
                int x = big.getWidth() - small.getWidth()-32;
                int y = big.getHeight() - (9-i)*small.getHeight();
                g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);

                //后面的标签框
                BufferedImage small2 = ChartUtils.getSmall(CHART_XYCOLORS[i],c.getSeries(i).getMaxY()+"");
                //BufferedImage small2 = ChartUtils.getSmall();
                //Graphics2D g = big.createGraphics();
                int x2 = big.getWidth() - 11*small2.getWidth()+5;
                int y2 = big.getHeight() - (9-i)*small2.getHeight();
                g.drawImage(small2, x2, y2, small2.getWidth(), small2.getHeight(), null);
                g.dispose();
            }

        }

        return big;


    }


    }