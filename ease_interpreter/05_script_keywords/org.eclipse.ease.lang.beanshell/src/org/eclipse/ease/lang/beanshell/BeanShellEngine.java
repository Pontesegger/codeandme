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
package org.eclipse.ease.lang.beanshell;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ease.AbstractScriptEngine;
import org.eclipse.ease.Logger;
import org.eclipse.ease.Script;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.Variable;

public class BeanShellEngine extends AbstractScriptEngine {

	private Interpreter fInterpreter = null;

	public BeanShellEngine() {
		super("BeanShell");
	}

	@Override
	protected boolean setupEngine() {
		fInterpreter = new Interpreter();

		fInterpreter.setClassLoader(getClass().getClassLoader());

		fInterpreter.setOut(getOutputStream());
		fInterpreter.setErr(getErrorStream());

		return true;
	}

	@Override
	protected boolean teardownEngine() {
		fInterpreter = null;

		return true;
	}

	@Override
	protected Object execute(final Script script, final Object reference, final String fileName, final boolean uiThread) throws Throwable {
		return fInterpreter.eval(script.getCode());
	}

	@Override
	protected Object internalGetVariable(final String name) {
		try {
			return fInterpreter.get(name);
		} catch (EvalError e) {
			Logger.error(IPluginConstants.PLUGIN_ID, "Cannot retrieve variable \"" + name + "\"", e);
		}

		return null;
	}

	@Override
	protected Map<String, Object> internalGetVariables() {
		Map<String, Object> variables = new HashMap<String, Object>();

		for (Variable variable : fInterpreter.getNameSpace().getDeclaredVariables())
			variables.put(variable.getName(), internalGetVariable(variable.getName()));

		return variables;
	}

	@Override
	protected boolean internalHasVariable(final String name) {
		for (Variable variable : fInterpreter.getNameSpace().getDeclaredVariables()) {
			if (variable.getName().equals(name))
				return true;
		}

		return false;
	}

	@Override
	protected void internalSetVariable(final String name, final Object content) {
		try {
			fInterpreter.set(name, content);
		} catch (EvalError e) {
			Logger.error(IPluginConstants.PLUGIN_ID, "Cannot set variable \"" + name + "\"", e);
		}
	}

	@Override
	protected Object internalRemoveVariable(final String name) {
		Object content = internalGetVariable(name);
		fInterpreter.getNameSpace().unsetVariable(name);

		return content;
	}

	@Override
	public String getSaveVariableName(final String name) {
		return BeanShellHelper.getSaveName(name);
	}

	@Override
	public void terminateCurrent() {
		// TODO Auto-generated method stub
		throw new RuntimeException("terminateCurrent not implemented");
	}

	@Override
	public void registerJar(final URL url) {
		// TODO Auto-generated method stub
		throw new RuntimeException("registerJar not implemented");
	}
}
