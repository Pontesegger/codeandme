package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

public class TextValue extends TextDebugElement implements IValue {

	private final String fValue;

	public TextValue(IDebugTarget target, String value) {
		super(target);

		fValue = value;
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		return "text type";
	}

	@Override
	public String getValueString() throws DebugException {
		return fValue;
	}

	@Override
	public boolean isAllocated() throws DebugException {
		return true;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {
		return new IVariable[0];
	}

	@Override
	public boolean hasVariables() throws DebugException {
		return false;
	}
}
