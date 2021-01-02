cmake_minimum_required(VERSION 3.4.1)
add_library(
        duration

        SHARED

        duration.cpp)

find_library(
        duration

        log)

target_link_libraries(
        duration
        ${log-lib})