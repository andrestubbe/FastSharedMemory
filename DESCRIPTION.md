# FastSharedMemory — Ultra-Fast Zero-Copy IPC & Shared Memory Mapped Files

> **High-Performance Inter-Process Communication (IPC) via Native Shared Memory for Java.**

---

## 🎯 Zweck & Aufgabe

`FastSharedMemory` ermöglicht blitzschnellen, prozessübergreifenden Datenaustausch ohne Netzwerk-Sockets, Named Pipes oder Serialisierung. Es teilt physikalische RAM-Segmente direkt zwischen Java-Prozessen oder zwischen Java und nativer C++/Python-Software.

---

## ⚙️ Was konkret implementiert werden muss

1. **Win32 Memory Mapped Files (`CreateFileMapping` / `MapViewOfFile`)**:
   - Bereitstellung benannter Shared-Memory-Segmente mit konfigurierbaren Lese-/Schreibrechten.

2. **Ring-Buffer & Synchronization**:
   - Zero-Copy Streaming-Architektur mit Win32 Semaphoren/Events für latenzfreie Frame- und Token-Übertragung.

3. **Cross-Platform Abstraction**:
   - Vorbereitung für POSIX `shm_open` / `mmap` auf Linux & macOS.

---

## 🔗 Wer bindet sich an `FastSharedMemory`?

- **`FastRobot` & `FastScreen`**: Streamt Live-Screen-Capture Framebuffer ohne Kopieraufwand direkt an externe Prozesse oder KI-Agents.
- **`FastAIBot` & `FastAIRuntime`**: Ermöglicht Zero-Copy Token- und Session-Austausch zwischen Bot-Orchestrator und lokaler Inferenz-Engine.
- **`FastAudioCapture`**: Übergibt kontinuierliche PCM-Audio-Buffer in Echtzeit an Speech-to-Text Engines (`FastSTT`).
- **`FastPointer` & `FastMemory`**: Stellt die Basis-Adresse bereit und verhindert GC-Freigaben während der Übertragung.

---

## 🔄 Die Zero-Copy Pipeline

```
FastSharedMemory (Teilt Framebuffer / Token-Stream prozessübergreifend)
  └── FastMemory (Hält & sichert 32-Byte aligned RAM ohne GC-Overhead)
        └── FastPointer (Zeigt direkt auf die RAM-Adresse)
              └── FastSIMD (Verarbeitet Bytes mit AVX2, z.B. RGBA-zu-BGRA Formatkonvertierung)
```
