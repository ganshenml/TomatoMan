package com.example.ganshenml.tomatoman.net;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.StringTool;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Wifi操作类
 */
public class WifiOperate {


    private WifiManager mWifiManager;
    private ConnectivityManager mConnectivityManager;

    private Context context;

    //网络类型
    public enum NetType {

        INVALID(0),
        Cellular(1),
        WIFI(2);

        public int type;

        NetType(int type) {
            this.type = type;
        }

    }


    public WifiOperate(Context context) {
        this.context = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    /**
     * 设置蜂窝网络开关
     *
     * @param enabled
     */
    public void setMobileDataStatus(boolean enabled) {

        // ConnectivityManager类
        Class<?> conMgrClass;
        // ConnectivityManager类中的字段
        Field iConMgrField;
        // IConnectivityManager类的引用
        Object iConMgr;
        // IConnectivityManager类
        Class<?> iConMgrClass;
        // setMobileDataEnabled方法
        Method setMobileDataEnabledMethod;
        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(mConnectivityManager.getClass().getName());
            // 取得ConnectivityManager类中的对象Mservice
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(mConnectivityManager);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            // 设置setMobileDataEnabled方法是否可访问
            setMobileDataEnabledMethod.setAccessible(true);
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置蜂窝网络的优先级别
     */
    public void setNetworkWithMobile() {
        mConnectivityManager.setNetworkPreference(ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * 设置WiFi优先
     */
    public void setNetworkWithWiFi() {

        mConnectivityManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
    }


    /**
     * 获取网络状态
     *
     * @return 网络类型
     */
    public int getNetWorkType() {

        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return NetType.WIFI.type;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return NetType.Cellular.type;
            }
        }
        return NetType.INVALID.type;
    }


    /**
     * 判断当前是否有网络
     *
     * @return
     */
    public boolean isNetworkConnected() {
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
        }
        return false;
    }

    /**
     * 当前是否是Wifi连接
     *
     * @return
     */
    public boolean isWifiConnected() {
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable() && mWiFiNetworkInfo.isConnected();
        }

        return false;
    }


    /**
     * 获取 WifiManager
     *
     * @return
     */
    public WifiManager getWifiManager() {
        return mWifiManager;
    }


    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);

        }
    }

    /**
     * 断开指定的网络ID
     *
     * @param netId
     */
    public void disconnectWifi(int netId) {

        if (mWifiManager != null) {
            mWifiManager.disableNetwork(netId);
            mWifiManager.disconnect();
        }

    }

    /**
     * 获取连接网络Id
     *
     * @return
     */
    public int getNetworkId() {

        if (mWifiManager != null) {
            return mWifiManager.getConnectionInfo().getNetworkId();
        }
        return -1;
    }


    //是否关闭
    public boolean isClose() {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        int status = mWifiManager.getWifiState();
        if (status == WifiManager.WIFI_STATE_DISABLED) {
            return true;
        }
        return false;
    }

    // 是否打开
    public boolean isOpen() {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int status = mWifiManager.getWifiState();
        if (status == WifiManager.WIFI_STATE_ENABLED) {
            return true;
        }
        return false;
    }


    public void startScan() {
        mWifiManager.startScan();
    }


    /**
     * get Cur Wifi SSID
     *
     * @return
     */
    public String returnWifiSSid() {

        return mWifiManager.getConnectionInfo().getSSID().replace("\"", "");
    }


    public String returnWifiBSsId() {

        String bSSid = mWifiManager.getConnectionInfo().getBSSID();
        if (StringTool.isEmpty(bSSid)) {
            return "";
        }

        return bSSid.replace("\"", "");
    }


    public int returnCurNetworkId() {

        return mWifiManager.getConnectionInfo().getNetworkId();

    }

    /**
     * 断开当前网络
     */
    public void disconnectCurWifi() {

        if (mWifiManager != null) {

            mWifiManager.disableNetwork(mWifiManager.getConnectionInfo().getNetworkId());
            mWifiManager.disconnect();

        }

    }

//
//    public void autConnected(ScanResult result, ActionListener listener) {
//        this.mListener = listener;
//        LogTool.log(LogTool.Aaron, "autConnected 开始进入");
//        if (!WiFi.connectToNewNetwork(mWifiManager, result, "", mConnectivityManager, new WiFi.CheckNetWorkCallback() {
//            @Override
//            public void onCheckSuccess() {
//                LogTool.log(LogTool.Aaron, "autConnected 连接到新的网络  成功进行回调");//Aaron_log_5.14
//                mListener.onFinished(true);
//            }
//        })) {
//            LogTool.log(LogTool.Aaron, "autConnected 连接到新的网络  失败，进行回调");//Aaron_log_5.14
//
//            mListener.onFinished(false);
//        }
//    }


    /**
     * 当前网络的网关地址
     *
     * @return
     */
    public String getCurNetworkRouterAddress() {

        DhcpInfo di = mWifiManager.getDhcpInfo();
        long ip = di.gateway;

        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf((int) (ip & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((ip >> 8) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((ip >> 16) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((ip >> 24) & 0xff)));

        return sb.toString();

    }


    /**
     * 获取手机当前连接网络的IP
     *
     * @return
     */
    public String getNetworkIp() {

        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int hostAddress = (wifiInfo == null) ? 0 : wifiInfo.getIpAddress();
        byte[] addressByte = {
                (byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))
        };

        try {
            InetAddress inetAddress = InetAddress.getByAddress(addressByte);
            return inetAddress.getHostAddress().toString();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean is5GWiFi() {

        int currentVersion = Build.VERSION.SDK_INT;

        if (currentVersion >= 21) {

            int frequency = mWifiManager.getConnectionInfo().getFrequency();

            return frequency > 5000;
        }

        return false;

    }


    private ActionListener mListener;


    public static abstract class ActionListener {

        public void onFinished(boolean success) {
        }
    }


}

