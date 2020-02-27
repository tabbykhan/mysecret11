# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

add_library(rest SHARED ../dev/cpp/rest.cpp cpp/main.cpp)

include_directories(../dev/cpp/include)

target_link_libraries(rest)