package com.mo9.risk.util;

import com.baidu.aip.speech.AipSpeech;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by jxguo on 2018/1/3.
 * 原始 PCM 的录音参数必须符合 8k/16k 采样率、16bit 位深、单声道，支持的格式有：pcm（不压缩）、
 * wav（不压缩，pcm编码）、amr（压缩格式）
 */
public class BaiduAutomaticSpeechRecognitionUtil {

    //设置APPID/AK/SK
    private static final String APP_ID = "10593398";
    private static final String API_KEY = "CN9D4u7Q8HfyCgYsKbFNT9q4";
    private static final String SECRET_KEY = "SjGUf7wklHXeBXUnPdnzSQv6H5lpGlAe";


    /**
     * 日志对象
     */
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(BaiduAutomaticSpeechRecognitionUtil.class);

    private static volatile AipSpeech client = null;

    public static  AipSpeech getClient(){
        if (client == null){
            synchronized (AipSpeech.class){
                if (client == null){
                    // 初始化一个AipSpeech
                    client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
                    // 可选：设置网络连接参数
                    client.setConnectionTimeoutInMillis(2000);
                    client.setSocketTimeoutInMillis(60000);

                    // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
                    //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
                    //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
                }
            }
        }
        return client;
    }

    public static String asr(List<byte[]> list) {
        if (list == null){
            return "";
        }
        StringBuffer result  = new StringBuffer();
        for (byte[] data : list){
            if (data != null){
                JSONObject asrRes = getClient().asr(data, "pcm", 8000, null);
                if (asrRes.get("err_msg").equals("success.")){
                    result.append(((JSONArray)asrRes.get("result")).get(0));
                }else {
                    logger.info("百度语音识别失败，错误码："+asrRes.get("err_no"));
                }
            }
        }
        return result.toString();
    }
}
