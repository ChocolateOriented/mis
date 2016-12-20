//package com.mo9.risk.util;
//
//
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * <p>通用ID生成器</p>
// * <p/>
// * <p>
// * 1.该类主要用于创建基于时间戳的长度为2位的唯一ID,用于基于时间顺序完成的分表存储的业务数据。
// * 2.ID格式如下"yyMMddhhmmssaaaXXXXX",其中前15位为精确到毫秒的15位时间戳,后五位为随机数。
// * 2.该规则保证在不同毫秒时间创建的ID数值不同，同一毫秒创建的两个Id重复概率为十万分之一。
// * </p>
// * <p/>
// * *******************************************************
// * Date				Author 		Changes
// * 2014-3-7下午6:35:47	Eric Cao	创建
// * *******************************************************
// */
//public class IdGenerator {
//    /**
//     * 时间戳格式
//     */
//    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyMMddHHmmssSSS");
//
//    /**
//     * 随机数格式
//     */
//    private static final DecimalFormat RANDOM_FORMT = new DecimalFormat("#####");
//
//    static {/**设置显示随机数的格式*/
//        RANDOM_FORMT.setMinimumIntegerDigits(5);
//        RANDOM_FORMT.setMaximumIntegerDigits(5);
//    }
//
//    /**
//     * 最大随机数.
//     */
//    public static final int MAX_RANDOM = 99999;
//
//    /**
//     * newId:创建一个新Id.<br/>
//     * TODO(DESC).<br/>
//     *
//     * @return
//     */
//    public static String newId() {
//        String timestamp = TIMESTAMP_FORMAT.format(new Date());
//        String random = RANDOM_FORMT.format(((long) (Math.random() * MAX_RANDOM)));
//        return timestamp + random;
//    }
//
//    /**
//     * parseDate:解析當前ID創建的時間.<br/>
//     * TODO(DESC).<br/>
//     *
//     * @param id
//     * @return
//     */
//    public static Date parseDate(String id) {
//        try {
//            return TIMESTAMP_FORMAT.parse(id.substring(0, 15));
//        } catch (ParseException e) {
//            throw new RuntimeException("Id格式錯誤.id:" + id);
//        }
//    }
//
//
//    public static void main(String[] args) {
//        Map<String, String> params = new HashMap<String, String>();
//        for (int i = 1; i < 500; i++) {
//            String id = newId();
//            if (params.get(id) != null) {
//                System.out.println("重複ID：" + i);
//            }
//            params.put(id, id);
//        }
//    }
//}
