/*******************************************************************************
 * Copyright (c) 2013 Christian Pontesegger.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christian Pontesegger - initial API and implementation
 *******************************************************************************/
package com.codeandme.debugger.textinterpreter;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * A synthetic interpreter that "executes" text input by writing its lines to stdout. The interpreter supports variable definitions in the form "name = some
 * content" and accumulates all processed characters into its "memory". The interpreter is implemented as a Job, and should therefore be started via schedule().
 */
public class TextInterpreter extends Job {

	private static final Pattern VARIABLE_MATCHER = Pattern.compile(".*?\\$\\{(.+?)\\}.*?");

	/**
	 * Main method not needed by Eclipse. Simply here to test the interpreter.
	 */
	public static void main(String[] args) {
		TextInterpreter interpreter = new TextInterpreter();
		// @formatter:off
		interpreter.setCode("hello world\r\n" + 
				"first name = Christian\r\n" + 
				"${first name}, you just defined a variable\r\n" + 
				"\r\n" + 
				"counter = 23\r\n" + 
				"\r\n" + 
				"we are running\r\n" + 
				"our interpreter");
		// @formatter:on
		interpreter.run(new NullProgressMonitor());
	}

	/** Debug action types. */
	private enum DebugAction {
		LOADED, SUSPEND, RESUME, TERMINATE
	};

	public TextInterpreter() {
		super("Text interpreter");
	}

	private List<String> fLines;
	private int fLineNumber;

	private final Map<String, String> fVariables = new HashMap<String, String>();
	private StringWriter fMemory;

	private IDebugger fDebugger;
	private boolean fTerminated;

	/**
	 * Set the source code to be executed. As our interpreter is line oriented, we immediately split the text into separate lines.
	 * 
	 * @param code
	 *            source code
	 */
	public void setCode(final String code) {
		String[] lines = code.replaceAll("\r\n", "\n").split("\n");
		fLines = new LinkedList<String>(Arrays.asList(lines));
	}

	public String getMemory() {
		return fMemory.toString();
	}

	public Map<String, String> getVariables() {
		return fVariables;
	}

	@Override
	protected synchronized IStatus run(final IProgressMonitor monitor) {
		fTerminated = false;
		fLineNumber = 1;

		fVariables.clear();
		fMemory = new StringWriter();

		debug(DebugAction.LOADED);

		while ((!fTerminated) && (!monitor.isCanceled()) && (!fLines.isEmpty())) {

			// read line to execute
			String line = fLines.remove(0);
			line = evaluate(line);

			// "execute" line of code
			System.out.println(line);

			// alter our simulated memory
			fMemory.append(line);

			// advance by one line
			fLineNumber++;

			debug(DebugAction.SUSPEND);
		}

		debug(DebugAction.TERMINATE);

		return Status.OK_STATUS;
	}

	public String evaluate(String lineOfCode) {
		// do variable replacement
		Matcher matcher = VARIABLE_MATCHER.matcher(lineOfCode);
		while (matcher.matches()) {
			lineOfCode = matcher.replaceFirst(fVariables.get(matcher.group(1)));
			matcher = VARIABLE_MATCHER.matcher(lineOfCode);
		}

		// try to register variable
		String[] tokens = lineOfCode.split("=");
		if (tokens.length == 2) {
			// variable found
			fVariables.put(tokens[0].trim(), tokens[1].trim());
		}


		return lineOfCode;
	}

	/**
	 * Set a debugger.
	 * 
	 * @param debugger
	 *            debugger to monitor execution
	 */
	public void setDebugger(final IDebugger debugger) {
		fDebugger = debugger;
	}

	/**
	 * Handle debug actions.
	 */
	private void debug(DebugAction action) {
		if (fDebugger != null) {
			switch (action) {
			case LOADED:
				// interpreter started, go in suspend mode
				fDebugger.loaded();
				suspend();
				break;

			case SUSPEND:
				// suspend if we hit a breakpoint or a step command was
				// executed
				if (fDebugger.isBreakpoint(fLineNumber)) {
					fDebugger.suspended(fLineNumber);
					suspend();
				}
				break;

			case RESUME:
				// interpreter resumed
				fDebugger.resumed();
				break;

			case TERMINATE:
				// interpreter terminated
				fDebugger.terminated();
				break;
			}
		}
	}

	private void suspend() {
		try {
			wait();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Resume interpreter. If the interpreter was suspended it will resume operation.
	 */
	public synchronized void resume() {
		debug(DebugAction.RESUME);
		notifyAll();
	}

	/**
	 * Terminate interpreter.
	 */
	public void terminate() {
		fTerminated = true;

		// in case we are suspended
		resume();
	}
}
