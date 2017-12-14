package com.codeandme.debugger.textinterpreter.debugger.events.debugger;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;

public class SuspendedEvent extends AbstractEvent implements IDebuggerEvent {

	private final int fLineNumber;

	public SuspendedEvent(int lineNumber) {
		fLineNumber = lineNumber;
	}

	public int getLineNumber() {
		return fLineNumber;
	}
}
