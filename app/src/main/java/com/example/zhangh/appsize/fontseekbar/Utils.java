package com.example.zhangh.appsize.fontseekbar;

import android.content.Context;

public class Utils {
    public Utils() {
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取字体缩放比例
     * @param currentIndex
     * @return
     */
    public static float getFontScale(int currentIndex){
        float textSizef=1;//标准
        switch (currentIndex){
            case 0:
                textSizef=1 + (currentIndex-1) * 0.2f;
                break;
            case 1:
                textSizef=1 + (currentIndex-1) * 0.2f;
                break;
            case 2:
                textSizef=1 + (currentIndex-1) * 0.25f;
                break;
            case 3:
                textSizef=1 + (currentIndex-1) * 0.25f;
                break;

        }

        return textSizef;
    }
}
