package com.zlj.utils.common;

/**
 * @Description:主要功能:Singleton helper class for lazily initialization
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil
 * @author: AbrahamCaiJin
 * @date: 2017年05月24日 18:17
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */

public abstract class SingletonWrap<T> {

    private T instance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (instance == null) {
            synchronized (SingletonWrap.class) {
                if (instance == null) {
                    instance = newInstance();
                }
            }
        }
        return instance;
    }
}
