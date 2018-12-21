package reverse.com.hookdata.Utils;

import android.util.Log;

/**
 * Created by ZhouZhiYong on 2018/12/11.
 */
public class LogUtils {
    private static  String TAG_doFinal = "HOOKDATA";
    //打印字符串日志
    public static void logString(String TAG,String content){
        Log.d(TAG,content);
    }

    //打印二进制字符串日志
    public static void logBinString(String TAG,byte[] content){
        logString(TAG,new String(content));
    }

    //打印二进制的16进制字符串日志
    public static void logBinHexStr(String TAG,byte[] content){
        CommonUtils.showHexStr(content,TAG);
    }

    //函数开头分割标志
    public static void startLog(String TAG ){
        Log.d(TAG,"\n");
        Log.d(TAG,"##############################################start##############################################");
    }

    //函数节为分割标志
    public static void endLog(String TAG){
        Log.d(TAG,"##############################################end##############################################");
        Log.d(TAG,"\n");
    }

}
