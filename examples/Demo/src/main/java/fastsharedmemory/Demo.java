package fastsharedmemory;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class Demo {

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

    public static void main(String[] args) throws Exception {
        System.out.println("==========================================================================");
        System.out.println("⚡ FastSharedMemory IPC Ring Buffer — High-Speed Demo");
        System.out.println("==========================================================================");
        System.out.println();

        String segmentName = "FastSharedMemory_IPC_Buffer_Demo";
        long capacityBytes = 4096; // 4KB Shared Off-Heap Segment

        System.out.println(">>> Creating Shared Memory IPC Segment: " + segmentName + " (" + capacityBytes + " bytes)");
        SharedMemory sharedMem = SharedMemory.create(segmentName, capacityBytes);
        long baseAddress = sharedMem.address();

        System.out.println("   Memory Mapped Region Address: 0x" + Long.toHexString(baseAddress).toUpperCase());
        System.out.println();

        System.out.println("📡 [1/3] Simulated IPC Message Batch Streaming (Producer -> Consumer)");
        int messages = 5;
        for (int i = 1; i <= messages; i++) {
            String payload = "MARKET_TICK_#00" + i + " SYMBOL=BTC-USD PRICE=67450." + (10 + i * 5) + " VOL=12.4";
            byte[] bytes = payload.getBytes();
            long offset = (i - 1) * 128L;

            long t0 = System.nanoTime();
            UNSAFE.copyMemory(bytes, Unsafe.ARRAY_BYTE_BASE_OFFSET, null, baseAddress + offset, bytes.length);
            long writeTime = System.nanoTime() - t0;

            byte[] readBuf = new byte[bytes.length];
            long t1 = System.nanoTime();
            UNSAFE.copyMemory(null, baseAddress + offset, readBuf, Unsafe.ARRAY_BYTE_BASE_OFFSET, bytes.length);
            long readTime = System.nanoTime() - t1;

            System.out.printf("   [IPC Slot %d] Transferred: \"%s\"%n", i, new String(readBuf));
            System.out.printf("                Write: %d ns | Read: %d ns | Latency: %d ns (Zero OS Context Switch)%n",
                    writeTime, readTime, (writeTime + readTime));
            Thread.sleep(150);
        }

        System.out.println();
        System.out.println("🚀 [2/3] IPC Throughput Stress Benchmark (500,000 Ring-Buffer Writes/Reads)");

        int totalTicks = 500000;
        byte[] tickData = "MARKET_DATA_STREAM_PAYLOAD_TICK_PACKET_X99".getBytes();

        long start = System.currentTimeMillis();
        for (int i = 0; i < totalTicks; i++) {
            long off = (i % 32) * 64L;
            UNSAFE.copyMemory(tickData, Unsafe.ARRAY_BYTE_BASE_OFFSET, null, baseAddress + off, tickData.length);
            UNSAFE.copyMemory(null, baseAddress + off, tickData, Unsafe.ARRAY_BYTE_BASE_OFFSET, tickData.length);
        }
        long duration = System.currentTimeMillis() - start;
        double opsPerSec = (totalTicks * 1000.0) / Math.max(duration, 1);

        System.out.printf("   Total IPC Ticks Processed: %,d%n", totalTicks);
        System.out.printf("   Total Execution Time     : %d ms%n", duration);
        System.out.printf("   IPC Throughput            : %,.0f operations/sec%n", opsPerSec);

        System.out.println();
        System.out.println("🔒 [3/3] Releasing Memory Mapped Region");
        sharedMem.close();

        System.out.println("==========================================================================");
        System.out.println("✅ FastSharedMemory Demo Completed Successfully!");
        System.out.println("==========================================================================");
    }
}
