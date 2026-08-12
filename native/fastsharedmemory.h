#ifndef FASTSHAREDMEMORY_H
#define FASTSHAREDMEMORY_H

#include <windows.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

inline HANDLE FastSharedMemory_CreateMapping(const char* name, uint64_t size) {
    if (!name || size == 0) return NULL;
    
    DWORD sizeHigh = static_cast<DWORD>(size >> 32);
    DWORD sizeLow = static_cast<DWORD>(size & 0xFFFFFFFF);

    HANDLE hMap = CreateFileMappingA(
        INVALID_HANDLE_VALUE,
        NULL,
        PAGE_READWRITE,
        sizeHigh,
        sizeLow,
        name
    );
    return hMap;
}

inline void* FastSharedMemory_MapView(HANDLE hMap, uint64_t size) {
    if (!hMap) return NULL;
    return MapViewOfFile(hMap, FILE_MAP_ALL_ACCESS, 0, 0, static_cast<SIZE_T>(size));
}

inline BOOL FastSharedMemory_UnmapView(void* address) {
    if (!address) return FALSE;
    return UnmapViewOfFile(address);
}

inline void FastSharedMemory_CloseMapping(HANDLE hMap) {
    if (hMap && hMap != INVALID_HANDLE_VALUE) {
        CloseHandle(hMap);
    }
}

#ifdef __cplusplus
}
#endif

#endif // FASTSHAREDMEMORY_H
