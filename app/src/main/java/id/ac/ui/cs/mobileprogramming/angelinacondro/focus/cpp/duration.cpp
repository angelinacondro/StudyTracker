//
// Created by owner on 01/01/2021.
//

#include <jni.h>
#include <String>
#include <iostream>


extern "C"
JNIEXPORT jlong JNICALL
Java_com_angelinacondro_studytracker_fragment_NativeLibrary_countDifference (JNIEnv *env,  jobject thiz, jlong timeStarts, jlong timeEnds){
    long difference = timeEnds - timeStarts;
    return difference;
}
