package com.thinkgem.jeesite.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
//		Calendar instance = Calendar.getInstance();
//		instance.set(2018,01,31);
//		instance.add(Calendar.MONTH,+1);
//        Date time = instance.getTime();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(simpleDateFormat.format(time));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date d = sdf.parse("2018-01-16");
            Calendar cld = Calendar.getInstance();
            cld.setTime(d);
            cld.add(Calendar.MONTH, -3);
            Date d2 = cld.getTime();
            System.out.println(sdf.format(d)+"减一月："+sdf.format(d2));

            //闰年的情况
            d = sdf.parse("2016-01-31");
            cld = Calendar.getInstance();
            cld.setTime(d);
            cld.add(Calendar.MONTH, -1);
            d2 = cld.getTime();
            System.out.println(sdf.format(d)+"加一月："+sdf.format(d2));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
