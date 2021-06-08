public class CustomCronField {

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(Integer weekdays) {
        this.weekdays = weekdays;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    private Integer minute = 0;
    private Integer hour = 0;
    private Integer month = 0;
    private Integer day = 0;
    private Integer weekdays = 0;
    private Integer year = 0;

}
