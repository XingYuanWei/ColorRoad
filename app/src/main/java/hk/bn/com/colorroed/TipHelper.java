package hk.bn.com.colorroed;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

class TipHelper {
    public static void Vibrate(final Activity activity,long milliseconds)
    {
        /**
         * final Activity activity ：调用该方法的Activity实例
         * long pattern ：震动的时长，单位是毫秒
         */
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat)
    {
        /**
         *
         * activity 调用该方法的Activity实例
         * pattern long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
         * isRepeat 是否反复震动，如果是true，反复震动，如果是false，只震动一次
         */
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    //调用例子
    // TipHelper.Vibrate(MainActivity.this,500);
    // TipHelper.Vibrate(MainActivity.this, new long[]{800, 1000, 800, 1000, 800, 1000}, true);
}
