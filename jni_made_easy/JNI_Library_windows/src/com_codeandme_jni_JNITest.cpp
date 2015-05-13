/*******************************************************************************
 * Copyright (c) 2015 Christian Pontesegger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christian Pontesegger - initial implementation
 *******************************************************************************/
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
