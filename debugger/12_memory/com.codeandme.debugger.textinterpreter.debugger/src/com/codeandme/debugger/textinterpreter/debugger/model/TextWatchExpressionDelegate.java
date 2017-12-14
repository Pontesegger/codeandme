package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IWatchExpressionDelegate;
import org.eclipse.debug.core.model.IWatchExpressionListener;

import com.codeandme.debugger.textinterpreter.debugger.events.model.EvaluateExpressionRequest;

public class TextWatchExpressionDelegate implements IWatchExpressionDelegate {

	@Override
	public void evaluateExpression(String expression, IDebugElement context, IWatchExpressionListener listener) {
		if (context instanceof TextStackFrame)
			((TextStackFrame) context).getDebugTarget().fireModelEvent(new EvaluateExpressionRequest(expression, listener));
	}
}
