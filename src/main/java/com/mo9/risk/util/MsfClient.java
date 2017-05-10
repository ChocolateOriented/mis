package com.mo9.risk.util;

/**
 * Project Name : msf
 * File Name	: MsfClient.java
 * Package Name : com.mo9.msf.core.client
 * Create Date  : 2015-6-8下午12:10:22
 *
 * Copyright ©2011 moKredit Inc. All Rights Reserved
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gamaxpay.commonutil.msf.BaseResponse;
import com.gamaxpay.commonutil.msf.JacksonConvertor;
import com.gamaxpay.commonutil.web.PostRequest;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;


/**
 * <p>Msf客户端程序</p>
 *
 * <p>DESC</p>
 *
 ********************************************************
 * Date				Author 		Changes
 * 2015-6-8		Eric Cao	创建
 ********************************************************
 */
public class MsfClient
{

    /**MSF默认客户端访问账户名.*/
    private static final String DEFAULT_ACCT="S_soaDefClient";
    /**MSF默认客户端访问密码.*/
    private static final String DEFAULT_PASSWD="kdjdi95@d7";

    public static final String SOA_PATH_CONFIG="soa.path";

    public static final String VALID_TYPE_SECRET="secret";

    /**本类唯一实体*/
    private static final MsfClient INSTANCE=new MsfClient();

    /**是否已经初始化*/
    private static   boolean inited;

    /**当前主机地址,含协议及端口号:例如"http://localhost/"*/
    private static String currHost ;

    private Logger logger = Logger.getLogger(MsfClient.class);

    /**
     * 获取本类唯一实例对象.
     *
     * @return
     */
    public static MsfClient instance()
    {
        if(!inited)
        {
            inited=true;
//            currHost = Mo9DataSourceProperty.getProperty(SOA_PATH_CONFIG);
//            currHost = "https://www.mo9.com";
            currHost=DictUtils.getDictValue("短信链接", "sms_url", "https://new.mo9.com");
            //currHost = "http://127.0.0.1";
//           currHost = "https://new.mo9.com";
        }
        return INSTANCE;
    }
  

    /**
     *
     * 客户端发送HTTP请求.
     *
     * @param url 请求的业务URI
     * @param params 请求参数
     * @param responseType 应答对象类型.
     * @return
     */
    public    <T extends BaseResponse>  T request(String url ,Map<String,String> params, Class<? extends BaseResponse>  responseType)
    {
        //Step2:通过HTTP协议,发送消息请求.
        String respText = "";
        try
        {

            System.out.println("MsfClient post url :"+url);
            respText = PostRequest.postRequest(url, params);
            System.out.println(respText);
        } catch (IOException e)
        {
            logger.warn("网络交互失败",e);
            return (T) BaseResponse.buildErrorResponse("网络交互失败.", e);
        }

        //Step3,解析对象并返回.
        return (T)(new JacksonConvertor().parse(respText, responseType));
    }

    public   BaseResponse  requestForBodyType(String url ,Map<String,String> params, Class<? extends Object>  bodyType)
    {
        //Step2:通过HTTP协议,发送消息请求.
        String respText = "";
        try
        {

            respText = PostRequest.postRequest(url, params);
//            System.out.println(respText);
        } catch (IOException e)
        {
            logger.warn("网络交互失败",e);
            return BaseResponse.buildErrorResponse("网络交互失败.", e);
        }

        //Step3,解析对象并返回.
        JSONObject jsonObj = JSON.parseObject(respText);
        JSONObject headerJsonObject = jsonObj.getJSONObject("header");
        JSONObject bodyJsonObject = jsonObj.getJSONObject("body");
        BaseResponse.Header header = JSON.toJavaObject(headerJsonObject, BaseResponse.Header.class);
        Object body = JSON.toJavaObject(bodyJsonObject, bodyType);
        return BaseResponse.buildResponse(header,body);

    }

    /**
     * 使用默认服务器账号发送支付请求.
     * 该方法将自动为用户填充请求的client_id,valid_type,valid_token三个参数.
     * @param url
     * @param params
     * @param responseType
     * @return
     */
    public    <T extends BaseResponse>  T requestByDefAcct(String url ,Map<String,String> params, Class<? extends BaseResponse>  responseType)
    {
        params.put("client_id", DEFAULT_ACCT);
        params.put("valid_type", VALID_TYPE_SECRET);
        params.put("valid_token", DEFAULT_PASSWD);
        return request(url,params,responseType);
    }

    /**
     * 使用默认服务器账号发送支付请求.
     * 该方法将自动为用户填充请求的client_id,valid_type,valid_token三个参数.
     * @param url
     * @param params
     * @param responseType
     * @return
     */
    public <T extends BaseResponse>  T requestFromServer(String path ,Map<String,String> params, Class<? extends BaseResponse>  responseType)
    {
        params.put("client_id", DEFAULT_ACCT);
        params.put("valid_type", VALID_TYPE_SECRET);
        params.put("valid_token", DEFAULT_PASSWD);
        return request(currHost+path,params,responseType);
    }


    public BaseResponse  requestFromServerForBodyType(String path ,Map<String,String> params, Class<? extends Object>  bodyType)
    {
        params.put("client_id", DEFAULT_ACCT);
        params.put("valid_type", VALID_TYPE_SECRET);
        params.put("valid_token", DEFAULT_PASSWD);
        return requestForBodyType(currHost+path,params,bodyType);
    }
}
