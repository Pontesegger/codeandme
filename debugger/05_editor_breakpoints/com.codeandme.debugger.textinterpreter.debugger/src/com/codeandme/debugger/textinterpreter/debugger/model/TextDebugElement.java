package com.codeandme.debugger.textinterpreter.debugger.model;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.DebugElement;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IDisconnect;
import org.eclipse.debug.core.model.IStep;
import org.eclipse.debug.core.model.ISuspendResume;
import org.eclipse.debug.core.model.ITerminate;

import com.codeandme.debugger.textinterpreter.debugger.events.debugger.TerminatedEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.model.DisconnectRequest;
import com.codeandme.debugger.textinterpreter.debugger.events.model.ResumeRequest;
import com.codeandme.debugger.textinterpreter.debugger.events.model.TerminateRequest;

public abstract class TextDebugElement extends DebugElement implements ISuspendResume, IDisconnect, ITerminate, IStep {
	public enum State {
		NOT_STARTED, SUSPENDED, RESUMED, TERMINATED, DISCONNECTED, STEPPING
	};

	private State fState = State.NOT_STARTED;

	protected TextDebugElement(IDebugTarget target) {
		super(target);
	}

	@Override
	public String getModelIdentifier() {
		return null;
	}

	@Override
	public TextDebugTarget getDebugTarget() {
		return (TextDebugTarget) super.getDebugTarget();
	}

	protected void setState(State state) {
		// only the DebugTarget saves the correct state.
		((TextDebugElement) getDebugTarget()).fState = state;
	}

	protected State getState() {
		return ((TextDebugElement) getDebugTarget()).fState;
	}

	// ************************************************************
	// ISuspendResume
	// ************************************************************

	@Override
	public boolean canResume() {
		return isSuspended();
	}

	@Override
	public boolean canSuspend() {
		// we cannot interrupt our debugger once it is running
		return false;
	}

	@Override
	public boolean isSuspended() {
		return (getState() == State.SUSPENDED);
	}

	@Override
	public void resume() {
		// resume request from eclipse

		// send resume request to debugger
		getDebugTarget().fireModelEvent(new ResumeRequest());
	}

	@Override
	public void suspend() throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "suspend() not supported"));
	}

	// ************************************************************
	// IDisconnect
	// ************************************************************

	@Override
	public boolean canDisconnect() {
		return canTerminate();
	}

	@Override
	public void disconnect() {
		// disconnect request from eclipse

		// send disconnect request to debugger
		getDebugTarget().fireModelEvent(new DisconnectRequest());

		// debugger is detached, simulate terminate event
		getDebugTarget().handleEvent(new TerminatedEvent());
	}

	@Override
	public boolean isDisconnected() {
		return isTerminated();
	}

	// ************************************************************
	// ITerminate
	// ************************************************************

	@Override
	public boolean canTerminate() {
		return !isTerminated();
	}

	@Override
	public boolean isTerminated() {
		return (getState() == State.TERMINATED);
	}

	@Override
	public void terminate() {
		// terminate request from eclipse

		// send terminate request to debugger
		getDebugTarget().fireModelEvent(new TerminateRequest());
	}

	// ************************************************************
	// IStep
	// ************************************************************

	@Override
	public boolean canStepInto() {
		return false;
	}

	@Override
	public boolean canStepOver() {
		return isSuspended();
	}

	@Override
	public boolean canStepReturn() {
		return false;
	}

	@Override
	public boolean isStepping() {
		return (getState() == State.STEPPING);
	}

	@Override
	public void stepInto() throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "stepInto() not supported"));
	}

	@Override
	public void stepOver() {
		// stepOver request from eclipse

		// send stepOver request to debugger
		getDebugTarget().fireModelEvent(new ResumeRequest(ResumeRequest.STEP_OVER));
	}

	@Override
	public void stepReturn() throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "stepReturn() not supported"));
	}
}
