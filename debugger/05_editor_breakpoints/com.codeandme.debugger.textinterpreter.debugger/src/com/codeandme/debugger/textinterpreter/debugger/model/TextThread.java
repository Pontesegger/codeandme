package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;

public class TextThread extends TextDebugElement implements IThread {

	public TextThread(final TextDebugTarget debugTarget) {
		super(debugTarget);
	}

	@Override
	public IStackFrame[] getStackFrames() {
		return new IStackFrame[0];
	}

	@Override
	public boolean hasStackFrames() {
		return false;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public IStackFrame getTopStackFrame() {
		return null;
	}

	@Override
	public String getName() {
		return "Text Thread";
	}

	@Override
	public IBreakpoint[] getBreakpoints() {
		return null;
	}
}
