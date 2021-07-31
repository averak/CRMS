package dev.abelab.crms.util;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateTimeUtil {

    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.JAPAN);

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM月dd日(E)", Locale.JAPAN);

    /**
     * 翌日を取得
     *
     * return 翌日
     */
    public static Date getNextDate() {
        final var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 時間を文字列に変換
     *
     * @param date 時間
     *
     * @return 時間（文字列）
     */
    public static String convertTimeToString(final Date date) {
        return timeFormatter.format(date);
    }

    /**
     * 日時を文字列に変換
     *
     * @param date 日時
     *
     * @return 日時（文字列）
     */
    public static String convertDateToString(final Date date) {
        return dateFormatter.format(date);
    }

}
