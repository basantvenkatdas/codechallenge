package com.foo.umbrella.utility;

import com.foo.umbrella.data.model.ForecastCondition;
import com.foo.umbrella.data.model.HourlyForecastDataForDay;

import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by gollaba on 9/18/17.
 */

public class Utility {

    public static String formTemperatureDisplayString(String temperature) {
        return temperature + UmbrellaConstants.DEGREE_TEXT;
    }

    public static boolean isTemperatureWarm(String temp) {
        if(temp == null || temp.trim().equals("")) {
            return false;
        }
        int tempInt = 0;
        try {
            tempInt = Integer.parseInt(temp);
        } catch (Exception e) {
        }
        if(tempInt > UmbrellaConstants.TEMPERATURE_CUTOFF_FOR_WARM_WEATHER) {
            return true;
        }
        return false;
    }

    public static int getTemperatureInteger(String temp) {
        int tempInt = 0;
        if(temp == null || temp.trim().equals("")) {
            return tempInt;
        }
        try {
            tempInt = Integer.parseInt(temp);
        } catch (Exception e) {
        }
        return tempInt;
    }

    public static List<HourlyForecastDataForDay> parseForecastData(List<ForecastCondition> forecastData) {
        int currentDay = 0;
        List<HourlyForecastDataForDay> list = new ArrayList<HourlyForecastDataForDay>();
        HourlyForecastDataForDay currData = null;
        int minTemp = Integer.MAX_VALUE;
        int maxTemp = Integer.MIN_VALUE;
        int startDay = 0;
        HourlyForecastDataForDay.TempByHour maxTempR = null;
        HourlyForecastDataForDay.TempByHour lowTempR = null;
        for (ForecastCondition el : forecastData) {
            if (currentDay != (el.getDateTime().getDayOfMonth())) {
                if (maxTempR != null) {
                    maxTempR.setIsMax(true);
                    maxTempR = null;
                    maxTemp = Integer.MIN_VALUE;
                }
                if (lowTempR != null) {
                    lowTempR.setIsMin(true);
                    lowTempR = null;
                    minTemp = Integer.MAX_VALUE;
                }
                currData =
                        new HourlyForecastDataForDay(
                                el.getDateTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                el.getDateTime().getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                String.valueOf(el.getDateTime().getDayOfMonth()));
                currentDay = el.getDateTime().getDayOfMonth();
                if (startDay == 0) {
                    currData.setIsToday(true);
                }
                if (startDay == 1) {
                    currData.setIsTomorrow(true);
                }
                startDay++;
                list.add(currData);
            }

            if (currData != null) {
                HourlyForecastDataForDay.TempByHour tempByHour = new HourlyForecastDataForDay.TempByHour(el.getTempCelsius(), el.getTempFahrenheit(), el.getDateTime().getHour(), el.getIcon());
                int temp = Utility.getTemperatureInteger(el.getTempCelsius());
                if (temp > maxTemp) {
                    maxTemp = temp;
                    maxTempR = tempByHour;
                }
                if (temp < minTemp) {
                    minTemp = temp;
                    lowTempR = tempByHour;
                }
                currData.addTempByHour(tempByHour);
            }
        }
        if (maxTempR != null) {
            maxTempR.setIsMax(true);
            maxTempR = null;
            maxTemp = Integer.MIN_VALUE;
        }
        if (lowTempR != null) {
            lowTempR.setIsMin(true);
            lowTempR = null;
            minTemp = Integer.MAX_VALUE;
        }
        return list;
    }

}
