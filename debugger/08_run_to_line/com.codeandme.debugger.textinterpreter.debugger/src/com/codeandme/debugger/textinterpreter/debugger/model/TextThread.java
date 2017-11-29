package com.codeandme.debugger.textinterpreter.debugger.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IThread;

public class TextThread extends TextDebugElement implements IThread {

	private final List<TextStackFrame> fStackFrames = new ArrayList<>();

	public TextThread(final TextDebugTarget debugTarget) {
		super(debugTarget);
	}

	public void addStackFrame(TextStackFrame stackFrame) {
		fStackFrames.add(0, stackFrame);
	}

	@Override
	public TextStackFrame[] getStackFrames() {
		return fStackFrames.toArray(new TextStackFrame[fStackFrames.size()]);
	}

	@Override
	public boolean hasStackFrames() {
		return getStackFrames().length > 0;
	}


	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TextStackFrame getTopStackFrame() {
		if (!fStackFrames.isEmpty())
			return fStackFrames.get(0);

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
