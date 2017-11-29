package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.debug.core.model.DebugElement;
import org.eclipse.debug.core.model.IDebugTarget;

public abstract class TextDebugElement extends DebugElement {

	protected TextDebugElement(IDebugTarget target) {
		super(target);
	}
	
	@Override
	public String getModelIdentifier() {
		return null;
	}
}
