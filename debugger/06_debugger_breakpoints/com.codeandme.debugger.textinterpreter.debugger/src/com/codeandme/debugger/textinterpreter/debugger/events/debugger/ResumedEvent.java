package com.codeandme.debugger.textinterpreter.debugger.events.debugger;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;

public class ResumedEvent extends AbstractEvent implements IDebuggerEvent {

	public static final int STEPPING = 1;
	public static final int CONTINUE = 2;

	private final int fType;

	public ResumedEvent(int type) {
		fType = type;
	}

	public int getType() {
		return fType;
	}

	@Override
	public String toString() {
		return super.toString() + "(" + ((fType == STEPPING) ? "STEPPING" : "CONTINUE") + ")";
	}
}
