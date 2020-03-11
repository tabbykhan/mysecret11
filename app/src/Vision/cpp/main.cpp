#include <jni.h>
#include <string>

extern "C" {

std::string getDomain(
        JNIEnv *env) {
    //std::string data = "http://139.162.28.58/";
    std::string data = "http://139.162.187.28/"; //new url

    return data;

}


std::string getBaseUrl(
        JNIEnv *env) {
    //std::string data = getDomain(env) + "imuons/admin/apis/mobile/v1/";
    std::string data = getDomain(env) + "admin/apis/mobile/v1/";  // new url
    return data;
}


std::string getBasePageUrl(
        JNIEnv *env) {
   std::string data = getDomain(env) + "imuons/admin/m/pages/";
  //  std::string data = getDomain(env) + "admin/m/pages/";
    return data;
}

}