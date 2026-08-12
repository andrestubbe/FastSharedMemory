# FastSharedMemory API Reference

## Class `fastsharedmemory.SharedMemory`

### Allocation & IPC Mapping
- `SharedMemory.create(String name, long size)`: Creates a named shared memory mapping.
- `SharedMemory.open(String name, long size)`: Opens an existing named shared memory mapping.

### Operation
- `pointer()`: Returns a `Pointer` pointing to the mapped shared memory block.
- `address()`: Returns the primitive 64-bit `long` address.
- `size()`: Returns total byte capacity.
- `name()`: Returns the Win32 IPC mapping name.
- `free()` / `close()`: Unmaps and closes the shared memory mapping.