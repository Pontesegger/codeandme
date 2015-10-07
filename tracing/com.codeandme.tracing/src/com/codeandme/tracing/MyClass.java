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
package com.codeandme.tracing;

import org.eclipse.core.runtime.Platform;

public class MyClass {

	private static final boolean DEBUG_THIS = Activator.DEBUG | "true".equalsIgnoreCase(Platform.getDebugOption("com.codeandme.tracing/debug/myclass"));

	public MyClass() {
		Activator.debug(DEBUG_THIS, "MyClass constructor called");
	}

	@Override
	public String toString() {
		Activator.debug(DEBUG_THIS, "MyClass toString() called");

		return super.toString();
	}
}
