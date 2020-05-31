package com.ang.reptile.code;

public class MessageCode {
    public static class Code{
        /**
         * 100-130
         */
        public static Integer ADDRESS_NOT_MATCHED = 100;
        public static Integer UPSTREAM_ACTIVEORDER_NOTFOUND = 101;
        public static Integer CREATE_ORDER_PART_SUCCESS = 102;
        public static Integer BANGJIA_ORDER_NOT_FOUND = 103;
        public static Integer LOGIN_FAIL = 104;



        /**
         * 200-220
         */
        public static Integer REQUEST_ERROR = 200;
    }

    public static class Message{
        public static String ADDRESS_NOT_MATCHED = "没能匹配上游地址到下游，请检查配置信息(address_mapper.json)！";
        public static String UPSTREAM_ACTIVEORDER_NOTFOUND = "没有发现可用的上游订单！";
        public static String CREATE_ORDER_PART_SUCCESS = "创建帮家订单部分成功部分失败！";
        public static String REQUEST_ERROR = "httpRequest出现异常！";
        public static String BANGJIA_ORDER_NOT_FOUND = "没有发现有效的帮家订单！";
        public static String LOGIN_FAIL = "登录失败！";
    }
}
