package reverse.com.hookdata.Utils;

/**
 * Created by ZhouZhiYong on 2018/12/11.
 */
public class CommonUtils {

    //打印当前堆栈
    public static  void printStack(String TAG){
        String stackInfo = "\n";
        stackInfo +="Function Stack:\n";
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                stackInfo +=stackElements[i].getClassName()+".";
                stackInfo +=stackElements[i].getMethodName()+"\n";
            }
        }
        stackInfo+="\n";
        LogUtils.logString(TAG,stackInfo);
    }

    //显示hex字符串
    public static void showHexStr(byte[] data,String TAG){
        String hexStr = ByteUtils.bytesToHexString(data);
        LogUtils.logString(TAG,hexStr);
        return;
    }
}
