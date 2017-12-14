package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlock;

import com.codeandme.debugger.textinterpreter.debugger.events.model.FetchMemoryRequest;

public class TextMemoryBlock extends TextDebugElement implements IMemoryBlock {

	private final long fStartAddress;
	private final long fLength;

	private boolean fDirty = true;
	private byte[] fContent = null;

	public TextMemoryBlock(IDebugTarget target, long startAddress, long length) {
		super(target);
		fStartAddress = startAddress;
		fLength = length;
	}

	@Override
	public long getStartAddress() {
		return fStartAddress;
	}

	@Override
	public long getLength() {
		return fLength;
	}

	@Override
	public byte[] getBytes() {
		if (fDirty) {
			if (fContent == null)
				fContent = new byte[(int) getLength()];

			getDebugTarget().fireModelEvent(new FetchMemoryRequest(getStartAddress(), getLength()));
			fDirty = false;
		}

		return fContent;
	}

	@Override
	public boolean supportsValueModification() {
		return false;
	}

	@Override
	public void setValue(long offset, byte[] bytes) throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.debugger.textinterpreter.debugger", "TextMemoryBlock.setValue() not supported"));
	}

	public long getEndAddress() {
		return getStartAddress() + getLength();
	}

	public void update(int startAddress, byte[] data) {
		int fromOffset;
		int toOffset;
		int length;

		if (startAddress <= getStartAddress()) {
			fromOffset = (int) (getStartAddress() - startAddress);
			toOffset = 0;
		} else {
			fromOffset = 0;
			toOffset = (int) (startAddress - getStartAddress());
		}

		length = (int) Math.min(getLength() - toOffset - fromOffset, data.length);

		System.arraycopy(data, fromOffset, fContent, toOffset, length);
	}

	public void setDirty() {
		fDirty = true;
	}
}
