package com.ang.reptile.util;

import java.util.Calendar;
import java.util.Date;

public class BangJiaOrderUtil {
    /**
     * 获取上门时间
     * 1. 等于下单时间的时间+2天
     * 2. 下单时间过了18点，就要+3
     * 3. 上门时间的小时可以随便写，我这里就默认是0了
     *
     * @param buyDate
     * @return
     */
    public static Date getRepairedDate(Date buyDate) {
        Calendar repaireDateCalender = Calendar.getInstance();
        repaireDateCalender.setTime(buyDate);
        //检查是否>=18点
        int hour = repaireDateCalender.get(Calendar.HOUR_OF_DAY);
        boolean isOffWork = false;
        if (hour >= 18) {
            isOffWork = true;
        }
        int offset = isOffWork ? 3 : 2;
        repaireDateCalender.add(Calendar.DAY_OF_MONTH, offset);
        return repaireDateCalender.getTime();
    }
}
