package com.ang.reptile.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DataParseUtil {
    private static Logger logger = LoggerFactory.getLogger(DataParseUtil.class);

    public static Result parseToPojo(String resultStr) {
        List<String> list = null;// = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(resultStr);
        if (jsonObject != null) {
            Integer total = 0;
            Integer code = jsonObject.getInteger("code");
            if (code != null && code.equals(200)) {
                JSONObject data = jsonObject.getJSONObject("data");
                total = data.getInteger("total");
                JSONArray items = data.getJSONArray("rows");
                list = items.toJavaList(String.class);
                return new Result(0, total, list == null ? new ArrayList<>() : list);
            } else {
                logger.error("===> 服务端返回的状态码不是200！");
                logger.error("===> 服务端返回的状态码: {}", code);
                logger.error("===> 服务端返回的信息值: {}", jsonObject.getString("message"));

                return null;
            }
        }

        return null;

    }

    public static class Result {
        private Integer currentPage;
        private Integer totalPage;
        private List<String> datas;

        public Result(Integer currentPage, Integer totalPage, List<String> datas) {
            this.currentPage = currentPage;
            this.totalPage = totalPage;
            this.datas = datas;
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(Integer totalPage) {
            this.totalPage = totalPage;
        }

        public List<String> getDatas() {
            return datas;
        }

        public void setDatas(List<String> datas) {
            this.datas = datas;
        }
    }
}
