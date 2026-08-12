# FastSharedMemory 0.1.0 [ALPHA] — Ultra-Fast Native Zero-Copy IPC for Java

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastSharedMemory/releases/tag/0.1.0)
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
        <version>0.1.0</version>
    </dependency>

    <!-- FastCore (Mandatory Native Loader) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastCore</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

---

## Documentation

* **[Description.md](docs/Description.md)**: Architectural overview and core module capabilities.
* **[COMPILE.md](docs/COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
* **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions and technical method specifications.
* **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: Engineering rationale for zero-allocation performance.
* **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones and planned features.
* **[CHANGELOG.md](docs/CHANGELOG.md)**: Version history and release notes.

---

## Platform Support

| Platform | Status |
|---|---|
| Windows 10/11 (x64) | ✅ Fully Supported |
| Linux (POSIX shm_open) | 🚧 Planned |
| macOS (POSIX shm_open) | 🚧 Planned |

---

## License

MIT License — See [LICENSE](LICENSE) for details.

---

**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*
