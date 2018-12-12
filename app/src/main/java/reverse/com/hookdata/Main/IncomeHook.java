package reverse.com.hookdata.Main;

import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import reverse.com.hookdata.HookEncrypt.HookEncrypt;
import reverse.com.hookdata.HookStream.HttpHook;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

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
        findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                final ClassLoader cl = ((Context)param.args[0]).getClassLoader();

                //hook数据流处理相关函数
                httpHook.startStreamHook(cl);

                //hook加密相关函数
                HookEncrypt.startEncryptHook(cl);

            }
        });



    }
}
