package io.github.yuu.hyperosgmskeeper;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.libxposed.api.XposedModule;

/**
 * Blocks HyperOS' screen-off-only GMS limiting transaction at its source.
 *
 * <p>On the verified ROM, this method first removes com.google.android.gms from the
 * Aurogon allowlist and then queues quick-freeze for every running GMS UID. The quick-freeze
 * transaction subsequently disables the UID and removes its alarms. Returning before the
 * transaction begins preserves all three without weakening Greeze policy for other UIDs.</p>
 */
public final class MainHook extends XposedModule {
    private static final String TAG = "HyperOSGmsKeeper";
    private static final AtomicBoolean INSTALLED = new AtomicBoolean(false);

    @Override
    public void onSystemServerStarting(SystemServerStartingParam param) {
        if (!INSTALLED.compareAndSet(false, true)) {
            return;
        }

        try {
            Class<?> serviceClass = Class.forName(
                    HookContract.GREEZE_MANAGER_CLASS,
                    false,
                    param.getClassLoader()
            );
            Method limitMethod = HookContract.findLimitMethod(serviceClass);

            hook(limitMethod).intercept(chain -> {
                log(Log.INFO, TAG,
                        "blocked GreezeManagerService.triggerGMSLimitAction()");
                return null;
            });

            log(Log.INFO, TAG,
                    "hook installed in system_server; scope is limited to system");
        } catch (Throwable error) {
            INSTALLED.set(false);
            log(Log.ERROR, TAG, "failed to install hook", error);
        }
    }
}
