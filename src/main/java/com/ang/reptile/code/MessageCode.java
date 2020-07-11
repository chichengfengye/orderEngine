package com.ang.reptile.code;

public class MessageCode {
    public static class Code{
        /**
         * 100-139 getOrder
         */




        /**
         * 140-179 truncateOrder
         */
        public static Integer ADDRESS_NOT_MATCHED = 140;

        /**
         * 180-219-createOrder
         */
        public static Integer CREATE_ORDER_PART_SUCCESS = 180;
        public static Integer BANGJIA_ORDER_NOT_FOUND = 181;


        /**
         * 220-259 internal code
         */
        public static Integer REQUEST_ERROR = 200;
        public static Integer LOGIN_FAIL = 201;

        /**
         * 260-299
         */
        public static Integer MERCHENT_REQ_NOT_FOUND = 260;

    }

    public static class Message{
        public static String ADDRESS_NOT_MATCHED = "没能匹配上游地址到下游，请检查配置信息(address_mapper.json)！";
        public static String CREATE_ORDER_PART_SUCCESS = "创建帮家订单部分成功部分失败！";
        public static String REQUEST_ERROR = "httpRequest出现异常！";
        public static String BANGJIA_ORDER_NOT_FOUND = "没有发现有效的帮家订单！";
        public static String LOGIN_FAIL = "登录失败！";
        public static String MERCHENT_REQ_NOT_FOUND = "没有查询到上游商家请求参数信息！";
    }
}
