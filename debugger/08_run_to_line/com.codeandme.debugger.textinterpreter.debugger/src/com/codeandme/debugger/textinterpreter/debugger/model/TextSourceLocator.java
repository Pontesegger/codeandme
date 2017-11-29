package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IPersistableSourceLocator;
import org.eclipse.debug.core.model.IStackFrame;

public class TextSourceLocator implements IPersistableSourceLocator {

	@Override
	public Object getSourceElement(IStackFrame stackFrame) {
		if (stackFrame instanceof TextStackFrame)
			return ((TextStackFrame) stackFrame).getSourceFile();

		return null;
	}

	@Override
	public String getMemento() throws CoreException {
		return null;
	}

	@Override
	public void initializeFromMemento(String memento) throws CoreException {
	}

	@Override
	public void initializeDefaults(ILaunchConfiguration configuration) throws CoreException {
	}
}
