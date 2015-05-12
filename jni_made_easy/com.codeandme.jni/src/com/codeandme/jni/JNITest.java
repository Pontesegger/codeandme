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