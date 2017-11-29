package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;

public class TextProcess extends TextDebugElement implements IProcess {

	public TextProcess(final TextDebugTarget debugTarget) {
		super(debugTarget);
	}

	@Override
	public String getLabel() {
		return "Text Process";
	}

	@Override
	public IStreamsProxy getStreamsProxy() {
		return null;
	}

	@Override
	public void setAttribute(final String key, final String value) {
	}

	@Override
	public String getAttribute(final String key) {
		return null;
	}

	@Override
	public int getExitValue() throws DebugException {
		return 0;
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
