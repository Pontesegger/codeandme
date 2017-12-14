package com.codeandme.debugger.textinterpreter.debugger.events.debugger;

import java.util.Map;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;

public class VariablesEvent extends AbstractEvent implements IDebuggerEvent {

	private final Map<String, String> mVariables;

	public VariablesEvent(Map<String, String> variables) {
		mVariables = variables;
	}

	public Map<String, String> getVariables() {
		return mVariables;
	}
}
