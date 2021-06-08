import java.util.*;
import java.util.stream.Collectors;

public class Application {

    //Cron表达式是一个字符串，以5或6个空格隔开，分为6或7个域，每一个域代表一个含义，即两种语法格式：
    //Seconds Minutes Hours DayOfMonth Month DayOfWeek
    //1       2       3     4          5     6
    //Seconds Minutes Hours DayofMonth Month DayofWeek Year
    //1       2       3     4          5     6         7



    private static final String QUESTION = "?";
    private static final String ASTERISK = "*";
    private static final String COMMA = ",";

    private static final Integer SUN = 1;
    private static final Integer MON = 2<<0;
    private static final Integer TUES = 2<<1;
    private static final Integer WED = 2<<2;
    private static final Integer THUR = 2<<3;
    private static final Integer FRI = 2<<4;
    private static final Integer SAT = 2<<5;


    private static final String SUN_Cron = "1,";
    private static final String MON_Cron = "2,";
    private static final String TUES_Cron = "3,";
    private static final String WED_Cron = "4,";
    private static final String THUR_Cron = "5,";
    private static final String FRI_Cron = "6,";
    private static final String SAT_Cron = "7,";

    /**
     * 替换 分钟、小时、日期、星期
     */
    private static final String ORIGINAL_CRON =     "0 %s %s ? * %s";
    private static final String ORIGINAL_CRON_Y =   "0 %s %s %s %s ? %s";
    private static final String ORIGINAL_CRON_EVERYDAY =   "0 %s %s * * ?";




    public static void main(String[] args){


    }



    //Cron to Bingbing
    public static CustomCronField parseCron(String cron) {

        List<String> result = Arrays.asList(cron.trim().split(" "));
        CustomCronField field = new CustomCronField();
        boolean includeYear = result.size() == 7 ? true : false;

        //计算WeekDay
        List<Integer> weekDays = new ArrayList<Integer>();
        //"，"分隔的就是选中了 周一到周日的某几天
        if (result.get(5).contains(COMMA)) {
            weekDays = Arrays.stream(result.get(5).split(COMMA))
                    .map(Integer::parseInt).collect(Collectors.toList());
        } else if (includeYear) {
            //不选择周一到周日中的任意一天：不重复
            //例如：0 15 04 9 6 ? 2021
            weekDays = getNoneWeekdays();
        }else if (result.get(5).equals(QUESTION) && !includeYear) {
            //* ,选择周一到周日：每一天
            weekDays = getAllWeekdays();
        }
        else {
            weekDays.add(Integer.parseInt(result.get(5)));
        }

        //Cron：    周一是2， 周二是3，  周三是4，  周四是5，    周五是6，   周六是7，   周日是1
        //bingbing：周一是2， 周二是4，  周三是8，  周四是16，   周五是32，  周六是64，  周日是1
        //          2的1次方  2的2次方  2的3次方   2的4次方    2的5次方    2的6次方   2的0次方

        int tempWeek = 0;
        for (Integer weekDay:weekDays) {
            if (weekDay>0){
                Double pow = Math.pow(2, weekDay - 1);
                tempWeek += (pow.intValue());
            }
        }
        field.setWeekdays(tempWeek);

        //计算其他属性

        if(includeYear){
            field.setDay(Integer.parseInt(result.get(3)));
            field.setMonth(Integer.parseInt(result.get(4)));
            field.setYear(Integer.parseInt(result.get(6)));
        }
        field.setMinute(Integer.parseInt(result.get(1)));
        field.setHour(Integer.parseInt(result.get(2)));

        return field;
    }

    public static String buildCron(CustomCronField field) {
        String minute = field.getMinute().toString();
        String hour = field.getHour().toString();


        String weekDay = "";

        //Cron：    周一是2， 周二是3，  周三是4，  周四是5，    周五是6，   周六是7，   周日是1
        //bingbing：周一是2， 周二是4，  周三是8，  周四是16，   周五是32，  周六是64，  周日是1
        //          2的1次方  2的2次方  2的3次方   2的4次方   2的5次方    2的6次方    2的0次方

        if (field.getWeekdays()==(127)){
            //全的周一到周日:每一天
            weekDay = QUESTION;
        }else if(field.getWeekdays()==0){
            //一天也不选：不重复
            weekDay = QUESTION;
        }
        else{
            if ((field.getWeekdays()&SUN)==SUN){
                weekDay += SUN_Cron;
            }
            if ((field.getWeekdays()&MON)==MON){
                weekDay += MON_Cron;
            }if ((field.getWeekdays()&TUES)==TUES){
                weekDay += TUES_Cron;
            }if ((field.getWeekdays()&WED)==WED){
                weekDay += WED_Cron;
            }if ((field.getWeekdays()&THUR)==THUR){
                weekDay += THUR_Cron;
            }if ((field.getWeekdays()&FRI)==FRI){
                weekDay += FRI_Cron;
            }if ((field.getWeekdays()&SAT)==SAT){
                weekDay += SAT_Cron;
            }
            //删除最后一个"，"
            weekDay = weekDay.substring(0, weekDay.length() - 1);
        }

        //不重复
        //例如：0 15 04 9 6 ? 2021
        if(field.getWeekdays()==0){
            //当前时间
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int date = c.get(Calendar.DATE);
            //TODO：为啥加1呢
            return String.format(ORIGINAL_CRON_Y, minute, hour,date+1,month+1,year);
        }else if(field.getWeekdays()==127){
            //每一天
            return String.format(ORIGINAL_CRON_EVERYDAY, minute, hour);
        }else{
            return String.format(ORIGINAL_CRON, minute, hour,weekDay);
        }


    }

    //? ,不选择周一到周日
    private static List<Integer> getNoneWeekdays() {
        return initArray(0);
    }

    //* ，每天
    private static List<Integer> getAllWeekdays() {
        return initArray(7).subList(0, 7);
    }

    private static List<Integer> initArray(Integer num) {
        List<Integer> result = new ArrayList<>(num);
        for (int i = 1; i <= num; i++) {
            result.add(i);
        }
        return result;
    }



}


