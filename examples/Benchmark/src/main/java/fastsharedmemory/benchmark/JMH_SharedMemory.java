package fastsharedmemory.benchmark;

import fastsharedmemory.SharedMemory;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
public class JMH_SharedMemory {

    private SharedMemory sharedMem;

    @Setup
    public void setup() {
        sharedMem = SharedMemory.create("JMH_SharedMem_Bench", 4096);
    }

    @TearDown
    public void tearDown() {
        sharedMem.close();
    }

    @Benchmark
    public long testSharedMemoryAddress() {
        return sharedMem.address();
    }
}
