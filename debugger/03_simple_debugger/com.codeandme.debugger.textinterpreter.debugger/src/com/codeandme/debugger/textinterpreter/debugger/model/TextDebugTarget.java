package com.codeandme.debugger.textinterpreter.debugger.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;

import com.codeandme.debugger.textinterpreter.debugger.Activator;
import com.codeandme.debugger.textinterpreter.debugger.dispatcher.EventDispatchJob;
import com.codeandme.debugger.textinterpreter.debugger.dispatcher.IEventProcessor;
import com.codeandme.debugger.textinterpreter.debugger.events.IDebugEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.debugger.DebuggerStartedEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.debugger.ResumedEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.debugger.TerminatedEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.model.ResumeRequest;

public class TextDebugTarget extends TextDebugElement implements IDebugTarget, IEventProcessor {

	public enum State {
		NOT_STARTED, SUSPENDED, RESUMED, TERMINATED, DISCONNECTED
	};

	private EventDispatchJob fDispatcher;

	private final TextProcess fProcess;

	private final List<TextThread> fThreads = new ArrayList<TextThread>();

	private final ILaunch fLaunch;

	private State fState = State.NOT_STARTED;

	private final IFile fFile;

	public TextDebugTarget(final ILaunch launch, IFile file) {
		super(null);

		fLaunch = launch;
		fFile = file;

		fireCreationEvent();

		// create a process
		fProcess = new TextProcess(this);
		fProcess.fireCreationEvent();
	}

	public void setEventDispatcher(final EventDispatchJob dispatcher) {
		fDispatcher = dispatcher;
	}

	/**
	 * Pass an event to the {@link EventDispatchJob} where it is handled
	 * asynchronously.
	 * 
	 * @param event
	 *            event to handle
	 */
	void fireModelEvent(final IDebugEvent event) {
		if (Activator.getDefault().isDebugging())
			System.out.println("Model     : new " + event);

		fDispatcher.addEvent(event);
	}

	@Override
	public void handleEvent(final IDebugEvent event) {

		if (!isDisconnected()) {
			if (Activator.getDefault().isDebugging())
				System.out.println("Model     : process " + event);

			if (event instanceof DebuggerStartedEvent) {
				// create debug thread
				TextThread thread = new TextThread(this);
				fThreads.add(thread);
				thread.fireCreationEvent();

				// debugger got started and waits in suspended mode
				fState = State.SUSPENDED;

				// inform eclipse of suspended state
				fireSuspendEvent(DebugEvent.CLIENT_REQUEST);

			} else if (event instanceof ResumedEvent) {
				fState = State.RESUMED;

				// inform eclipse of resumed state
				fireResumeEvent(DebugEvent.UNSPECIFIED);

			} else if (event instanceof TerminatedEvent) {
				// debugger is terminated
				fState = State.TERMINATED;

				// we do not need our dispatcher anymore
				fDispatcher.terminate();

				// inform eclipse of terminated state
				fireTerminateEvent();
			}
		}
	}

	@Override
	public ILaunch getLaunch() {
		return fLaunch;
	}

	@Override
	public IProcess getProcess() {
		return fProcess;
	}

	@Override
	public TextThread[] getThreads() {
		return fThreads.toArray(new TextThread[fThreads.size()]);
	}

	@Override
	public boolean hasThreads() {
		return !fThreads.isEmpty();
	}

	@Override
	public String getName() {
		return "Text DebugTarget";
	}

	@Override
	public boolean supportsBreakpoint(final IBreakpoint breakpoint) {
		return false;
	}

	@Override
	public TextDebugTarget getDebugTarget() {
		return this;
	}

	// ************************************************************
	// IBreakpointListener
	// ************************************************************

	@Override
	public void breakpointAdded(final IBreakpoint breakpoint) {
	}

	@Override
	public void breakpointRemoved(final IBreakpoint breakpoint, final IMarkerDelta delta) {
	}

	@Override
	public void breakpointChanged(final IBreakpoint breakpoint, final IMarkerDelta delta) {
	}

	// ************************************************************
	// IDisconnect
	// ************************************************************

	@Override
	public boolean canDisconnect() {
		return false;
	}

	@Override
	public void disconnect() throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "disconnect() not supported"));
	}

	@Override
	public boolean isDisconnected() {
		return false;
	}

	// ************************************************************
	// ITerminate
	// ************************************************************

	@Override
	public boolean canTerminate() {
		return false;
	}

	@Override
	public boolean isTerminated() {
		return (fState == State.TERMINATED);
	}

	@Override
	public void terminate() throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "disconnect() not supported"));
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
		return (fState == State.SUSPENDED);
	}

	@Override
	public void resume() {
		// resume request from eclipse

		// send resume request to debugger
		fireModelEvent(new ResumeRequest());
	}

	@Override
	public void suspend() throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "suspend() not supported"));
	}

	// ************************************************************
	// IMemoryBlockRetrieval
	// ************************************************************

	@Override
	public boolean supportsStorageRetrieval() {
		return false;
	}

	@Override
	public IMemoryBlock getMemoryBlock(final long startAddress, final long length) throws DebugException {
		throw new DebugException(new Status(IStatus.ERROR, "com.codeandme.textinterpreter.debugger", "getMemoryBlock() not supported"));
	}
}
