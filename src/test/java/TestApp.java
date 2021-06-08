import junit.framework.Assert;
import org.junit.jupiter.api.Test;

public class TestApp {

    @Test
    public void toCronTest(){
        CustomCronField customCronField = new CustomCronField();
        customCronField.setHours(15);
        customCronField.setMinutes(30);

        Integer weekDay = 1;
        customCronField.setWeekdays(weekDay);
        String s = Application.buildCron(customCronField);
        Assert.assertEquals(weekDay.toString(),s.split(" ")[5]);

        weekDay = 2;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals(weekDay.toString(),s.split(" ")[5]);

        weekDay = 4;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("3",s.split(" ")[5]);

        weekDay = 8;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("4",s.split(" ")[5]);

        weekDay = 16;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("5",s.split(" ")[5]);

        weekDay = 32;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("6",s.split(" ")[5]);

        weekDay = 64;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("7",s.split(" ")[5]);
////////////////////////////////////////////////////////////////////////////////
        weekDay = 63;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("1,2,3,4,5,6",s.split(" ")[5]);

        weekDay = 59;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("1,2,4,5,6",s.split(" ")[5]);

        weekDay = 95;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("1,2,3,4,5,7",s.split(" ")[5]);

        weekDay = 0;
        customCronField.setWeekdays(weekDay);
        s = Application.buildCron(customCronField);
        Assert.assertEquals("?",s.split(" ")[5]);
    }

    @Test
    public void toCustomer(){
        CustomCronField customCronField = Application.parseCron("0 30 15 0 0 1,2,3,4,5,7 *");
        Assert.assertEquals(95,customCronField.getWeekdays().intValue());

        customCronField = Application.parseCron("0 30 15 0 0 1,2,4,5,6 *");
        Assert.assertEquals(59,customCronField.getWeekdays().intValue());

        customCronField = Application.parseCron("0 30 15 0 0 ? *");
        Assert.assertEquals(0,customCronField.getWeekdays().intValue());
    }
}
