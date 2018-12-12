package reverse.com.hookdata.HookEncrypt;

import java.nio.ByteBuffer;

import javax.crypto.Cipher;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import reverse.com.hookdata.Utils.CommonUtils;
import reverse.com.hookdata.Utils.LogUtils;


public class HookEncrypt  {

    private static  String TAG_doFinal = "HOOKDATA";
    public static void startEncryptHook(XC_LoadPackage.LoadPackageParam lpparam) {
        hookdoFinal001(lpparam);
        hookdoFinal002(lpparam);
        hookdoFinal003(lpparam);
        hookdoFinal004(lpparam);
        hookdoFinal005(lpparam);
        hookdoFinal006(lpparam);
        hookdoFinal007(lpparam);
    }

    // byte[] doFinal()
    private static void hookdoFinal001(XC_LoadPackage.LoadPackageParam lpparam){
        XposedHelpers.findAndHookMethod(Cipher.class, "doFinal", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG_doFinal);
                LogUtils.logString(TAG_doFinal,"hookdoFinal001");
                byte[] output = (byte[])param.getResult();
                LogUtils.logString(TAG_doFinal,"==========输出数据=========");
                LogUtils.logString(TAG_doFinal,"输出数据String");
                LogUtils.logBinString(TAG_doFinal,output);
                LogUtils.logBinHexStr(TAG_doFinal,output);

               // CommonUtils.printStack(TAG_doFinal);

                LogUtils.endLog(TAG_doFinal);
            }
        });
    }

    //int doFinal(byte[] output, int outputOffset)
    private static void hookdoFinal002(XC_LoadPackage.LoadPackageParam lpparam){

        XposedHelpers.findAndHookMethod(Cipher.class, "doFinal", byte[].class,int.class,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG_doFinal);
                LogUtils.logString(TAG_doFinal,"hookdoFinal002");
                byte[] output = (byte[])param.args[0];
                LogUtils.logString(TAG_doFinal,"==========输出数据=========");
                LogUtils.logString(TAG_doFinal,"输出数据String");
                LogUtils.logBinString(TAG_doFinal,output);
                LogUtils.logString(TAG_doFinal,"输出数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,output);
                // CommonUtils.printStack(TAG_doFinal);
                LogUtils.endLog(TAG_doFinal);
            }
        });

    }

    // byte[] doFinal(byte[] input)
    private static void hookdoFinal003(XC_LoadPackage.LoadPackageParam lpparam){

        XposedHelpers.findAndHookMethod(Cipher.class, "doFinal", byte[].class,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG_doFinal);
                LogUtils.logString(TAG_doFinal,"hookdoFinal003");
                byte[] input = (byte[])param.args[0];
                byte[] output =(byte[])param.getResult() ;

                LogUtils.logString(TAG_doFinal,"==========输入数据=========");
                LogUtils.logString(TAG_doFinal,"输入数据String");
                LogUtils.logBinString(TAG_doFinal,input);
                LogUtils.logString(TAG_doFinal,"输入数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,input);

                LogUtils.logString(TAG_doFinal,"==========输出数据=========");
                LogUtils.logString(TAG_doFinal,"输出数据String");
                LogUtils.logBinString(TAG_doFinal,output);
                LogUtils.logString(TAG_doFinal,"输出数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,output);
                // CommonUtils.printStack(TAG_doFinal);

                LogUtils.endLog(TAG_doFinal);
            }
        });
    }

    //byte[] doFinal(byte[] input, int inputOffset, int inputLen)
    private static void hookdoFinal004(XC_LoadPackage.LoadPackageParam lpparam){

        XposedHelpers.findAndHookMethod(Cipher.class, "doFinal", byte[].class,int.class,int.class,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG_doFinal);
                LogUtils.logString(TAG_doFinal,"hookdoFinal004");
                byte[] input = (byte[])param.args[0];
                byte[] output =(byte[])param.getResult() ;

                LogUtils.logString(TAG_doFinal,"==========输入数据=========");
                LogUtils.logString(TAG_doFinal,"输入数据String");
                LogUtils.logBinString(TAG_doFinal,input);
                LogUtils.logString(TAG_doFinal,"输入数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,input);

                LogUtils.logString(TAG_doFinal,"==========输出数据=========");
                LogUtils.logString(TAG_doFinal,"输出数据String");
                LogUtils.logBinString(TAG_doFinal,output);
                LogUtils.logString(TAG_doFinal,"输出数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,output);
                // CommonUtils.printStack(TAG_doFinal);

                LogUtils.endLog(TAG_doFinal);
            }
        });

    }

    //int doFinal(byte[] input, int inputOffset, int inputLen,byte[] output)
    private static void hookdoFinal005(XC_LoadPackage.LoadPackageParam lpparam){

        XposedHelpers.findAndHookMethod(Cipher.class, "doFinal", byte[].class,int.class,int.class,byte[].class,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG_doFinal);
                LogUtils.logString(TAG_doFinal,"hookdoFinal005");
                byte[] input = (byte[])param.args[0];
                byte[] output =(byte[])param.args[3] ;

                LogUtils.logString(TAG_doFinal,"==========输入数据=========");
                LogUtils.logString(TAG_doFinal,"输入数据String");
                LogUtils.logBinString(TAG_doFinal,input);
                LogUtils.logString(TAG_doFinal,"输入数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,input);

                LogUtils.logString(TAG_doFinal,"==========输出数据=========");
                LogUtils.logString(TAG_doFinal,"输出数据String");
                LogUtils.logBinString(TAG_doFinal,output);
                LogUtils.logString(TAG_doFinal,"输出数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,output);
                // CommonUtils.printStack(TAG_doFinal);

                LogUtils.endLog(TAG_doFinal);
            }
        });
    }

    //doFinal(byte[] input, int inputOffset, int inputLen,byte[] output, int outputOffset)
    private static void hookdoFinal006(XC_LoadPackage.LoadPackageParam lpparam){
        XposedHelpers.findAndHookMethod(Cipher.class, "doFinal", byte[].class,int.class,int.class,byte[].class,int.class,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG_doFinal);
                LogUtils.logString(TAG_doFinal,"hookdoFinal006");
                byte[] input = (byte[])param.args[0];
                byte[] output =(byte[])param.args[3] ;

                LogUtils.logString(TAG_doFinal,"==========输入数据=========");
                LogUtils.logString(TAG_doFinal,"输入数据String");
                LogUtils.logBinString(TAG_doFinal,input);
                LogUtils.logString(TAG_doFinal,"输入数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,input);

                LogUtils.logString(TAG_doFinal,"==========输出数据=========");
                LogUtils.logString(TAG_doFinal,"输出数据String");
                LogUtils.logBinString(TAG_doFinal,output);
                LogUtils.logString(TAG_doFinal,"输出数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,output);
                // CommonUtils.printStack(TAG_doFinal);

                LogUtils.endLog(TAG_doFinal);
            }
        });
    }

    // doFinal(ByteBuffer input, ByteBuffer output)
    private static void hookdoFinal007(XC_LoadPackage.LoadPackageParam lpparam){
        XposedHelpers.findAndHookMethod(Cipher.class, "doFinal", ByteBuffer.class,ByteBuffer.class,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG_doFinal);
                LogUtils.logString(TAG_doFinal,"hookdoFinal007");
                byte[] input = ((ByteBuffer)param.args[0]).array();
                byte[] output =((ByteBuffer)param.args[1]).array() ;

                LogUtils.logString(TAG_doFinal,"==========输入数据=========");
                LogUtils.logString(TAG_doFinal,"输入数据String");
                LogUtils.logBinString(TAG_doFinal,input);
                LogUtils.logString(TAG_doFinal,"输入数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,input);

                LogUtils.logString(TAG_doFinal,"==========输出数据=========");
                LogUtils.logString(TAG_doFinal,"输出数据String");
                LogUtils.logBinString(TAG_doFinal,output);
                LogUtils.logString(TAG_doFinal,"输出数据HexString");
                LogUtils.logBinHexStr(TAG_doFinal,output);

                // CommonUtils.printStack(TAG_doFinal);
                LogUtils.endLog(TAG_doFinal);
            }
        });
    }

}
