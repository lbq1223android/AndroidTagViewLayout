package ntu.zss.tagviewlayout;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * DensityHelper
 */
public class DensityHelper
{
    public static float density = 1.0f;
    public static int screenWidth = 0;
    public static int screenHeight = 0;

    public static void init(Activity activity)
    {
        // set screen density
        DensityHelper.density = activity.getResources().getDisplayMetrics().density;

        // get screen width and height
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        DensityHelper.screenWidth = dm.widthPixels;
        DensityHelper.screenHeight = dm.heightPixels;
    }

    public static int dp2px(int dpValue)
    {
        return (int) (dpValue * density + 0.5f);
    }

    public static int px2dp(int pxValue)
    {
        return (int) (pxValue*1.0 / density + 0.5f);
    }

    public static int dp2px(float dpValue)
    {
        return (int) (dpValue * density + 0.5f);
    }

    public static int px2dp(float pxValue)
    {
        return (int) (pxValue*1.0 / density + 0.5f);
    }
}
