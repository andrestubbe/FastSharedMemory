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

## Table of Contents

- [Key Features](#key-features)
- [Quick Start](#quick-start)
- [API Reference](#api-reference)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)

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

## Key Features

- **⚡ Zero-Copy IPC**: Direct memory-mapped file transfer bypassing network sockets and slow pipes.
- **🚀 Massive Throughput**: Optimized for high-frequency video frame capture (`FastRobot`/`FastScreen`) and AI token streaming.
- **🔒 Process Synchronization**: Integrated support for Win32 named handles and events.
- **📦 Zero GC Overhead**: Operates entirely outside the JVM Garbage Collector.

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
        <version>0.1.1</version>
    </dependency>

    <!-- FastCore (Mandatory Native Loader) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastCore</artifactId>
        <version>0.1.1</version>
    </dependency>
</dependencies>
```

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

* 🗺️ **[ROADMAP.md](docs/ROADMAP.md)** — Feature roadmap and future release plans.
* 📖 **[PHILOSOPHY.md](docs/PHILOSOPHY.md)** — Architectural principles and zero-copy contracts.
* 🛠️ **[COMPILE.md](docs/COMPILE.md)** — Native C++ compilation guide using MSVC.
* 📚 **[REFERENCE.md](docs/REFERENCE.md)** — Detailed API reference and specifications.
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
