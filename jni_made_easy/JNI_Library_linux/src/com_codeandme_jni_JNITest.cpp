#include  <iostream>

#include "com_codeandme_jni_JNITest.h"

using namespace std;

JNIEXPORT void JNICALL Java_com_codeandme_jni_JNITest_hello
(JNIEnv *env, jobject jthis, jstring data) {

	jboolean iscopy;
	const char *charData = env->GetStringUTFChars(data, &iscopy);
	cout << "Hello " << charData << endl;
	env->ReleaseStringUTFChars(data, charData);
}
