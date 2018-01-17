package com.mo9.risk.util;

import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jxguo on 2018/1/3.
 */
public class WavCutUtil {

    /**
     * 日志对象
     */
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(WavCutUtil.class);

    /**
     * 流转二进制数据
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] inputStreamToByte(BufferedInputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int ch;
        while ((ch = is.read(buffer)) != -1) {
            bytestream.write(buffer,0,ch);
        }
        byte data[] = bytestream.toByteArray();
        bytestream.close();
        return data;
    }

    /**
     * 数组反转
     * @param array
     */
    public static byte[] reverse(byte[] array){
        byte temp;
        int len=array.length;
        for(int i=0;i<len/2;i++){
            temp=array[i];
            array[i]=array[len-1-i];
            array[len-1-i]=temp;
        }
        return array;
    }


    public static byte[] cut(byte[] data,long totaltime, int start, int end) {
        byte[] fbyte = null;
        if(start<0 || end<=0 || start>=totaltime || end>totaltime || start>=end){
            return null;
        }
        //音频数据大小（44为128kbps比特率wav文件头长度）
        long wavSize = data.length-44;
        //截取的音频数据大小
        long splitSize = (wavSize/totaltime)*(end-start);
        //截取时跳过的音频数据大小
        long skipSize = (wavSize/totaltime)*start;
        int splitSizeInt = Integer.parseInt(String.valueOf(splitSize));
        int skipSizeInt = Integer.parseInt(String.valueOf(skipSize));
        //存放文件大小,4代表一个int占用字节数
        ByteBuffer buf1 = ByteBuffer.allocate(4);
        //放入文件长度信息
        buf1.putInt(splitSizeInt+36);
        //代表文件长度
        byte[] flen = buf1.array();
        //存放音频数据大小，4代表一个int占用字节数
        ByteBuffer buf2 = ByteBuffer.allocate(4);
        //放入数据长度信息
        buf2.putInt(splitSizeInt);
        //代表数据长度
        byte[] dlen = buf2.array();
        //数组反转
        flen = reverse(flen);
        dlen = reverse(dlen);
        //定义wav头部信息数组
        byte[] head = new byte[44];
        //读取源wav文件头部信息
        for (int i=0; i<head.length; i++){
            head[i] = data[i];
        }
        //4代表一个int占用字节数
        for(int i=0; i<4; i++){
            //替换原头部信息里的文件长度
            head[i+4] = flen[i];
            //替换原头部信息里的数据长度
            head[i+40] = dlen[i];
        }
        //存放截取的音频数据
        fbyte = new byte[splitSizeInt+head.length];
        //放入修改后的头部信息
        for(int i=0; i<head.length; i++){
            fbyte[i] = head[i];
        }
        int index = 44;
        for (int i=44+skipSizeInt; i<fbyte.length + skipSizeInt; i++){  //读取要截取的数据到目标数组
            fbyte[index] = data[i];
            index ++;
        }
        return fbyte;
    }


    /**
     * 获取音频时长
     * @param url
     * @return
     */
    private static double getTimelen(URL url){
        Clip clip = null;
        double timeLen = 0;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip.open(ais);
            timeLen = clip.getMicrosecondLength() / 1000000D;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取音频时长失败",e);
        }
        return timeLen;
    }

    /**
     *
     * @param realPath 音频的url地址
     * @param period   切片时间
     * @return
     */
    public static List<byte[]> cutWavforPeriod(String realPath, int period){
        List<byte[]> list= null;
        URL url = null;
        try {
            url = new URL(realPath);
            BufferedInputStream input = new BufferedInputStream(url.openStream());
            byte[] data = inputStreamToByte(input);
            long t1 = (long) getTimelen(url);
            if (period > t1){
                list = new ArrayList<byte[]>(1);
                list.add(data);
                return list;
            }
            int count = (int) (t1%period==0 ? t1/period : t1/period+1);
            list = new ArrayList<byte[]>(count);
            for (int i=1; i<=count; i++){
                if (i == count){
                    list.add(cut(data, t1, (i-1)*period, (int) t1));
                }else {
                    list.add(cut(data, t1, (i-1)*period,i*period));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("音频切片失败", e);
        }
        return list;
    }
}
