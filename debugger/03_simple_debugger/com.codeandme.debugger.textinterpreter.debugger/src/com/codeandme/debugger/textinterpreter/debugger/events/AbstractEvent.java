package com.codeandme.debugger.textinterpreter.debugger.events;

public abstract class AbstractEvent implements IDebugEvent {

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
