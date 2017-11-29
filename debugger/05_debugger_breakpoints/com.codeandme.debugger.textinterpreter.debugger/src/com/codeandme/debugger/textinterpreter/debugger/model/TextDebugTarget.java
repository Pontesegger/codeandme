package com.codeandme.debugger.textinterpreter.debugger.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
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
import com.codeandme.debugger.textinterpreter.debugger.events.debugger.SuspendedEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.debugger.TerminatedEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.model.BreakpointRequest;

public class TextDebugTarget extends TextDebugElement implements IDebugTarget, IEventProcessor {

	private EventDispatchJob fDispatcher;

	private final TextProcess fProcess;

	private final List<TextThread> fThreads = new ArrayList<TextThread>();

	private final ILaunch fLaunch;

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
	 * Pass an event to the {@link EventDispatchJob} where it is handled asynchronously.
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
				setState(State.SUSPENDED);

				// add breakpoint listener
				DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);

				// attach deferred breakpoints to debugger
				IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(getModelIdentifier());
				for (IBreakpoint breakpoint : breakpoints)
					breakpointAdded(breakpoint);

				// inform eclipse of suspended state
				fireSuspendEvent(DebugEvent.CLIENT_REQUEST);

			} else if (event instanceof SuspendedEvent) {
				// debugger got started and waits in suspended mode
				setState(State.SUSPENDED);

				// inform eclipse of suspended state
				fireSuspendEvent(DebugEvent.CLIENT_REQUEST);

			} else if (event instanceof ResumedEvent) {
				if (((ResumedEvent) event).getType() == ResumedEvent.STEPPING) {
					setState(State.STEPPING);
					fireResumeEvent(DebugEvent.STEP_OVER);

				} else {
					setState(State.RESUMED);
					fireResumeEvent(DebugEvent.UNSPECIFIED);
				}

			} else if (event instanceof TerminatedEvent) {
				// debugger is terminated
				setState(State.TERMINATED);

				 // unregister breakpoint listener
				DebugPlugin.getDefault().getBreakpointManager().removeBreakpointListener(this);

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
		if (fFile.equals(breakpoint.getMarker().getResource())) {
			// breakpoint on our source file
			return true;
		}

		return false;
	}

	private boolean isEnabledBreakpoint(IBreakpoint breakpoint) {
		try {
			return breakpoint.isEnabled() && DebugPlugin.getDefault().getBreakpointManager().isEnabled();
		} catch (CoreException e) {
			// ignore invalid breakpoint
		}

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
		if ((supportsBreakpoint(breakpoint)) && (isEnabledBreakpoint(breakpoint)))
			fireModelEvent(new BreakpointRequest(breakpoint, BreakpointRequest.ADDED));
	}

	@Override
	public void breakpointRemoved(final IBreakpoint breakpoint, final IMarkerDelta delta) {
		if (supportsBreakpoint(breakpoint))
			fireModelEvent(new BreakpointRequest(breakpoint, BreakpointRequest.REMOVED));
	}

	@Override
	public void breakpointChanged(final IBreakpoint breakpoint, final IMarkerDelta delta) {
		breakpointRemoved(breakpoint, delta);
		breakpointAdded(breakpoint);
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
