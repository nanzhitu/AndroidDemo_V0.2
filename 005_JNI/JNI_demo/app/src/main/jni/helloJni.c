#include <jni.h>

JNIEXPORT jstring JNICALL Java_com_example_administrator_jni_1demo_MainActivity_helloJni(JNIEnv *env, jobject instance)
{
// TODO
return (*env)->NewStringUTF(env, "hello jni");
}