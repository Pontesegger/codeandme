package com.codeandme.debugger.textinterpreter.debugger.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

import com.codeandme.debugger.textinterpreter.debugger.events.model.FetchVariablesRequest;

public class TextStackFrame extends TextDebugElement implements IStackFrame {

	private final IThread fThread;
	private int fLineNumber = 1;

	private final List<TextVariable> fVariables = new ArrayList<TextVariable>();
	private boolean fDirtyVariables = true;

	public TextStackFrame(IDebugTarget target, IThread thread) {
		super(target);
		fThread = thread;
	}

	@Override
	public IThread getThread() {
		return fThread;
	}

	@Override
	public synchronized IVariable[] getVariables() {
		if (fDirtyVariables) {
			fDirtyVariables = false;
			getDebugTarget().fireModelEvent(new FetchVariablesRequest());
		}

		return fVariables.toArray(new IVariable[fVariables.size()]);
	}

	public synchronized void setVariables(Map<String, String> variables) {
		for (String name : variables.keySet()) {
			boolean processed = false;
			// try to find existing variable
			for (TextVariable variable : fVariables) {
				if (variable.getName().equals(name)) {
					// variable exists
					String newValue = variables.get(name);
					String oldValue = null;
					try {
						oldValue = variable.getValue().getValueString();
					} catch (DebugException e) {
					}

					variable.setChanged(!newValue.equals(oldValue));
					variable.setValue(variables.get(name));
					variable.fireChangeEvent(DebugEvent.CONTENT);

					processed = true;
					break;
				}
			}

			if (!processed) {
				// not found, create new variable
				TextVariable textVariable = new TextVariable(getDebugTarget(), name, variables.get(name));
				fVariables.add(textVariable);
				textVariable.fireCreationEvent();
			}
		}
	}

	@Override
	public synchronized void fireChangeEvent(int detail) {
		fDirtyVariables = true;

		super.fireChangeEvent(detail);
	}

	@Override
	public boolean hasVariables() {
		return getVariables().length > 0;
	}

	@Override
	public int getLineNumber() {
		return fLineNumber;
	}

	@Override
	public int getCharStart() {
		return -1;
	}

	@Override
	public int getCharEnd() {
		return -1;
	}

	@Override
	public String getName() {
		return getSourceFile().getName() + ", line " + getLineNumber();
	}

	@Override
	public IRegisterGroup[] getRegisterGroups() {
		return new IRegisterGroup[0];
	}

	@Override
	public boolean hasRegisterGroups() {
		return getRegisterGroups().length > 0;
	}

	public void setLineNumber(int lineNumber) {
		fLineNumber = lineNumber;
	}

	public IFile getSourceFile() {
		return (getDebugTarget()).getFile();
	}
}
