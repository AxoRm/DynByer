package me.axorom.dynbyer.utils;

import java.util.concurrent.TimeUnit;

public class TimeTranslation {
    public static String getDurationBreakdown(long millis) {
        if(millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        StringBuilder sb = new StringBuilder(64);
        if (days != 0) {
            sb.append(days);
            sb.append("д. ");
        }
        if (hours!= 0) {
            sb.append(hours);
            sb.append("ч. ");
        }
        if (minutes != 0 && days == 0) {
            sb.append(minutes);
            sb.append("м. ");
        }
        if (days == 0 && hours == 0) {
            sb.append(seconds);
            sb.append("с. ");
        }
        return(sb.toString());
    }
}
