package fastsharedmemory;

import fastpointer.Pointer;
import sun.misc.Unsafe;
import java.lang.reflect.Field;

/**
 * SharedMemory — Win32 Named Shared Memory Mapped File IPC Engine for Java.
 */
public final class SharedMemory implements AutoCloseable {

    private static final Unsafe UNSAFE;

    static {
        Unsafe unsafe = null;
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {
            try {
                Field f = Unsafe.class.getDeclaredField("Unsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);
            } catch (Exception ignored) {}
        }
        UNSAFE = unsafe;
    }

    private final String name;
    private final long size;
    private final long handle;
    private final long address;
    private boolean closed;

    private SharedMemory(String name, long size, long handle, long address) {
        this.name = name;
        this.size = size;
        this.handle = handle;
        this.address = address;
        this.closed = false;
    }

    public static SharedMemory create(String name, long size) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be null or empty");
        if (size <= 0) throw new IllegalArgumentException("Size must be > 0");

        long handle = 0;
        long address = 0;

        if (FastSharedMemoryNative.isNativeLoaded()) {
            handle = FastSharedMemoryNative.createMapping(name, size);
            if (handle != 0) {
                address = FastSharedMemoryNative.mapView(handle, size);
            }
        }

        // Fallback simulate using Unsafe if native DLL is not active
        if (address == 0) {
            address = UNSAFE.allocateMemory(size);
            UNSAFE.setMemory(address, size, (byte) 0);
            handle = address;
        }

        return new SharedMemory(name, size, handle, address);
    }

    public static SharedMemory open(String name, long size) {
        return create(name, size);
    }

    public Pointer pointer() {
        checkClosed();
        return Pointer.of(address);
    }

    public long address() {
        checkClosed();
        return address;
    }

    public long size() {
        return size;
    }

    public String name() {
        return name;
    }

    public synchronized void free() {
        if (closed) return;
        if (FastSharedMemoryNative.isNativeLoaded() && handle != address) {
            FastSharedMemoryNative.unmapView(address);
            FastSharedMemoryNative.closeMapping(handle);
        } else if (address != 0) {
            UNSAFE.freeMemory(address);
        }
        closed = true;
    }

    @Override
    public void close() {
        free();
    }

    private void checkClosed() {
        if (closed) throw new IllegalStateException("SharedMemory segment has been closed.");
    }
}
