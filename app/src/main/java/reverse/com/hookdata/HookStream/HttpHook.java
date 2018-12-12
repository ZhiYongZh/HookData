package reverse.com.hookdata.HookStream;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import reverse.com.hookdata.Utils.ByteUtils;
import reverse.com.hookdata.Utils.CommonUtils;
import reverse.com.hookdata.Utils.LogUtils;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by ZhouZhiYong on 2018/12/11.
 */
public class HttpHook {
    private String TARGET_APP = "";
    //private String[] TARGET_APPS = new String[]{"ws.coverme.im"};
    private SharedPreferences msp = null;
    private Application mApp = null;
    private String LOG_FILENAME = "_test_network";
    private boolean NETWORK = true;
    private boolean HTTP_DATA = true;//是否显示HTTP流参数和数据
    private boolean SOCKET_DATA = true;
    private boolean HTTP_RESPONSE = true;
    private static HttpHook thisObj = null;
    private static String TAG = "HOOKDATA";

    private boolean TCPLOCATION = true;//是否开启TCP数据的定位
    private boolean UDPLOCATION = true;//是否开启TCP数据的定位
    private boolean HTTPLOCATION = true;//是否开启HTTP数据的定位
    private boolean IPLOCATION = true;//是否开启IP的定位，用于无法从DNS找到IP后，寻找如何下放方式

    private HttpHook(String packageName) {
        this.TARGET_APP = packageName;
    }

    public static HttpHook getInstance(String packageName) {
        if (thisObj == null) {
            thisObj = new HttpHook(packageName);
        }
        return thisObj;

    }

