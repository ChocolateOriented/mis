//package com.mo9.risk.util;
//
///**
// * Project Name : msf
// * File Name	: ErrorResponse.java
// * Package Name : com.mo9.msf.core.entity
// * Create Date  : 2015-6-3上午9:43:14
// * <p/>
// * Copyright ©2011 moKredit Inc. All Rights Reserved
// */
//
//import java.text.MessageFormat;
//
//
//import java.text.MessageFormat;
//
//import org.apache.log4j.Logger;
//
//
///**
// * <p>基础应答报文</p>
// *
// * <p>DESC</p>
// *
// ********************************************************
// * Date				Author 		Changes
// * 2015-6-3		Eric Cao	创建
// ********************************************************
// */
//public class BaseResponse {
//    private static Logger log = Logger.getLogger(BaseResponse.class);
//
//    /**应答消息头*/
//    private Header header;
//
//    /**应答消息正文*/
//    private Object body;
//
//    /**
//     * 验证当前应答消息是否是成功业务应答.
//     * @return
//     */
//    public boolean successful() {
//        if (this.header != null && Header.STATUS_CODE_SUCCESS.equalsIgnoreCase(header.getStatus_code())) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 构建一个失败的应答报文.
//     * @param status_code 状态码.
//     * @param desc
//     * @return
//     */
//    public static BaseResponse buildErrorResponse(String status_code, String desc) {
//        Header header = new Header();
//        header.serial_no = IdGenerator.newId();
//        header.status_code = status_code;
//        header.desc = desc;
//        BaseResponse resp = new BaseResponse();
//        resp.header = header;
//        return resp;
//    }
//
//    /**
//     * 构建一个失败的应答报文.
//     * @param status_code 状态码.
//     * @param desc
//     * @return
//     */
//    public static BaseResponse buildErrorResponse(String desc) {
//        Header header = new Header();
//        header.serial_no = IdGenerator.newId();
//        header.status_code = Header.STATUS_CODE_FAILED;
//        header.desc = desc;
//        BaseResponse resp = new BaseResponse();
//        resp.header = header;
//        return resp;
//    }
//
//    /**
//     * 构建一个失败的应答报文.
//     * @param status_code 状态码.
//     * @param desc
//     * @return
//     */
//    public static BaseResponse buildErrorResponse(String desc, Exception e) {
//        Header header = new Header();
//        header.serial_no = IdGenerator.newId();
//        header.status_code = Header.STATUS_CODE_FAILED;
//        header.desc = desc;
//        BaseResponse resp = new BaseResponse();
//        resp.header = header;
//        if (e != null) {
//            String logtxt = MessageFormat.format("BuildErrorResponse#serial_no:{0},status_code:{1},desc:{2}", header.serial_no, header.status_code, header.desc);
//            log.warn(logtxt, e);
//        }
//        return resp;
//    }
//
//    /**
//     * 构建一个失败的应答报文.
//     * @param status_code 状态码.
//     * @param desc
//     * @return
//     */
//    public static BaseResponse buildSuccessResponse(Object result) {
//        Header header = new Header();
//        header.serial_no = IdGenerator.newId();
//        header.status_code = Header.STATUS_CODE_SUCCESS;
//        header.desc = "SUCCESS";
//        BaseResponse resp = new BaseResponse();
//        resp.header = header;
//        resp.body = result;
//        return resp;
//    }
//
//    public static BaseResponse buildResponse(Header header, Object result) {
//        BaseResponse resp = new BaseResponse();
//        resp.header = header;
//        resp.body = result;
//        return resp;
//    }
//
//    public Header getHeader() {
//        return header;
//    }
//
//
//    public void setHeader(Header header) {
//        this.header = header;
//    }
//
//    public Object getBody() {
//        return body;
//    }
//
//    /**
//     * @see java.lang.Object#toString()
//     */
//    @Override
//    public String toString() {
//
//        return MessageFormat.format("BuildErrorResponse#serial_no:{0},status_code:{1},desc:{2}", header.serial_no, header.status_code, header.desc);
//    }
//
//    public void setBody(Object body) {
//        this.body = body;
//    }
//
//
//    /**
//     * <p>应答消息头</p>
//     */
//    public static final class Header {
//
//        /***
//         * 应答消息序列号
//         */
//        private String serial_no;
//
//        /**
//         * 应答状态码.
//         * 应答状态码为"0000"时表示业务处理成功,其他均表示业务处理失败.
//         */
//        private String status_code;
//
//        /**应答状态码:业务处理成功*/
//        public static final String STATUS_CODE_SUCCESS = "0000";
//        /**应答状态码:业务处理失败*/
//        public static final String STATUS_CODE_FAILED = "9999";
//
//        /**
//         * 应答描述.
//         */
//        private String desc;
//
//        public String getSerial_no() {
//            return serial_no;
//        }
//
//        public void setSerial_no(String serial_no) {
//            this.serial_no = serial_no;
//        }
//
//        public String getStatus_code() {
//            return status_code;
//        }
//
//        public void setStatus_code(String status_code) {
//            this.status_code = status_code;
//        }
//
//        public String getDesc() {
//            return desc;
//        }
//
//        public void setDesc(String desc) {
//            this.desc = desc;
//        }
//
//    }
//}
