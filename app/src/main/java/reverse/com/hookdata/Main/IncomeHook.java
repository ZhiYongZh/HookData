package reverse.com.hookdata.Main;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import reverse.com.hookdata.HookEncrypt.HookEncrypt;
import reverse.com.hookdata.HookStream.HttpHook;
import reverse.com.hookdata.Utils.CommonUtils;
import reverse.com.hookdata.Utils.LogUtils;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by ZhouZhiYong on 2018/12/11.
 */
public class IncomeHook implements IXposedHookLoadPackage {
    public static final String PACKAGE_NAME = "com.wifi.key";
    HttpHook httpHook = HttpHook.getInstance(PACKAGE_NAME);

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!PACKAGE_NAME.equals(lpparam.packageName)) {
            System.out.println("******No Package hook******");
            return;
        }
        findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                final ClassLoader cl = ((Context) param.args[0]).getClassLoader();

                //hook数据流处理相关函数
                 httpHook.startStreamHook(cl);

                //hook加密相关函数
                //对于HTTP中的数据若是URL、BASE64编码 先解码再从输出日志中查找二进制数据
                HookEncrypt.startEncryptHook(cl);


                HookAPP(cl);
            }
        });

    }

    private void HookAPP(ClassLoader cl) {

    }

}