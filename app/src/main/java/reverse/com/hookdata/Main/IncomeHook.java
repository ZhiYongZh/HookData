package reverse.com.hookdata.Main;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import reverse.com.hookdata.HookEncrypt.HookEncrypt;
import reverse.com.hookdata.HookStream.HttpHook;

/**
 * Created by ZhouZhiYong on 2018/12/11.
 */
public class IncomeHook implements IXposedHookLoadPackage {
    public static final String PACKAGE_NAME = "com.tuan800.tao800";
    HttpHook httpHook = HttpHook.getInstance(PACKAGE_NAME);
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if(!PACKAGE_NAME.equals(lpparam.packageName))
        {
            System.out.println("******No Package hook******");
            return;
        }

        //hook数据流处理相关函数
        httpHook.startStreamHook(lpparam);

        //hook加密相关函数
        HookEncrypt.startEncryptHook(lpparam);




    }
}
