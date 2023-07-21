package me.baraban4ik.ecosalary.utils;

import me.baraban4ik.ecosalary.EcoSalary;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.TimeUnit;

public class Time {

    public static String getCooldownTime(long seconds) {
        String str = "";

        FileConfiguration config = EcoSalary.instance.getConfig();

        String daysCase1 = config.getString("Messages.time.days.1");
        String daysCase2 = config.getString("Messages.time.days.2");
        String daysCase3 = config.getString("Messages.time.days.3");

        String hoursCase1 = config.getString("Messages.time.hours.1");
        String hoursCase2 = config.getString("Messages.time.hours.2");
        String hoursCase3 = config.getString("Messages.time.hours.3");

        String minutesCase1 = config.getString("Messages.time.minutes.1");
        String minutesCase2 = config.getString("Messages.time.minutes.2");
        String minutesCase3 = config.getString("Messages.time.minutes.3");

        String secondsCase1 = config.getString("Messages.time.seconds.1");
        String secondsCase2 = config.getString("Messages.time.seconds.2");
        String secondsCase3 = config.getString("Messages.time.seconds.3");

        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (long)day * 24L;
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;

        if (day > 0) str = str + choosePluralMerge(day, daysCase1, daysCase2, daysCase3) + " ";
        if (hours > 0L) str = str + choosePluralMerge(hours, hoursCase1, hoursCase2, hoursCase3) + " ";
        if (minute > 0L) str = str + choosePluralMerge(minute, minutesCase1, minutesCase2, minutesCase3) + " ";
        if (second > 0L) str = str + choosePluralMerge(second, secondsCase1, secondsCase2, secondsCase3) + " ";

        return str.trim();
    }

    public static String choosePluralMerge(long number, String caseOne, String caseTwo, String caseFive) {
        String str = number + " ";
        number = Math.abs(number);

        if (number % 10L == 1L && number % 100L != 11L) str = str + caseOne;
        else if (number % 10L < 2L || number % 10L > 4L || number % 100L >= 10L && number % 100L < 20L) str = str + caseFive;
        else str = str + caseTwo;

        return str;
    }
}
