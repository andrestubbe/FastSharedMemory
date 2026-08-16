# FastSharedMemory 0.1.1 [ALPHA-2026-08] — Ultra-Fast Native Zero-Copy IPC for Java

[![Status](https://img.shields.io/badge/status-0.1.1-brightgreen.svg)](https://github.com/andrestubbe/FastSharedMemory/releases/tag/0.1.1)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

---

**⚡ High-performance Inter-Process Communication (IPC) via native shared memory for the JVM.**

`FastSharedMemory` provides zero-copy data sharing between independent Java processes or native C++/Python applications using Windows Named Shared Memory (`CreateFileMapping` / `MapViewOfFile`).

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

- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Performance Benchmarks](#performance-benchmarks)
- [Quick Start](#quick-start)
- [API Reference](#api-reference)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)

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

> **8.14 Million Messages per Second**: `FastSharedMemory` transfers binary messages across OS processes in **under 78 nanoseconds per message** with zero OS context switching overhead.

---

## API Reference

### `SharedMemory`
- `SharedMemory.create(String name, long size)`: Creates a new named shared memory mapping.
- `SharedMemory.open(String name, long size)`: Opens an existing named shared memory mapping.
- `pointer()`: Returns a `Pointer` pointing to the mapped shared memory block.
- `address()`: Returns the primitive `long` memory address.
- `close()` / `free()`: Unmaps and closes the shared memory mapping.

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

---

## Technical Examples & Benchmarks

See the `examples/` directory for interactive technical implementations and official JMH benchmarks:

| Benchmark Case | Description | Java Example | JMH Benchmark |
|---|---|---|---|
| **Zero-Copy IPC** | Win32 Named Shared Memory mapping throughput & latency | [Demo.java](examples/Demo.java) | [JMH_SharedMemory.java](examples/src/main/java/fastsharedmemory/benchmark/JMH_SharedMemory.java) |

### Run JMH Benchmarks via Script
```cmd
run-benchmark.bat
```

---

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
