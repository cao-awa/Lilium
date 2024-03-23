package com.github.cao.awa.lilium.threading;

import com.github.cao.awa.lilium.annotation.threading.ForceMainThread;

public class ThreadingUtil {
    public static boolean forceMainThread(Class<?> clazz) {
        return clazz.isAnnotationPresent(ForceMainThread.class);
    }
}
