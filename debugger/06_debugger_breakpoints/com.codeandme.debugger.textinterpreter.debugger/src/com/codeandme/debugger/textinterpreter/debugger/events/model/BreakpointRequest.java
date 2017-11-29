package com.codeandme.debugger.textinterpreter.debugger.events.model;

import org.eclipse.core.resources.IMarker;
import org.eclipse.debug.core.model.IBreakpoint;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;

public class BreakpointRequest extends AbstractEvent implements IModelRequest {

	public static final int ADDED = 1;
	public static final int REMOVED = 2;
	
	private final int fType;
	private final IBreakpoint fBreakpoint;

	public BreakpointRequest(IBreakpoint breakpoint, int type) {
		fBreakpoint = breakpoint;
		fType = type;
	}

	public IBreakpoint getBreakpoint() {
		return fBreakpoint;
	}

	public int getType() {
		return fType;
	}

	@Override
	public String toString() {
		int line = getBreakpoint().getMarker().getAttribute(IMarker.LINE_NUMBER, -1);
		return super.toString() + " (" +((getType() == ADDED) ? "ADDED" : "REMOVED") + ", line " + line + ")";
	}
}
