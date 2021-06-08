import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.apache.commons.lang3.StringUtils;


import java.util.*;
import java.util.stream.Collectors;

public class Application {

    //Cron tab:
    //Seconds Minutes Hours DayOfMonth Month DayOfWeek
    //1       2       3     4          5     6

    private static final String QUESTION = "?";
    private static final String ASTERISK = "*";
    private static final String COMMA = ",";

    private static final Integer MON = 1;
    private static final Integer TUES = 2;
    private static final Integer WED = 4;
    private static final Integer THUR = 8;
    private static final Integer FRI = 16;
    private static final Integer SAT = 32;
    private static final Integer SUN = 64;
    /**
     * 替换 分钟、小时、日期、星期
     */
    private static final String ORIGINAL_CRON = "0 %s %s 0 0 %s *";




    public static void main(String[] args){


    }
    /**
     * 检查cron表达式的合法性
     *
     * @param cron cron exp
     * @return true if valid
     */
    public static boolean checkValid(String cron) {
        try {
            CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING);
            CronParser parser = new CronParser(cronDefinition);
            parser.parse(cron);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }


    //Cron to Bingbing
    public static CustomCronField parseCron(String cron) {

        List<String> result = Arrays.asList(cron.trim().split(" "));
        CustomCronField field = new CustomCronField();

        field.setMinutes(Integer.parseInt(result.get(1)));
        field.setHours(Integer.parseInt(result.get(2)));

        //计算日期
        List<Integer> weekDays = new ArrayList<Integer>();
        if (result.get(5).contains(COMMA)) {
            weekDays = Arrays.stream(result.get(5).split(COMMA))
                    .map(Integer::parseInt).collect(Collectors.toList());
        } else if (result.get(5).equals(QUESTION)) {
            weekDays = getInitWeekdays();
        } else {
            weekDays.add(Integer.parseInt(result.get(5)));
        }

        //周一是1， 周二是2，  周三是4，  周四是8，   周五是16，  周六是32，  周日是64
        //2的0次方  2的1次方  2的2次方   2的3次方   2的4次方    2的5次方    2的6次方

        int tempWeek = 0;
        for (Integer weekDay:weekDays) {
            if (weekDay>0){
                Double pow = Math.pow(2, weekDay - 1);
                tempWeek += (pow.intValue());
            }
        }
        field.setWeekdays(tempWeek);

        return field;
    }

    public static String buildCron(CustomCronField field) {
        String minute = field.getMinutes().toString();
        String hour = field.getHours().toString();
        String weekDay = "";

        //周一是1， 周二是2，  周三是4，  周四是8，   周五是16，  周六是32，  周日是64
        //2的0次方  2的1次方  2的2次方   2的3次方   2的4次方    2的5次方    2的6次方
        if (field.getWeekdays()==(127)){
            //全的周一到周日
            weekDay = ASTERISK;
        }else if(field.getWeekdays()==0){
            //一天也不选
            weekDay = QUESTION;
        }
        else{
            if ((field.getWeekdays()&MON)==MON){
                weekDay += "1,";
            }if ((field.getWeekdays()&TUES)==TUES){
                weekDay += "2,";
            }if ((field.getWeekdays()&WED)==WED){
                weekDay += "3,";
            }if ((field.getWeekdays()&THUR)==THUR){
                weekDay += "4,";
            }if ((field.getWeekdays()&FRI)==FRI){
                weekDay += "5,";
            }if ((field.getWeekdays()&SAT)==SAT){
                weekDay += "6,";
            }if ((field.getWeekdays()&SUN)==SUN){
                weekDay += "7,";
            }
            weekDay = weekDay.substring(0, weekDay.length() - 1);
        }

        if (weekDay.equals(QUESTION)) {
            return String.format(ORIGINAL_CRON, minute, hour,weekDay);
        } else {
            return String.format(ORIGINAL_CRON, minute, hour,weekDay);
        }
    }

    //? ,不选择周一到周日
    private static List<Integer> getInitWeekdays() {
        return initArray(0);
    }

    private static List<Integer> initArray(Integer num) {
        List<Integer> result = new ArrayList<>(num);
        for (int i = 0; i <= num; i++) {
            result.add(i);
        }
        return result;
    }



}