    public void startStreamHook(ClassLoader cl) throws Throwable {

        if (TCPLOCATION) {

            //发送
            Hookoutput001(cl);
            //Hookoutput002(lpparam);

            //接收
            Hookinput(cl);
        }

        if (UDPLOCATION) {
            HookUdpData(cl);
        }


        if (HTTPLOCATION) {
            //hook HttpClient接口
            hookHttpClient(cl);

            //hook volley库 okhttp3
            hookOkhttp(cl);

            //上述是从整个报文发送接收处查找，下面对每个HEADER或者参数进行定位，以便解析每个字段含义的处理地方

        }

        //网络监控开始  IP寻找
        if (IPLOCATION) {
            findAndHookConstructor(InetSocketAddress.class, String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    mLog("网络地址", param.args[0] + ":" + param.args[1]);
                    super.beforeHookedMethod(param);
                }
            });
            //
            findAndHookMethod("java.net.DatagramSocket", cl, "createSocket", int.class, InetAddress.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    mLog("udp监听", ((InetAddress) param.args[1]).toString() + ":" + (Integer) param.args[0]);
                    super.beforeHookedMethod(param);
                }
            });
            findAndHookMethod("java.net.DatagramSocket", cl, "bind", SocketAddress.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    mLog("udp监听", ((SocketAddress) param.args[0]).toString());
                    super.beforeHookedMethod(param);
                }
            });
            findAndHookConstructor(DatagramSocket.class, SocketAddress.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    mLog("udp监听", ((SocketAddress) param.args[0]).toString());
                    super.beforeHookedMethod(param);
                }
            });

            //
            findAndHookMethod("android.webkit.WebView", cl, "loadUrl", String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    String d = (String) param.args[0];
                    mLog("webview", d);
                    super.beforeHookedMethod(param);
                }
            });
            findAndHookMethod("android.webkit.WebView", cl, "loadUrl", String.class, Map.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    String d = (String) param.args[0];
                    if (HTTP_DATA) {
                        Map d1 = (Map) param.args[1];
                        mLog("webview", d + ":" + d1.toString());
                    } else {
                        mLog("webview", d);
                    }

                    super.beforeHookedMethod(param);
                }
            });
            findAndHookMethod("android.webkit.WebView", cl, "postUrl", String.class, byte[].class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    String d = (String) param.args[0];
                    if (HTTP_DATA) {
                        String d1 = new String((byte[]) param.args[1]);
                        mLog("webview", d + ":" + d1);
                    } else {
                        mLog("webview", d);
                    }

                    super.beforeHookedMethod(param);
                }
            });
            findAndHookMethod("java.nio.channels.SocketChannel",cl, "open", SocketAddress.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    mLog("tcp连接", ((SocketAddress) param.args[0]).toString());
                    super.beforeHookedMethod(param);
                }
            });

            findAndHookMethod("java.net.Socket", cl, "startupSocket", InetAddress.class, int.class, InetAddress.class, int.class, boolean.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    mLog("tcp连接", ((InetAddress) param.args[0]).toString() + ":" + (Integer) param.args[1]);
                    super.beforeHookedMethod(param);
                }
            });
            findAndHookMethod("java.net.Socket", cl, "connect", SocketAddress.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    mLog("tcp连接", ((SocketAddress) param.args[0]).toString());
                    super.beforeHookedMethod(param);
                }
            });
            findAndHookConstructor(ServerSocket.class, int.class, int.class, InetAddress.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    mLog("tcp监听", ((InetAddress) param.args[0]).toString());
                    super.beforeHookedMethod(param);
                }
            });
            findAndHookMethod("java.net.ServerSocket", cl, "bind", SocketAddress.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    mLog("tcp监听", ((SocketAddress) param.args[0]).toString());
                    super.beforeHookedMethod(param);
                }
            });

            //
            findAndHookMethod("java.net.URL", cl, "openConnection", java.net.Proxy.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    URL url = (URL) param.thisObject;
                    mLog("urlconnp", url.toString() + ":" + ((Proxy) param.args[0]).toString());
                    super.beforeHookedMethod(param);
                }
            });
            if (HTTP_DATA) {
                findAndHookMethod("java.net.URLConnection", cl, "setRequestProperty", String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("urlconnheader", (String) param.args[0] + ":" + (String) param.args[1]);
                        super.beforeHookedMethod(param);
                    }

                });
                findAndHookMethod("java.net.URLConnection", cl, "addRequestProperty", String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        mLog("urlconnheader", (String) param.args[0] + ":" + (String) param.args[1]);
                        super.beforeHookedMethod(param);
                    }

                });
            }
            findAndHookMethod("java.net.URL", cl, "openConnection", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                        throws Throwable {
                    URL url = (URL) param.thisObject;
                    mLog("urlconn", url.toString());
                    super.beforeHookedMethod(param);
                }
            });
        }
    }

    //Hook http参数
    private void HookHTTPArgs(ClassLoader cl) {
        findAndHookMethod("java.net.URLConnection", cl, "setRequestProperty", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                mLog("urlconnheader", (String) param.args[0] + ":" + (String) param.args[1]);
                super.beforeHookedMethod(param);
            }

        });
        findAndHookMethod("java.net.URLConnection", cl, "addRequestProperty", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {
                mLog("urlconnheader", (String) param.args[0] + ":" + (String) param.args[1]);
                super.beforeHookedMethod(param);
            }

        });
    }

    //HookUdp数据
    private void HookUdpData(ClassLoader cl) {

        findAndHookMethod("java.net.DatagramSocket", cl, "send", DatagramPacket.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG);
                LogUtils.logString(TAG,"HookUdpData");
                LogUtils.logString(TAG, "UDP数据的发送，DatagramSocket.send");
                DatagramPacket udpData = (DatagramPacket) param.args[0];
                byte[] data = udpData.getData();
                LogUtils.logBinHexStr(TAG, data);
                //CommonUtils.printStack(TAG);
                LogUtils.endLog(TAG);

            }
        });
    }

    //一般网络传输通过获取OutputStream实例，通过write发送报文
    private void Hookoutput001(ClassLoader cl) {
        findAndHookMethod("java.io.OutputStream", cl, "write", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                byte[] data = (byte[]) param.args[0];
                LogUtils.startLog(TAG);
                LogUtils.logString(TAG,"Hookoutput001");
                LogUtils.logString(TAG, "OutputStream.write发送数据-Hookoutput001");
                LogUtils.logBinHexStr(TAG, data);
                LogUtils.logBinString(TAG, data);
                //CommonUtils.printStack(TAG);
                LogUtils.endLog(TAG);
            }
        });
    }

