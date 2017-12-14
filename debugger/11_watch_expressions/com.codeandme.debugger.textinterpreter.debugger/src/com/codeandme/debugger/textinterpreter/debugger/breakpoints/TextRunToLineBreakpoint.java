package com.codeandme.debugger.textinterpreter.debugger.breakpoints;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class TextRunToLineBreakpoint extends TextLineBreakpoint {

	public TextRunToLineBreakpoint() {
		super();
	}

	public TextRunToLineBreakpoint(IResource resource, int lineNumber) throws CoreException {
		super(resource, lineNumber, false);
	}
}
