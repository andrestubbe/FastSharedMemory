package examples;

import fastpointer.Pointer;
import fastsharedmemory.SharedMemory;

public class Demo {
    public static void main(String[] args) {
        System.out.println("=== FastSharedMemory 0.1.0 Interactive Demo ===");

        String shmName = "FastSharedMemoryDemoSegment";
        long size = 1024 * 1024; // 1 MB Shared RAM Segment

        // 1. Create Win32 Named Shared Memory Segment
        try (SharedMemory shm = SharedMemory.create(shmName, size)) {
            System.out.printf("Created Win32 Shared Memory Segment '%s' (%d bytes)%n", shm.name(), shm.size());

            // 2. Integrate with FastPointer
            Pointer ptr = shm.pointer();
            System.out.printf("Mapped Shared Memory Address: 0x%016X%n", ptr.address());

            // 3. Write IPC data into Shared Memory
            ptr.setInt(0, 0xCAFEBABE);
            ptr.setLong(8, 9876543210L);

            // 4. Verify Zero-Copy IPC Read
            int magic = ptr.getInt(0);
            long payload = ptr.getLong(8);

            System.out.printf("IPC Zero-Copy Read: Magic Header = 0x%X, Payload = %d%n", magic, payload);
            System.out.println("=== Demo finished successfully! ===");
        }
    }
}
