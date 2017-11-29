package com.codeandme.debugger.textinterpreter.debugger.dispatcher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.codeandme.debugger.textinterpreter.debugger.Activator;
import com.codeandme.debugger.textinterpreter.debugger.events.IDebugEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.debugger.IDebuggerEvent;
import com.codeandme.debugger.textinterpreter.debugger.events.model.IModelRequest;

public class EventDispatchJob extends Job {

	private final List<IDebugEvent> fEvents = new ArrayList<IDebugEvent>();
	private boolean fTerminated = false;
	private final IEventProcessor fHost;
	private final IEventProcessor fDebugger;

	public EventDispatchJob(final IEventProcessor host, final IEventProcessor debugger) {
		super("Text Debugger event dispatcher");

		fHost = host;
		fDebugger = debugger;

		setSystem(true);
		schedule();
	}

	public void addEvent(final IDebugEvent event) {
		synchronized (fEvents) {
			if (Activator.getDefault().isDebugging())
				System.out.println("Dispatcher: [+] " + event);
			
			fEvents.add(event);
			fEvents.notifyAll();
		}
	}

	@Override
	protected IStatus run(final IProgressMonitor monitor) {

		while (!fTerminated) {
			IDebugEvent event = null;

			// wait for new events
			synchronized (fEvents) {
				if (fEvents.isEmpty()) {
					try {
						synchronized (this) {
							fEvents.wait();
						}
					} catch (InterruptedException e) {
					}
				}

				if (!fEvents.isEmpty())
					event = fEvents.remove(0);
			}

			if (monitor.isCanceled())
				terminate();

			if (event != null)
				handleEvent(event);
		}

		return Status.OK_STATUS;
	}

	private void handleEvent(final IDebugEvent event) {
		// forward event handling to target
		if (event instanceof IDebuggerEvent) {
			if (Activator.getDefault().isDebugging())
				System.out.println("Dispatcher: debugger -> " + event + " -> model");
			
			fHost.handleEvent(event);
		}

		else if (event instanceof IModelRequest) {
			if (Activator.getDefault().isDebugging())
				System.out.println("Dispatcher: debugger <- " + event + " <- model");
			
			fDebugger.handleEvent(event);
		}

		else
			throw new RuntimeException("Unknown event detected: " + event);
	}

	public void terminate() {
		fTerminated = true;

		// wake up job
		synchronized (fEvents) {
			fEvents.notifyAll();
		}
	}
}
