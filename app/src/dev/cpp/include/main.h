//
// Created by bitu on 2/9/18.
//
#include <jni.h>

#ifndef REST_NATIVE_LIB_H
#define REST_NATIVE_LIB_H

#endif //REST_NATIVE_LIB_H
extern "C" {

std::string getDomain(JNIEnv *env);
std::string getBaseUrl(JNIEnv *env);
std::string getBasePageUrl(JNIEnv *env);

}