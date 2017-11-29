package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

public class TextStackFrame extends TextDebugElement implements IStackFrame {

	private final IThread fThread;
	private int fLineNumber = 1;

	public TextStackFrame(IDebugTarget target, IThread thread) {
		super(target);
		fThread = thread;
	}

	@Override
	public IThread getThread() {
		return fThread;
	}

	@Override
	public IVariable[] getVariables() {
		return new IVariable[0];
	}

	@Override
	public boolean hasVariables() {
		return getVariables().length > 0;
	}

	@Override
	public int getLineNumber() {
		return fLineNumber;
	}

	@Override
	public int getCharStart() {
		return -1;
	}

	@Override
	public int getCharEnd() {
		return -1;
	}

	@Override
	public String getName() {
		return getSourceFile().getName() + ", line " + getLineNumber();
	}

	@Override
	public IRegisterGroup[] getRegisterGroups() {
		return new IRegisterGroup[0];
	}

	@Override
	public boolean hasRegisterGroups() {
		return getRegisterGroups().length > 0;
	}

	public void setLineNumber(int lineNumber) {
		fLineNumber = lineNumber;
	}

	public IFile getSourceFile() {
		return (getDebugTarget()).getFile();
	}
}
