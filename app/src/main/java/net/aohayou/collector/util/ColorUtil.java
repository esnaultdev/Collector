package net.aohayou.collector.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

public class ColorUtil {
    @ColorInt
    public static int getColor(@NonNull Context context, @ColorRes int colorRes) {
        return getColor(context.getResources(), context.getTheme(), colorRes);
    }

    @ColorInt
    public static int getColor(@NonNull Resources resource, @NonNull Resources.Theme theme,
                               @ColorRes int colorRes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return resource.getColor(colorRes);
        } else {
            return resource.getColor(colorRes, theme);
        }
    }
}
