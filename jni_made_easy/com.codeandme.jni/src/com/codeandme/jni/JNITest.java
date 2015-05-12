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
package com.codeandme.jni;

public class JNITest {

	static {
		// load library
		if (System.getProperty("os.name").startsWith("Windows"))
			// windows
			System.loadLibrary("libJNI_Library_windows");

		else
			// linux
			System.loadLibrary("JNI_Library_linux");
	}

	public static void main(final String[] args) {
		new JNITest().hello("world");
	}

	// native method signature
	public native void hello(String name);
}