/*
    private void Hookoutput002(XC_LoadPackage.LoadPackageParam loadPackageParam){
        findAndHookMethod("java.io.OutputStream", lpparam.classLoader, "write", byte[].class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG);
                LogUtils.logString(TAG,"OutputStream.write发送数据-Hookoutput002");
                byte[] Data = (byte[]) param.args[0];
                LogUtils.logBinHexStr(TAG,data);
                LogUtils.logBinString(TAG,data);
                //CommonUtils.printStack(TAG);
                LogUtils.endLog(TAG);
            }

            }
        });
*/

    //一般网络接受数据通过获取InputStream实例，通过read获取response数据
    private void Hookinput(ClassLoader cl) {
        findAndHookMethod(InputStream.class, "read", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtils.startLog(TAG);
                LogUtils.logString(TAG,"Hookinput");
                byte[] revDatabuffer = (byte[]) param.args[0];
                int off = (int) param.args[1];
                int len = (int) param.args[2];
                byte[] revData = new byte[len];
                System.arraycopy(revDatabuffer, off, revData, 0, len);
                LogUtils.logBinHexStr(TAG, revData);
                LogUtils.logBinString(TAG, revData);
                //CommonUtils.printStack(TAG);;
                LogUtils.endLog(TAG);
            }
        });
    }


    public void mLog(String tag, String text) {
        Log.i(TARGET_APP, "gjh" + tag + ":" + text);
        System.out.println("gjh->" + tag + ":" + text);
        if (msp != null) {
            if (HTTP_DATA) {
                mSharePrefer(text, tag);
            } else {
                int i = text.indexOf("?");
                if (i > 0)
                    mSharePrefer(text.substring(0, i), tag);
                else
                    mSharePrefer(text, tag);
            }
        }
    }


    public void mSharePrefer(String key, String value) {
        System.out.println(key + "->" + value);
//        SharedPreferences.Editor editor = msp.edit();
//        editor.putString(key, value);
//        editor.commit();
    }

    //Hook okHttp3
    private void hookOkhttp(ClassLoader cl){
                findAndHookMethod("okhttp3.RealCall", cl, "execute", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.startLog(TAG);
                        LogUtils.logString(TAG,"hookOkhttp");
                        LogUtils.logString(TAG,"查看Request数据");
                        //查看Request数据
                        Object thisObj = param.thisObject;
                        Class<?> thisClass = thisObj.getClass();
                        Field originalRequest = thisClass.getDeclaredField("originalRequest");
                        originalRequest.setAccessible(true);
                        okhttp3.Request req = (okhttp3.Request)originalRequest.get(thisObj);
                        String reqUrl = req.url().toString();
                        LogUtils.logString(TAG,reqUrl);
                        CommonUtils.printStack(TAG);
                        LogUtils.endLog(TAG);
                    }


                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.startLog(TAG);
                        LogUtils.logString(TAG,"hookOkhttp");
                        //查看Response数据
                        LogUtils.logString(TAG,"查看Response数据");
                        okhttp3.Response resp = (okhttp3.Response)param.getResult();
                        byte[] data = resp.body().bytes();
                        LogUtils.logString(TAG,"查看Response数据String");
                        LogUtils.logBinString(TAG,data);
                        LogUtils.logString(TAG,"查看Response数据HexString");
                        LogUtils.logBinHexStr(TAG,data);
                        //CommonUtils.printStack(TAG);
                        LogUtils.startLog(TAG);
                    }
                });

            }

    //Hook HttpClient库的HTTP发送、接收
    public void hookHttpClient(ClassLoader cl) {

        //execute为在抽象类AbstractHttpClient中的final函数，且该函数其他重载最终转调用这里的函数，所以直接hook这里
        findAndHookMethod("org.apache.http.impl.client.AbstractHttpClient",cl,
                "execute", HttpHost.class, HttpRequest.class, HttpContext.class, new XC_MethodHook() {
                    //查看Request数据
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.startLog(TAG);
                        LogUtils.logString(TAG, "hookHttpClient");
                        //HttpHost host = (HttpHost) param.args[0];
                        HttpRequest request = (HttpRequest) param.args[1];
                        if (request instanceof HttpGet) {
                            LogUtils.logString(TAG, "=========GET请求=========");
                            HttpGet httpGet = (HttpGet) request;
                            LogUtils.logString(TAG, httpGet.getURI().toString());
                            if (HTTP_DATA) {
                                Header[] headers = request.getAllHeaders();
                                if (headers != null) {
                                    //LogUtils.logString(TAG, "输出HTTP HEADERS数据");
                                    for (int i = 0; i < headers.length; i++) {
                                        LogUtils.logString(TAG, headers[i].getName() + ": " + headers[i].getValue());
                                    }
                                    //Get请求无数据体，参数在头部和URL中
                                }
                            }
                        } else if (request instanceof HttpPost) {
                            LogUtils.logString(TAG, "=========POST请求=========");
                            HttpPost httpPost = (HttpPost) request;
                            LogUtils.logString(TAG, httpPost.getURI().toString());
                            if (HTTP_DATA) {// until get header
                                Header[] headers = request.getAllHeaders();
                                if (headers != null) {
                                    //LogUtils.logString(TAG, "输出HTTP HEADERS数据");
                                    for (int i = 0; i < headers.length; i++) {
                                        LogUtils.logString(TAG, headers[i].getName() + ": " + headers[i].getValue());
                                    }
                                }
                                //LogUtils.logString(TAG, "输出HTTP BODY数据");
                                HttpEntity entity = httpPost.getEntity();
                                String contentType = null;
                                if (entity.getContentType() != null) {
                                    contentType = entity.getContentType().getValue();
                                    if (URLEncodedUtils.CONTENT_TYPE.equals(contentType)) {
                                        try {
                                            //body类型：name11=file1&name2=value2
                                            byte[] data = new byte[(int) entity.getContentLength()];
                                            entity.getContent().read(data);
                                            String content = new String(data, HTTP.DEFAULT_CONTENT_CHARSET);
                                            LogUtils.logString(TAG, content);
                                        } catch (Exception e) {

                                        }
                                    } else if (contentType.startsWith(HTTP.DEFAULT_CONTENT_TYPE)) {
                                        try {
                                            //body类型：二进制
                                            byte[] data = new byte[(int) entity.getContentLength()];
                                            entity.getContent().read(data);
                                            String content = new String(data, contentType.substring(contentType.lastIndexOf("=") + 1));
                                            LogUtils.logString(TAG, content);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    byte[] data = new byte[(int) entity.getContentLength()];
                                    try {
                                        entity.getContent().read(data);
                                        String content = new String(data, HTTP.DEFAULT_CONTENT_CHARSET);
                                        LogUtils.logString(TAG, "BODY数据体的String");
                                        LogUtils.logString(TAG, content);
                                        LogUtils.logString(TAG, "BODY数据体的HEXString");
                                        LogUtils.logBinHexStr(TAG, data);
                                    } catch (Exception e) {
                                    }
                                }
                            }//get header
                        } else {
                            HttpEntityEnclosingRequestBase get = (HttpEntityEnclosingRequestBase) request;
                            HttpEntity entity = get.getEntity();
                            LogUtils.logString(TAG, get.getURI().toString());
                            if (HTTP_DATA) {
                                Header[] headers = request.getAllHeaders();
                                if (headers != null) {
                                    //LogUtils.logString(TAG, "输出HTTP HEADERS数据");
                                    for (int i = 0; i < headers.length; i++) {
                                        LogUtils.logString(TAG, headers[i].getName() + ":" + headers[i].getValue());
                                    }
                                }
                                if (entity != null) {
                                   // LogUtils.logString(TAG, "输出HTTP BODY数据");
                                    String content = EntityUtils.toString(entity);
                                    LogUtils.logString(TAG, content);
                                }
                            }
                        }

                        //CommonUtils.printStack(TAG);
                        LogUtils.endLog(TAG);
                    }

                    //查看Response数据
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        //
                        LogUtils.startLog(TAG);
                        LogUtils.logString(TAG, "hookHttpClient");
                        HttpResponse resp = (HttpResponse) param.getResult();
                        if (resp != null && HTTP_RESPONSE) {
                            LogUtils.logString(TAG, "HTTP_RESPONSE数据：");
                            Header[] headers = resp.getAllHeaders();
                            if (headers != null) {
                               // LogUtils.logString(TAG, "HTTP_RESPONSE  HEADERS数据：");
                                for (int i = 0; i < headers.length; i++) {
                                    LogUtils.logString(TAG, headers[i].getName() + ":" + headers[i].getValue());
                                }
                            }

                            HttpEntity entity = resp.getEntity();
                            if (entity != null) {
                                byte[] data = new byte[(int) entity.getContentLength()];
                                entity.getContent().read(data);
                                LogUtils.logString(TAG, "RESPONSE BODY 数据String");
                                LogUtils.logBinString(TAG, data);
                                LogUtils.logString(TAG, "RESPONSE BODY 数据HEXString");
                                LogUtils.logBinHexStr(TAG, data);
                            }
                        }
                        LogUtils.endLog(TAG);
                    }
                });
    }
}
