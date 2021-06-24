package com.example.foodorderapp.helper;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatHelper {
    static final String FORMAT_PRICE = "###,###";
    static final String FORMAT_DATE = "hh:mm, dd MMM yyyy";

    static DecimalFormat df = new DecimalFormat(FORMAT_PRICE);
    public static String formatPrice(long price){
        String formatPrice = df.format(price) + "đ";
        return formatPrice;
    }

    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
        Calendar calendar = Calendar.getInstance();
        Date d = calendar.getTime();
        return sdf.format(d);
    }

    public static SpannableString strikeThroughPrice(String text){
        SpannableString ss = new SpannableString(text);
        StrikethroughSpan span = new StrikethroughSpan();
        ss.setSpan(span,0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }
}
