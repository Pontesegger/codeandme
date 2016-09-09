/*******************************************************************************
 * Copyright (c) 2015 Christian Pontesegger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christian Pontesegger - initial implementation
 *******************************************************************************/
package com.codeandme.tools.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RegistryToggleState;

/**
 * Base handler for toggle commands. To use this handler attach a state to the
 * command definition:
 * 
 * <pre>
 *  &lt;command ...&gt;
 *     &lt;state
 *        class="org.eclipse.ui.handlers.RegistryToggleState:true"
 *        id="org.eclipse.ui.commands.toggleState"&gt;
 *      &lt;/state&gt;
 *  &lt;/command&gt;
 * </pre>
 */
public abstract class AbstractToggleHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Command command = event.getCommand();
		final boolean oldValue = HandlerUtil.toggleCommandState(command);

		executeToggle(event, !oldValue);

		return null;
	}

	/**
	 * Execute the toggle event.
	 * 
	 * @param event
	 *            An event containing all the information about the current
	 *            state of the application; must not be null.
	 * @param checked
	 *            new state of the toggle element
	 */
	protected abstract void executeToggle(ExecutionEvent event, boolean checked);

	/**
	 * Sets the new state of the command. Additionally triggers a refresh of UI
	 * elements depending on that state.
	 * 
	 * @param command
	 *            command that contains the state
	 * @param value
	 *            value to set the state to
	 * @throws ExecutionException
	 *             thrown when no {@link RegistryToggleState} is attached to the
	 *             command
	 */
	public static void setCommandState(final Command command, final boolean value) throws ExecutionException {
		State state = getState(command);
		if (!state.getValue().equals(value))
			state.setValue(new Boolean(value));
	}

	/**
	 * Get the current command state.
	 * 
	 * @param command
	 *            command that contains the state
	 * @return current command state
	 * @throws ExecutionException
	 *             thrown when no {@link RegistryToggleState} is attached to the
	 *             command
	 */
	public static boolean getCommandState(final Command command) throws ExecutionException {
		return (Boolean) getState(command).getValue();
	}

	private static State getState(final Command command) throws ExecutionException {
		final State state = command.getState(RegistryToggleState.STATE_ID);
		if (state == null)
			throw new ExecutionException("The command does not have a toggle state"); //$NON-NLS-1$
		if (!(state.getValue() instanceof Boolean))
			throw new ExecutionException("The command's toggle state doesn't contain a boolean value"); //$NON-NLS-1$
		return state;
	}
}