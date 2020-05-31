package com.ang;

import com.ang.reptile.pojo.BangJiaOrder;
import com.ang.reptile.util.QueryDataMapUtil;
import com.ang.reptile.zhujie.NotEncoded;

import java.util.Calendar;
import java.util.HashMap;

public class test {
    public static void main(String[] args) {
//        Calendar calendar = Calendar.getInstance();
//        System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
//        System.out.println(NotEncoded.class == NotEncoded.class);
//        System.out.println(NotEncoded.class.isAssignableFrom(NotEncoded.class));
        BangJiaOrder order = new BangJiaOrder();
        HashMap<String, String> map = QueryDataMapUtil.getQueryDataMap(order, BangJiaOrder.class);
        System.out.println(map);

    }
}
