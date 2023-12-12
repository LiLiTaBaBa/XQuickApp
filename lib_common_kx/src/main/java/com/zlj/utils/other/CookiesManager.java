package com.zlj.utils.other;

import android.net.Uri;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.List;

/**
 * Created by zlj on 2019/10/22.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 */
public class CookiesManager {
    private static CookiesManager cookiesManager;

    private CookiesManager(){
    }
    public static CookiesManager getInstance(){
        if(cookiesManager==null){
            synchronized (CookiesManager.class){
                if(cookiesManager==null){
                    cookiesManager=new CookiesManager();
                }
            }
        }
        return cookiesManager;
    }


    /**
     * 同步cookie
     * @param url 要加载的地址链接
     */
    public void syncCookie(String url,List<String> cookies) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //CookieSyncManager.createInstance(BaseApplication.getContext());
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookie();
        } else {
            cookieManager.removeSessionCookies(null);
        }
        String host = Uri.parse(url).getHost();
        LogUtils.e("syncCookies:--host--- " + host);
        // Set<String> cookies = CookiesUtils.getInstance().getCookie(baseUrl, host);
        for (String cookie : cookies) {
            LogUtils.e("syncCookies: " + cookie);
            cookieManager.setCookie(host, cookie);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            cookieManager.flush();
        }
        LogUtils.e("syncCookies: "+  CookieManager.getInstance().getCookie(url));

    }

    /**
     * 请求Cookies
     */
    public void clearCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookie();
        } else {
            cookieManager.removeSessionCookies(null);
        }
        cookieManager.removeAllCookie();
    }
}
