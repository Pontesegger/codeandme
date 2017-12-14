package com.codeandme.debugger.textinterpreter.debugger.events.debugger;

import com.codeandme.debugger.textinterpreter.debugger.events.AbstractEvent;

public class MemoryEvent extends AbstractEvent implements IDebuggerEvent {

	private final byte[] fData;
	private final int fStartAddress;

	public MemoryEvent(int startAddress, byte[] data) {
		fStartAddress = startAddress;
		fData = data;
	}

	public byte[] getData() {
		return fData;
	}

	public int getStartAddress() {
		return fStartAddress;
	}
}
