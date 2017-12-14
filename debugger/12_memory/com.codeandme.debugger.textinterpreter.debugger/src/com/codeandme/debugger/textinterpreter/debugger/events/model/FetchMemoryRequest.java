package com.codeandme.debugger.textinterpreter.debugger.events.model;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;

public class FetchMemoryRequest extends AbstractEvent implements IModelRequest {

	private final long fStartAddress;
	private final long fLength;

	public FetchMemoryRequest(long startAddress, long length) {
		fStartAddress = startAddress;
		fLength = length;
	}

	public long getStartAddress() {
		return fStartAddress;
	}

	public long getLength() {
		return fLength;
	}
}
