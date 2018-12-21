package reverse.com.hookdata.HookTool;

import reverse.com.hookdata.Utils.LogUtils;

/**
 * Created by ZhouZhiYong on 2018/12/20.
 */
public class DataOpt {
    private static  String TAG_doFinal = "HOOKDATA";
    public static void showByteArrData(byte[] data)throws Throwable{

        LogUtils.logString(TAG_doFinal,"数据String");
        LogUtils.logBinString(TAG_doFinal,data);
        LogUtils.logString(TAG_doFinal,"数据HexString");
        LogUtils.logBinHexStr(TAG_doFinal,data);

        CompressUtil.CType cpType = CompressUtil.compressType(data);
        if (cpType != CompressUtil.CType.UNKOWN){
            byte[] unpressData = CompressUtil.uncompress(data,cpType.name());
            LogUtils.logString(TAG_doFinal,"==========解压后的数据=========");
            LogUtils.logBinString(TAG_doFinal,unpressData);
            LogUtils.logBinHexStr(TAG_doFinal,unpressData);
        }
    }
}
