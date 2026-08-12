# FastSharedMemory (FastIPC)

## 1. Vision & Kernidee
**FastSharedMemory** löst das Problem der langsamen Inter-Prozess-Kommunikation (IPC). 

Wenn ein FastJava-Bot (z.B. `FastScreen`) Bilder erfasst und diese an ein lokales KI-Vision-Modell (z.B. in Python via PyTorch/Ollama) senden muss, ist der klassische Weg über TCP-Sockets (HTTP/REST) oder lokale Dateien viel zu langsam. Die Daten müssen kopiert, serialisiert, über den Netzwerk-Stack gesendet und wieder deserialisiert werden.

**Die Lösung:** Memory-Mapped Files (Shared Memory).
Java und Python (oder C++) greifen auf **exakt denselben Bereich im Arbeitsspeicher (RAM)** zu. Es gibt keinerlei Datenkopie (Zero-Copy). Java schreibt einen Frame in den Speicher, Python liest ihn im selben Moment.

## 2. Java High-Level API

```java
public interface FastSharedMemory {
    static FastSharedMemory open(String name, long sizeBytes) { return new FastSharedMemoryImpl(name, sizeBytes); }

    // Gibt einen direkten Zeiger auf den Speicher zurück (für Unsafe / FastBytes)
    long getPointer();

    // Liest/Schreibt Daten in den Shared Memory
    void writeBytes(long offset, byte[] data);
    void readBytes(long offset, byte[] dest);

    // Synchronisation (damit Python weiß, wann Java fertig geschrieben hat)
    void signalReady();
    void waitForConsumer();
    
    void close();
}
```

## 3. C++ JNI Backend (Win32)
Das Backend nutzt das Windows Memory Mapping System.

1. **Mapping:** `CreateFileMapping` mit `INVALID_HANDLE_VALUE` (nutzt die Windows-Auslagerungsdatei, schreibt aber faktisch nur in den RAM) und einem eindeutigen Namen (z.B. `"Local\\FastJava_VisionFrame"`).
2. **View:** `MapViewOfFile` liefert den nativen Pointer auf diesen Speicherblock.
3. **Synchronisation:** Es werden Win32-Events (`CreateEvent`) wie `"Local\\FastJava_FrameReady"` genutzt. Java ruft `SetEvent` auf, Python hängt in `WaitForSingleObject`. Das blockiert den Thread mit 0% CPU-Last, bis die Daten da sind.

## 4. Agent-Kit (KI-Integration)
Das ist die Brücke zwischen der "Ausführungs-Ebene" (FastJava) und der "Denk-Ebene" (Python AI).

**Beispielhafter Workflow:**
1. Agent befiehlt: "Analysiere Bildschirm".
2. `FastScreen` erfasst das Bild.
3. `FastSharedMemory` schreibt das BGRA-Array in den RAM und setzt das Event `FrameReady`.
4. Der Python-Prozess liest den Frame instantan, lässt das Vision-Modell laufen und schreibt das Ergebnis (z.B. als FastJSON-String) in einen zweiten Shared-Memory-Block (`ResultReady`).
5. Java liest das Ergebnis in unter 1 Millisekunde.
