package com.codeandme.debugger.textinterpreter.debugger.events.debugger;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.model.EvaluateExpressionRequest;

public class EvaluateExpressionResult extends AbstractEvent implements IDebuggerEvent {

	private String fResult;
	private EvaluateExpressionRequest fRequest;

	public EvaluateExpressionResult(String result, EvaluateExpressionRequest request) {
		fResult = result;
		fRequest = request;
	}

	public String getResult() {
		return fResult;
	}

	public EvaluateExpressionRequest getOriginalRequest() {
		return fRequest;
	}
}
