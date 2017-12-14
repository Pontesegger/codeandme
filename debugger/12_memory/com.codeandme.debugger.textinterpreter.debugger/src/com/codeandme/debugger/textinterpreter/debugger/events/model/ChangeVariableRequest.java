package com.codeandme.debugger.textinterpreter.debugger.events.model;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;

public class ChangeVariableRequest extends AbstractEvent implements IModelRequest {
	
	private String fName;
	private String fContent;

	public ChangeVariableRequest(String name, String content) {
		fName = name;
		fContent = content;
	}
	
	public String getName() {
		return fName;
	}
	
	public String getContent() {
		return fContent;
	}
}
