package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
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

	@Override
	public boolean canResume() {
		return getDebugTarget().canResume();
	}

	@Override
	public boolean canSuspend() {
		return getDebugTarget().canSuspend();
	}

	@Override
	public boolean isSuspended() {
		return getDebugTarget().isSuspended();
	}

	@Override
	public void resume() throws DebugException {
		getDebugTarget().resume();
	}

	@Override
	public void suspend() throws DebugException {
		getDebugTarget().suspend();
	}

	@Override
	public boolean canStepInto() {
		return false;
	}

	@Override
	public boolean canStepOver() {
		return false;
	}

	@Override
	public boolean canStepReturn() {
		return false;
	}

	@Override
	public boolean isStepping() {
		return false;
	}

	@Override
	public void stepInto() throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "stepInto() not supported"));
	}

	@Override
	public void stepOver() throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "stepOver() not supported"));
	}

	@Override
	public void stepReturn() throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "stepReturn() not supported"));
	}

	@Override
	public boolean canTerminate() {
		return getDebugTarget().canTerminate();
	}

	@Override
	public boolean isTerminated() {
		return getDebugTarget().isTerminated();
	}

	@Override
	public void terminate() throws DebugException {
		getDebugTarget().terminate();
	}
}
