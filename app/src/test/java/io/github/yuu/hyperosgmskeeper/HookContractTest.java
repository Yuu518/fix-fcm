package io.github.yuu.hyperosgmskeeper;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import org.junit.Test;

public final class HookContractTest {
    @Test
    public void resolvesOnlyNoArgGmsLimitMethod() throws Exception {
        Method method = HookContract.findLimitMethod(FakeGreezeManager.class);

        assertEquals("triggerGMSLimitAction", method.getName());
        assertEquals(0, method.getParameterCount());
        assertEquals(void.class, method.getReturnType());
    }

    private static final class FakeGreezeManager {
        @SuppressWarnings("unused")
        private void triggerGMSLimitAction() {
        }

        @SuppressWarnings("unused")
        private void triggerGMSLimitAction(int uid) {
        }
    }
}
