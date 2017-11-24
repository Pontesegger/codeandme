/*******************************************************************************
 * Copyright (c) 2013 Christian Pontesegger.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christian Pontesegger - initial API and implementation
 *******************************************************************************/
package com.codeandme.debugger.textinterpreter;

public interface IDebugger {

	int UNSPECIFIED = 0;
	int SUSPEND_BREAKPOINT = 1;
	int SUSPEND_STEP_OVER = 2;
	int RESUME_STEP_OVER = 3;

	void loaded();

	boolean isBreakpoint(int lineNumber);

	void terminated();

	void suspended(int lineNumber);

	void resumed();
}
