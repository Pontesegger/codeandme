package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IWatchExpressionResult;

import com.codeandme.debugger.textinterpreter.debugger.events.debugger.EvaluateExpressionResult;

public class TextWatchExpressionResult implements IWatchExpressionResult {

	private String fExpressionText;
	private TextValue fValue;

	public TextWatchExpressionResult(EvaluateExpressionResult event, IDebugTarget debugTarget) {
		fExpressionText = event.getOriginalRequest().getExpression();
		fValue = new TextValue(debugTarget, event.getResult());
	}

	@Override
	public IValue getValue() {
		return fValue;
	}

	@Override
	public boolean hasErrors() {
		return false;
	}

	@Override
	public String[] getErrorMessages() {
		return null;
	}

	@Override
	public String getExpressionText() {
		return fExpressionText;
	}

	@Override
	public DebugException getException() {
		return null;
	}
}
