package net.gility.acrida.utils;

import android.content.Context;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.R;

/**
 * 白天和夜间模式切换
 * Created by 火蚁 on 15/5/26.
 */
public class ThemeSwitchUtils {

    public static void switchTheme(Context context) {

    }

    public static int getTitleReadedColor() {
        if (ApplicationLoader.getNightModeSwitch()) {
            return R.color.night_infoTextColor;
        } else {
            return R.color.day_infoTextColor;
        }
    }

    public static int getTitleUnReadedColor() {
        if (ApplicationLoader.getNightModeSwitch()) {
            return R.color.night_textColor;
        } else {
            return R.color.day_textColor;
        }
    }

    public static String getWebViewBodyString() {
        if (ApplicationLoader.getNightModeSwitch()) {
            return "<body class='night'><div class='contentstyle' id='article_body'>";
        } else {
            return "<body ><div class='contentstyle' id='article_body'>";
        }
    }
}
