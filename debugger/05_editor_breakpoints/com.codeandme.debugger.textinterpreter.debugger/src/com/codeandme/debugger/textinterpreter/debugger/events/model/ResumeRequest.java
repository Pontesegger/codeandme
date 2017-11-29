package com.codeandme.debugger.textinterpreter.debugger.events.model;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;

public class ResumeRequest extends AbstractEvent implements IModelRequest {

	public static final int STEP_OVER = 1;
	public static final int CONTINUE = 2;
	public static final int RUN_TO_LINE = 3;

	private final int fType;

	public ResumeRequest(int type) {
		fType = type;
	}

	public ResumeRequest() {
		this(CONTINUE);
	}

	public int getType() {
		return fType;
	}

	@Override
	public String toString() {
		return super.toString() + "(" + getTypeName() + ")";
	}

	private String getTypeName() {
		switch (getType()) {
		case STEP_OVER:
			return "STEP_OVER";
		case CONTINUE:
			return "CONTINUE";
		case RUN_TO_LINE:
			return "RUN_TO_LINE";
		default:
			return "<unknown>";
		}
	}
}
