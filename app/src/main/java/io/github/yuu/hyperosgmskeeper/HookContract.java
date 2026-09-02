package io.github.yuu.hyperosgmskeeper;

import java.lang.reflect.Method;

/** The deliberately narrow contract between this module and HyperOS. */
public final class HookContract {
    static final String GREEZE_MANAGER_CLASS =
            "com.miui.server.greeze.GreezeManagerService";
    static final String GMS_LIMIT_METHOD = "triggerGMSLimitAction";

    private HookContract() {
    }

    static Method findLimitMethod(Class<?> serviceClass) throws NoSuchMethodException {
        return serviceClass.getDeclaredMethod(GMS_LIMIT_METHOD);
    }
}
