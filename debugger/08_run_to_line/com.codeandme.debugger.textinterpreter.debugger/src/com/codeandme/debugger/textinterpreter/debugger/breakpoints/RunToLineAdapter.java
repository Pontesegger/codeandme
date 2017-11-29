package com.codeandme.debugger.textinterpreter.debugger.breakpoints;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IRunToLineTarget;

public class RunToLineAdapter implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adapterType.equals(IRunToLineTarget.class))
			return (T) new RunToLineTarget();
		
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IRunToLineTarget.class };
	}
}
