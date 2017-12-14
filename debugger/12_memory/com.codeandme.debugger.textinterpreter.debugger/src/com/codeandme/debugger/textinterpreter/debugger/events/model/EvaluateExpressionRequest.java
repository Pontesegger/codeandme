package com.codeandme.debugger.textinterpreter.debugger.events.model;

import org.eclipse.debug.core.model.IWatchExpressionListener;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;

public class EvaluateExpressionRequest extends AbstractEvent implements IModelRequest {

	private String fExpression;
	private IWatchExpressionListener fListener;

	public EvaluateExpressionRequest(String expression, IWatchExpressionListener listener) {
		fExpression = expression;
		fListener = listener;
	}

	public String getExpression() {
		return fExpression;
	}

	public IWatchExpressionListener getListener() {
		return fListener;
	}
}
