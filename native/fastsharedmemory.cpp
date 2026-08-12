#include "fastsharedmemory.h"
#include <jni.h>

extern "C" {

JNIEXPORT jlong JNICALL Java_fastsharedmemory_FastSharedMemoryNative_createMapping(JNIEnv* env, jclass clazz, jstring nameStr, jlong size) {
    if (!nameStr) return 0;
    const char* name = env->GetStringUTFChars(nameStr, NULL);
    HANDLE hMap = FastSharedMemory_CreateMapping(name, static_cast<uint64_t>(size));
    env->ReleaseStringUTFChars(nameStr, name);
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(hMap));
}

JNIEXPORT jlong JNICALL Java_fastsharedmemory_FastSharedMemoryNative_mapView(JNIEnv* env, jclass clazz, jlong handle, jlong size) {
    HANDLE hMap = reinterpret_cast<HANDLE>(static_cast<uintptr_t>(handle));
    void* ptr = FastSharedMemory_MapView(hMap, static_cast<uint64_t>(size));
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(ptr));
}

JNIEXPORT void JNICALL Java_fastsharedmemory_FastSharedMemoryNative_unmapView(JNIEnv* env, jclass clazz, jlong address) {
    void* ptr = reinterpret_cast<void*>(static_cast<uintptr_t>(address));
    FastSharedMemory_UnmapView(ptr);
}

JNIEXPORT void JNICALL Java_fastsharedmemory_FastSharedMemoryNative_closeMapping(JNIEnv* env, jclass clazz, jlong handle) {
    HANDLE hMap = reinterpret_cast<HANDLE>(static_cast<uintptr_t>(handle));
    FastSharedMemory_CloseMapping(hMap);
}

}
