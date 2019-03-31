package com.example.maxpayne.mytodoapp.recycler_view;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.example.maxpayne.mytodoapp.App;
import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class DateUtils {
    public static String convertDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(timestamp);
    }

    private static SpannableString colorDate(String dateString, int color) {
        SpannableString sps = new SpannableString(dateString);
        sps.setSpan(new ForegroundColorSpan(color),
                0, dateString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sps;
    }

    private static long getDaysToDeadline(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long diff = timestamp - cal.getTimeInMillis();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static SpannableString makeDeadlineStringRV(long timestamp) {
        long daysToDeadline = getDaysToDeadline(timestamp);
        if (timestamp == DbContract.ToDoEntry.TIMELESS_CODE) {
            return colorDate(App.getContext().getString(R.string.item_timeless),
                    Color.BLACK);
        } else if (daysToDeadline < -1) {
            return colorDate(convertDate(timestamp), Color.RED);
        } else if (daysToDeadline == -1) {
            return colorDate(App.getContext().getString(R.string.item_yesterday),
                    Color.RED);
        } else if (daysToDeadline == 0) {
            return colorDate(App.getContext().getString(R.string.item_today),
                    Color.parseColor("#E91E63"));
        } else if (daysToDeadline == 1) {
            return colorDate(App.getContext().getString(R.string.item_tomorrow),
                    Color.parseColor("#F57C00"));
        } else if (daysToDeadline <= 7) {
            return colorDate(convertDate(timestamp), Color.parseColor("#388E3C"));
        } else {
            return colorDate(convertDate(timestamp), Color.BLACK);
        }
    }

    public static SpannableString makeEndDateStringRV(long timestamp) {
        return colorDate(convertDate(timestamp), Color.BLACK);
    }
}
