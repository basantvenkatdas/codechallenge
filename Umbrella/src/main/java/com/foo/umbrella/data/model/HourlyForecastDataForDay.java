package com.foo.umbrella.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gollaba on 9/16/17.
 */
public class HourlyForecastDataForDay {

    private  String displayDay;

    public static class TempByHour {
        private String temperatureCentrigrade;
        private String temperatureFarenheit;
        private String hour;
        private String condition;
        private boolean isMax;
        private boolean isMin;

        public TempByHour(String temperatureC, String temperatureF, int hourNum, String condition) {
            this.temperatureCentrigrade = temperatureC;
            this.temperatureFarenheit = temperatureF;
            if(hourNum == 12) {
                this.hour = "12:00 PM";
            }else {
                this.hour = hourNum % 12 + ":00 " +(hourNum / 12 > 0 ? "PM" : "AM");
            }
           this.condition = condition;
        }

        public String getTemperatureCentrigrade() {
            return temperatureCentrigrade;
        }

        public String getTemperatureFarenheit() {
            return temperatureFarenheit;
        }

        public String getHour() {
            return hour;
        }
        public String getCondition() {
            return condition;
        }


        public void setIsMax(boolean b) {
            this.isMax = b;
        }

        public boolean isMax() {
            return isMax;
        }

        public void setIsMin(boolean b) {
            this.isMin = b;
        }

        public boolean isMin() {
            return isMin;
        }
    }

    private String weekDay = null;
    private String month = null;
    private String dayOfTheMonth = null;
    private boolean  isToday = false;
    private boolean  isTomorrow = false;
    private List<TempByHour> tempByHourList = new ArrayList<TempByHour>();


    public HourlyForecastDataForDay(String weekDay, String month, String dayOfTheMonth) {
        this.weekDay = weekDay;
        this.month = month;
        this.dayOfTheMonth = dayOfTheMonth;
    }

    public List<TempByHour> getTempByHourList() {
        return tempByHourList;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public String getMonth() {
        return month;
    }

    public String getDayOfTheMonth() {
        return dayOfTheMonth;
    }

    public void setDisplayDay(String s) {
        this.displayDay = s;
    }

    public String getDisplayDay() {
        return displayDay;
    }

    public void addTempByHour(TempByHour tempByHour) {
        tempByHourList.add(tempByHour);
    }

    public void setIsToday(boolean b) {
        this.isToday = b;
    }

    public void setIsTomorrow(boolean b) {
        this.isTomorrow = b;
    }

    public boolean isToday() {
        return isToday;
    }

    public boolean isTomorrow() {
        return isTomorrow;
    }
}
