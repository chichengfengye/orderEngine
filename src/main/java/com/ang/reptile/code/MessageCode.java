package com.ang.reptile.code;

public class MessageCode {
    public static class Code{
        /**
         * 100-130
         */
        public static Integer ADDRESS_NOT_MATCHED = 100;
        public static Integer UPSTREAM_ACTIVEORDER_NOTFOUND = 101;
        public static Integer CREATE_ORDER_PART_SUCCESS = 102;
    }

    public static class Message{
        public static String ADDRESS_NOT_MATCHED = "没能匹配上游地址到下游，请检查配置信息(address_mapper.json)！";
        public static String UPSTREAM_ACTIVEORDER_NOTFOUND = "没有发现可用的上游订单！";
        public static String CREATE_ORDER_PART_SUCCESS = "创建下游订单部分成功部分失败！";

    }
}
