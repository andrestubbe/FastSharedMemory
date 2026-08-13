package fastsharedmemory.benchmark;

import fastpointer.Pointer;
import fastsharedmemory.SharedMemory;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
public class JMH_SharedMemory {

    private SharedMemory shm;
    private Pointer pointer;

    @Setup
    public void setup() {
        shm = SharedMemory.create("JMH_FastSharedMemory_Bench", 1048576);
        pointer = shm.pointer();
    }

    @TearDown
    public void tearDown() {
        if (shm != null) {
            shm.free();
        }
    }

    @Benchmark
    public int testZeroCopyRead() {
        return pointer.getInt(0);
    }

    @Benchmark
    public void testZeroCopyWrite() {
        pointer.setInt(0, 1337);
    }
}
