package com.zlj.utils.other;

import android.content.Context;
import android.content.res.Resources;

import com.zlj.kxapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by zlj on 2018/9/24 0024.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 */

public class ADFilterTool {
    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }

    static HashMap<String, String> adList = new HashMap<String, String>();

    //直接使用哈希表来保存相关信息
    public static String checkAd(String packag, boolean dex) {
        adList.put("com.vpon.adon.android.WebInApp", "Vpo");
        adList.put("com.google.ads", "AdMob");
        adList.put("com.google.android.gms.ads", "谷歌广告");
        adList.put("com.mobisage.android", "adsage");
        adList.put("com.adchina.android.ads.views", "AdChina");
        adList.put("com.mt.airad.MultiAD", "MultiAD");
        adList.put("com.greystripe.android.sdk", "Greystripe");
        adList.put("com.mdotm.android.ads", "MdotM");
        adList.put("com.millennialmedia.android", "MillennialMedia");
        adList.put("com.mt.airad.MultiAD", "AirAD");
        adList.put("com.inmobi.androidsdk", "InMobi");
        adList.put("cn.aduu.adsdk", "aduu");
        adList.put("com.donson.momark", "Momark");
        adList.put("com.doumob.main", "Doumob");
        adList.put("com.iadpush.adp.IA", "IadPush");
        adList.put("cn.appmedia.ad", "Appmedia");
        adList.put("com.zestadz.android", "ZestADZ");
        adList.put("com.smaato.SOMA", "Smaato");
        adList.put("com.l.adlib_android", "LSense");
        adList.put("com.energysource.szj", "AdTouch");
        adList.put("net.cavas.show", "芒果");
        adList.put("com.adsmogo.adview", "芒果");
        adList.put("com.lmmob.ad.sdk", "力美");
        adList.put("com.lmmob.sdk", "力美");
        adList.put("com.mobisage.android", "艾德思奇");
        adList.put("net.youmi.android", "有米");
        adList.put("net.youmi.toolkit.android", "有米推送");
        adList.put("cn.domob.android.ads", "多盟");
        adList.put("com.adwo.adsdk", "安沃");
        adList.put("com.winad.android.ads", "赢告");
        adList.put("com.winad.android.wall", "赢告");
        adList.put("com.winad.android.adwall.push", "赢告推送");
        adList.put("com.wiyun.common", "微云");
        adList.put("com.wiyun.offer", "微云");
        adList.put("com.wooboo.adlib_android", "哇棒");
        adList.put("com.tencent.mobwin", "聚赢");
        adList.put("com.baidu.mobads", "百度广告");
        adList.put("com.umengAd.android", "友盟");
        adList.put("com.fractalist.sdk.base.sys", "飞云");
        adList.put("net.miidi.ad.wall", "米迪");
        adList.put("net.miidi.ad.banner", "米迪 ");
        adList.put("com.suizong.mobplate.ads", "随踪");
        adList.put("com.telead.adlib_android", "天翼");
        adList.put("com.telead.adlib.adwall", "天翼");
        adList.put("com.l.adlib_android", "百分联通");
        adList.put("com.mobile.app.adlist", "第七传媒");
        adList.put("com.mobile.app.adpush", "第七传媒");
        adList.put("com.adzhidian.view", "指点传媒");
        adList.put("com.huawei.hiad.core", "华为聚点");
        adList.put("cn.aduu.adsdk", "优友");
        adList.put("com.izp", "亿赞普");
        adList.put("com.waps.OffersWebView", "万普世纪");
        adList.put("com.adsmogo.offers.adapters", "万普世纪");
        adList.put("com.bypush", "艾普 ");
        adList.put("com.dianle", "点乐 ");
        adList.put("com.yjfsdk.advertSdk", "易积分");
        adList.put("com.juzi.main", "桔子平台");
        adList.put("com.etonenet.pointwall", "移通");
        adList.put("com.kuguo.ad", "酷果");
        adList.put("com.kuguo.pushads", "酷果推送广告");
        adList.put("com.dianru.push", "酷果推送广告");
        adList.put("com.longmob.service", "掌龙广告平台");
        adList.put("com.dianru.sdk", "点入广告");
        adList.put("com.nd.dianjin.activity", "91点金");
        adList.put("com.nd.dianjin.service", "91点金");
        adList.put("com.snowfish.cn", "易接");
        adList.put("cn.ganga.offline.cn", "易接");
        adList.put("cn.casee", "架势");
        adList.put("com.wqmobile", "帷千");
        adList.put("com.ignitevision.android", "天幕");
        adList.put("com.mobisage", "艾德思奇");
        adList.put("com.iflytek.voiceads", "讯飞移动广告");
        adList.put("com.qq.e.ads", "腾讯广告");
        Collection<String> keyset = adList.keySet();
        List<String> list = new ArrayList<String>(keyset);
        Collections.sort(list);//排序，用在这好像多余了
        for (int i = 0; i < list.size(); i++) {
            if (!dex && packag.startsWith(list.get(i)) || packag.indexOf(list.get(i)) > -1) {
                //判断是否传入的包名是否从dex解析的
                return adList.get(list.get(i));
            }
            if (dex) {
                String test = list.get(i);
                if (packag.replace("/", ".").indexOf(test) != -1) {
                    return adList.get(list.get(i));


                }
            }
            //System.out.println(list.get(i)+" - "+adList.get(list.get(i)));
        }
        return "";
    }


}
