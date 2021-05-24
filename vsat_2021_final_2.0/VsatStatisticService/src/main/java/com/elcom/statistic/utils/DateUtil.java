package com.elcom.statistic.utils;

import com.elcom.statistic.constant.Constant;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.elcom.statistic.constant.Constant.PARTITION_TYPE_MONTH;
import java.time.LocalDate;

public class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

        public static List<Long> getPartitionNameByTwoDate(String start, String end, int getType) {
        try {
            LocalDate s = LocalDate.parse(start.split(" ")[0]);
            LocalDate e = LocalDate.parse(end.split(" ")[0]);
            List<LocalDate> totalDates = new ArrayList<>();
            while (!s.isAfter(e)) {
                totalDates.add(s);
                s = s.plusDays(1);
            }
            List<Long> result = new ArrayList<>();
            String date = "";
            for (LocalDate totalDate : totalDates) {
                date = totalDate.toString();
                if( getType == Constant.PARTITION_TYPE_MONTH )
                    date = date.substring(0, 8);
                date = date.replaceAll("-", "");
                Long newVal = new Long(date);
                if( !result.contains(newVal) )
                    result.add(newVal);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return null;
    }
        
    public static List<String> getListPartitionBetweenTwoDates(Date startDate, Date endDate, int partitionType) {
        try {
            if(partitionType==PARTITION_TYPE_MONTH){
                List<String> listPartition=new ArrayList<String>();
                Date _curDate=new Date(startDate.getTime());
                while (_curDate.getTime()<=endDate.getTime()){

                    String _curMonth=(_curDate.getMonth()+1)<10?"0"+String.valueOf(_curDate.getMonth()+1):String.valueOf(_curDate.getMonth()+1);
                    listPartition.add(String.valueOf(_curDate.getYear()+1900)+String.valueOf(_curMonth));
                    Calendar c = Calendar.getInstance();
                    c.setTime(_curDate);
                    c.add(Calendar.MONTH, 1);  //adding a month directly - gives the start of next month.
                    _curDate=c.getTime();
                }
                return listPartition;
            }
            else{
                List<String> listPartition=new ArrayList<String>();
                Date _curDate=new Date(startDate.getTime());
                while (_curDate.getTime()<=endDate.getTime()){
                    String _curMonth=(_curDate.getMonth()+1)<10?"0"+String.valueOf(_curDate.getMonth()+1):String.valueOf(_curDate.getMonth()+1);
                    String _sCurDate=_curDate.getDate()<10?("0"+_curDate.getDate()):String.valueOf(_curDate.getDay());

                    listPartition.add(String.valueOf(_curDate.getYear()+1900)+String.valueOf(_curMonth)+String.valueOf(_sCurDate));
                    Calendar c = Calendar.getInstance();
                    c.setTime(_curDate);
                    c.add(Calendar.DATE, 1);  //adding a month directly - gives the start of next month.
                    _curDate=c.getTime();
                }
                return listPartition;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSListPartitionBetweenTwoDates(Date startDate, Date endDate,int partitionType) {
        try {
            String sListPartition="";
            if(partitionType==PARTITION_TYPE_MONTH){
                List<String> listPartition=new ArrayList<String>();
                Date _curDate=new Date(startDate.getTime());
                while (_curDate.getTime()<=endDate.getTime()){

                    String _curMonth=(_curDate.getMonth()+1)<10?"0"+String.valueOf(_curDate.getMonth()+1):String.valueOf(_curDate.getMonth()+1);
                    listPartition.add(String.valueOf(_curDate.getYear()+1900)+String.valueOf(_curMonth));
                    Calendar c = Calendar.getInstance();
                    c.setTime(_curDate);
                    c.add(Calendar.MONTH, 1);  //adding a month directly - gives the start of next month.
                    _curDate=c.getTime();
                }
                for(int i=0;i<listPartition.size();i++){
                    if(i==0){
                        sListPartition+=listPartition.get(i);
                    }
                    else{
                        sListPartition+=","+listPartition.get(i);
                    }

                }
                return sListPartition;
            }
            else{
                List<String> listPartition=new ArrayList<String>();
                Date _curDate=new Date(startDate.getTime());
                while (_curDate.getTime()<=endDate.getTime()){
                    String _curMonth=(_curDate.getMonth()+1)<10?"0"+String.valueOf(_curDate.getMonth()+1):String.valueOf(_curDate.getMonth()+1);
                    String _sCurDate=_curDate.getDate()<10?("0"+_curDate.getDate()):String.valueOf(_curDate.getDay());

                    listPartition.add(String.valueOf(_curDate.getYear()+1900)+String.valueOf(_curMonth)+String.valueOf(_sCurDate));
                    Calendar c = Calendar.getInstance();
                    c.setTime(_curDate);
                    c.add(Calendar.DATE, 1);  //adding a month directly - gives the start of next month.
                    _curDate=c.getTime();
                }
                for(int i=0;i<listPartition.size();i++){
                    if(i==0){
                        sListPartition+=listPartition.get(i);
                    }
                    else{
                        sListPartition+=","+listPartition.get(i);
                    }
                }
                return sListPartition;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getDaysBetweenTwoDates(String strDate1, String strDate2) {

        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date1 = myFormat.parse(strDate1);
            Date date2 = myFormat.parse(strDate2);
            return TimeUnit.DAYS.convert(date2.getTime() - date1.getTime(), TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static long minutesFromTwoTimes(DateTimeFormatter fmt, String firstTime, String secondTime) {
        try {
            LocalTime t1 = LocalTime.parse(firstTime, fmt);
            LocalTime t2 = LocalTime.parse(secondTime, fmt);
            long result = ChronoUnit.MINUTES.between(t1, t2);
            return result < 0 ? 0 : result;
        } catch (Exception ex) {
            LOGGER.info("DateUtil.minutesFromTwoTimes.ex: " + ex.toString());
        }
        return 0;
    }

    public static boolean validateFormat(String s, String format) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(s);
            if (!s.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    public static Date getDayOfThisMonth(int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static Date cacularDate(Date dateFrom, int value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFrom);
        cal.add(Calendar.DATE, value);
        return cal.getTime();
    }

    public static Date stringToDateReport(String dateInString) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {

            date = formatter.parse(dateInString);
            LOGGER.info(formatter.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String changeFormat(String s, String inputFormat, String outFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        return toString(formatter.parse(s), outFormat);
    }

    public static Date toDate(String s, DateTimeFormat format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format.getDescription());
        return formatter.parse(s);
    }

    public static Date toDate(String s, String format, Date defaultVal) throws ParseException {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(s);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static Date toDate(String s, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(s);
    }

    public static String toString(Date s, String format) throws ParseException {
        if (s == null) {
            return "";
        }

        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(s);
    }

    public static Date add(Date dt, int calendar, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, amount);
        dt = c.getTime();
        return dt;
    }

    public static Date addSecond(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.SECOND, amount);
        dt = c.getTime();
        return dt;
    }

    public static Date addMiliSecond(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MILLISECOND, amount);
        dt = c.getTime();
        return dt;
    }

    public static Date addHour(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.HOUR, amount);
        dt = c.getTime();
        return dt;
    }

    public static Date addDay(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, amount);
        dt = c.getTime();
        return dt;
    }

    public static boolean isValidDate(String value) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            df.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidFormat(String value) {
        String format = "yyyy-MM-dd";
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public static Date addMonth(Date dt, Integer amount) {

        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, amount);
        dt = c.getTime();
        return dt;
    }

    public static int getNextMonthIntValue(Date currDate) {

        Calendar c = Calendar.getInstance();
        c.setTime(currDate);
        c.add(Calendar.MONTH, 1);
        return Integer.parseInt(new SimpleDateFormat("MM").format(c.getTime()));
    }

    public static String today(DateTimeFormat format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format.getDescription());
        return df.format(new Date());
    }

    public static String today(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    public static Integer subtract(Date dt1, Date dt2) {
        long diff = Math.abs(dt1.getTime() - dt2.getTime());
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return Integer.valueOf(String.valueOf(diffDays));
    }

    public static Integer getDayOfMonth(Date from) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getMonth(Date from) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        return cal.get(Calendar.MONTH);
    }

    public static Integer getYear(Date from) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        return cal.get(Calendar.YEAR);
    }

    public static Date getLastDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public Timestamp toTimestamp(Date data) {
        return new java.sql.Timestamp(data.getTime());
    }

    //compute by milisecons
    public static long getDateDiff(Date startDate, Date endDate, TimeUnit timeUnit) {
        long diffInMillies = endDate.getTime() - startDate.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static long dateToLong(String format, String date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        Date inputDate;
        try {
            inputDate = simpleDateFormat.parse(date);
            return inputDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static Date stringToDateByForm(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {

            date = formatter.parse(dateString);
            LOGGER.info(formatter.format(date));
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
//    public static void main(String[] args) {
//        Date date1 = new Date(System.currentTimeMillis());
//        Date date2 = new Date(System.currentTimeMillis() - 600000);
//        LOGGER.info(date1 + " | " + date2 + " | Same day : " + isSameDay(date1, date2));
//    }
}
