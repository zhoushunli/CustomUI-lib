package com.zhousl.customui.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by inshot-user on 2017/7/17.
 */

public class UIUtil {
    public static int dp2px(int dpValue, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }
}
