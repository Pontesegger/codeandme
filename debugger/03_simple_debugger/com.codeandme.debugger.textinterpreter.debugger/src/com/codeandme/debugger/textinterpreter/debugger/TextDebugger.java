package com.codeandme.debugger.textinterpreter.debugger;

import com.codeandme.debugger.textinterpreter.IDebugger;
import com.codeandme.debugger.textinterpreter.TextInterpreter;
import com.codeandme.debugger.textinterpreter.debugger.dispatcher.EventDispatchJob;
import com.codeandme.debugger.textinterpreter.debugger.dispatcher.IEventProcessor;
import com.codeandme.debugger.textinterpreter.debugger.events.IDebugEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.debugger.DebuggerStartedEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.debugger.ResumedEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.debugger.TerminatedEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.model.ResumeRequest;

public class TextDebugger implements IDebugger, IEventProcessor {

	private EventDispatchJob fDispatcher;
	private final TextInterpreter fInterpreter;

	public TextDebugger(final TextInterpreter interpreter) {
		fInterpreter = interpreter;
	}

	public void setEventDispatcher(final EventDispatchJob dispatcher) {
		fDispatcher = dispatcher;
	}

	@Override
	public void loaded() {
		fireEvent(new DebuggerStartedEvent());
	}

	@Override
	public void suspended(final int lineNumber) {
		throw new RuntimeException("not implemented yet");
	}

	@Override
	public void resumed() {
		fireEvent(new ResumedEvent());
	}

	@Override
	public void terminated() {
		fireEvent(new TerminatedEvent());
	}

	@Override
	public boolean isBreakpoint(final int lineNumber) {
		return false;
	}

	@Override
	public void handleEvent(final IDebugEvent event) {
		if (Activator.getDefault().isDebugging())
			System.out.println("Debugger  : process " + event);

		if (event instanceof ResumeRequest)
			fInterpreter.resume();
	}

	/**
	 * Pass an event to the {@link EventDispatchJob} where it is handled asynchronously.
	 * 
	 * @param event
	 *            event to handle
	 */
	private void fireEvent(final IDebugEvent event) {
		if (Activator.getDefault().isDebugging())
			System.out.println("Debugger  : new " + event);

		fDispatcher.addEvent(event);
	}
}
