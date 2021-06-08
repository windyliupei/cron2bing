import junit.framework.Assert;
import org.junit.jupiter.api.Test;

public class TestApp {

    @Test
    public void toCronTest(){
        CustomCronField customCronField = new CustomCronField();
        customCronField.setHour(15);
        customCronField.setMinute(30);

        //Sun
        Integer weekDay = 1;
        customCronField.setWeekdays(weekDay);
        String s = Application.buildCron(customCronField);
        Assert.assertEquals("1",s.split(" ")[5]);

        //Mon
        weekDay = 2;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("2",s.split(" ")[5]);

        //Tues
        weekDay = 4;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("3",s.split(" ")[5]);

        //Wen
        weekDay = 8;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("4",s.split(" ")[5]);

        //Thur
        weekDay = 16;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("5",s.split(" ")[5]);

        //Fri
        weekDay = 32;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("6",s.split(" ")[5]);

        //Sat
        weekDay = 64;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("7",s.split(" ")[5]);
////////////////////////////////////////////////////////////////////////////////
        //不重复
        weekDay = 0;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("?",s.split(" ")[5]);

        //周一到周六
        weekDay = 126;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("2,3,4,5,6,7",s.split(" ")[5]);

        //工作日
        weekDay = 62;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("2,3,4,5,6",s.split(" ")[5]);

        //每天
        weekDay = 127;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("?",s.split(" ")[5]);
    }

    @Test
    public void toCustomer(){
        //不重复
        CustomCronField customCronField = Application.parseCron("0 15 04 9 6 ? 2021");
        Assert.assertEquals(0,customCronField.getWeekdays().intValue());

        //周一
        customCronField = Application.parseCron("0 15 04 ? * 2");
        Assert.assertEquals(2,customCronField.getWeekdays().intValue());

        //周日
        customCronField = Application.parseCron("0 15 04 ? * 1");
        Assert.assertEquals(1,customCronField.getWeekdays().intValue());

        //周一到周六
        customCronField = Application.parseCron("0 15 04 ? * 2,3,4,5,6,7");
        Assert.assertEquals(126,customCronField.getWeekdays().intValue());

        //工作日
        customCronField = Application.parseCron("0 15 04 ? * 2,3,4,5,6");
        Assert.assertEquals(62,customCronField.getWeekdays().intValue());

        //每天
        customCronField = Application.parseCron("0 15 04 * * ?");
        Assert.assertEquals(127,customCronField.getWeekdays().intValue());
    }

    @Test
    public void overTodayTest(){
        CustomCronField customCronField = new CustomCronField();
        customCronField.setHour(15);
        customCronField.setMinute(30);

        //不重复
        Integer weekDay = 0;
        customCronField.setWeekdays(weekDay);
        String s = Application.buildCron(customCronField);
        Assert.assertEquals("?",s.split(" ")[5]);

        customCronField.setHour(20);
        customCronField.setMinute(30);
        weekDay = 0;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("?",s.split(" ")[5]);
    }
}
