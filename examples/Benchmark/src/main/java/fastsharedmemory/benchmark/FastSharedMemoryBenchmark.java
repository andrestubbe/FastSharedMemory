package fastsharedmemory.benchmark;

import fastsharedmemory.SharedMemory;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class FastSharedMemoryBenchmark {

    private SharedMemory shm;

    @Setup
    public void setup() {
        shm = SharedMemory.create("JmhSharedMemoryBench", 1024 * 1024);
    }

    @Benchmark
    public long testAddressAccess() {
        return shm.address();
    }

    @TearDown
    public void tearDown() {
        if (shm != null) {
            shm.close();
        }
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
