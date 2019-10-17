package cn.ce.api.util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    private static String pat = "yyyy:MM:dd HH:mm:ss:SSS";
    private static DateFormat df = null;
    private static SimpleDateFormat sdf = null;

    /**
     * @Title: getTimeStamp
     * @Description: 获取时间戳
     * @param: @return
     * @return: Long
     * @throws
     */
    public static Long getTimeStamp(){
        return new Date().getTime();
    }

    /**
     * @Title: getDateTime
     * @Description: 获取当前时间和日期
     * @param: @return
     * @return: String
     * @throws
     */
    public static String getDateTime(){
        df = DateFormat.getDateTimeInstance();
        return df.format(new Date());
    }

    /**
     * @Title: getDateComplete
     * @Description: 获取当前时间和日期及毫秒
     * @param: @return
     * @return: String
     * @throws
     */
    public static String getDateComplete(){
        sdf = new SimpleDateFormat(pat);
        return sdf.format(new Date());
    }

    /**
     * @Title: getDateType
     * @Description: 获取当前日期是Date类型
     * @param: @param strDate
     * @param: @return
     * @param: @throws ParseException
     * @return: Date
     * @throws
     */
    public static Date getDateType(String strDate) throws ParseException{
        sdf = new SimpleDateFormat(pat);
        return sdf.parse(strDate);
    }


    public static void main(String[] args) {
        System.out.println(getDateTime());
    }

}
