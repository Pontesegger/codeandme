package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

public class TextVariable extends TextDebugElement implements IVariable {

	private final String fName;
	private IValue fValue;
	private boolean fChanged = false;

	protected TextVariable(IDebugTarget target, String name, String value) {
		super(target);
		fName = name;
		setValue(value);
	}

	@Override
	public void setValue(String expression) {
		fValue = new TextValue(getDebugTarget(), expression);
	}

	@Override
	public void setValue(IValue value) {
		fValue = value;
	}

	@Override
	public boolean supportsValueModification() {
		return false;
	}

	@Override
	public boolean verifyValue(String expression) throws DebugException {
		return false;
	}

	@Override
	public boolean verifyValue(IValue value) throws DebugException {
		return false;
	}

	@Override
	public IValue getValue() {
		return fValue;
	}

	@Override
	public String getName() {
		return fName;
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		return "text type";
	}

	@Override
	public boolean hasValueChanged() throws DebugException {
		return fChanged;
	}

	public void setChanged(boolean changed) {
		fChanged = changed;
	}
}
