package fastsharedmemory;

import fastcore.FastCore;

/**
 * FastSharedMemoryNative — JNI Native Loader using FastCore.
 */
public final class FastSharedMemoryNative {

    private static boolean loaded = false;

    static {
        try {
            FastCore.loadLibrary("FastSharedMemory", FastSharedMemoryNative.class);
            loaded = true;
        } catch (Throwable t) {
            loaded = false;
        }
    }

    public static boolean isNativeLoaded() {
        return loaded;
    }

    public static native long createMapping(String name, long size);
    public static native long mapView(long handle, long size);
    public static native void unmapView(long address);
    public static native void closeMapping(long handle);
}
