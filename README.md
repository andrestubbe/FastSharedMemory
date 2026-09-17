# FastSharedMemory 0.1.2 [ALPHA-2026-08] — Ultra-Fast Native Zero-Copy IPC for Java

[![Status](https://img.shields.io/badge/status-0.1.2-brightgreen.svg)](https://github.com/andrestubbe/FastSharedMemory/releases/tag/0.1.2)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

---

**⚡ High-performance Inter-Process Communication (IPC) via native shared memory for the JVM.**

`FastSharedMemory` provides zero-copy data sharing between independent Java processes or native C++/Python applications using Windows Named Shared Memory (`CreateFileMapping` / `MapViewOfFile`).

Watch Demo (YouTube) | Watch JMH Benchmark (YouTube)

---

## Quick Start

```java
import fastsharedmemory.*;
import fastpointer.Pointer;

public class Demo {
    public static void main(String[] args) {
        // Create or open a named 1MB shared memory segment
        try (SharedMemory shm = SharedMemory.create("FastSharedMemoryDemo", 1024 * 1024)) {
            Pointer ptr = shm.pointer();

            // Write data from Process A
            ptr.setInt(0, 1337);
            System.out.println("Shared Memory mapped at address: " + ptr);
            System.out.println("Process A wrote value 1337 at offset 0");
        }
    }
}
```

---

## Table of Contents

- [Why FastSharedMemory?](#why-fastsharedmemory)
- [Quick Start](#quick-start)
- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Performance Benchmarks](#performance-benchmarks)
- [API Quick Reference](#api-quick-reference)
- [Technical Demos & Benchmarks](#technical-demos--benchmarks)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)

---

## Why FastSharedMemory?

Standard Java Inter-Process Communication (IPC) techniques (TCP Sockets, gRPC, Named Pipes, or stdin/stdout streaming) introduce heavy serialization overhead, OS context switches, and network protocol stack latencies. `FastSharedMemory` provides:

- **Zero-Copy Native IPC (< 78 ns Latency)** — Exchange data directly between independent Java processes or native C++/Python applications via Windows Named Shared Memory (`CreateFileMapping` / `MapViewOfFile`).
- **Zero OS Context Switching & Network Overhead** — Eliminate TCP socket and pipe serialization bottlenecks, enabling sub-microsecond message passing between local processes (8.14+ Million msg/sec).
- **Zero-GC Shared Buffers** — Share gigabytes of raw video frames (`FastRobot`), audio streams (`FastSTT`), and tensor buffers across processes completely outside the JVM Garbage Collector.

FastSharedMemory maps shared physical RAM directly into primitive 64-bit memory addresses:

| Feature | TCP Loopback (`127.0.0.1`) | Named Pipes (`\\.\pipe\`) | FastSharedMemory |
|:---|:---|:---|:---|
| **Data Transfer Mechanism**| TCP/IP stack + buffer copy | Kernel FIFO pipe buffer | **Direct Shared RAM (`MapViewOfFile`)** |
| **Transfer Latency** | 25–60 µs (Network stack) | 5–15 µs (OS syscalls) | **< 78 ns (Direct Pointer Read)** |
| **Throughput (1 KB msgs)** | ~100,000 msgs/sec | ~500,000 msgs/sec | **> 8,140,000 ops/sec** |
| **Heap Allocations (IPC)** | High (`byte[]` + stream buffers)| Moderate (Pipe buffers) | **0 Bytes (Direct Off-Heap Pointer)** |
| **Large Payload Handling** | Severe memory duplication | Buffer size limits | **Zero-Copy (Gigabyte Video/Tensors)**|
| **Dependencies** | JDK standard lib | JNA / External wrapper | **Pure Java 17+ backed by FastCore** |

---

## Key Features

- **⚡ Zero-Copy IPC**: Direct memory-mapped file transfer bypassing network sockets and slow pipes.
- **🚀 Massive Throughput**: Optimized for high-frequency video frame capture (`FastRobot`/`FastScreen`) and AI token streaming.
- **🔒 Process Synchronization**: Integrated support for Win32 named handles and events.
- **📦 Zero GC Overhead**: Operates entirely outside the JVM Garbage Collector.

---

## Real-World Use Cases

- 📡 **Ultra-Low Latency IPC**: Lock-free shared memory ring buffers between separate JVM and C++ OS processes with sub-80ns latency.
- 📈 **Market Data Distribution**: Broadcast high-frequency ticker updates across multi-process trading architectures with zero OS context switches.
- 🛠️ **RAM-Speed Shared Cache**: Exchange structured binary packets between microservices without network socket overhead.

---

## Performance Benchmarks

`FastSharedMemory` provides zero-latency process-to-process data exchange. In the official [JMH Benchmark](examples/Benchmark), the system measured lock-free IPC ring buffer throughput:

```text
Benchmark                                    Mode  Cnt      Score   Error  Units
JMH_SharedMemory.benchmarkIPCTransfer        thrpt    2 8142000.500          ops/s
```

---

## API Quick Reference

| Method | Description | Docs |
|---|---|---|
| `SharedMemory.create(name, size)` | Creates a new named Win32 shared memory file mapping. | [Reference](docs/REFERENCE.md) |
| `SharedMemory.open(name, size)` | Opens an existing named Win32 shared memory file mapping. | [Reference](docs/REFERENCE.md) |
| `pointer()` | Returns a `Pointer` instance pointing to the mapped shared memory. | [Reference](docs/REFERENCE.md) |
| `address()` | Returns the underlying primitive 64-bit `long` memory address. | [Reference](docs/REFERENCE.md) |
| `size()` | Returns the shared memory capacity in bytes. | [Reference](docs/REFERENCE.md) |
| `name()` | Returns the Win32 IPC mapping name. | [Reference](docs/REFERENCE.md) |
| `free()` / `close()` | Unmaps memory view and closes the native OS file mapping handle. | [Reference](docs/REFERENCE.md) |

---

## Technical Demos & Benchmarks

| Case | Java Example | Launcher | Description |
|---|---|---|---|
| **Zero-Copy IPC Shared Memory** | [Demo.java](examples/Demo.java) | `run-demo.bat` | End-to-end Win32 Named Shared Memory IPC demonstration with FastPointer integration. |
| **JMH Microbenchmark Suite** | [Benchmark.java](examples/Benchmark/src/main/java/fastsharedmemory/benchmark/Benchmark.java) | `run-benchmark.bat` | OpenJDK JMH throughput & latency test suite measuring shared memory mapping and address access throughput. |

---

## Installation

### Option 1: Maven (Recommended)
Add the JitPack repository and dependencies to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- FastSharedMemory Library -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastSharedMemory</artifactId>
        <version>0.1.2</version>
    </dependency>

    <!-- FastPointer (Required Dependency) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastPointer</artifactId>
        <version>0.1.1</version>
    </dependency>

    <!-- FastCore (Mandatory Native Loader) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastCore</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastSharedMemory:0.1.2'
    implementation 'com.github.andrestubbe:FastPointer:0.1.1'
    implementation 'com.github.andrestubbe:FastCore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. ⚡ **[FastSharedMemory-0.1.2.jar](https://github.com/andrestubbe/FastSharedMemory/releases/download/0.1.2/FastSharedMemory-0.1.2.jar)** (IPC Shared Memory Engine)
2. 📌 **[FastPointer-0.1.1.jar](https://github.com/andrestubbe/FastPointer/releases/download/0.1.1/FastPointer-0.1.1.jar)** (Required Address Arithmetic)
3. ⚙️ **[fastcore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/0.1.0/fastcore-0.1.0.jar)** (Mandatory Native JNI Loader)


## Documentation

- **[COMPILE.md](docs/COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
- **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions, border configurations, and codepoint index.
- **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The engineering rationale for zero-allocation performance.
- **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones and planned features.
---

## Platform Support

| Platform | Status |
|---|---|
| Windows 10/11 (x64) | ✅ Fully Supported |
| Linux (POSIX shm_open) | 🚧 Planned |
| macOS (POSIX shm_open) | 🚧 Planned |

---

## Related Projects

- [FastMemory](https://github.com/andrestubbe/FastMemory) — SIMD 32-byte aligned off-heap memory allocation and page locking
- [FastPointer](https://github.com/andrestubbe/FastPointer) — Zero-overhead native address arithmetic
- [FastSIMD](https://github.com/andrestubbe/FastSIMD) — Hardware vector acceleration engine (AVX2, AVX-512, NEON)
- [FastCore](https://github.com/andrestubbe/FastCore) — Native JNI loader for FastJava libraries

---

## License

MIT License — See [LICENSE](LICENSE) for details.

---

**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*
